package com.scfs.service.interf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.interf.PMSSupplierBindDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.interf.dto.PMSSupplierBindReqDto;
import com.scfs.domain.interf.dto.PMSSupplierBindResDto;
import com.scfs.domain.interf.entity.PMSSupplierBind;
import com.scfs.domain.result.PageResult;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

@Service
public class PMSSupplierBindService {

	@Autowired
	private PMSSupplierBindDao pMSSupplierBindDao;

	@Autowired
	private PmsSupplierRpcService pmsSupplierRpcService;

	@Autowired
	private CacheService cacheService;

	public PageResult<PMSSupplierBindResDto> queryInfoByCon(PMSSupplierBindReqDto pMSSupplierBindReqDto) {
		PageResult<PMSSupplierBindResDto> result = new PageResult<PMSSupplierBindResDto>();
		int offSet = PageUtil.getOffSet(pMSSupplierBindReqDto.getPage(), pMSSupplierBindReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, pMSSupplierBindReqDto.getPer_page());
		pMSSupplierBindReqDto.setUserId(ServiceSupport.getUser().getId());
		List<PMSSupplierBind> pMSSupplierBindList = pMSSupplierBindDao.queryByCondition(pMSSupplierBindReqDto,
				rowBounds);
		List<PMSSupplierBindResDto> pageList = convertToResDto(pMSSupplierBindList);
		result.setItems(pageList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), pMSSupplierBindReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(pMSSupplierBindReqDto.getPage());
		result.setPer_page(pMSSupplierBindReqDto.getPer_page());
		return result;
	}

	public PMSSupplierBind edit(Integer id) {
		PMSSupplierBind pMSSupplierBind = pMSSupplierBindDao.selectById(id);
		return pMSSupplierBind;
	}

	public void update(PMSSupplierBind pMSSupplierBind) {
		// 校验是否可以绑定数据
		boolean status = pmsSupplierRpcService.validateSupplier(pMSSupplierBind.getPmsSupplierNo());
		if (status) {
			PMSSupplierBindReqDto req = new PMSSupplierBindReqDto();
			req.setSupplierNo(pMSSupplierBind.getSupplierNo());
			req.setPmsSupplierNo(pMSSupplierBind.getPmsSupplierNo());
			Integer result = pMSSupplierBindDao.update(pMSSupplierBind);
			if (result <= 0) {
				throw new BaseException(ExcMsgEnum.PMSS_SUPPLIER_UPDATE_ERROR);
			}
		} else {
			throw new BaseException(ExcMsgEnum.PMSS_SUPPLIER_ADD_NOT_ERROR);
		}
	}

