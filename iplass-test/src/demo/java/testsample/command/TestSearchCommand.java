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

package testsample.command;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.UploadFileHandle;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.Equals;

@CommandClass
public class TestSearchCommand implements Command {
	
	private EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
	
	@Override
	public String execute(RequestContext request) {
		
		String accountId = request.getParam("accountId");
		
		UploadFileHandle upFile = request.getParamAsFile("upFile");
		if (upFile != null) {
			//process upload file
			System.out.println("file uploaded:name=" + upFile.getFileName() + ", size=" + upFile.getSize());
		}
		
		SearchResult<Entity> res = em.searchEntity(
				new Query().select(User.ACCOUNT_ID).from(User.DEFINITION_NAME)
				.where(new Equals(User.ACCOUNT_ID, accountId)));
		
		request.setAttribute("res", res);
		
		return "OK";
	}

}
