package com.scfs.service.base.auditFlow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.AuditFlowDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.dto.req.AuditFlowReqDto;
import com.scfs.domain.base.dto.resp.AuditFlowResDto;
import com.scfs.domain.base.entity.AuditFlow;
import com.scfs.domain.pay.dto.resq.MergePayOrderResDto;
import com.scfs.domain.pay.dto.resq.PayOrderResDto;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.project.entity.ProjectItem;
import com.scfs.domain.project.entity.ProjectPool;
import com.scfs.domain.report.resp.ProfitTargetResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.common.SequenceService;
import com.scfs.service.pay.MergePayOrderService;
import com.scfs.service.pay.PayService;
import com.scfs.service.project.ProjectItemService;
import com.scfs.service.project.ProjectPoolService;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2017年7月22日.
 */
@Service
public class AuditFlowService {
	@Autowired
	private AuditFlowDao auditFlowDao;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private ProjectItemService projectItemService;
	@Autowired
	private PayOrderDao payOrderDao;
	@Autowired
	private PayService payService;
	@Autowired
	private ProjectPoolService projectPoolService;
	@Autowired
	private MergePayOrderService mergePayOrderService;

	/**
	 * 获取列表信息
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<AuditFlowResDto> queryAuditFlowResult(AuditFlowReqDto reqDto) {
		reqDto.setUserId(ServiceSupport.getUser().getId());
		PageResult<AuditFlowResDto> pageResult = new PageResult<AuditFlowResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<AuditFlowResDto> auditFlowResDtos = convertToResDtos(auditFlowDao.queryResultsByCon(reqDto, rowBounds));
		pageResult.setItems(auditFlowResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(reqDto.getPage());
		pageResult.setPer_page(reqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 添加数据
	 * 
	 * @param profitTarget
	 * @return
	 */
	public int createAuditFlow(AuditFlow auditFlow) {
		if (!auditFlow.getAuditFlowType().equals(BaseConsts.FIVE)
				&& !auditFlow.getAuditFlowType().equals(BaseConsts.INT_14)) { // 非付款单和合并付款单
			int count = auditFlowDao.queryCountByType(auditFlow.getAuditFlowType());
			if (count > 0) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "已存在同审核类型的审核流");
			}
		}
		auditFlow.setAuditFlowNo(sequenceService.getNumDateByBusName(BaseConsts.AUDIT_FLOW_NO, SeqConsts.AUDIT_FLOW_NO,
				BaseConsts.INT_13));
		int id = auditFlowDao.insert(auditFlow);
		if (id <= BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加审核流失败");
		}
		return auditFlow.getId();
	}

	/**
	 * 更新信息
	 * 
	 * @param profitTarget
	 * @return
	 */
	public BaseResult updateAuditFlow(AuditFlow auditFlow) {
		BaseResult baseResult = new BaseResult();
		int count = auditFlowDao.queryCountByType(auditFlow.getAuditFlowType());
		AuditFlow oldAuditFlow = auditFlowDao.queryEntityById(auditFlow.getId());
		if (!auditFlow.getAuditFlowType().equals(oldAuditFlow.getAuditFlowType())) {
			if (!auditFlow.getAuditFlowType().equals(BaseConsts.FIVE)
					&& !auditFlow.getAuditFlowType().equals(BaseConsts.INT_14)) { // 非付款单和合并付款单
				if (count > 0) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "已存在同审核类型的审核流");
				}
			}
		}
		int result = auditFlowDao.updateById(auditFlow);
		if (result == BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "更新审核流失败");
		}
		return baseResult;
	}

	/**
	 * 删除信息
	 * 
	 * @param auditFlow
	 * @return
	 */
	public BaseResult deleteAuditFlow(AuditFlow auditFlow) {
		BaseResult baseResult = new BaseResult();
		auditFlowDao.queryLockEntityById(auditFlow.getId());
		auditFlow.setIsDelete(BaseConsts.ONE);
		int result = auditFlowDao.updateById(auditFlow);
		if (result == BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "删除审核流失败");
		}
		return baseResult;
	}

	/**
	 * 详情信息
	 * 
	 * @param auditFlow
	 * @return
	 */
	public Result<AuditFlowResDto> detailAuditFlowById(AuditFlow auditFlow) {
		Result<AuditFlowResDto> result = new Result<AuditFlowResDto>();
		AuditFlowResDto resDto = convertToResDto(auditFlowDao.queryEntityById(auditFlow.getId()));
		result.setItems(resDto);
		return result;
	}

	public List<AuditFlowResDto> convertToResDtos(List<AuditFlow> auditFlowList) {
		List<AuditFlowResDto> auditFlowResDtoList = new ArrayList<AuditFlowResDto>();
		if (ListUtil.isEmpty(auditFlowList)) {
			return auditFlowResDtoList;
		}
		for (AuditFlow auditFlow : auditFlowList) {
			AuditFlowResDto auditFlowResDto = convertToResDto(auditFlow);
			List<CodeValue> operList = getOperList();
			auditFlowResDto.setOpertaList(operList);
			auditFlowResDtoList.add(auditFlowResDto);
		}
		return auditFlowResDtoList;
	}

	public AuditFlowResDto convertToResDto(AuditFlow auditFlow) {
		AuditFlowResDto auditFlowResDto = new AuditFlowResDto();
		BeanUtils.copyProperties(auditFlow, auditFlowResDto);
		auditFlowResDto.setAuditFlowTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_POTYPE,
				Integer.toString(auditFlow.getAuditFlowType())));
		auditFlowResDto.setIsFirstRiskName(ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_FIRST,
				Integer.toString(auditFlow.getIsFirstRisk())));
		auditFlowResDto.setIsFirstLawName(ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_FIRST,
				Integer.toString(auditFlow.getIsFirstLaw())));
		auditFlowResDto.setAuditFlowNodes(getAuditFlowNodes(auditFlow));
		return auditFlowResDto;
	}

	/**
	 * 获取付款单的审核流
	 * 
	 * @return
	 */
	public List<AuditFlow> queryAuditFlow4Pay() {
		return auditFlowDao.queryAuditFlow4Pay();
	}
	
	/**
	 * 根据审核类型获取审核流
	 * 
	 * @param auditFlowType 审核类型
	 * @param projectId 项目ID--可选
	 * @return
	 */
	public List<Integer> getAuditFlows(Integer auditFlowType, Integer projectId) {
		return getAuditFlows(auditFlowType, projectId, null);
	}

	/**
	 * 根据审核类型获取审核流
	 * 
	 * @param auditFlowType 审核类型
	 * @param projectId 项目ID--可选
	 * @param paramsMap 参数
	 * @return
	 */
	public List<Integer> getAuditFlows(Integer auditFlowType, Integer projectId, Map<String, Object> paramsMap) {
		List<AuditNode> auditFlowNodeList = getAuditFlowNodeList(auditFlowType, projectId, paramsMap);
		List<Integer> auditflows = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(auditFlowNodeList)) {
			auditflows.add(BaseConsts.ZERO); // 审核开始节点
			for (AuditNode auditNode : auditFlowNodeList) {
				auditflows.add(auditNode.getAuditNodeState());
			}
		}
		return auditflows;
	}

	/**
	 * 根据审核类型获取审核流
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public List<Integer> getAudit() {
		List<AuditNode> auditFlowNodeList = getStartAuditBybusiList();
		List<Integer> auditflows = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(auditFlowNodeList)) {
			auditflows.add(BaseConsts.ZERO); // 审核开始节点
			for (AuditNode auditNode : auditFlowNodeList) {
				auditflows.add(auditNode.getAuditNodeState());
			}
		}
		return auditflows;
	}
	
	/**
	 * 根据审核类型获取起始审核节点
	 * @param auditFlowType 审核类型
	 * @param projectId 项目ID--可选，当且仅当查询项目相关属性时必传，如付款单、合并付款单
	 * @return
	 */
	public AuditNode getStartAuditNode(Integer auditFlowType, Integer projectId) {
		return getStartAuditNode(auditFlowType, projectId, null);
	}

	/**
	 * 根据审核类型获取起始审核节点
	 * @param auditFlowType 审核类型
	 * @param projectId 项目ID--可选，当且仅当查询项目相关属性时必传，如付款单、合并付款单
	 * @param paramsMap 参数
	 * @return
	 */
	public AuditNode getStartAuditNode(Integer auditFlowType, Integer projectId, Map<String, Object> paramsMap) {
		AuditNode startAuditNode = new AuditNode();
		List<AuditNode> auditFlowNodeList = getAuditFlowNodeList(auditFlowType, projectId, paramsMap);
		if (!CollectionUtils.isEmpty(auditFlowNodeList)) {
			startAuditNode = auditFlowNodeList.get(0);
		} else {
			startAuditNode = null;
		}
		return startAuditNode;
	}

	/**
	 * 根据审核类型获取起始审核节点
	 * 
	 * @param auditType 审核类型
	 * @param busiUnit 经营单位 付款 付借款类型
	 * @return
	 */
	public AuditNode getStartAuditBybusi() {
		AuditNode startAuditNode = new AuditNode();
		List<AuditNode> auditFlowNodeList = getStartAuditBybusiList();
		if (!CollectionUtils.isEmpty(auditFlowNodeList)) {
			startAuditNode = auditFlowNodeList.get(0);
		} else {
			startAuditNode = null;
		}
		return startAuditNode;
	}

	/**
	 * 获取当前节点的下一个节点
	 * @param auditNodeState
	 * @param auditFlowType
	 * @param projectId
	 * @return
	 */
	public AuditNode getNextAuditNode(Integer auditNodeState, Integer auditFlowType, Integer projectId) {
		return getNextAuditNode(auditNodeState, auditFlowType, projectId, null);
	}
	
	/**
	 * 获取当前节点的下一个节点
	 * 
	 * @param auditNodeState 当前节点状态
	 * @param auditFlowType 审核流类型
	 * @param projectId 项目ID
	 * @param paramsMap 参数
	 * @return
	 */
	public AuditNode getNextAuditNode(Integer auditNodeState, Integer auditFlowType, Integer projectId, Map<String, Object> paramsMap) {
		AuditNode nextAuditNode = new AuditNode();
		List<AuditNode> auditFlowNodeList = getAuditFlowNodeList(auditFlowType, projectId, paramsMap);
		if (!CollectionUtils.isEmpty(auditFlowNodeList)) {
			int currIndex = -1;
			int size = auditFlowNodeList.size();
			for (int i = 0; i < size; i++) {
				AuditNode node = auditFlowNodeList.get(i);
				if (node.getAuditNodeState().equals(auditNodeState)) {
					currIndex = i;
					break;
				}
			}
			if (currIndex >= 0 && (currIndex + 1) < size) {
				nextAuditNode = auditFlowNodeList.get(currIndex + 1);
			} else {
				nextAuditNode = null;
			}
		} else {
			nextAuditNode = null;
		}
		return nextAuditNode;
	}

	/**
	 * 获取当前节点的下一个节点
	 * 
	 * @param auditNodeState 当前节点状态
	 * @param auditFlowType 审核流类型
	 * @param projectId 项目ID
	 * @return
	 */
	public AuditNode getNextAudit(Integer auditNodeState) {
		AuditNode nextAuditNode = new AuditNode();
		List<AuditNode> auditFlowNodeList = getStartAuditBybusiList();
		if (!CollectionUtils.isEmpty(auditFlowNodeList)) {
			int currIndex = -1;
			int size = auditFlowNodeList.size();
			for (int i = 0; i < size; i++) {
				AuditNode node = auditFlowNodeList.get(i);
				if (node.getAuditNodeState().equals(auditNodeState)) {
					currIndex = i;
					break;
				}
			}
			if (currIndex >= 0 && (currIndex + 1) < size) {
				nextAuditNode = auditFlowNodeList.get(currIndex + 1);
			} else {
				nextAuditNode = null;
			}
		} else {
			nextAuditNode = null;
		}
		return nextAuditNode;
	}

	/**
	 * 获取审核流节点编排字符串
	 * 
	 * @param auditFlow
	 * @return
	 */
	public String getAuditFlowNodes(AuditFlow auditFlow) {
		StringBuffer auditFlowNodes = new StringBuffer();
		List<AuditNode> auditFlowNodeList = getAuditFlowNodeListByParam(auditFlow, null, null);
		if (!CollectionUtils.isEmpty(auditFlowNodeList)) {
			int count = 0;
			int size = auditFlowNodeList.size();
			for (AuditNode auditNode : auditFlowNodeList) {
				auditFlowNodes.append(auditNode.getAuditNodeName());
				count++;
				if (count != size) {
					auditFlowNodes.append("-->");
				}
			}
		}
		return auditFlowNodes.toString();
	}

	/**
	 * 获取审核流节点列表
	 * 
	 * @param auditFlowType
	 * @param projectId
	 * @return
	 */
	public List<AuditNode> getAuditFlowNodeList(Integer auditFlowType, Integer projectId, Map<String, Object> paramsMap) {
		// 付款单特殊处理，根据项目条款的审批方式获取审核流
		AuditFlow auditFlow = null;
		if ((auditFlowType.equals(BaseConsts.FIVE) || auditFlowType.equals(BaseConsts.INT_14)) && projectId != null) { // 5-付款单或14-合并付款单
			ProjectItem projectItem = projectItemService.getProjectItem(projectId);
			if (projectItem == null) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目条款不存在或已过期");
			}
			if (StringUtils.isBlank(projectItem.getPayAuditType())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目条款未设置付款审核方式");
			}
			// 付款和合并付款共用付款审核流
			auditFlow = auditFlowDao.queryAuditFlowByTypeAndNo(BaseConsts.FIVE, projectItem.getPayAuditType());
			// 重置审核流类型
			if (null != auditFlow && auditFlowType.equals(BaseConsts.INT_14)) {
				auditFlow.setAuditFlowType(BaseConsts.INT_14);
			}
		} else {
			auditFlow = auditFlowDao.queryAuditFlowByType(auditFlowType);
		}
		if (null == auditFlow) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置审核流");
		}
		List<AuditNode> auditFlowNodeList = getAuditFlowNodeListByParam(auditFlow, projectId, paramsMap);
		return auditFlowNodeList;
	}

	/**
	 * 获取审核流节点列表 针对经营单位
	 * 
	 * @param auditFlowType
	 * @param busiUnit
	 * @return
	 */
	public List<AuditNode> getStartAuditBybusiList() {
		List<AuditNode> auditFlowNodeList = getAuditFlowByBusiList();
		return auditFlowNodeList;
	}

	/**
	 * 获取审核流节点列表
	 * 
	 * @param auditFlow
	 * @param projectId
	 * @return
	 */
	public List<AuditNode> getAuditFlowNodeListByParam(AuditFlow auditFlow, Integer projectId, Map<String, Object> paramsMap) {
		List<AuditNode> auditFlowNodeList = Lists.newArrayList();
		if (StringUtils.isNotBlank(auditFlow.getLawAudit())) {
			AuditNode auditNode = new AuditNode();
			auditNode.setIndex(new Integer(auditFlow.getLawAudit()));
			auditNode.setAuditNodeName("法务审核");
			auditNode.setAuditNodeState(BaseConsts.INT_11);
			boolean isAdd = isAddAuditFlowNode(auditFlow, projectId, paramsMap);
			if (isAdd) { // 是否允许添加节点
				auditFlowNodeList.add(auditNode);
			}
		}
		if (StringUtils.isNotBlank(auditFlow.getBizAudit())) {
			AuditNode auditNode = new AuditNode();
			auditNode.setIndex(new Integer(auditFlow.getBizAudit()));
			auditNode.setAuditNodeName("商务审核");
			auditNode.setAuditNodeState(BaseConsts.TEN);
			auditFlowNodeList.add(auditNode);
		}
		if (StringUtils.isNotBlank(auditFlow.getCareerAudit())) {
			AuditNode auditNode = new AuditNode();
			auditNode.setIndex(new Integer(auditFlow.getCareerAudit()));
			auditNode.setAuditNodeName("事业部审核");
			auditNode.setAuditNodeState(BaseConsts.INT_14);
			auditFlowNodeList.add(auditNode);
		}
		if (StringUtils.isNotBlank(auditFlow.getPurchaseAudit())) {
			AuditNode auditNode = new AuditNode();
			auditNode.setIndex(new Integer(auditFlow.getPurchaseAudit()));
			auditNode.setAuditNodeName("采购审核");
			auditNode.setAuditNodeState(BaseConsts.INT_15);
			auditFlowNodeList.add(auditNode);
		}
		if (StringUtils.isNotBlank(auditFlow.getSupplyChainGroupAudit())) {
			AuditNode auditNode = new AuditNode();
			auditNode.setIndex(new Integer(auditFlow.getSupplyChainGroupAudit()));
			auditNode.setAuditNodeName("供应链小组审核");
			auditNode.setAuditNodeState(BaseConsts.INT_16);
			auditFlowNodeList.add(auditNode);
		}
		if (StringUtils.isNotBlank(auditFlow.getSupplyChainServiceAudit())) {
			AuditNode auditNode = new AuditNode();
			auditNode.setIndex(new Integer(auditFlow.getSupplyChainServiceAudit()));
			auditNode.setAuditNodeName("供应链服务部审核");
			auditNode.setAuditNodeState(BaseConsts.INT_17);
			auditFlowNodeList.add(auditNode);
		}
		if (StringUtils.isNotBlank(auditFlow.getGoodsRiskAudit())) {
			AuditNode auditNode = new AuditNode();
			auditNode.setIndex(new Integer(auditFlow.getGoodsRiskAudit()));
			auditNode.setAuditNodeName("商品风控审核");
			auditNode.setAuditNodeState(BaseConsts.INT_18);
			auditFlowNodeList.add(auditNode);
		}
		if (StringUtils.isNotBlank(auditFlow.getBusiAudit())) {
			AuditNode auditNode = new AuditNode();
			auditNode.setIndex(new Integer(auditFlow.getBusiAudit()));
			auditNode.setAuditNodeName("业务审核");
			auditNode.setAuditNodeState(BaseConsts.INT_20);
			auditFlowNodeList.add(auditNode);
		}
		if (StringUtils.isNotBlank(auditFlow.getFinanceAudit())) {
			AuditNode auditNode = new AuditNode();
			auditNode.setIndex(new Integer(auditFlow.getFinanceAudit()));
			auditNode.setAuditNodeName("财务专员审核");
			auditNode.setAuditNodeState(BaseConsts.INT_25);
			auditFlowNodeList.add(auditNode);
		}
		if (StringUtils.isNotBlank(auditFlow.getFinance2Audit())) {
			AuditNode auditNode = new AuditNode();
			auditNode.setIndex(new Integer(auditFlow.getFinance2Audit()));
			auditNode.setAuditNodeName("财务主管审核");
			auditNode.setAuditNodeState(BaseConsts.INT_30);
			auditFlowNodeList.add(auditNode);
		}
		if (StringUtils.isNotBlank(auditFlow.getRiskSpecialAudit())) {
			AuditNode auditNode = new AuditNode();
			auditNode.setIndex(new Integer(auditFlow.getRiskSpecialAudit()));
			auditNode.setAuditNodeName("风控专员审核");
			auditNode.setAuditNodeState(BaseConsts.INT_35);
			boolean isAdd = isAddAuditFlowNode(auditFlow, projectId, paramsMap); // 是否允许添加节点
			if (isAdd) {
				auditFlowNodeList.add(auditNode);
			}
		}
		if (StringUtils.isNotBlank(auditFlow.getRiskAudit())) {
			AuditNode auditNode = new AuditNode();
			auditNode.setIndex(new Integer(auditFlow.getRiskAudit()));
			auditNode.setAuditNodeName("风控主管审核");
			auditNode.setAuditNodeState(BaseConsts.INT_40);
			boolean isAdd = isAddAuditFlowNode(auditFlow, projectId, paramsMap); // 是否允许添加节点
			if (isAdd) {
				auditFlowNodeList.add(auditNode);
			}
		}
		if (StringUtils.isNotBlank(auditFlow.getDeptManageAudit())) {
			AuditNode auditNode = new AuditNode();
			auditNode.setIndex(new Integer(auditFlow.getDeptManageAudit()));
			auditNode.setAuditNodeName("部门主管审核");
			auditNode.setAuditNodeState(BaseConsts.INT_80);
			auditFlowNodeList.add(auditNode);
		}
		if (StringUtils.isNotBlank(auditFlow.getBossAudit())) {
			AuditNode auditNode = new AuditNode();
			auditNode.setIndex(new Integer(auditFlow.getBossAudit()));
			auditNode.setAuditNodeName("总经理审核");
			auditNode.setAuditNodeState(BaseConsts.INT_90);
			auditFlowNodeList.add(auditNode);
		}
		Collections.sort(auditFlowNodeList);
		return auditFlowNodeList;
	}

	/**
	 * 获取审核流节点列表
	 * 
	 * @param auditFlow
	 * @param projectId
	 * @return
	 */
	public List<AuditNode> getAuditFlowByBusiList() {
		List<AuditNode> auditFlowNodeList = Lists.newArrayList();
		AuditNode auditNode = new AuditNode();
		auditNode.setIndex(BaseConsts.ONE);
		auditNode.setAuditNodeName("部门主管审核");
		auditNode.setAuditNodeState(BaseConsts.INT_80);
		auditFlowNodeList.add(auditNode);

		AuditNode node = new AuditNode();
		node.setIndex(BaseConsts.TWO);
		node.setAuditNodeName("财务主管审核");
		node.setAuditNodeState(BaseConsts.INT_30);
		auditFlowNodeList.add(node);

		Collections.sort(auditFlowNodeList);
		return auditFlowNodeList;
	}

	/**
	 * 判断是否添加审核节点
	 * 
	 * @param auditFlow
	 * @param projectId
	 * @return
	 */
	public boolean isAddAuditFlowNode(AuditFlow auditFlow, Integer projectId, Map<String, Object> paramMap) {
		boolean isAdd = true;
		if ((auditFlow.getAuditFlowType().equals(BaseConsts.FIVE)
				|| auditFlow.getAuditFlowType().equals(BaseConsts.INT_14)) && projectId != null) {
			int count = payOrderDao.queryFinishedBillCount(projectId);
			// 非首单且配置(法务首单审核、风控首单审核)为是，则不添加审核节点
			if (count > 0 && (auditFlow.getIsFirstLaw() == 1 || auditFlow.getIsFirstRisk() == 1)) {
				isAdd = false;
			}
			if (isAdd == false) {
				if (auditFlow.getAuditFlowType().equals(BaseConsts.FIVE) && auditFlow.getIsFirstRisk() == 1 && null != paramMap) {
					Object payOrderId = paramMap.get("payOrderId");
					if (null != payOrderId) {
						PayOrderResDto payOrderResDto = getPayOrder((Integer)payOrderId);
						//项目余额小于等于0
						if (null != payOrderResDto && DecimalUtil.le(payOrderResDto.getPayProjectBalanceAmount(), BigDecimal.ZERO)) {
							isAdd = true;
						}
					}
				}
			}
			if (isAdd == false) {
				if (auditFlow.getAuditFlowType().equals(BaseConsts.INT_14) && auditFlow.getIsFirstRisk() == 1 && null != paramMap) {
					Object mergerPayOrderId = paramMap.get("mergerPayOrderId");
					if (null != mergerPayOrderId) {
						MergePayOrderResDto mergePayOrderResDto = getMergePayOrder((Integer)mergerPayOrderId);
						//项目余额小于等于0
						if (null != mergePayOrderResDto && DecimalUtil.le(mergePayOrderResDto.getPayProjectBalanceAmount(), BigDecimal.ZERO)) {
							isAdd = true;
						}
					}
				}
			}
		}
		return isAdd;
	}
	
	private PayOrderResDto getPayOrder(Integer payOrderId) {
		PayOrder payOrder = new PayOrder();
		payOrder.setId(payOrderId);
		PayOrderResDto payDto = payService.detailPayOrderById(payOrder).getItems();
		ProjectPool pp = projectPoolService.queryProjectPoolByProjectId(payDto.getProjectId());
		if (pp != null) {
			payDto.setProjectTotalAmount(pp.getProjectAmount());
			payDto.setProjectBalanceAmount(pp.getRemainFundAmount());
			payDto.setProjectAmountUnit(pp.getCurrencyType());
			payDto.setProjectAmountUnitTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, pp.getCurrencyType() + ""));
			payDto.setPayProjectBalanceAmount(
					DecimalUtil.subtract(pp.getRemainFundAmount(), payDto.getPayAdvanceAmount()));
		}
		return payDto;
	}
	
	private MergePayOrderResDto getMergePayOrder(Integer mergePayOrderId) {
		MergePayOrderResDto mergePayOrderResDto = mergePayOrderService.detailPayOrderById(mergePayOrderId);
		ProjectPool pp = projectPoolService.queryProjectPoolByProjectId(mergePayOrderResDto.getProjectId());
		if (pp != null) {
			mergePayOrderResDto.setProjectTotalAmount(pp.getProjectAmount());
			mergePayOrderResDto.setProjectBalanceAmount(pp.getRemainFundAmount());
			mergePayOrderResDto.setProjectAmountUnit(pp.getCurrencyType());
			mergePayOrderResDto.setProjectAmountUnitTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, pp.getCurrencyType() + ""));
			mergePayOrderResDto.setPayProjectBalanceAmount(
					DecimalUtil.subtract(pp.getRemainFundAmount(), mergePayOrderResDto.getPayAdvanceAmount()));
		}
		return mergePayOrderResDto;
	}

	/**
	 * 获取操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<CodeValue> getOperList() {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState();
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
	private List<String> getOperListByState() {
		List<String> opertaList = Lists.newArrayList();
		opertaList.add(OperateConsts.EDIT);
		opertaList.add(OperateConsts.DELETE);
		opertaList.add(OperateConsts.DETAIL);
		return opertaList;
	}

	/**
	 * 根据审核流编号查询审核节点
	 * 
	 * @param auditFlowNo
	 * @return
	 */
	public AuditFlow queryAuditFlowByNo(String auditFlowNo) {
		return auditFlowDao.queryAuditFlowByNo(auditFlowNo);
	}
}
