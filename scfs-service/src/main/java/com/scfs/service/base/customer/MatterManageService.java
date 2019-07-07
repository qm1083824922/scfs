package com.scfs.service.base.customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.MatterManageDao;
import com.scfs.dao.base.entity.MatterServiceDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.dto.req.MatterManageReqDto;
import com.scfs.domain.base.dto.resp.MatterManageFileResDto;
import com.scfs.domain.base.dto.resp.MatterManageResDto;
import com.scfs.domain.base.dto.resp.MatterServiceResDto;
import com.scfs.domain.base.entity.MatterManage;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.MatterManageAuditService;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.FileUploadService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 *  事项管理
 *  File: MatterManageService.java
 *  Description:
 *  TODO
 *  Date,                   Who,
 *  2017年07月29日         Administrator
 *
 * </pre>
 */
@Service
public class MatterManageService {
	@Autowired
	private MatterManageDao matterManageDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private MatterServiceDao matterServiceDao;
	@Autowired
	private MatterServiceService matterServiceService;// 服务要求
	@Autowired
	private MatterManageAuditService matterManageAuditService;
	@Autowired
	private AuditFlowService auditFlowService;

	/**
	 * 获取列表信息
	 * 
	 * @param customerMaintainReqDto
	 * @return
	 */
	public PageResult<MatterManageResDto> queryMatterManageResultsByCon(MatterManageReqDto reqDto) {
		if (!ServiceSupport.isAllowPerm(BusUrlConsts.QUERY_MATTER_MANAGE_POWER)) {// 判断用户是否拥有权限
			reqDto.setUserId(ServiceSupport.getUser().getId());
		}
		PageResult<MatterManageResDto> result = new PageResult<MatterManageResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<MatterManage> matterList = matterManageDao.queryResultsByCon(reqDto, rowBounds);
		List<MatterManageResDto> matterResList = convertToResult(matterList);
		result.setItems(matterResList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(reqDto.getPage());
		result.setPer_page(reqDto.getPer_page());
		return result;
	}

	/**
	 * 添加数据
	 * 
	 * @param matterManage
	 * @return
	 */
	public Integer insertMatterManage(MatterManage matterManage) {
		if (matterManage.getMatterName() == BaseConsts.ONE) {
			matterManage.setMatterType(BaseConsts.ONE);
		} else {
			matterManage.setMatterType(BaseConsts.TWO);
		}
		matterManage.setMatterNo(sequenceService.getNumDateByBusName(BaseConsts.MATTER_MANAGE_NO,
				SeqConsts.S_MATTER_MANAGE_NO, BaseConsts.INT_13));
		matterManage.setStage(BaseConsts.ONE);
		matterManage.setIsDelete(BaseConsts.ZERO);
		matterManage.setState(BaseConsts.ZERO);
		matterManage.setCreator(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
		matterManage.setCreatorId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
		matterManage.setCreateAt(new Date());
		Integer result = matterManageDao.insert(matterManage);
		if (result < 1)
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(matterManage));
		return matterManage.getId();
	}

	/**
	 * 编辑/浏览
	 * 
	 * @param matterManage
	 * @return
	 */
	public Result<MatterManageResDto> queryMatterManageById(MatterManage matterManage) {
		Result<MatterManageResDto> result = new Result<MatterManageResDto>();
		MatterManage matter = matterManageDao.queryEntityById(matterManage.getId());
		result.setItems(convertToResDto(matter));
		return result;
	}

	public MatterManageResDto queryMatterManageById(Integer id) {
		MatterManageResDto result = convertToResDto(matterManageDao.queryEntityById(id));
		return result;
	}

	/**
	 * 编辑信息
	 * 
	 * @param matterManage
	 * @return
	 */
	public BaseResult updateMatterManage(MatterManage matterManage) {
		BaseResult result = new BaseResult();
		matterManageDao.queryEntityById(matterManage.getId());
		matterManageDao.updateById(matterManage);
		return result;
	}

	/**
	 * 提交
	 * 
	 * @param matterManage
	 * @return
	 */
	public BaseResult submitMatterManage(MatterManage matterManage) {
		BaseResult result = new BaseResult();
		MatterManage auitResult = matterManageDao.queryEntityById(matterManage.getId());
		if (auitResult.getMatterName().equals(BaseConsts.ONE)) {
			MatterServiceResDto matter = matterServiceService.queryMatterServiceById(matterManage.getId());
			if (matter.getId() == null) {
				result.setMsg("服务要求不能为空!");
				return result;
			}
		}
		AuditNode startAuditNode = auditFlowService.getStartAuditNode(BaseConsts.INT_27, null);
		if (null == startAuditNode) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置审核流");
		}
		matterManage.setState(startAuditNode.getAuditNodeState());

		matterManageDao.updateById(matterManage);
		matterManageAuditService.startAudit(auitResult, startAuditNode);
		return result;
	}

