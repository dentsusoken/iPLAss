package org.iplass.mtp.web.template.report;

import java.io.InputStream;
import java.io.OutputStream;

import org.jxls.common.Context;

/**
 * <p>
 * JXLS専用帳票出力ロジックインターフェース
 * 
 * JXLSを用いて帳票出力する際に出力処理を記載してもらうインタフェース
 * アプリ担当者は、このインタフェースを継承して独自に帳票処理を記載する事が可能。
 * </p>
 *  
 * @author Y.Ishida
 *
 */
public interface JxlsReportOutputLogic {
	public void reportWrite(InputStream is, OutputStream os, Context context);
}
