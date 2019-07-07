package com.scfs.web.config;

import net.sf.jxls.processor.CellProcessor;
import net.sf.jxls.processor.RowProcessor;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/3.
 */
public class JxlsView extends AbstractExcelView {

	private XLSTransformer transformer = new XLSTransformer();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.view.document.AbstractExcelView#
	 * buildExcelDocument(java.util.Map,
	 * org.apache.poi.hssf.usermodel.HSSFWorkbook,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		transformer.transformWorkbook(workbook, model);
	}

	public void setCellProcessor(CellProcessor cellProcessor) {
		transformer.registerCellProcessor(cellProcessor);
	}

	public void setRowProcessor(RowProcessor rowProcessor) {
		transformer.registerRowProcessor(rowProcessor);
	}

}