package com.scfs.service.support;

import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import net.sf.jxls.reader.ReaderBuilder;
import net.sf.jxls.reader.XLSDataReadException;
import net.sf.jxls.reader.XLSReader;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/9.
 */
public class ExcelService {
	/**
	 * 解析EXCEL
	 *
	 * @param importFile excel输入流
	 * @param xmlConfig  解析excel配置文件路径
	 * @param beans 输出解析后的结果bean
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map resolverExcel(MultipartFile importFile, String xmlConfig, Map beans) {
		InputStream inputXML = null;
		try {
			inputXML = ExcelService.class.getResourceAsStream(xmlConfig);
			XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
			// 通过XSLReader 的read方法，它会自动映射pojo类，得到数据集合
			mainReader.read(importFile.getInputStream(), beans);
			IOUtils.closeQuietly(inputXML);
		} catch (XLSDataReadException e) {
			throw new BaseException(ExcMsgEnum.RESOLVER_EXCEL_ERROR, e.getCellName());
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (inputXML != null) {
				IOUtils.closeQuietly(inputXML);
			}
		}
		return beans;
	}
}
