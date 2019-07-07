package com.scfs.service.interf;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.InvokeTypeEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.common.entity.InvokeLog;
import com.scfs.domain.interf.entity.PMSSupplierBind;
import com.scfs.domain.project.entity.ProjectItem;
import com.scfs.rpc.pms.dto.SupplierPmsHttpResDto;
import com.scfs.rpc.pms.entity.Supplier;
import com.scfs.rpc.pms.entity.SupplierInfo;
import com.scfs.rpc.pms.service.SupplierRpcService;
import com.scfs.service.common.InvokeLogService;
import com.scfs.service.project.ProjectItemService;
import com.scfs.service.support.CacheService;

/**
 * Created by Administrator on 2016年12月20日.
 */
@Service
public class PmsSupplierRpcService {
	private final static Logger LOGGER = LoggerFactory.getLogger(PmsSupplierRpcService.class);
	@Autowired
	private InvokeLogService invokeLogService;
	@Autowired
	private SupplierRpcService supplierRpcService;
	@Autowired
	private ProjectItemService projectItemService;
	@Autowired
	private CacheService cacheService;

	public boolean validateSupplier(String pmsSupplierNo) {
		boolean isSuccess = false;
		Supplier supplier = new Supplier();
		supplier.setProvider_sn(pmsSupplierNo);
		String data = JSON.toJSONString(supplier, SerializerFeature.WriteDateUseDateFormat,
				SerializerFeature.WriteMapNullValue);
		InvokeLog invokeLog = null;
		try {
			invokeLog = save(supplier, data);
		} catch (Exception e) {
			LOGGER.error("新增接口调用日志失败：", e);
			return isSuccess;
		}
		if (null != invokeLog) {
			try {
				SupplierPmsHttpResDto supplierPmsHttpResDto = supplierRpcService.invokeValidateSupplier(data);
				invokeLog.setReturnMsg(supplierPmsHttpResDto.getMsg());
				if (supplierPmsHttpResDto.getFlag().equals(BaseConsts.FLAG_NO)) { // 返回结果N，调用失败
					invokeLogService.invokeError(invokeLog);
				} else {
					invokeLogService.invokeSuccess(invokeLog);
				}
				isSuccess = true;
			} catch (Exception e) {
				LOGGER.error("调用pms校验供应商接口失败：", e);
				invokeLogService.invokeException(invokeLog, e);
			}
		}
		return isSuccess;
	}

	/**
	 * 开启供应商
	 * 
	 * @param pmsSupplierBind
	 * @return
	 */
	public boolean openSupplierInfo(PMSSupplierBind pmsSupplierBind) {
		return uploadSupplierInfo(pmsSupplierBind, BaseConsts.ONE); // 1-开启
	}

	/**
	 * 禁用供应商
	 * 
	 * @param pmsSupplierBind
	 * @return
	 */
	public boolean closeSupplierInfo(PMSSupplierBind pmsSupplierBind) {
		return uploadSupplierInfo(pmsSupplierBind, BaseConsts.TWO); // 2-禁用
	}

	public boolean uploadSupplierInfo(PMSSupplierBind pmsSupplierBind, Integer status) {
		boolean isSuccess = false;
		SupplierInfo supplierInfo = new SupplierInfo();
		supplierInfo.setProvider_sn(pmsSupplierBind.getPmsSupplierNo());
		supplierInfo.setCreate_time(pmsSupplierBind.getCreateAt());
		supplierInfo.setStatus(status);
		ProjectItem projectItem = projectItemService.getProjectItem(pmsSupplierBind.getProjectId());
		if (null == projectItem) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目[" + pmsSupplierBind.getProjectId() + "]条款不存在");
		}
		BaseProject baseProject = cacheService.getProjectById(pmsSupplierBind.getProjectId());
		if (!baseProject.getBizType().equals(BaseConsts.SIX)) { // 6-融通质押
			if (null == projectItem.getSpreadfixedpoints()
					|| DecimalUtil.le(projectItem.getSpreadfixedpoints(), BigDecimal.ZERO)) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
						"项目[" + pmsSupplierBind.getProjectId() + "]条款扣点不能为空且必须大于0");
			}
		}
		BigDecimal spreadfixedpoints = (null == projectItem.getSpreadfixedpoints() ? BigDecimal.ZERO
				: DecimalUtil.multiply(projectItem.getSpreadfixedpoints(), new BigDecimal("100")));
		supplierInfo.setDeduction_point(spreadfixedpoints.setScale(2, BigDecimal.ROUND_HALF_UP));
		// supplierInfo.setDeduction_point(DecimalUtil.multiply(new
		// BigDecimal("0.07996"), new BigDecimal("100")).setScale(2,
		// BigDecimal.ROUND_HALF_UP));

		String data = JSON.toJSONString(supplierInfo, SerializerFeature.WriteDateUseDateFormat,
				SerializerFeature.WriteMapNullValue);
		InvokeLog invokeLog = null;
		try {
			invokeLog = save(supplierInfo, data);
		} catch (Exception e) {
			LOGGER.error("新增接口调用日志失败：", e);
			return isSuccess;
		}
		if (null != invokeLog) {
			try {
				SupplierPmsHttpResDto supplierPmsHttpResDto = supplierRpcService.invokeUploadSupplierInfo(data);
				invokeLog.setReturnMsg(supplierPmsHttpResDto.getMsg());
				if (supplierPmsHttpResDto.getFlag().equals(BaseConsts.FLAG_NO)) { // 返回结果N，调用失败
					invokeLogService.invokeError(invokeLog);
				} else {
					invokeLogService.invokeSuccess(invokeLog);
				}
				isSuccess = true;
			} catch (Exception e) {
				LOGGER.error("调用pms上传供应商信息接口失败：", e);
				invokeLogService.invokeException(invokeLog, e);
			}
		}
		return isSuccess;
	}

	private InvokeLog save(Supplier supplier, String data) throws Exception {
		InvokeLog invokeLog = new InvokeLog();
		if (StringUtils.isNotBlank(data)) {
			invokeLog.setContent(data);
		}
		invokeLog.setInvokeType(InvokeTypeEnum.PMS_SUPPLIER.getType());
		invokeLog.setInvokeMode(BaseConsts.ONE); // 1-实时
		invokeLog.setProvider(BaseConsts.THREE); // 3-pms
		invokeLog.setConsumer(BaseConsts.ONE); // 1-scfs
		invokeLog.setTryAgainFlag(BaseConsts.ZERO); // 0-不可重新调用
		invokeLogService.add(invokeLog);
		return invokeLog;
	}

	private InvokeLog save(SupplierInfo supplierInfo, String data) throws Exception {
		InvokeLog invokeLog = new InvokeLog();
		if (StringUtils.isNotBlank(data)) {
			invokeLog.setContent(data);
		}
		invokeLog.setInvokeType(InvokeTypeEnum.PMS_SUPPLIER_UPLOAD.getType());
		invokeLog.setInvokeMode(BaseConsts.ONE); // 1-实时
		invokeLog.setProvider(BaseConsts.THREE); // 3-pms
		invokeLog.setConsumer(BaseConsts.ONE); // 1-scfs
		invokeLog.setTryAgainFlag(BaseConsts.ZERO); // 0-不可重新调用
		invokeLogService.add(invokeLog);
		return invokeLog;
	}
}
