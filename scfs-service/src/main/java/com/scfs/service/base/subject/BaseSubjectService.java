package com.scfs.service.base.subject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseSubjectDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.subject.dto.req.AddSubjectDto;
import com.scfs.domain.base.subject.dto.req.QuerySubjectReqDto;
import com.scfs.domain.base.subject.dto.resp.QuerySubjectResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.common.SequenceService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: BaseSubjectService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年9月26日				Administrator
 *
 * </pre>
 */
@Service
public class BaseSubjectService {
	@Autowired
	private BaseSubjectDao baseSubjectDao;

	@Autowired
	private SequenceService sequenceService;

	@Autowired
	private CacheService cacheService;

	public int addBaseSubject(AddSubjectDto addSubjectDto) {
		// 查询该编号是否存在
		QuerySubjectReqDto query = new QuerySubjectReqDto();
		query.setAbbreviation(addSubjectDto.getAbbreviation());
		List<BaseSubject> subject = baseSubjectDao.querySubjectByAbb(query);
		if (((addSubjectDto.getSubjectType()) & (BaseConsts.ONE)) == BaseConsts.ONE) {
			if (StringUtils.isEmpty(addSubjectDto.getInvoiceQuotaType())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "开票限额不能为空");
			}
			if (addSubjectDto.getFinanceManagerId() == null) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "财务主管不能为空");
			}
			if (addSubjectDto.getDepartmentManagerId() == null) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "部门主管不能为空");
			}
		}
		if (CollectionUtils.isNotEmpty(subject)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
					"该简称已存在:" + JSONObject.toJSON(addSubjectDto.getAbbreviation()));
		} else {
			if (addSubjectDto.getSubjectType() == BaseConsts.SUBJECT_TYPE_WAREHOUSE) {
				query.setSubjectNo(addSubjectDto.getSubjectNo());
				query.setSubjectType(addSubjectDto.getSubjectType());
				List<BaseSubject> subject1 = baseSubjectDao.querySubjectByType(query);
				if (CollectionUtils.isNotEmpty(subject1)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
							"该编号已存在:" + JSONObject.toJSON(addSubjectDto.getSubjectNo()));
				}
			}
			addSubjectDto.setState(BaseConsts.ONE);
			addSubjectDto.setCreator(ServiceSupport.getUser().getChineseName());
			if (addSubjectDto.getSubjectType() != BaseConsts.SUBJECT_TYPE_WAREHOUSE)
				addSubjectDto.setSubjectNo(BaseConsts.NO_PLACEHOLDER);
			// 该字段无初始值，后期手动维护
			addSubjectDto.setPmsSupplierCode("null");
			int id = baseSubjectDao.insertBaseSubject(addSubjectDto);
			if (id <= 0) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "新增失败:" + JSONObject.toJSON(addSubjectDto.getId()));
			}

		}
		return addSubjectDto.getId();
	}

	public Result<QuerySubjectResDto> querySubjectById(int id) {
		Result<QuerySubjectResDto> result = new Result<QuerySubjectResDto>();
		QuerySubjectResDto subjectResDto = convertToDto(loadAndLockEntityById(id));
		result.setItems(subjectResDto);
		return result;
	}

	public BaseSubject loadAndLockEntityById(int id) {
		BaseSubject obj = baseSubjectDao.loadAndLockEntityById(id);
		if (obj == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, baseSubjectDao.getClass(), id);
		}
		return obj;
	}

	public PageResult<QuerySubjectResDto> querySubjectByCond(QuerySubjectReqDto querySubjectReqDto) {
		PageResult<QuerySubjectResDto> pageResult = new PageResult<QuerySubjectResDto>();
		int offSet = PageUtil.getOffSet(querySubjectReqDto.getPage(), querySubjectReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, querySubjectReqDto.getPer_page());
		List<QuerySubjectResDto> querySubjectResDtos = convertToResult(
				baseSubjectDao.querySubjectByCond(querySubjectReqDto, rowBounds));
		pageResult.setItems(querySubjectResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), querySubjectReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(querySubjectReqDto.getPage());
		pageResult.setPer_page(querySubjectReqDto.getPer_page());

		return pageResult;
	}

	private List<QuerySubjectResDto> convertToResult(List<BaseSubject> baseSubjects) {
		List<QuerySubjectResDto> querySubjectResDtos = new ArrayList<QuerySubjectResDto>();
		if (CollectionUtils.isEmpty(baseSubjects)) {
			return querySubjectResDtos;
		}
		for (BaseSubject baseSubject : baseSubjects) {
			// 操作集合
			QuerySubjectResDto querySubjectResDto = convertToDto(baseSubject);
			List<CodeValue> operList = getOperList(baseSubject.getState());
			querySubjectResDto.setOpertaList(operList);
			querySubjectResDtos.add(querySubjectResDto);
		}
		return querySubjectResDtos;
	}

	private QuerySubjectResDto convertToDto(BaseSubject baseSubject) {
		if (baseSubject == null) {
			return null;
		}
		QuerySubjectResDto querySubjectResDto = new QuerySubjectResDto();
		querySubjectResDto.setState(baseSubject.getState());
		querySubjectResDto.setStateLabel(
				ServiceSupport.getValueByBizCode(BizCodeConsts.SUBJECT_STATE, baseSubject.getState() + ""));
		querySubjectResDto.setSubjectType(baseSubject.getSubjectType());
		querySubjectResDto.setSubjectTypeLabel(getSubjectNameByType(baseSubject.getSubjectType()));
		querySubjectResDto.setAbbreviation(baseSubject.getAbbreviation());
		querySubjectResDto.setChineseName(baseSubject.getChineseName());
		querySubjectResDto.setEnglishName(baseSubject.getEnglishName());
		querySubjectResDto.setRegNo(baseSubject.getRegNo());
		querySubjectResDto.setRegPhone(baseSubject.getRegPhone());
		querySubjectResDto.setRegPlace(baseSubject.getRegPlace());
		querySubjectResDto.setOfficeAddress(baseSubject.getOfficeAddress());
		querySubjectResDto.setId(baseSubject.getId());
		querySubjectResDto.setOmsSupplierNo(baseSubject.getOmsSupplierNo());
		querySubjectResDto.setPmsCustNo(baseSubject.getPmsCustNo());
		querySubjectResDto.setSubjectNo(baseSubject.getSubjectNo());
		querySubjectResDto.setWarehouseNo(baseSubject.getWarehouseNo());
		querySubjectResDto.setCreateAt(baseSubject.getCreateAt());
		querySubjectResDto.setCreator(baseSubject.getCreator());
		querySubjectResDto.setFinanceManagerId(baseSubject.getFinanceManagerId());// 财务主管
		querySubjectResDto.setDepartmentManagerId(baseSubject.getDepartmentManagerId());// 部门主管
		if (!StringUtils.isEmpty(baseSubject.getInvoiceQuotaType())) {
			querySubjectResDto.setInvoiceQuotaType(baseSubject.getInvoiceQuotaType());// 开票限额类型
			querySubjectResDto.setInvoiceQuotaAmount(ServiceSupport
					.getValueByBizCode(BizCodeConsts.PROVIDE_INVOICE_QUOTA, baseSubject.getInvoiceQuotaType() + ""));
		}
		if (querySubjectResDto.getFinanceManagerId() != null) {
			querySubjectResDto
					.setFinanceManagerName(cacheService.getUserChineseNameByid(baseSubject.getFinanceManagerId()));
		}
		if (querySubjectResDto.getDepartmentManagerId() != null) {
			querySubjectResDto.setDepartmentManagerName(
					cacheService.getUserChineseNameByid(baseSubject.getDepartmentManagerId()));
		}
		if (baseSubject.getWarehouseType() != null) {
			querySubjectResDto.setWarehouseType(baseSubject.getWarehouseType());
			querySubjectResDto.setWarehouseTypeLabel(ServiceSupport.getValueByBizCode(BizCodeConsts.WAREHOUSE_TYPE,
					baseSubject.getWarehouseType() + ""));
		}
		if (baseSubject.getNation() != null) {
			querySubjectResDto.setNation(baseSubject.getNation());
			querySubjectResDto.setNationLabel(
					ServiceSupport.getValueByBizCode(BizCodeConsts.NATION_TYPE, baseSubject.getNation() + ""));
		}
		return querySubjectResDto;
	}

	private String getSubjectNameByType(Integer subjectType) {
		StringBuilder sBuilder = new StringBuilder();
		if (StringUtils.isEmpty(subjectType)) {
			return "";
		}
		if (subjectType == BaseConsts.TWO) {
			sBuilder.append(ServiceSupport.getValueByBizCode(BizCodeConsts.SUBJECT_TYPE, BaseConsts.TWO + ""));
			return sBuilder.toString();
		} else {
			if ((subjectType & BaseConsts.ONE) == BaseConsts.ONE) {
				sBuilder.append(ServiceSupport.getValueByBizCode(BizCodeConsts.SUBJECT_TYPE, BaseConsts.ONE + ""))
						.append(",");
			}
			if ((subjectType & BaseConsts.FOUR) == BaseConsts.FOUR) {
				sBuilder.append(ServiceSupport.getValueByBizCode(BizCodeConsts.SUBJECT_TYPE, BaseConsts.FOUR + ""))
						.append(",");
			}
			if ((subjectType & BaseConsts.EIGHT) == BaseConsts.EIGHT) {
				sBuilder.append(ServiceSupport.getValueByBizCode(BizCodeConsts.SUBJECT_TYPE, BaseConsts.EIGHT + ""))
						.append(",");
			}
		}
		return sBuilder.toString().endsWith(",") ? sBuilder.toString().substring(0, sBuilder.toString().length() - 1)
				: sBuilder.toString();
	}

	/**
	 * 根据状态得到操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState(Integer state) {
		List<String> opertaList = Lists.newArrayList();
		if (state == null) {
			return opertaList;
		}
		switch (state) {
		// 状态,1表示待提交，2表示已完成，3表示已锁定
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.SUBMIT);
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.DELETE);
			break;
		case BaseConsts.TWO:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.LOCK);
			opertaList.add(OperateConsts.EDIT);
			break;
		case BaseConsts.THREE:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.UNLOCK);
			break;
		}

		return opertaList;
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
				QuerySubjectResDto.Operate.operMap);
		return oprResult;
	}

	public void updateBaseSubject(BaseSubject vo) {
		BaseSubject baseSubject = loadAndLockEntityById(vo.getId());
		if (((vo.getSubjectType()) & (BaseConsts.ONE)) == BaseConsts.ONE) {
			if (StringUtils.isEmpty(vo.getInvoiceQuotaType())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "开票限额不能为空");
			}
			if (StringUtils.isEmpty(vo.getFinanceManagerId())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "财务主管不能为空");
			}
		}
		if (baseSubject.getSubjectType() == 2) {
			QuerySubjectReqDto query = new QuerySubjectReqDto();
			query.setSubjectNo(vo.getSubjectNo());
			query.setSubjectType(baseSubject.getSubjectType());
			if (baseSubject.getSubjectNo().equals(vo.getSubjectNo())) {
				int result = baseSubjectDao.updateBaseSubject(vo);
				if (result <= 0) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "更新失败" + vo.getId());
				}
			} else {
				List<BaseSubject> subject = baseSubjectDao.querySubjectByType(query);
				if (CollectionUtils.isNotEmpty(subject)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "该编号已存在");
				} else {
					int result = baseSubjectDao.updateBaseSubject(vo);
					if (result <= 0) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "更新失败" + vo.getId());
					}
				}
			}
		} else {
			int result = baseSubjectDao.updateBaseSubject(vo);
			if (result <= 0) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "更新失败:" + vo.getId());
			}
		}

	}

	public void deleteSubject(BaseSubject vo) {
		BaseSubject baseSubject = loadAndLockEntityById(vo.getId());
		baseSubject.setDeleteAt(new Date());
		baseSubject.setIsDelete(BaseConsts.ONE);
		baseSubject.setDeleter(ServiceSupport.getUser().getChineseName());
		int updRes = baseSubjectDao.deleteLogicSubject(baseSubject);
		if (updRes <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "删除失败:" + baseSubject.getId());
		}
	}

	public void submitSubject(BaseSubject vo) {
		BaseSubject baseSubject = loadAndLockEntityById(vo.getId());
		String subjectNo = baseSubject.getSubjectNo();
		if ((baseSubject.getSubjectType() & BaseConsts.SUBJECT_TYPE_BUSI_UNIT) == BaseConsts.SUBJECT_TYPE_BUSI_UNIT) {
			subjectNo = sequenceService.getNumIncByBusName(BaseConsts.BUSIUNIT_NO_PREFIX, SeqConsts.S_BUSI_UNIT_NO,
					BaseConsts.THREE);
		} else if ((baseSubject.getSubjectType()
				& BaseConsts.SUBJECT_TYPE_CUSTOMER) == BaseConsts.SUBJECT_TYPE_CUSTOMER) {
			subjectNo = sequenceService.getNumIncByBusName(BaseConsts.CUSTOMER_NO_PREFIX, SeqConsts.S_CUSTOMER_NO,
					BaseConsts.SIX);
		} else if ((baseSubject.getSubjectType()
				& BaseConsts.SUBJECT_TYPE_SUPPLIER) == BaseConsts.SUBJECT_TYPE_SUPPLIER) {
			subjectNo = sequenceService.getNumIncByBusName(BaseConsts.CUSTOMER_NO_PREFIX, SeqConsts.S_CUSTOMER_NO,
					BaseConsts.SIX);
		}
		if (StringUtils.isEmpty(subjectNo)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "客户【" + baseSubject.getAbbreviation() + "】主体类型错误");
		}
		baseSubject.setSubjectNo(subjectNo);
		baseSubject.setState(BaseConsts.TWO);

		int updRes = baseSubjectDao.submitSubject(baseSubject);
		if (updRes <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "提交失败:" + baseSubject.getId());
		}
	}

	public void lockSubject(BaseSubject vo) {
		BaseSubject baseSubject = loadAndLockEntityById(vo.getId());
		baseSubject.setLockAt(new Date());
		baseSubject.setState(BaseConsts.THREE);
		baseSubject.setLockedBy(ServiceSupport.getUser().getChineseName());
		int updRes = baseSubjectDao.lockSubject(baseSubject);
		if (updRes <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "锁定失败:" + baseSubject.getId());
		}
	}

	public void unlockSubject(BaseSubject vo) {
		BaseSubject baseSubject = loadAndLockEntityById(vo.getId());
		baseSubject.setLockAt(null);
		baseSubject.setState(BaseConsts.TWO);
		baseSubject.setLockedBy(null);
		int updRes = baseSubjectDao.unLockSubject(baseSubject);
		if (updRes <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "解锁锁失败:" + baseSubject.getId());
		}
	}

	/**
	 * 根据pms供应商编码和类型查询基本信息
	 * 
	 * @param querySubjectReqDto
	 * @return
	 */
	public List<BaseSubject> querySubTypeAndPmsSupplier(QuerySubjectReqDto querySubjectReqDto) {
		List<BaseSubject> custs = baseSubjectDao.querySubjectByCond(querySubjectReqDto);
		return custs;
	}
}
