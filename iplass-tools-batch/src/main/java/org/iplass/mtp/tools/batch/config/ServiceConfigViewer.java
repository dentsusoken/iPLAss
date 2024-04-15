/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.tools.batch.config;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import org.iplass.mtp.impl.core.config.BootstrapProps;
import org.iplass.mtp.impl.core.config.ServiceDefinition;
import org.iplass.mtp.impl.core.config.ServiceDefinitionParser;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.MtpBatchResourceDisposer;
import org.iplass.mtp.util.StringUtil;

public class ServiceConfigViewer {

	private static enum Option {
		MODE("-m"),
		OUT_FILE("-o");

		private final String value;

		private Option(String value) {
			this.value = value;
		}
	}

	private static enum Mode {
		PARSE_ONLY,		// パースのみ
		PARSE_LOAD			// パース及びサービスのロード
	}

	private Mode mode = Mode.PARSE_ONLY;
	private String configFileName = null;
	private String outputFileName = null;

	/**
	 * コンストラクタ
	 *
	 * 引数オプション：
	 *   -m [MODE]・・・モード(PARSE_ONLY:パースのみ、PARSE_LOAD:パース及びサービスのロード)
	 *   -o [FILE]・・・出力ファイル名(未指定時はコンソール出力)
	 **/
	public ServiceConfigViewer(String[] args) {
		for (int i = 0; i < args.length; i += 2) {
			if (args.length > i + 1) {
				if (args[i + 1].startsWith("-")) continue;
				if (Option.MODE.value.equals(args[i])) {
					mode = Mode.valueOf(args[i + 1]);
				} else if (Option.OUT_FILE.value.equals(args[i])) {
					outputFileName = args[i + 1];
				}
			}
		}

		configFileName = BootstrapProps.getInstance().getProperty(
				BootstrapProps.CONFIG_FILE_NAME, BootstrapProps.DEFAULT_CONFIG_FILE_NAME);
	}

	/**
	 * メイン処理
	 *
	 * @param args 引数
	 */
	public static void main(String... args) {
		try {
			new ServiceConfigViewer(args).execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// リソース破棄
			MtpBatchResourceDisposer.disposeResource();
		}
	}

	public boolean execute() throws Exception {
		return proceed();
	}

	private boolean proceed() {
		boolean isSuccess = false;

		Writer writer = null;
		boolean toFile = StringUtil.isNotBlank(outputFileName);

		try {
			ServiceDefinitionParser parser = new ServiceDefinitionParser();
			ServiceDefinition sd = parser.read(configFileName);

			if (mode.equals(Mode.PARSE_LOAD)) {
				BootstrapProps.getInstance().setProperty(BootstrapProps.CONFIG_FILE_NAME, configFileName);
				Stream.of(sd.getService()).forEach(sc -> {
					// 全てのサービスをロード
					if (sc.getName() != null) {
						ServiceRegistry.getRegistry().getService(sc.getName());
					} else {
						ServiceRegistry.getRegistry().getService(sc.getInterfaceName());
					}
				});
			}

			if (toFile) {
				writer = Files.newBufferedWriter(Paths.get(outputFileName));
			} else {
				writer = System.console() != null ? System.console().writer() : new OutputStreamWriter(System.out);
			}

			// マージ済みなのでincludesとinheritsはクリアする
			sd.setIncludes(null);
			sd.setInherits(null);

			writeMergedServiceConfig(sd, writer);

			isSuccess = true;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (writer != null) {
				try {
					writer.flush();
					if (toFile) {
						writer.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return isSuccess;
	}

	private void writeMergedServiceConfig(final ServiceDefinition sd, Writer writer) {
		try {
			JAXBContext context = JAXBContext.newInstance(ServiceDefinition.class);
			Marshaller marshaller = context.createMarshaller();

			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			marshaller.marshal(sd, writer);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

}