	/**
	 * 删除数据
	 * 
	 * @param reqDto
	 * @return
	 */
	public BaseResult deleteMatterManage(MatterManageReqDto reqDto) {
		matterManageDao.queryEntityById(reqDto.getId());
		matterServiceDao.deleteById(reqDto.getId());
		BaseResult result = new BaseResult();
		MatterManage matterManage = new MatterManage();
		matterManage.setIsDelete(BaseConsts.ONE);
		matterManage.setDeleteAt(new Date());
		matterManage.setId(reqDto.getId());
		matterManageDao.updateById(matterManage);
		return result;
	}

	private List<MatterManageResDto> convertToResult(List<MatterManage> matterList) {
		List<MatterManageResDto> customerResDtoList = new ArrayList<MatterManageResDto>();
		if (CollectionUtils.isEmpty(matterList)) {
			return customerResDtoList;
		}
		for (MatterManage matterManage : matterList) {
			MatterManageResDto restDto = convertToResDto(matterManage);
			restDto.setOpertaList(getOperList(matterManage));
			customerResDtoList.add(restDto);
		}
		return customerResDtoList;
	}

	/**
	 * @param model
	 * @return
	 */
	public MatterManageResDto convertToResDto(MatterManage model) {
		MatterManageResDto result = new MatterManageResDto();
		if (model != null) {
			result.setId(model.getId());
			result.setMatterNo(model.getMatterNo());
			result.setCustMainId(model.getCustMainId());
			result.setMatterName(model.getMatterName());
			result.setMatterNameValue(
					ServiceSupport.getValueByBizCode(BizCodeConsts.MATTER_NAME, model.getMatterName() + ""));
			result.setMatterType(model.getMatterType());
			result.setMatterTypeValue(
					ServiceSupport.getValueByBizCode(BizCodeConsts.MATTER_TYPE, model.getMatterType() + ""));
			result.setProjectId(model.getProjectId());
			if (model.getProjectId() != null) {
				result.setProjectName(cacheService.showProjectNameById(model.getProjectId()));
			}
			result.setCustomerName(model.getCustomerName());
			result.setCustomerAbbreviate(model.getCustomerAbbreviate());
			result.setStage(model.getStage());
			result.setStageValue(ServiceSupport.getValueByBizCode(BizCodeConsts.MATTER_STAGE, model.getStage() + ""));
			result.setHkCompany(model.getHkCompany());
			result.setEnterpriseBus(model.getEnterpriseBus());
			result.setMatterDescribe(model.getMatterDescribe());
			result.setOfficeAddress(model.getOfficeAddress());
			result.setRegAddress(model.getRegAddress());
			result.setContacts(model.getContacts());
			result.setContactsNumber(model.getContactsNumber());
			result.setDisGoods(model.getDisGoods());
			result.setBizManagerId(model.getBizManagerId());
			result.setBizManagerName(cacheService.getUserByid(model.getBizManagerId()).getChineseName());
			result.setBusinessManagerId(model.getBusinessManagerId());
			result.setBusinessManagerName(cacheService.getUserByid(model.getBizManagerId()).getChineseName());
			result.setDepartmentId(model.getDepartmentId());
			result.setDepartmentIdName(cacheService.getUserByid(model.getDepartmentId()).getChineseName());
			result.setJusticeId(model.getJusticeId());
			result.setJusticeName(cacheService.getUserByid(model.getJusticeId()).getChineseName());
			result.setFinanceManagerId(model.getFinanceManagerId());
			result.setFinanceManagerName(cacheService.getUserByid(model.getFinanceManagerId()).getChineseName());
			result.setRiskManagerId(model.getRiskManagerId());
			result.setRiskManagerName(cacheService.getUserByid(model.getRiskManagerId()).getChineseName());
			result.setState(model.getState());
			result.setStateValue(ServiceSupport.getValueByBizCode(BizCodeConsts.MATTER_STATE, model.getState() + ""));
			result.setRemark(model.getRemark());
			result.setCreator(model.getCreator());
			result.setCreateAt(model.getCreateAt());
			result.setCreatorId(model.getCreatorId());
			result.setDeleteAt(model.getDeleteAt());
			result.setUpdateAt(model.getUpdateAt());
			result.setIsDelete(model.getIsDelete());
		}
		return result;
	}

