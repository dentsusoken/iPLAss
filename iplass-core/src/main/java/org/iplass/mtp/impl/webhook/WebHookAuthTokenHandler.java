/**
 * 
 */
package org.iplass.mtp.impl.webhook;

import java.io.Serializable;
import java.sql.Timestamp;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.token.AuthTokenInfo;
import org.iplass.mtp.impl.auth.authenticate.token.AuthToken;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenHandler;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenStore;
import org.iplass.mtp.spi.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * データベースでwebHookのパスワードや、トークンを管理するシークレットサービス
 * rdb使用です。
 * 
 * @author lisf06
 *
 */
public class WebHookAuthTokenHandler extends AuthTokenHandler{
	private static Logger logger = LoggerFactory.getLogger(WebHookAuthTokenHandler.class);
	public static final String BASIC_AUTHENTICATION_TYPE = "WHBA";
	public static final String BEARER_AUTHENTICATION_TYPE = "WHBT";
	public static final String HMAC_AUTHENTICATION_TYPE = "WHHM";
	public static final String TYPE_WEBHOOK_AUTHTOKEN_HANDLER="WEBHOOKATH";
	@Override
	protected Serializable createDetails(String seriesString, String tokenString, String userUniqueId,
			String policyName, AuthTokenInfo tokenInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthTokenInfo toAuthTokenInfo(AuthToken authToken) {
		WebHookAuthTokenInfo info = new WebHookAuthTokenInfo();
		info.setType(authToken.getType());
		info.setKey(authToken.getSeries());
		
		return info;
	}

	//retrieve token with tenantid, series and type. from : super.getBySeries(int tenantId, String type, String series)
	
	//creeateToken, from : super.create(AuthToken token)
	/**
	 * database->raw data
	 * 対応の認証の情報Stringを取得
	 * basicはbase64(userName:Password)形式、そのまま使える
	 * */
	public String getSecret(final int tenantId,final String series,final String type) {
		AuthToken token = authTokenStore().getBySeries(tenantId, type, series);
		return token.getToken();
	}
	
	/**
	 * raw data -> database
	 * 外側でseries生成してください
	 */
	public void insertSecret(final int tenantId, final String type, final String metaDataId, final String series, final String tokenSecret) {
		Timestamp startDate = new Timestamp(java.lang.System.currentTimeMillis());
		AuthToken token = new AuthToken(tenantId, type, metaDataId, series, tokenSecret, "", startDate, null);
		authTokenStore().create(token);
	}
	
	/**
	 * raw data -> database
	 */
	public void updateSecret(final int tenantId, final String type, final String metaDataId, final String series, final String tokenSecret) {
		AuthToken oldToken = authTokenStore().getBySeries(tenantId, type, series);
		Timestamp startDate = new Timestamp(java.lang.System.currentTimeMillis());
		AuthToken newToken = new AuthToken(tenantId, type, metaDataId, series, tokenSecret, "", startDate, null);
		authTokenStore().update(newToken, oldToken);
	}
	
	/**
	 * raw data -> database
	 * 特定のデータを削除する
	 */
	public void deleteSecret(final int tenantId, final String type, final String series) {
		authTokenStore().deleteBySeries(tenantId, type, series);
	}
	/**
	 * do not use
	 * */
	@Override
	public Credential toCredential(AuthToken newToken) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
