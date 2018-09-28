/*
 * Copyright 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.crypt;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.iplass.mtp.impl.core.config.PropertyValueCoder;
import org.iplass.mtp.impl.core.config.ServiceRegistryInitializer;

public class Encoder {
	
	
	private static Properties getProperties() throws IOException {
		String fileName = ServiceRegistryInitializer.getCryptoConfigFileName();
		if (fileName != null) {
			Properties prop = new Properties();
			Path path = Paths.get(fileName);
			if (Files.exists(path)) {
				System.out.println("load CryptConfigFile from file path:" + fileName);
				try (InputStreamReader is = new InputStreamReader(new FileInputStream(path.toFile()), "utf-8")) {
					prop.load(is);
				}
			} else {
				System.out.println("load CryptConfigFile from classpath:" + fileName);
				try (InputStream is = PropertyValueCoder.class.getResourceAsStream(fileName)) {
					if (is == null) {
						System.err.println("CryptConfigFile:" + fileName + " not found.");
						
					}
					
					InputStreamReader isr = new InputStreamReader(is, "utf-8");
					prop.load(isr);
				}
			}
			return prop;
		} else {
			return System.getProperties();
		}
	}

	/**
	 * 第一引数：暗号化したい文字列。もしくは暗号化したい文字列が保存されているファイルへのパス。
	 * 
	 * 以下、VMオプションで指定可能。
	 * keyFactoryAlgorithm　：パスワードベース暗号化（PBE）の鍵生成アルゴリズム。未指定の場合はデフォルト利用。
	 * keyLength　：鍵長。未指定の場合は、デフォルト利用。
	 * cipherAlgorithm ：エンコード時のブロック暗号化方式の暗号化アルゴリズム。未指定の場合はデフォルト利用。
	 * keySalt ：key生成時のsalt文字列。未指定の場合はデフォルト利用。
	 * keyStretch ：key生成時のstretch回数。未指定の場合はデフォルト利用。
	 * 
	 * 実行時に、暗号化のためのパスフレーズをコンソールから入力する
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		Properties prop = getProperties();
		
		String keyAlg = prop.getProperty(PropertyValueCoder.KEY_ALGORITHM, PropertyValueCoder.DEFAULT_KEY_ALGORITHM);
		int keyLength = PropertyValueCoder.DEFAULT_KEY_LENGTH;
		if (prop.getProperty(PropertyValueCoder.KEY_LENGTH) != null) {
			keyLength = Integer.parseInt(prop.getProperty(PropertyValueCoder.KEY_LENGTH));
		}
		String cipherAlg = prop.getProperty(PropertyValueCoder.CIPHER_ALGORITHM, PropertyValueCoder.DEFAULT_CIPHER_ALGORITHM);
		byte[] keySalt = null;
		if (prop.getProperty(PropertyValueCoder.KEY_SALT) != null) {
			keySalt = prop.getProperty(PropertyValueCoder.KEY_SALT).getBytes("utf-8");
		}
		int keyStretch = PropertyValueCoder.DEFAULT_STRETCH;
		if (prop.getProperty(PropertyValueCoder.KEY_STRETCH) != null) {
			keyStretch = Integer.parseInt(prop.getProperty(PropertyValueCoder.KEY_STRETCH));
		}
		
		PropertyValueCoder coder = new PropertyValueCoder(keyAlg, keyLength, cipherAlg, keySalt, keyStretch,
				() -> {
					System.out.println("enter passphrase:");
					BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
					try {
						String passphrase1 = console.readLine();
						System.out.println("enter same passphrase again:");
						String passphrase2 = console.readLine();
						
						if (!passphrase1.equals(passphrase2)) {
							throw new RuntimeException("passphrase unmatch");
						}
						return passphrase1.toCharArray();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});

		
		while (true) {
			String plain;
			if (args.length > 0) {
				if (args[0].equals("-file")) {
					String filePath;
					if (args.length > 1) {
						filePath = args[1];
					} else {
						System.out.println("enter file path of plain text:");
						BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
						filePath = console.readLine();
					}
					plain = new String(Files.readAllBytes(Paths.get(filePath)), "utf-8");
				} else {
					plain = args[0];
				}
			} else {
				System.out.println("enter plain text:");
				BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
				plain = console.readLine();
			}
			
			if (plain.equals("")) {
				return;
			}
			
			System.out.println("encrypted text:");
			System.out.println(coder.encode(plain));
			System.out.println();
			
			// 引数指定で起動する場合、バッチ処理を終了する。
			if (args.length > 1 && args[0].equals("-file")) {
				return;
			}
		}

	}

}
