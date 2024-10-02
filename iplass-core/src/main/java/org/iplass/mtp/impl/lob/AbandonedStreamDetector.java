/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
 * 
 * Unless you have purchased a commercial license,
 * the following license terms apply:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.iplass.mtp.impl.lob;

import java.io.Closeable;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Cleaner;
import java.lang.ref.Cleaner.Cleanable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AbandonedStreamDetector {

	private static Logger logger = LoggerFactory.getLogger(AbandonedStreamDetector.class);

	private boolean detectAbandonedStream;
	private boolean logAbandoned;
	private Cleaner cleaner;

	AbandonedStreamDetector(boolean detectAbandonedStream, boolean logAbandoned) {
		this.detectAbandonedStream = detectAbandonedStream;
		this.logAbandoned = logAbandoned;
		if (detectAbandonedStream) {
			cleaner = Cleaner.create();
		}
	}

	InputStream registerOrNot(InputStream in) {
		if (detectAbandonedStream) {
			return new DetectAbandonedInputStream(in);
		} else {
			return in;
		}
	}

	OutputStream registerOrNot(OutputStream out) {
		if (detectAbandonedStream) {
			return new DetectAbandonedOutputStream(out);
		} else {
			return out;
		}
	}

	private class DetectAbandonedInputStream extends FilterInputStream {
		State state;
		Cleanable cleanable;

		public DetectAbandonedInputStream(InputStream in) {
			super(in);
			state = new State(in,
					logAbandoned ? new Exception("StackTrace where LobStream was created") : null);
			cleanable = cleaner.register(this, state);
		}

		@Override
		public void close() throws IOException {
			state.callCorrectly = true;
			try {
				cleanable.clean();
			} catch (WrapException e) {
				if (e.getCause() instanceof IOException) {
					throw (IOException) e.getCause();
				} else {
					throw (RuntimeException) e.getCause();
				}
			}
		}
	}

	private class DetectAbandonedOutputStream extends FilterOutputStream {
		State state;
		Cleanable cleanable;

		public DetectAbandonedOutputStream(OutputStream out) {
			super(out);
			state = new State(out,
					logAbandoned ? new Exception("StackTrace where LobStream was created") : null);
			cleanable = cleaner.register(this, state);
		}

		@Override
		public void close() throws IOException {
			state.callCorrectly = true;
			try {
				cleanable.clean();
			} catch (WrapException e) {
				if (e.getCause() instanceof IOException) {
					throw (IOException) e.getCause();
				} else {
					throw (RuntimeException) e.getCause();
				}
			}
		}
	}

	private static class State implements Runnable {
		private Closeable stream;
		private Exception createdStackTrace;
		private volatile boolean callCorrectly;

		State(Closeable stream, Exception createdStackTrace) {
			this.stream = stream;
			this.createdStackTrace = createdStackTrace;
		}

		@Override
		public void run() {
			try {
				stream.close();
			} catch (IOException | RuntimeException e) {
				if (callCorrectly) {
					throw new WrapException(e);
				} else {
					logger.error("Error on closing stream", e);
				}
			} finally {
				if (!callCorrectly) {
					if (createdStackTrace == null) {
						logger.warn("Abandoned Stream Detected: " + stream);
					} else {
						logger.warn("Abandoned Stream Detected: " + stream, createdStackTrace);
					}
				}
			}
		}
	}

	private static class WrapException extends RuntimeException {
		private static final long serialVersionUID = -1586276797291855842L;

		WrapException(Exception cause) {
			super(cause);
		}

	}

}
