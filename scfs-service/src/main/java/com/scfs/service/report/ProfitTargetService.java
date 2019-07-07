package com.scfs.service.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.report.ProfitReportMonthDao;
import com.scfs.dao.report.ProfitTargetDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.report.entity.MounthProfitReport;
import com.scfs.domain.report.entity.ProfitTarget;
import com.scfs.domain.report.req.ProfitReportReqMonthDto;
import com.scfs.domain.report.req.ProfitTargetReqDto;
import com.scfs.domain.report.resp.ProfitTargetResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.ProfitTargetAuditService;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.CommonService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 *
 *  File: ProfitTargetService.java
 *  Description: 业务指标目标值
 *  TODO
 *  Date,                   Who,
 *  2017年07月17日         Administrator
 *
 * </pre>
 */
@Service
public class ProfitTargetService {
	@Autowired
	private ProfitTargetDao profitTargetDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private ProfitTargetAuditService profitTargetAuditService;
	@Autowired
	private AuditFlowService auditFlowService;
	@Autowired
	private ProfitReportMonthDao profitReportMonthDao;
	@Autowired
	private CommonService commonService;

	/**
	 * 获取列表信息
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<ProfitTargetResDto> queryProfitTargeResult(ProfitTargetReqDto reqDto) {
		List<Integer> userIds = new ArrayList<Integer>();
		if (ServiceSupport.isAllowPerm(BusUrlConsts.QUERY_PROFIT_TARGET_POWER)) {// 判断用户是否拥有权限
			Integer departmentId = ServiceSupport.getUser().getDepartmentId();
			List<BaseUser> baseUserList = cacheService.getUsersByDepartmentId(departmentId);// 获取部门下用户
			if (!CollectionUtils.isEmpty(baseUserList)) {
				for (BaseUser baseUser : baseUserList) {
					userIds.add(baseUser.getId());
				}
			}
			if (userIds.size() == BaseConsts.ZERO) {
				userIds.add(BaseConsts.ZERO);
			}
		} else {
			List<CodeValue> codeList = commonService.getAllCdByKey("USER_PROJECT");
			if (!ListUtil.isEmpty(codeList)) {
				for (CodeValue codeValue : codeList) {
					Integer projectId = Integer.parseInt(codeValue.getCode());
					BaseProject baseProject = cacheService.getProjectById(projectId);
					if (baseProject != null && baseProject.getBizSpecialId() != null) {
						userIds.add(baseProject.getBizSpecialId());
					}
				}
			}
		}
		reqDto.setUserIds(userIds);
		PageResult<ProfitTargetResDto> pageResult = new PageResult<ProfitTargetResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<ProfitTargetResDto> resDto = convertToProfitTargetResDtos(
				profitTargetDao.queryResultsByCon(reqDto, rowBounds));
		pageResult.setItems(resDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(reqDto.getPage());
		pageResult.setPer_page(reqDto.getPer_page());

		if (reqDto.getNeedSum() != null && reqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			if (!CollectionUtils.isEmpty(resDto)) {
				BigDecimal targetProfitAmount = BigDecimal.ZERO;// 利润目标值;
				BigDecimal targetBizManager = BigDecimal.ZERO;// 业务利润目标值
				BigDecimal targetSaleBlance = BigDecimal.ZERO;// 销售毛利润目标值
				BigDecimal targetSaleAmount = BigDecimal.ZERO;// 经营收入目标值
				BigDecimal targetManageAmount = BigDecimal.ZERO;// 管理费用目标值
				BigDecimal targetWarehouseAmount = BigDecimal.ZERO;// 经营费用目标值
				BigDecimal targetFundVost = BigDecimal.ZERO;// 资金成本目标值
				BigDecimal profitAmount = BigDecimal.ZERO;// 利润完成值
				BigDecimal bizManagerAmount = BigDecimal.ZERO;// 业务利润完成值
				BigDecimal saleBlanceAmount = BigDecimal.ZERO;// 销售毛利润完成值
				BigDecimal saleAmount = BigDecimal.ZERO;// 经营收入完成值
				BigDecimal manageAmount = BigDecimal.ZERO;// 管理费用完成值
				BigDecimal warehouseAmount = BigDecimal.ZERO;// 经营费用完成值
				BigDecimal fundCost = BigDecimal.ZERO;// 资金成本完成值
				String profitAmountRateStr = "0.00%";// 利润完成比例
				String bizManagerAmountRateStr = "0.00%";// 业务利润完成比例
				String saleBlanceAmountRateStr = "0.00%";// 销售毛利润完成比例
				String saleAmountRateStr = "0.00%";// 经营收入完成比例
				String manageAmountRateStr = "0.00%";// 管理费用完成比例
				String warehouseAmountRateStr = "0.00%";// 经营费用完成比例
				String fundCostRateStr = "0.00%";// 资金成本完成比例

				List<ProfitTargetResDto> resDtoSum = convertToProfitTargetResDtos(
						profitTargetDao.queryResultsByCon(reqDto));
				for (ProfitTargetResDto targetModel : resDtoSum) {
					if (targetModel != null) {
						targetProfitAmount = DecimalUtil.add(targetProfitAmount, targetModel.getTargetProfitAmount());
						targetBizManager = DecimalUtil.add(targetBizManager, targetModel.getTargetBizManager());
						targetSaleBlance = DecimalUtil.add(targetSaleBlance, targetModel.getTargetSaleBlance());
						targetSaleAmount = DecimalUtil.add(targetSaleAmount, targetModel.getTargetSaleAmount());
						targetManageAmount = DecimalUtil.add(targetManageAmount, targetModel.getTargetManageAmount());
						targetWarehouseAmount = DecimalUtil.add(targetWarehouseAmount,
								targetModel.getTargetWarehouseAmount());
						targetFundVost = DecimalUtil.add(targetFundVost, targetModel.getTargetFundVost());

						profitAmount = DecimalUtil.add(profitAmount, targetModel.getProfitAmount());
						bizManagerAmount = DecimalUtil.add(bizManagerAmount, targetModel.getBizManagerAmount());
						saleBlanceAmount = DecimalUtil.add(saleBlanceAmount, targetModel.getSaleBlanceAmount());
						saleAmount = DecimalUtil.add(saleAmount, targetModel.getSaleAmount());
						manageAmount = DecimalUtil.add(targetProfitAmount, targetModel.getManageAmount());
						warehouseAmount = DecimalUtil.add(warehouseAmount, targetModel.getWarehouseAmount());
						fundCost = DecimalUtil.add(fundCost, targetModel.getFundCost());
					}

					if (DecimalUtil.gt(targetProfitAmount, BigDecimal.ZERO)) {
						if (DecimalUtil.le(profitAmount, BigDecimal.ZERO)) {
							profitAmount = BigDecimal.ZERO;
						}
						profitAmountRateStr = DecimalUtil
								.toPercentString(DecimalUtil.divide(profitAmount, targetProfitAmount));
					}
					if (DecimalUtil.gt(targetBizManager, BigDecimal.ZERO)) {
						if (DecimalUtil.le(bizManagerAmount, BigDecimal.ZERO)) {
							bizManagerAmount = BigDecimal.ZERO;
						}
						profitAmountRateStr = DecimalUtil
								.toPercentString(DecimalUtil.divide(bizManagerAmount, targetBizManager));
					}
					if (DecimalUtil.gt(targetSaleBlance, BigDecimal.ZERO)) {
						if (DecimalUtil.le(saleBlanceAmount, BigDecimal.ZERO)) {
							saleBlanceAmount = BigDecimal.ZERO;
						}
						saleBlanceAmountRateStr = DecimalUtil
								.toPercentString(DecimalUtil.divide(saleBlanceAmount, targetSaleBlance));
					}
					if (DecimalUtil.gt(targetSaleAmount, BigDecimal.ZERO)) {
						if (DecimalUtil.le(saleAmount, BigDecimal.ZERO)) {
							saleAmount = BigDecimal.ZERO;
						}
						saleAmountRateStr = DecimalUtil
								.toPercentString(DecimalUtil.divide(saleAmount, targetSaleAmount));
					}
					if (DecimalUtil.gt(targetManageAmount, BigDecimal.ZERO)) {
						if (DecimalUtil.le(manageAmount, BigDecimal.ZERO)) {
							manageAmount = BigDecimal.ZERO;
						}
						manageAmountRateStr = DecimalUtil
								.toPercentString(DecimalUtil.divide(manageAmount, targetManageAmount));
					}
					if (DecimalUtil.gt(targetWarehouseAmount, BigDecimal.ZERO)) {
						if (DecimalUtil.le(warehouseAmount, BigDecimal.ZERO)) {
							warehouseAmount = BigDecimal.ZERO;
						}
						warehouseAmountRateStr = DecimalUtil
								.toPercentString(DecimalUtil.divide(warehouseAmount, targetWarehouseAmount));
					}
					if (DecimalUtil.gt(targetFundVost, BigDecimal.ZERO)) {
						if (DecimalUtil.le(fundCost, BigDecimal.ZERO)) {
							fundCost = BigDecimal.ZERO;
						}
						fundCostRateStr = DecimalUtil.toPercentString(DecimalUtil.divide(fundCost, targetFundVost));
					}
				}
				String totalStr = "利润目标值  : " + DecimalUtil.formatScale2(targetProfitAmount) + " &nbsp;  利润完成值: "
						+ DecimalUtil.formatScale2(profitAmount) + " &nbsp;  利润完成比例: " + profitAmountRateStr
						+ " &nbsp;&nbsp;&nbsp;  业务利润目标值: " + DecimalUtil.formatScale2(targetBizManager)
						+ " &nbsp;业务利润完成值: " + DecimalUtil.formatScale2(bizManagerAmount) + " &nbsp;业务利润完成比例: "
						+ bizManagerAmountRateStr + " &nbsp;&nbsp;&nbsp;  销售毛利润目标值: "
						+ DecimalUtil.formatScale2(targetSaleBlance) + " &nbsp;销售毛利完成值: "
						+ DecimalUtil.formatScale2(saleBlanceAmount) + " &nbsp;销售毛利比例: " + saleBlanceAmountRateStr
						+ " &nbsp;&nbsp;&nbsp;  经营收入目标值: " + DecimalUtil.formatScale2(targetSaleAmount)
						+ " &nbsp;经营收入完成值: " + DecimalUtil.formatScale2(saleAmount) + " &nbsp;经营收入完成比例: "
						+ saleAmountRateStr + " &nbsp;&nbsp;&nbsp;  管理费用目标值: "
						+ DecimalUtil.formatScale2(targetManageAmount) + " &nbsp;管理费用完成值: "
						+ DecimalUtil.formatScale2(manageAmount) + " &nbsp;管理费用完成比例: " + manageAmountRateStr
						+ " &nbsp;&nbsp;&nbsp;  经营费用目标值: " + DecimalUtil.formatScale2(targetWarehouseAmount)
						+ " &nbsp;经营费用完成值: " + DecimalUtil.formatScale2(warehouseAmount) + " &nbsp;经营费用完成比例: "
						+ warehouseAmountRateStr + " &nbsp;&nbsp;&nbsp;  资金成本目标值: "
						+ DecimalUtil.formatScale2(targetFundVost) + " &nbsp;资金成本完成值: "
						+ DecimalUtil.formatScale2(fundCost) + " &nbsp;资金成本完成比例: " + fundCostRateStr;
				pageResult.setTotalStr(totalStr);
			}
		}
		return pageResult;
	}

	/**
	 * 添加数据
	 * 
	 * @param profitTarget
	 * @return
	 */
	public int createProfitTarge(ProfitTarget profitTarget) {
		profitTarget.setState(BaseConsts.ZERO);
		profitTarget.setCreatorId(ServiceSupport.getUser().getId());
		profitTarget.setCreator(ServiceSupport.getUser().getChineseName());
		int id = profitTargetDao.insert(profitTarget);
		if (id <= BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(profitTarget));
		}
		return profitTarget.getId();
	}

	/**
	 * 更新信息
	 * 
	 * @param profitTarget
	 * @return
	 */
	public BaseResult updateProfitTarge(ProfitTarget profitTarget) {
		BaseResult baseResult = new BaseResult();
		int result = profitTargetDao.updateById(profitTarget);
		if (result == BaseConsts.ZERO) {
			baseResult.setSuccess(false);
			baseResult.setMsg("更新，请重试");
		}
		return baseResult;
	}

	/**
	 * 提交
	 * 
	 * @param profitTarget
	 * @return
	 */
	public BaseResult submitProfitTarge(ProfitTarget profitTarget) {
		ProfitTarget model = profitTargetDao.queryEntityById(profitTarget.getId());
		BaseResult baseResult = new BaseResult();
		AuditNode startAuditNode = auditFlowService.getStartAuditNode(BaseConsts.INT_23, null);
		if (null == startAuditNode) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置审核流");
		}
		profitTarget.setState(startAuditNode.getAuditNodeState());
		int result = profitTargetDao.updateById(profitTarget);
		profitTargetAuditService.startAudit(model, startAuditNode);
		if (result == BaseConsts.ZERO) {
			baseResult.setSuccess(false);
			baseResult.setMsg("提交失败，请重试");
		}
		return baseResult;
	}

	/**
	 * 删除信息
	 * 
	 * @param profitTarget
	 * @return
	 */
	public BaseResult deleteProfitTarge(ProfitTarget profitTarget) {
		BaseResult baseResult = new BaseResult();
		profitTargetDao.queryEntityById(profitTarget.getId());
		profitTargetDao.deleteById(profitTarget.getId());
		return baseResult;
	}

	/**
	 * 详情信息
	 * 
	 * @param profitTarget
	 * @return
	 */
	public Result<ProfitTargetResDto> detailProfitTargeById(ProfitTarget profitTarget) {
		Result<ProfitTargetResDto> result = new Result<ProfitTargetResDto>();
		ProfitTargetResDto resDto = convertToProfitTargetResDto(profitTargetDao.queryEntityById(profitTarget.getId()));
		result.setItems(resDto);
		return result;
	}

	public ProfitTargetResDto detailProfitTargeById(Integer id) {
		ProfitTargetResDto result = convertToProfitTargetResDto(profitTargetDao.queryEntityById(id));
		return result;
	}

	public ProfitTargetResDto queryProfitTargeById(Integer id) {
		return convertToProfitTargetResDto(profitTargetDao.queryEntityById(id));
	}

	public List<ProfitTargetResDto> convertToProfitTargetResDtos(List<ProfitTarget> targetList) {
		List<ProfitTargetResDto> profitTargetResDto = new ArrayList<ProfitTargetResDto>();
		if (ListUtil.isEmpty(targetList)) {
			return profitTargetResDto;
		}
		for (ProfitTarget profitTarget : targetList) {
			ProfitTargetResDto crofitTargetResDto = convertToProfitTargetResDto(profitTarget);
			List<CodeValue> operList = getOperList(profitTarget.getState());
			crofitTargetResDto.setOpertaList(operList);
			profitTargetResDto.add(crofitTargetResDto);
		}
		return profitTargetResDto;
	}

	public ProfitTargetResDto convertToProfitTargetResDto(ProfitTarget model) {
		ProfitTargetResDto result = new ProfitTargetResDto();
		if (model != null) {
			result.setId(model.getId());
			result.setIssue(model.getIssue());
			result.setUserId(model.getUserId());
			result.setUserIdName(cacheService.getUserChineseNameByid(model.getUserId()));
			result.setTargetProfitAmount(model.getTargetProfitAmount());
			result.setTargetBizManager(model.getTargetBizManager());
			result.setTargetSaleBlance(model.getTargetSaleBlance());
			result.setTargetSaleAmount(model.getTargetSaleAmount());
			result.setTargetManageAmount(model.getTargetManageAmount());
			result.setTargetWarehouseAmount(model.getTargetWarehouseAmount());
			result.setTargetFundVost(model.getTargetFundVost());
			result.setBusiId(model.getBusiId());
			result.setBusiIdName(cacheService.getUserChineseNameByid(model.getBusiId()));
			result.setDeptManageId(model.getDeptManageId());
			result.setDeptManageIdName(cacheService.getUserChineseNameByid(model.getDeptManageId()));
			result.setState(model.getState());
			result.setStateName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.PROFIT_TARGET_STATE, model.getState() + ""));
			BigDecimal profitAmountRate = BigDecimal.ZERO;
			BigDecimal bizManagerAmountRate = BigDecimal.ZERO;
			BigDecimal saleBlanceAmountRate = BigDecimal.ZERO;
			BigDecimal saleAmountRate = BigDecimal.ZERO;
			BigDecimal manageAmountRate = BigDecimal.ZERO;
			BigDecimal warehouseAmountRate = BigDecimal.ZERO;
			BigDecimal fundCostRate = BigDecimal.ZERO;
			if (model.getState() == BaseConsts.THREE) {
				ProfitReportReqMonthDto mounthDto = new ProfitReportReqMonthDto();
				mounthDto.setUserId(model.getUserId());
				mounthDto.setBizSpecialId(model.getUserId());
				mounthDto.setStartStatisticsDate(model.getIssue());
				mounthDto.setEndStatisticsDate(model.getIssue());
				MounthProfitReport mounthReport = profitReportMonthDao.queryProfitReportMounthSum(mounthDto);
				if (mounthReport != null) {
					BigDecimal profitAmount = mounthReport.getProfitAmount();
					BigDecimal bizManagerAmount = mounthReport.getBizManagerAmount();
					BigDecimal saleBlanceAmount = mounthReport.getSaleBlanceAmount();
					BigDecimal saleAmount = mounthReport.getSaleAmount();
					BigDecimal manageAmount = mounthReport.getManageAmount();
					BigDecimal warehouseAmount = mounthReport.getWarehouseAmount();
					BigDecimal fundCost = mounthReport.getFundCost();
					result.setProfitAmount(profitAmount);
					result.setBizManagerAmount(bizManagerAmount);
					result.setSaleBlanceAmount(saleBlanceAmount);
					result.setSaleAmount(saleAmount);
					result.setManageAmount(manageAmount);
					result.setWarehouseAmount(warehouseAmount);
					result.setFundCost(fundCost);
					if (DecimalUtil.gt(model.getTargetProfitAmount(), BigDecimal.ZERO)) {
						if (DecimalUtil.le(profitAmount, BigDecimal.ZERO)) {
							profitAmount = BigDecimal.ZERO;
						}
						profitAmountRate = DecimalUtil.divide(profitAmount, model.getTargetProfitAmount());
					}
					if (DecimalUtil.gt(model.getTargetBizManager(), BigDecimal.ZERO)) {
						if (DecimalUtil.le(bizManagerAmount, BigDecimal.ZERO)) {
							bizManagerAmount = BigDecimal.ZERO;
						}
						bizManagerAmountRate = DecimalUtil.divide(bizManagerAmount, model.getTargetBizManager());
					}
					if (DecimalUtil.gt(model.getTargetSaleBlance(), BigDecimal.ZERO)) {
						if (DecimalUtil.le(saleBlanceAmount, BigDecimal.ZERO)) {
							saleBlanceAmount = BigDecimal.ZERO;
						}
						saleBlanceAmountRate = DecimalUtil.divide(saleBlanceAmount, model.getTargetSaleBlance());
					}
					if (DecimalUtil.gt(model.getTargetSaleAmount(), BigDecimal.ZERO)) {
						if (DecimalUtil.le(saleAmount, BigDecimal.ZERO)) {
							saleAmount = BigDecimal.ZERO;
						}
						saleAmountRate = DecimalUtil.divide(saleAmount, model.getTargetSaleAmount());
					}
					if (DecimalUtil.gt(model.getTargetManageAmount(), BigDecimal.ZERO)) {
						if (DecimalUtil.le(manageAmount, BigDecimal.ZERO)) {
							manageAmount = BigDecimal.ZERO;
						}
						manageAmountRate = DecimalUtil.divide(manageAmount, model.getTargetManageAmount());
					}
					if (DecimalUtil.gt(model.getTargetWarehouseAmount(), BigDecimal.ZERO)) {
						if (DecimalUtil.le(warehouseAmount, BigDecimal.ZERO)) {
							warehouseAmount = BigDecimal.ZERO;
						}
						warehouseAmountRate = DecimalUtil.divide(warehouseAmount, model.getTargetWarehouseAmount());
					}
					if (DecimalUtil.gt(model.getTargetFundVost(), BigDecimal.ZERO)) {
						if (DecimalUtil.le(fundCost, BigDecimal.ZERO)) {
							fundCost = BigDecimal.ZERO;
						}
						fundCostRate = DecimalUtil.divide(fundCost, model.getTargetFundVost());
					}
				}
			}
			result.setProfitAmountRateStr(DecimalUtil.toPercentString(profitAmountRate));
			result.setBizManagerAmountRateStr(DecimalUtil.toPercentString(bizManagerAmountRate));
			result.setSaleBlanceAmountRateStr(DecimalUtil.toPercentString(saleBlanceAmountRate));
			result.setSaleAmountRateStr(DecimalUtil.toPercentString(saleAmountRate));
			result.setManageAmountRateStr(DecimalUtil.toPercentString(manageAmountRate));
			result.setWarehouseAmountRateStr(DecimalUtil.toPercentString(warehouseAmountRate));
			result.setFundCostRateStr(DecimalUtil.toPercentString(fundCostRate));
		}
		return result;
	}

	/**
	 * 获取操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<CodeValue> getOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				ProfitTargetResDto.Operate.operMap);
		return oprResult;
	}

	/**
	 * 根据状态得到操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState(Integer state) {
		List<String> opertaList = Lists.newArrayList();
		switch (state) {
		// 状态 0 待提交 1待主管审核 2待部门主管审核
		case BaseConsts.ZERO:
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.DELETE);
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.SUBMIT);
			break;
		default:
			opertaList.add(OperateConsts.DETAIL);
			break;
		}
		return opertaList;
	}
}