	public void updatePro(PMSSupplierBind pMSSupplierBind) {
		Integer result = pMSSupplierBindDao.update(pMSSupplierBind);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.PMSS_SUPPLIER_UPDATE_ERROR);
		}
	}

	public void delete(Integer id) {

		Integer result = pMSSupplierBindDao.deleteById(id);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.PMSS_SUPPLIER_DELETE_ERROR);
		}
	}

	public void submit(Integer id) {
		PMSSupplierBind pMSSupplierBind = pMSSupplierBindDao.selectById(id);
		// 上传数据
		boolean status = pmsSupplierRpcService.openSupplierInfo(pMSSupplierBind);
		if (status) {
			pMSSupplierBind.setStatus(BaseConsts.TWO);
		} else {
			pMSSupplierBind.setStatus(BaseConsts.ONE);
		}
		Integer result = pMSSupplierBindDao.submit(pMSSupplierBind);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.PMSS_SUPPLIER_SUBMIT_ERROR);
		}

	}

	/**
	 * 解绑
	 * 
	 * @param id
	 */
	public void unbind(Integer id) {
		PMSSupplierBind pMSSupplierBind = pMSSupplierBindDao.selectById(id);
		// 上传数据
		boolean status = pmsSupplierRpcService.closeSupplierInfo(pMSSupplierBind);
		if (status) {
			pMSSupplierBind.setStatus(BaseConsts.ONE);
			Integer result = pMSSupplierBindDao.submit(pMSSupplierBind);
			if (result <= 0) {
				throw new BaseException(ExcMsgEnum.PMSS_SUPPLIER_BIND_DATABASE_ERROR);
			}
		} else {
			throw new BaseException(ExcMsgEnum.PMSS_SUPPLIER_UNBIND_ERROR);
		}
	}

	/**
	 * 绑定
	 * 
	 * @param id
	 */
	public void bind(Integer id) {
		PMSSupplierBind pMSSupplierBind = pMSSupplierBindDao.selectById(id);
		// 上传数据
		boolean status = pmsSupplierRpcService.openSupplierInfo(pMSSupplierBind);
		if (status) {
			pMSSupplierBind.setStatus(BaseConsts.TWO);
		} else {
			throw new BaseException(ExcMsgEnum.PMSS_SUPPLIER_UNBIND_ERROR);
		}
		Integer result = pMSSupplierBindDao.submit(pMSSupplierBind);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.PMSS_SUPPLIER_BIND_DATABASE_ERROR);
		}

	}

	public void add(PMSSupplierBind pMSSupplierBind) {
		// 校验是否可以绑定数据
		boolean status = pmsSupplierRpcService.validateSupplier(pMSSupplierBind.getPmsSupplierNo());
		if (status) {
			pMSSupplierBind.setStatus(BaseConsts.ZERO);
			pMSSupplierBind.setCreateAt(new Date());
			pMSSupplierBind.setCreator(ServiceSupport.getUser().getChineseName());
			pMSSupplierBind.setCreatorId(ServiceSupport.getUser().getId());
			Integer result = pMSSupplierBindDao.insert(pMSSupplierBind);
			if (result <= 0) {
				throw new BaseException(ExcMsgEnum.PMSS_SUPPLIER_ADD_ERROR);
			}
		} else {
			throw new BaseException(ExcMsgEnum.PMSS_SUPPLIER_ADD_NOT_ERROR);
		}

	}

	public PMSSupplierBindResDto detail(Integer id) {
		PMSSupplierBind pMSSupplierBind = pMSSupplierBindDao.selectById(id);
		PMSSupplierBindResDto pMSSupplierBindResDto = convertToResDto(pMSSupplierBind);
		return pMSSupplierBindResDto;
	}

	private List<PMSSupplierBindResDto> convertToResDto(List<PMSSupplierBind> pMSSupplierBindList) {
		// TODO Auto-generated method stub
		List<PMSSupplierBindResDto> billDeliveryDtlResDtoList = new ArrayList<PMSSupplierBindResDto>(5);
		if (CollectionUtils.isEmpty(pMSSupplierBindList)) {
			return billDeliveryDtlResDtoList;
		}
		for (PMSSupplierBind pMSSupplierBind : pMSSupplierBindList) {
			PMSSupplierBindResDto pMSSupplierBindResDto = convertToResDto(pMSSupplierBind);
			billDeliveryDtlResDtoList.add(pMSSupplierBindResDto);
		}
		return billDeliveryDtlResDtoList;
	}

	private PMSSupplierBindResDto convertToResDto(PMSSupplierBind pMSSupplierBind) {
		PMSSupplierBindResDto pMSSupplierBindResDto = new PMSSupplierBindResDto();
		BeanUtils.copyProperties(pMSSupplierBind, pMSSupplierBindResDto);
		pMSSupplierBindResDto.setProjectName(cacheService.showProjectNameById(pMSSupplierBind.getProjectId()));
		pMSSupplierBindResDto
				.setSupplierName(cacheService.getSupplierById(pMSSupplierBind.getSupplierId()).getChineseName());
		pMSSupplierBindResDto.setStatus(pMSSupplierBind.getStatus());
		pMSSupplierBindResDto.setStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.PMSS_STATUS,
				Integer.toString(pMSSupplierBind.getStatus())));
		pMSSupplierBindResDto.setOpertaList(getOperList(pMSSupplierBind.getStatus()));
		pMSSupplierBindResDto.setCreateAt(pMSSupplierBind.getCreateAt());
		pMSSupplierBindResDto.setCreator(pMSSupplierBind.getCreator());
		return pMSSupplierBindResDto;
	}

	/**
	 * 根据状态获取操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState(Integer state) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList(5);
		opertaList.add(OperateConsts.DETAIL);
		switch (state) {
		// 状态, 0-待提交 1-已提交未绑定 2-已提交已绑定
		case BaseConsts.ZERO:
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.DELETE);
			opertaList.add(OperateConsts.SUBMIT);
			break;
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.DELETE);
			opertaList.add(OperateConsts.BIND);
			break;
		case BaseConsts.TWO:
			opertaList.add(OperateConsts.UNBIND);
			opertaList.add(OperateConsts.EDIT);
			break;
		}
		return opertaList;
	}

	private List<CodeValue> getOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				PMSSupplierBindResDto.Operate.operMap);
		return oprResult;
	}

	public PMSSupplierBind queryEntityByProjectIdAndSupplierId(Integer projectId, Integer supplierId) {
		PMSSupplierBindReqDto pMSSupplierBindReqDto = new PMSSupplierBindReqDto();
		pMSSupplierBindReqDto.setProjectId(projectId);
		pMSSupplierBindReqDto.setSupplierId(supplierId);
		PMSSupplierBind pMSSupplierBind = pMSSupplierBindDao.queryEntityByProjectIdAndSupplierId(projectId, supplierId);
		return pMSSupplierBind;
	}

	/**
	 * 根据供应商的PMS供应商编号查询供应商的ID
	 * 
	 * @param pmsSupplierNo
	 * @return
	 */
	public PMSSupplierBind querySupplierByNo(String pmsSupplierNo) {
		if (StringUtils.isEmpty(pmsSupplierNo)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "PMS供应商编号为空");
		}
		PMSSupplierBind supplierId = pMSSupplierBindDao.queryEntityBySupplierNo(pmsSupplierNo);
		return supplierId;
	}

	/**
	 * 根据供应商编号和经营单位查询PMS供应商绑定数据
	 * 
	 * @param pMSSupplierBindReqDto
	 * @return
	 */
	public PMSSupplierBind queryPmsBySuppNoAndBui(PMSSupplierBindReqDto pMSSupplierBindReqDto) {
		PMSSupplierBind pmsSupplierBind = pMSSupplierBindDao.queryBySupplierNoAndBusi(pMSSupplierBindReqDto);
		return pmsSupplierBind;
	}
}
