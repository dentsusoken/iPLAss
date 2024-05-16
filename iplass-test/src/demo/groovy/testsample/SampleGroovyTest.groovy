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

package testsample

import static org.junit.Assert.*;

import org.iplass.mtp.test.AuthUser;
import org.iplass.mtp.test.MTPJUnitTestExtension
import org.iplass.mtp.test.MTPTest;
import org.iplass.mtp.test.TenantName;
import org.iplass.mtp.test.TestRequestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith

@TenantName("testDemo1")
@AuthUser(userId="testuser", password="testtest")
@ExtendWith(MTPJUnitTestExtension.class)
class SampleGroovyTest {

	@Test
	void testGroovyCommand() {

		TestRequestContext req = new TestRequestContext();
		req.setParam("accountId", "testuser");

		String status = MTPTest.invokeCommand("testsample/command/TestSearchGroovyCommand", req);

		assertEquals("OK", status);
		assertEquals("testuser", req.res.first.accountId);
		assertEquals("stringFromTestUtil", req.utilClassRes);
	}

	@Test
	void testUtilityClass() {

		//call static method
		Class testUtilClass = MTPTest.getUtilityClass("testsample.TestUtil");
		assertEquals("stringFromTestUtil", testUtilClass.staticMethedCall());

		//create instance with args
		def bean1 = MTPTest.newUC("testsample.TestUtil", "hoge");
		assertEquals("hoge", bean1.strVal);

		def bean2 = MTPTest.newUC("testsample.TestUtil", 5);
		assertEquals(5, bean2.intVal);

		def bean3 = MTPTest.newUC("testsample.TestUtil", "fuga", 10);
		assertEquals("fuga", bean3.strVal);
		assertEquals(10, bean3.intVal);

		bean3.addIntVal(5);
		assertEquals(15, bean3.intVal);
	}
}
