package com.scfs.web.config;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

/**
 * <P>
 * xls view
 * </P>
 *
 */
public abstract class AbstractExcelView extends AbstractUrlBasedView {

	protected Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * The content type for an Excel response
	 */
	private static final String CONTENT_TYPE = "application/vnd.ms-excel";

	/**
	 * Default Constructor. Sets the content type of the view to
	 * "application/vnd.ms-excel".
	 */
	public AbstractExcelView() {
		setContentType(CONTENT_TYPE);
	}

	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}

	/**
	 * Renders the Excel view, given the specified model.
	 */
	@Override
	protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Workbook workbook;
		if (this.getUrl() != null) {
			workbook = getTemplateSource(this.getUrl(), request);
		} else {
			workbook = new HSSFWorkbook();
			log.debug("Created Excel Workbook from scratch");
		}

		buildExcelDocument(model, workbook, request, response);

		// Set the content type.
		response.setContentType(getContentType());

		// Should we set the content length here?
		// response.setContentLength(workbook.getBytes().length);

		// Flush byte array to servlet output stream.
		ServletOutputStream out = response.getOutputStream();
		workbook.write(out);
		out.flush();
		out.close();
	}

	/**
	 * Creates the workbook from an existing XLS document.
	 *
	 * @param url
	 *            the URL of the Excel template without localization part nor
	 *            extension
	 * @param request
	 *            current HTTP request
	 * @return the HSSFWorkbook
	 * @throws Exception
	 *             in case of failure
	 */
	protected Workbook getTemplateSource(String url, HttpServletRequest request) throws Exception {
		File file = null;
		if (url != null && url.startsWith("/WEB-INF")) {
			file = new File(request.getSession().getServletContext().getRealPath(url));
		} else {
			file = ResourceUtils.getFile(url);
		}

		// Create the Excel document from the source.
		if (log.isDebugEnabled()) {
			log.debug("Loading Excel workbook from " + url);
		}
		Workbook workbook = WorkbookFactory.create(new FileInputStream(file));
		return workbook;
	}

	/**
	 * Subclasses must implement this method to create an Excel HSSFWorkbook
	 * document, given the model.
	 *
	 * @param model
	 *            the model Map
	 * @param workbook
	 *            the Excel workbook to complete
	 * @param request
	 *            in case we need locale etc. Shouldn't look at attributes.
	 * @param response
	 *            in case we need to set cookies. Shouldn't write to it.
	 */
	protected abstract void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception;

}