	private List<CodeValue> getOperList(MatterManage matterManage) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(matterManage);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				MatterManageResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getOperListByState(MatterManage matter) {
		List<String> opertaList = Lists.newArrayList();
		switch (matter.getState()) {
		case BaseConsts.ZERO:
			opertaList.add(OperateConsts.DELETE);
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.SUBMIT);
			opertaList.add(OperateConsts.DETAIL);
			break;
		default:
			opertaList.add(OperateConsts.DETAIL);
			break;
		}
		return opertaList;
	}

	/**
	 * 文件相关操作
	 * 
	 * @param state
	 * @return
	 */
	public PageResult<MatterManageFileResDto> queryFileList(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<MatterManageFileResDto> pageResult = new PageResult<MatterManageFileResDto>();
		fileAttReqDto.setBusType(BaseConsts.INT_37);
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<MatterManageFileResDto> list = convertToFileResDto(fielAttach);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), fileAttReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(fileAttReqDto.getPage());
		pageResult.setPer_page(fileAttReqDto.getPer_page());
		pageResult.setItems(list);
		return pageResult;
	}

	public List<MatterManageFileResDto> queryFileList(Integer matterId) {
		FileAttachSearchReqDto fileAttReqDto = new FileAttachSearchReqDto();
		fileAttReqDto.setBusId(matterId);
		fileAttReqDto.setBusType(BaseConsts.INT_37);
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<MatterManageFileResDto> list = convertToFileResDto(fielAttach);
		return list;
	}

	private List<MatterManageFileResDto> convertToFileResDto(List<FileAttach> fileAttach) {
		List<MatterManageFileResDto> list = new LinkedList<MatterManageFileResDto>();
		if (ListUtil.isEmpty(fileAttach)) {
			return list;
		}
		for (FileAttach model : fileAttach) {
			MatterManageFileResDto result = new MatterManageFileResDto();
			result.setId(model.getId());
			result.setBusId(model.getBusId());
			result.setBusTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.FILE_BUS_TYPE, model.getBusType() + ""));
			result.setName(model.getName());
			result.setType(model.getType());
			result.setCreateAt(model.getCreateAt());
			result.setCreator(model.getCreator());
			result.setCreatorId(model.getCreatorId());
			List<CodeValue> operList = getOperList();
			result.setOpertaList(operList);
			list.add(result);
		}
		return list;
	}

	private List<CodeValue> getOperList() {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState();
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				MatterManageFileResDto.Operate.operMap);
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
		opertaList.add(OperateConsts.DOWNLOAD);
		opertaList.add(OperateConsts.DELETE);
		return opertaList;
	}
}
