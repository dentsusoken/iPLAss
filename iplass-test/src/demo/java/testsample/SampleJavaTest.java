/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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

package testsample;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Proxy;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.Group;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.EntityValidationException;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.test.AuthUser;
import org.iplass.mtp.test.MTPJUnitTestRule;
import org.iplass.mtp.test.MTPTest;
import org.iplass.mtp.test.NoAuthUser;
import org.iplass.mtp.test.TenantName;
import org.iplass.mtp.test.TestRequestContext;
import org.iplass.mtp.test.TestUploadFileHandle;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

@TenantName("testDemo1")
@AuthUser(userId="testuser", password="testtest")
public class SampleJavaTest {
	
	static final String COMMAND_NAME = "testsample/command/TestSearchCommand";
	
	@Rule
	public MTPJUnitTestRule rule = new MTPJUnitTestRule();
	
	//for upload test
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	
	@Test
	public void searchByCommand() throws IOException {
		TestRequestContext req = new TestRequestContext();
		req.setParam("accountId", "testuser");
		
		//if test upload file, use TestUploadFileHandle
		File upFile = tempFolder.newFile("upFile.txt");
		TestUploadFileHandle uh = new TestUploadFileHandle(upFile, "upFile.txt", "text/plain");
		req.setParam("upFile", uh);
		
		String status = MTPTest.invokeCommand(COMMAND_NAME, req);
		
		assertEquals("OK", status);
		
		@SuppressWarnings("unchecked")
		SearchResult<Entity> res = (SearchResult<Entity>) req.getAttribute("res");
		assertEquals("testuser", res.getFirst().getValue(User.ACCOUNT_ID));
	}
	
	@Test
	public void searchByCommandWithMock() {
		
		//Create and set mock
		EntityManager mock = (EntityManager) Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader(), new Class<?>[]{EntityManager.class},
				(proxy, method, args) -> {
					return null;
				});
		MTPTest.setManagerMock(EntityManager.class, mock);

		//Test
		TestRequestContext req = new TestRequestContext();
		req.setParam("accountId", "testuser");
		
		MTPTest.invokeCommand(COMMAND_NAME, req);
		
		assertNull(req.getAttribute("res"));
	}
	
	@Test(expected = EntityValidationException.class)
	public void testWithTransaction() {
		
		//MTPTest#invokeCommand is automatically begin/end transaction
		TestRequestContext req = new TestRequestContext();
		req.setParam("accountId", "testuser");
		MTPTest.invokeCommand(COMMAND_NAME, req);

		
		//if you need to transaction-ed test code, use MTPTest#transaction
		MTPTest.transaction(() -> {
			
			EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
			Group g = new Group();
			g.setCode("testGroup");
			em.insert(g);
			
		});
		
	}
	
	@Test
    @AuthUser(userId="testuser2", password="testtest")
	public void runAnotherUser() {
		
		AuthContext auth = AuthContext.getCurrentContext();
		assertEquals("testuser2", auth.getUser().getAccountId());
	}
	
	@Test
	@NoAuthUser
	public void runNoAuth() {
		
		AuthContext auth = AuthContext.getCurrentContext();
		assertFalse(auth.isAuthenticated());
	}
	
	@Test
	@TenantName("testDemo2")
	@AuthUser(userId="test", password="testtest")
	public void runAnotherTenant() {
		TestRequestContext req = new TestRequestContext();
		req.setParam("accountId", "test");
		
		String status = MTPTest.invokeCommand(COMMAND_NAME, req);
		
		assertEquals("OK", status);
		
		@SuppressWarnings("unchecked")
		SearchResult<Entity> res = (SearchResult<Entity>) req.getAttribute("res");
		assertEquals("test", res.getFirst().getValue(User.ACCOUNT_ID));
	}
	
}
