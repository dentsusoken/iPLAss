/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.web.template.report;

import java.io.IOException;
import java.util.List;

import org.jxls.area.Area;
import org.jxls.command.GridCommand;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.formula.FastFormulaProcessor;
import org.jxls.formula.FormulaProcessor;
import org.jxls.formula.StandardFormulaProcessor;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;

/**
 * <p>
 * JxlsHelperのサブクラス。<BR><BR>
 * 
 * アプリ開発者は、JXLSで帳票出力処理をカスタマイズしたい場合、<BR>
 * 本クラスで定義されたメソッドもしくは、{@link JxlsHelper#processTemplate(Context, Transformer)}を用いて、
 * テンプレートファイルからExcel帳票の生成を実行することが可能。
 * <p>
 * @author Y.Ishida
 *
 */
public class MtpJxlsHelper extends JxlsHelper {
	
	public MtpJxlsHelper() {
		super();
	}
	
	public static MtpJxlsHelper getInstance() {
		return new MtpJxlsHelper();
	}
	
    public MtpJxlsHelper processTemplateAtCell(Transformer transformer, Context context,
            String targetCell) throws IOException {
        getAreaBuilder().setTransformer(transformer);
        List<Area> xlsAreaList = getAreaBuilder().build();
        if (xlsAreaList.isEmpty()) {
            throw new IllegalStateException("No XlsArea were detected for this processing");
        }
        Area firstArea = xlsAreaList.get(0);
        CellRef targetCellRef = new CellRef(targetCell);
        firstArea.applyAt(targetCellRef, context);
        if (isProcessFormulas()) {
            setFormulaProcessor(firstArea);
            firstArea.processFormulas();
        }
        String sourceSheetName = firstArea.getStartCellRef().getSheetName();
        if (!sourceSheetName.equalsIgnoreCase(targetCellRef.getSheetName())) {
            if (isHideTemplateSheet()) {
                transformer.setHidden(sourceSheetName, true);
            }
            if (isDeleteTemplateSheet()) {
                transformer.deleteSheet(sourceSheetName);
            }
        }
        transformer.write();
        return this;
    }
	
    public MtpJxlsHelper processGridTemplate(Transformer transformer, Context context, String objectProps) throws IOException {
        getAreaBuilder().setTransformer(transformer);
        List<Area> xlsAreaList = getAreaBuilder().build();
        for (Area xlsArea : xlsAreaList) {
            GridCommand gridCommand = (GridCommand) xlsArea.getCommandDataList().get(0).getCommand();
            gridCommand.setProps(objectProps);
            setFormulaProcessor(xlsArea);
            xlsArea.applyAt(new CellRef(xlsArea.getStartCellRef().getCellName()), context);
            if (isProcessFormulas()) {
                xlsArea.processFormulas();
            }
        }
        transformer.write();
        return this;
    }
    
    public void processGridTemplateAtCell(Transformer transformer, Context context,
            String objectProps, String targetCell) throws IOException {
        getAreaBuilder().setTransformer(transformer);
        List<Area> xlsAreaList = getAreaBuilder().build();
        Area firstArea = xlsAreaList.get(0);
        CellRef targetCellRef = new CellRef(targetCell);
        GridCommand gridCommand = (GridCommand) firstArea.getCommandDataList().get(0).getCommand();
        gridCommand.setProps(objectProps);
        firstArea.applyAt(targetCellRef, context);
        if (isProcessFormulas()) {
            setFormulaProcessor(firstArea);
            firstArea.processFormulas();
        }
        String sourceSheetName = firstArea.getStartCellRef().getSheetName();
        if (!sourceSheetName.equalsIgnoreCase(targetCellRef.getSheetName())) {
            if (isHideTemplateSheet()) {
                transformer.setHidden(sourceSheetName, true);
            }
            if (isDeleteTemplateSheet()) {
                transformer.deleteSheet(sourceSheetName);
            }
        }
        transformer.write();
    }
    
    private Area setFormulaProcessor(Area xlsArea) {
        FormulaProcessor fp = getFormulaProcessor();
        if (fp == null) {
            if (isUseFastFormulaProcessor()) {
                fp = new FastFormulaProcessor();
            } else {
                fp = new StandardFormulaProcessor();
            }
        }
        xlsArea.setFormulaProcessor(fp);
        return xlsArea;
    }
    
}
