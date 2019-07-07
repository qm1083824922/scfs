package com.scfs.service.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.IOUtil;
import com.scfs.domain.common.entity.AsyncExcel;

import net.sf.jxls.transformer.XLSTransformer;

/**
 * Created by Administrator on 2017/2/16.
 */
public class XlsWorkbookUtil {

	private final static XLSTransformer transformer = new XLSTransformer();

	private final static Logger LOGGER = LoggerFactory.getLogger(XlsWorkbookUtil.class);

	public static void buildWorkbook(Map<String, Object> model, AsyncExcel asyncExcel) throws Exception {
		FileInputStream fileInputStream = null;
		OutputStream outputStream = null;
		try {
			WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
			ServletContext servletContext = webApplicationContext.getServletContext();
			// "/WEB-INF/excel/export/logistics/bill_delivery_list.xls"
			String realPath = servletContext.getRealPath(asyncExcel.getTemplatePath());
			LOGGER.info(asyncExcel.getPoType() + "模板真实路径：" + realPath);
			File file = new File(realPath);

			fileInputStream = new FileInputStream(file);
			Workbook workbook = WorkbookFactory.create(fileInputStream);
			transformer.transformWorkbook(workbook, model);
			Date date = new Date();
			String d = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, date);
			File saveFileDir = new File(asyncExcel.getExcelPath() + File.separator + d);
			if (!saveFileDir.exists()) {
				saveFileDir.mkdirs();
			}
			String fileRealPath = asyncExcel.getExcelPath() + File.separator + d + File.separator
					+ asyncExcel.getName();
			asyncExcel.setExcelPath(fileRealPath);
			outputStream = new FileOutputStream(new File(fileRealPath));
			workbook.write(outputStream);
		} finally {
			IOUtil.closeQuietly(fileInputStream, outputStream);
		}
	}

	public static Object bytes2Obj(byte[] bytes) {
		InputStream inputStream = new ByteArrayInputStream(bytes);
		ObjectInputStream objectInputStream = null;
		try {
			objectInputStream = new ObjectInputStream(inputStream);
			return objectInputStream.readObject();
		} catch (Exception e) {
			LOGGER.error("反序列化异常：", e);
		} finally {
			IOUtil.closeQuietly(inputStream, objectInputStream);
		}
		return null;
	}

	public static byte[] obj2Bytes(Object obj) {
		if (obj == null) {
			return null;
		} else {
			ByteArrayOutputStream byteArrayOutputStream = null;
			ObjectOutputStream objectOutputStream = null;
			try {
				byteArrayOutputStream = new ByteArrayOutputStream();
				objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
				objectOutputStream.writeObject(obj);
				byte[] bytes = byteArrayOutputStream.toByteArray();
				return bytes;
			} catch (Exception e) {
				LOGGER.error("序列化异常：", e);
			} finally {
				IOUtil.closeQuietly(byteArrayOutputStream, objectOutputStream);
			}
		}
		return null;
	}

}
