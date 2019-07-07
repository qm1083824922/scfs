package com.scfs.service.common;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.common.AsyncExcelDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.CodeValue;
import com.scfs.domain.common.dto.req.AsyncExcelReqDto;
import com.scfs.domain.common.dto.resp.AsyncExcelResDto;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.result.PageResult;
import com.scfs.service.support.ServiceSupport;
import com.scfs.service.util.XlsWorkbookUtil;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/2/15.
 */
@Service("asyncExcelService")
public class AsyncExcelService {

	private final static Logger LOGGER = LoggerFactory.getLogger(AsyncExcelService.class);

	@Value("${excel.save.path.prex}")
	private String excelSavePathPrex;

	@Autowired
	private AsyncExcelDao asyncExcelDao;

	public void addAsyncExcel(Serializable paramObj, AsyncExcel asyncExcel) {
		if (paramObj != null) {
			asyncExcel.setArgs(XlsWorkbookUtil.obj2Bytes(paramObj));
		}
		Date d = new Date();
		asyncExcel.setName(asyncExcel.getName() + "_" + DateFormatUtils.format("yyyy-MM-dd_HH-mm-ss", d) + ".xls");
		asyncExcel.setExcelPath(excelSavePathPrex);
		asyncExcel.setCreateAt(d);
		asyncExcel.setCreatorId(ServiceSupport.getUser().getId());
		asyncExcel.setCreator(ServiceSupport.getUser().getChineseName());
		asyncExcel.setYn(BaseConsts.ZERO);
		asyncExcelDao.insert(asyncExcel);
	}

	public void downExcelFileById(int id, HttpServletResponse response) {
		try {
			AsyncExcel asyncExcelWithBLOBs = asyncExcelDao.queryAsyncExcelById(id);
			String fileName = asyncExcelWithBLOBs.getName();
			OutputStream os = response.getOutputStream();
			response.reset();
			response.addHeader("Content-Disposition",
					"attachment; filename=" + new String(fileName.getBytes("GBK"), "ISO8859-1"));
			response.setHeader("Content-Type", "application/vnd.ms-excel");
			os.write(FileUtils.readFileToByteArray(new File(asyncExcelWithBLOBs.getExcelPath())));
			os.flush();
		} catch (Exception e) {
			LOGGER.error("【{}】下载Excel文件异常：", id, e);
		}
	}

	public PageResult<AsyncExcelResDto> queryAsyncExcelList(AsyncExcelReqDto asyncExcelReqDto) {
		PageResult<AsyncExcelResDto> pageResult = new PageResult<AsyncExcelResDto>();
		int offSet = PageUtil.getOffSet(asyncExcelReqDto.getPage(), asyncExcelReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, asyncExcelReqDto.getPer_page());
		asyncExcelReqDto.setUserId(ServiceSupport.getUser().getId());
		List<AsyncExcelResDto> vos = new ArrayList<AsyncExcelResDto>();
		List<AsyncExcel> asyncExcelWithBLOBsList = asyncExcelDao.queryAsyncExcelByCon(asyncExcelReqDto, rowBounds);
		for (AsyncExcel asyncExcelWithBLOBs : asyncExcelWithBLOBsList) {
			AsyncExcelResDto res = convertToResDto(asyncExcelWithBLOBs);
			vos.add(res);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), asyncExcelReqDto.getPer_page());
		pageResult.setItems(vos);
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(asyncExcelReqDto.getPage());
		pageResult.setPer_page(asyncExcelReqDto.getPer_page());
		return pageResult;
	}

	private AsyncExcelResDto convertToResDto(AsyncExcel asyncExcelWithBLOBs) {
		AsyncExcelResDto res = new AsyncExcelResDto();
		BeanUtils.copyProperties(asyncExcelWithBLOBs, res);
		res.setPoTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.FILE_BUS_TYPE, res.getPoType() + ""));
		StringBuilder con = new StringBuilder();
		if (null != asyncExcelWithBLOBs.getArgs()) {
			Object ars1 = XlsWorkbookUtil.bytes2Obj(asyncExcelWithBLOBs.getArgs());
			con.append(JSONObject.toJSONString(ars1));
		}
		res.setArg1(con.toString());
		// 获取操作权限
		if (asyncExcelWithBLOBs.getResult() != null && asyncExcelWithBLOBs.getResult() == BaseConsts.ZERO) {// 导出成功，才显示下载
			res.setOpertaList(getOperList());
		}
		return res;
	}

	private List<CodeValue> getOperList() {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState();
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				AsyncExcelResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getOperListByState() {
		List<String> opertaList = Lists.newArrayList(1);
		opertaList.add(OperateConsts.DOWNLOAD);
		return opertaList;
	}
}
