/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.web.template.tags;

import java.util.regex.Pattern;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.Permission;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 認可情報に従って制御を行うためのJSPタグです。
 * 特定ロールの場合のみボディコンテンツを表示したり、ボディコンテンツの処理を特権実行するなどの制御が可能です。
 * </p>
 * 
 * <p>JSPでの利用例を以下に示します。</p>
 * 
 * <pre>
 * &lt;%{@literal @}page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%&gt;
 * &lt;%{@literal @}taglib prefix="m" uri="http://iplass.org/tags/mtp"%&gt;
 * 
 * :
 * 
 * &lt;!-- roleAまたはroleBの場合のみボディコンテンツを表示します --&gt;
 * &lt;m:auth role="roleA,roleB"&gt;
 *   this content only show with role:"roleA" or "roleB".
 *   :
 *   :
 * &lt;/m:auth&gt;
 * 
 * 
 * 
 * &lt;!-- some/actionXアクションのAction権限を保持する場合ボディコンテンツを表示します --&gt;
 * &lt;m:auth permission="&lt;%=new ActionPermission(\"some/actionX\", new MapActionParameter().put(\"defName\",\"Hoge\"))%&gt;"&gt;
 *   this content only show with action permission:"some/actionX?defName=Hoge".
 *   :
 *   :
 * &lt;/m:auth&gt;
 * 
 * 
 * &lt;!-- ボディコンテンツの処理を特権実行します --&gt;
 * &lt;m:auth privileged="true"&gt;
 *   doPrivileged...
 *   
 *   &lt;%
 *     //some privileged execution
 *     :
 *     :
 *   
 *   %&gt;
 *   :
 *   :
 * &lt;/m:auth&gt;
 * 
 * </pre>
 * 
 * <p>
 * 指定可能な属性の説明
 * <table border="1">
 * <tr>
 * <th>属性名</th><th>Script可</th><th>デフォルト値</th><th>説明</th>
 * </tr>
 * <tr>
 * <td>role</td><td>○</td><td>&nbsp;</td><td>
 * ロール名指定します。<br>
 * 当該ロールを保持する場合、ボディコンテンツが出力されます。
 * 複数のロール名をカンマ区切りで指定することが可能です。
 * 複数指定された場合、いずれかのロールを保持する場合にボディコンテンツが出力されます。
 * </td>
 * </tr>
 * <tr>
 * <td>permission</td><td>○</td><td>&nbsp;</td><td>
 * {@link Permission}のインスタンスを指定します。
 * 当該権限を保持する場合、ボディコンテンツが出力されます。
 * </td>
 * </tr>
 * <tr>
 * <td>privileged</td><td>○</td><td>false</td><td>
 * trueが指定された場合、ボディコンテンツの出力処理を特権実行します。
 * </td>
 * </tr>
 * </table>
 * </p>
 * 
 * @author K.Higuchi
 *
 */
public class AuthTag extends TagSupport implements TryCatchFinally {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(AuthTag.class);
	
	private static Pattern sp = Pattern.compile("[,\\s]+");
	
	private Boolean privileged = Boolean.FALSE;
	private String role;
	private Permission permission;
	
	private AuthService authService = ServiceRegistry.getRegistry().getService(AuthService.class);
	private AuthContextHolder doAuthContext;
	private AuthContextHolder prev;
	
	public Boolean getPrivileged() {
		return privileged;
	}

	public void setPrivileged(Boolean privileged) {
		this.privileged = privileged;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	@Override
	public int doStartTag() throws JspException {
		AuthContext auth = AuthContext.getCurrentContext();
		if (role != null) {
			boolean userInRole = false;
			if (role.indexOf(',') < 0) {
				userInRole = auth.userInRole(role);
			} else {
				String[] roles = sp.split(role);
				for (String r: roles) {
					userInRole |= userInRole | auth.userInRole(r);
					if (userInRole) {
						break;
					}
				}
			}
			if (!userInRole) {
				if (log.isDebugEnabled()) {
					log.debug("userInRole(\"" + role + "\") == false, SKIP_BODY");
				}
				return SKIP_BODY;
			}
		}
		
		if (permission != null) {
			if (!auth.checkPermission(permission)) {
				if (log.isDebugEnabled()) {
					log.debug("checkPermission(" + permission + ") == false, SKIP_BODY");
				}
				return SKIP_BODY;
			}
		}
		
		if (privileged != null && privileged.booleanValue()) {
			doAuthContext = AuthContextHolder.getAuthContext().privilegedAuthContextHolder();
			ExecuteContext exec = ExecuteContext.getCurrentContext();
			prev = authService.doSecuredActionPre(doAuthContext, exec);
		}
		
		return EVAL_BODY_INCLUDE;
	}
	
	@Override
	public void doCatch(Throwable t) throws Throwable {
		throw t;
	}
	
	@Override
	public void doFinally() {
		
		if (privileged != null && privileged.booleanValue()) {
			ExecuteContext exec = ExecuteContext.getCurrentContext();
			authService.doSecuredActionPost(doAuthContext, true, prev, exec);
		}
		
		doAuthContext = null;
		prev = null;
		
		privileged = Boolean.FALSE;
		role = null;
		permission = null;
	}

}
