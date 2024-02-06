/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.script.template;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.Script;
import org.iplass.mtp.impl.util.KeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GroovyTemplateCompiler {

	private static Logger logger = LoggerFactory.getLogger(GroovyTemplateCompiler.class);

	//References: Groovy's GStringTemplateEngine

	private static KeyGenerator keyGen = new KeyGenerator();
	
	private static boolean isSpace(int c) {
		if (c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '\f' || c == '\b') {
			return true;
		} else {
			return false;
		}
	}

	public static String randomName() {
		return keyGen.generateId();
	}
	
	public static GroovyTemplate compile(String templateCode, String templateName, GroovyScriptEngine scriptEngine) {
		return compile(templateCode, templateName, GTmplBase.class.getName(), scriptEngine);
	}
	
	public static GroovyTemplate compile(String templateCode, String templateName, String baseClassName, GroovyScriptEngine scriptEngine) {

		//クラス名にできない文字を_に変換
		templateName = templateName.replaceAll("[^\\w\\d]", "_");

		String groovyCode;
		try {
			groovyCode = parseToScript(new StringReader(templateCode), templateName, baseClassName);

			if (logger.isDebugEnabled()) {
				logger.debug("translate " + templateName + " to script...\n==========================\n" + groovyCode + "\n==========================");
			}

		} catch (IOException e) {
			//発生しえない
			throw new SystemException(e);
		}
		Script script = scriptEngine.createScript(groovyCode, templateName);
		return new GroovyTemplate(script);
	}


	private static String parseToScript(Reader templateCode, String templateName, String baseClassName) throws IOException {

		StringBuilder templateExpressions = new StringBuilder("package org.iplass.mtp.tmp.templates;\n");
		StringBuilder importSection = new StringBuilder();

		StringBuilder templateSection = new StringBuilder();
		templateSection.append("class ").append(templateName);
		if (baseClassName != null) {
			templateSection.append(" extends ").append(baseClassName);
		}
		templateSection.append(" {\n def getTemplate() { return { out -> out << \"\"\"");

		boolean writingString = true;
		boolean isComment = false;

		int bc = -1;
		int c = -1;
		while (true) {
			bc = c;
			c = templateCode.read();
			if (c == -1) break;
			if (isComment) {
				if (c == '-') {
					c = templateCode.read();
					if (c == '-') {
						c = templateCode.read();
						if (c == '%') {
							c = templateCode.read();
							if (c == '>') {
								isComment = false;
							}
						}
					}
				}
				continue;
			}
			if (c == '<') {
				c = templateCode.read();
				if (c == '%') {
					c = templateCode.read();
					if (c == '=') {
						parseExpression(templateCode, writingString, templateSection);
						writingString = true;
						continue;
					} else if (c == '@') {
						parseImport(templateCode, importSection);
						continue;
					} else if (c == '-') {
						c = templateCode.read();
						if (c == '-') {
							isComment = true;
							continue;
						} else {
							appendCharacter('<', templateSection, writingString);
							writingString = true;
							appendCharacter('%', templateSection, writingString);
							writingString = true;
							appendCharacter('-', templateSection, writingString);
							writingString = true;
						}
					} else {
						parseSection(c, templateCode, writingString, templateSection);
						writingString = false;
						continue;
					}
				} else {
					appendCharacter('<', templateSection, writingString);
					writingString = true;
				}
			} else if (c == '"') {
				appendCharacter('\\', templateSection, writingString);
				writingString = true;
			} else if (c == '$') {
				appendCharacter('$', templateSection, writingString);
				writingString = true;
				c = templateCode.read();
				if (c == '{') {
					appendCharacter('{', templateSection, writingString);
					templateSection.append("nte{->");
					parseExpr(templateCode, templateSection);
					templateSection.append("}}");
					writingString = true;
					continue;
				} else if (c == 'h') {
					c = templateCode.read();
					
					if (bc == '\\') {
						appendCharacter('h', templateSection, writingString);
					}
					if (c == '{') {
						appendCharacter('{', templateSection, writingString);
						templateSection.append("escHtml{->");
						parseExpr(templateCode, templateSection);
						templateSection.append("}}");
						writingString = true;
						continue;
					}
				} else if (c == 'x') {
					c = templateCode.read();
					
					if (bc == '\\') {
						appendCharacter('x', templateSection, writingString);
					}
					if (c == '{') {
						appendCharacter('{', templateSection, writingString);
						templateSection.append("escXml{->");
						parseExpr(templateCode, templateSection);
						templateSection.append("}}");
						writingString = true;
						continue;
					}
				} else if (c == 'j') {
					c = templateCode.read();
					
					if (bc == '\\') {
						appendCharacter('j', templateSection, writingString);
					}
					if (c == '{') {
						appendCharacter('{', templateSection, writingString);
						templateSection.append("escJs{->");
						parseExpr(templateCode, templateSection);
						templateSection.append("}}");
						writingString = true;
						continue;
					}
				} else if (c == 's') {
					c = templateCode.read();
					
					if (bc == '\\') {
						appendCharacter('s', templateSection, writingString);
					}
					if (c == '{') {
						appendCharacter('{', templateSection, writingString);
						templateSection.append("escEql{->");
						parseExpr(templateCode, templateSection);
						templateSection.append("}}");
						writingString = true;
						continue;
					} else if (c == 'l') {
						c = templateCode.read();
						
						if (bc == '\\') {
							appendCharacter('l', templateSection, writingString);
						}
						if (c == '{') {
							appendCharacter('{', templateSection, writingString);
							templateSection.append("escEqlLike{->");
							parseExpr(templateCode, templateSection);
							templateSection.append("}}");
							writingString = true;
							continue;
						}
					}
				}
			}
			appendCharacter((char) c, templateSection, writingString);
			writingString = true;
		}

		if (writingString) {
			templateSection.append("\"\"\"");
		}

		templateSection.append("}.asWritable()}}");

		if (importSection.length() > 0) {
			templateExpressions.append(importSection);
		}
		templateExpressions.append(templateSection);

		return templateExpressions.toString();
	}

	private static void appendCharacter(final char c, final StringBuilder templateExpressions, final boolean writingString) {
		if (!writingString) {
			templateExpressions.append("out << \"\"\"");
		}
		templateExpressions.append(c);
	}

	private static void parseExpr(Reader reader, StringBuilder templateExpressions) throws IOException {
		int depth = 0;
		CurrentContext context = CurrentContext.CODE;
		int bc = -1;
		int c = -1;
		boolean esc = false;
		
		while (true) {
			bc = c;
			c = reader.read();
			if (c == -1) break;
			if (c == '}' && depth == 0 && context == CurrentContext.CODE) {
				break;
			}
			if (context == CurrentContext.CODE) {
				switch (c) {
				case '{':
					depth++;
					break;
				case '}':
					depth--;
					break;
				case '"':
					context = CurrentContext.STRING_D;
					break;
				case '\'':
					context = CurrentContext.STRING_S;
					break;
				case '/':
					if (bc == '/') {
						context = CurrentContext.COMMENT_LINE;
					}
					break;
				case '*':
					if (bc == '/') {
						context = CurrentContext.COMMENT_BLOCK;
					}
					break;
				default:
					break;
				}
			} else {
				switch (context) {
				case COMMENT_BLOCK:
					if (c == '/' && bc == '*') {
						context = CurrentContext.CODE;
					}
					break;
				case COMMENT_LINE:
					if (c == '\n') {
						context = CurrentContext.CODE;
					}
					break;
				case STRING_D:
					if (c == '\\' && !esc) {
						esc = true;
					} else {
						if (c == '"' && !esc) {
							context = CurrentContext.CODE;
						}
						esc = false;
					}
					break;
				case STRING_S:
					if (c == '\\' && !esc) {
						esc = true;
					} else {
						if (c == '\'' && !esc) {
							context = CurrentContext.CODE;
						}
						esc = false;
					}
					break;
				default:
					break;
				}
			}
			templateExpressions.append((char) c);
		}
	}
	
	private static void parseImport(final Reader reader, final StringBuilder importSection) throws IOException {
		boolean consumePageToken = false;
		while (true) {
			int c = reader.read();
			if (c == -1) break;
			if (c == '%') {
				c = reader.read();
				if (c == '>') break;
				importSection.append('%');
			} else if (!consumePageToken && c == 'p') {
				//check page directive
				c = reader.read();
				if (c == 'a') {
					c = reader.read();
					if (c == 'g') {
						c = reader.read();
						if (c == 'e') {
							c = reader.read();
							if (isSpace(c)) {
								consumePageToken = true;
								parsePageImportDirective(reader, importSection);
								return;
							} else {
								importSection.append("page");
							}
						} else {
							importSection.append("pag");
						}
					} else {
						importSection.append("pa");
					}
				} else {
					importSection.append('p');
				}
			}
			
			importSection.append((char) c);
			
			if (!isSpace(c)) {
				consumePageToken = true;
			}
		}
		importSection.append(";\n");
	}

	private static void parsePageImportDirective(Reader reader, StringBuilder importSection) throws IOException {
		while (true) {
			int c = reader.read();
			if (c == -1) break;
			if (c == '%') {
				c = reader.read();
				if (c == '>') break;
				importSection.append('%');
			}
			if (!isSpace(c)) {
				if (c == 'i') {
					if (reader.read() == 'm'
							&& reader.read() == 'p'
							&& reader.read() == 'o'
							&& reader.read() == 'r'
							&& reader.read() == 't'
							) {
						//import
						int cc = reader.read();
						while (isSpace(cc)) {
							cc = reader.read();
						}
						if (cc == '=') {
							cc = reader.read();
							while (isSpace(cc)) {
								cc = reader.read();
							}
							if (cc == '"' || cc == '\'') {
								parseImportAttribute(reader, importSection, cc);
							}
						}
					}
				}
			}
		}
	}

	private static void parseImportAttribute(Reader reader, StringBuilder importSection, int terminate) throws IOException {
		boolean insertImport = true;
		boolean isappending = false;
		while (true) {
			int c = reader.read();
			if (c == -1) break;
			if (c == terminate) break;
			if (c == ',') {
				insertImport = true;
				continue;
			}
			if (insertImport) {
				if (isappending) {
					importSection.append(";\n");
					isappending = false;
				}
				importSection.append("import ");
				insertImport = false;
			}
			if (!isSpace(c)) {
				importSection.append((char) c);
				isappending = true;
			}
		}
		
		if (isappending) {
			importSection.append(";\n");
		}
	}


	/**
	 * Parse a &lt;% .... %&gt; section
	 */
	private static void parseSection(final int pendingC, final Reader reader,
			final boolean writingString, final StringBuilder templateExpressions) throws IOException {
		if (writingString) {
			templateExpressions.append("\"\"\";\n");
		}
		templateExpressions.append((char) pendingC);
		
		while (true) {
			int c = reader.read();
			if (c == -1) break;
			if (c == '%') {
				c = reader.read();
				if (c == '>') break;
				templateExpressions.append('%');
			}
			templateExpressions.append((char) c);
		}
		templateExpressions.append("\n ");
	}

	/**
	 * Parse a &lt;%= .... %&gt; expression
	 *
	 */
	private static void parseExpression(final Reader reader,
			final boolean writingString, final StringBuilder templateExpressions) throws IOException {
		if (!writingString) {
			templateExpressions.append("out << \"\"\"");
		}
		
		templateExpressions.append("${");
		
		while (true) {
			int c = reader.read();
			if (c == -1) break;
			if (c == '%') {
				c = reader.read();
				if (c == '>') break;
				templateExpressions.append('%');
			}
			templateExpressions.append((char) c);
		}
		
		templateExpressions.append('}');
	}
	
	
	enum CurrentContext {
		//TODO slashコメント対応が難しい。。。
		CODE,
		STRING_D,
		STRING_S,
		COMMENT_LINE,
		COMMENT_BLOCK
	}
}
