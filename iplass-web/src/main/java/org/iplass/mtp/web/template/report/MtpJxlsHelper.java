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

public class MtpJxlsHelper extends JxlsHelper {
	
	public MtpJxlsHelper() {
		super();
	}
	
	public static MtpJxlsHelper getInstance() {
		return new MtpJxlsHelper();
	}
	
    public JxlsHelper processTemplateAtCell(Transformer transformer, Context context,
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
	
    public JxlsHelper processGridTemplate(Transformer transformer, Context context, String objectProps) throws IOException {
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
}
