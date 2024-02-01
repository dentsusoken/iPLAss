/*
 * Copyright 2012 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.impl.core.config.BootstrapProps;
import org.iplass.mtp.impl.core.config.DefaultPropertyValueCoder;
import org.iplass.mtp.impl.core.config.PropertyValueCoder;

public class Encoder {
	
	
	private static Properties getProperties() throws IOException {
		String fileName = BootstrapProps.getInstance().getProperty(BootstrapProps.CRYPT_CONFIG_FILE_NAME);
		if (fileName != null) {
			Properties prop = new Properties();
			Path path = Paths.get(fileName);
			if (Files.exists(path)) {
				System.out.println("load CryptConfigFile from file path:" + fileName);
				try (FileInputStream fis = new FileInputStream(path.toFile());
						InputStreamReader is = new InputStreamReader(fis, "utf-8")) {
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
		
		String propertyValueCoderName = prop.getProperty(PropertyValueCoder.PROPERTY_VALUE_CODER, DefaultPropertyValueCoder.class.getName());
		if(DefaultPropertyValueCoder.class.getName().equals(propertyValueCoderName)) {
			prop.setProperty(DefaultPropertyValueCoder.PASSPHRASE_SUPPLIER, EncoderConsolePassphraseSupplier.class.getName());
		}
		
		PropertyValueCoder coder = (PropertyValueCoder) Class.forName(propertyValueCoderName).newInstance();
		coder.open(prop);

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
