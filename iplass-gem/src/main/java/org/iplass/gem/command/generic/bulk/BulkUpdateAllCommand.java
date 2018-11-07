package org.iplass.gem.command.generic.bulk;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.generic.search.DetailSearchCommand;
import org.iplass.gem.command.generic.search.FixedSearchCommand;
import org.iplass.gem.command.generic.search.NormalSearchCommand;
import org.iplass.gem.command.generic.search.SearchCommandBase;
import org.iplass.gem.command.generic.search.SearchContext;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMappings;
import org.iplass.mtp.command.annotation.action.ParamMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.command.annotation.action.TokenCheck;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ActionMappings({
	@ActionMapping(name=BulkUpdateAllCommand.BULK_UPDATE_ALL_ACTION_NAME,
			displayName="更新",
			paramMapping={
				@ParamMapping(name=Constants.DEF_NAME, mapFrom="${0}", condition="subPath.length==1"),
				@ParamMapping(name=Constants.VIEW_NAME, mapFrom="${0}", condition="subPath.length==2"),
				@ParamMapping(name=Constants.DEF_NAME, mapFrom="${1}", condition="subPath.length==2")
			},
			result={
				@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.JSP,
						value=Constants.CMD_RSLT_JSP_BULK_EDIT,
						templateName="gem/generic/bulk/bulkEdit",
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION),
				@Result(status=Constants.CMD_EXEC_ERROR, type=Type.JSP,
						value=Constants.CMD_RSLT_JSP_BULK_EDIT,
						templateName="gem/generic/bulk/bulkEdit",
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION),
				@Result(status=Constants.CMD_EXEC_ERROR_TOKEN, type=Type.JSP,
						value=Constants.CMD_RSLT_JSP_ERROR,
						templateName="gem/generic/common/error",
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION),
				@Result(status=Constants.CMD_EXEC_ERROR_VIEW, type=Type.JSP,
						value=Constants.CMD_RSLT_JSP_ERROR,
						templateName="gem/generic/common/error",
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION)
			},
			tokenCheck=@TokenCheck
	)
})
@CommandClass(name = "gem/generic/detail/BulkUpdateAllCommand", displayName = "一括全更新")
public class BulkUpdateAllCommand extends BulkCommandBase {

	private static Logger logger = LoggerFactory.getLogger(BulkUpdateAllCommand.class);

	public static final String BULK_UPDATE_ALL_ACTION_NAME = "gem/generic/bulk/bulkUpdateAll";

	@Override
	public String execute(RequestContext request) {
		String searchType = request.getParam(Constants.SEARCH_TYPE);

		SearchCommandBase command = null;
		if (Constants.SEARCH_TYPE_NORMAL.equals(searchType)) {
			command = new NormalSearchCommand();
		} else if (Constants.SEARCH_TYPE_DETAIL.equals(searchType)) {
			command = new DetailSearchCommand();
		} else if (Constants.SEARCH_TYPE_FIXED.equals(searchType)) {
			command = new FixedSearchCommand();
		}

		String ret = Constants.CMD_EXEC_SUCCESS;
		if (command != null) {
			SearchContext context = command.getContext(request);

			Query query = new Query();
			query.select(Entity.OID, Entity.VERSION, Entity.UPDATE_DATE);
			query.from(context.getDefName());
			query.setWhere(context.getWhere());
			query.setVersiond(context.isVersioned());
			SearchResult<Entity> result = em.searchEntity(query);

			List<Entity> entities = result.getList();
			if (entities.size() > 0) {
				// 先頭に「行番号_」を付加する
				List<String> oid = IntStream.range(0, entities.size())
						.mapToObj(i -> i + "_" + entities.get(i).getOid())
						.collect(Collectors.toList());
				List<String> version = IntStream.range(0, entities.size())
						.mapToObj(i -> i + "_" + entities.get(i).getVersion())
						.collect(Collectors.toList());
				List<String> updateDate = IntStream.range(0, entities.size())
						.mapToObj(i -> i + "_" + entities.get(i).getUpdateDate().getTime())
						.collect(Collectors.toList());

				//大量データを考慮してトランザクションを分割(100件毎)
				int count = oid.size();
				int countPerHundret = count / 100;
				if (count % 100 > 0) countPerHundret++;
				for (int i = 0; i < countPerHundret; i++) {
					int current = i * 100;
					List<String> subOidList = oid.stream()
							.skip(current).limit(100).collect(Collectors.toList());
					List<String> subVersionList = version.stream()
							.skip(current).limit(100).collect(Collectors.toList());
					List<String> subUpdateDate = updateDate.stream()
							.skip(current).limit(100).collect(Collectors.toList());
					ret = Transaction.requiresNew(t -> {
						String r = null;
						try {
							// 一括全更新の場合、リクエストスコープに一括更新しようとするエンティティ情報をセットします。
							request.setAttribute(Constants.OID, subOidList.toArray(new String[] {}));
							request.setAttribute(Constants.VERSION, subVersionList.toArray(new String[] {}));
							request.setAttribute(Constants.TIMESTAMP, subUpdateDate.toArray(new String[] {}));
							// 一括更新処理を呼び出します。
							BulkUpdateListCommand updateCommand = new BulkUpdateListCommand();
							r = updateCommand.execute(request);
						} catch (ApplicationException e) {
							if (logger.isDebugEnabled()) {
								logger.debug(e.getMessage(), e);
							}
							request.setAttribute(Constants.MESSAGE, e.getMessage());
							t.rollback();
							return Constants.CMD_EXEC_ERROR;
						} finally {
							request.removeAttribute(Constants.OID);
							request.removeAttribute(Constants.VERSION);
							request.removeAttribute(Constants.TIMESTAMP);
						}
						return r;
					});
					if (!Constants.CMD_EXEC_SUCCESS.equals(ret)) {
						break;
					}
				}
			}
		}

		return ret;
	}
}
