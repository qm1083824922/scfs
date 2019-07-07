package com.scfs.service.base.send;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.SenderManageDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.dto.req.SenderManageReqDto;
import com.scfs.domain.base.dto.resp.SenderManageResDto;
import com.scfs.domain.base.entity.SenderManage;
import com.scfs.domain.logistics.dto.resp.StlResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.AuditService;
import com.scfs.service.logistics.StlService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 *
 *  File: SenderManageService.java
 *  Description:
 *  TODO
 *  Date,                   Who,
 *  2017年06月22日         Administrator
 *
 * </pre>
 */
@Service
public class SenderManageService {
	@Autowired
	private SenderManageDao senderManageDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private StlService stlService;
	@Autowired
	private AuditService auditService;

	/**
	 * 获取列表信息
	 * 
	 * @param senderManageReqDto
	 * @return
	 */
	public PageResult<SenderManageResDto> querySenderManageResultsByCon(SenderManageReqDto senderManageReqDto) {
		PageResult<SenderManageResDto> result = new PageResult<SenderManageResDto>();
		int offSet = PageUtil.getOffSet(senderManageReqDto.getPage(), senderManageReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, senderManageReqDto.getPer_page());
		List<SenderManage> sendersList = senderManageDao.queryResultsByCon(senderManageReqDto, rowBounds);
		List<SenderManageResDto> sendersResList = convertToResult(sendersList);
		result.setItems(sendersResList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), senderManageReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(senderManageReqDto.getPage());
		result.setPer_page(senderManageReqDto.getPer_page());
		return result;
	}

	/**
	 * 添加数据
	 * 
	 * @param senderManage
	 * @return
	 */
	public Integer addSenderManage(SenderManage senderManage) {
		senderManage.setStatus(BaseConsts.ZERO);
		senderManage.setIsDelete(BaseConsts.ZERO);
		senderManage.setCreator(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
		senderManage.setCreateAt(new Date());
		Integer result = senderManageDao.insert(senderManage);
		if (result < 1)
			throw new BaseException(ExcMsgEnum.GOODS_ADD_EXCEPTION);
		return senderManage.getId();
	}

	/**
	 * 编辑/浏览
	 * 
	 * @param senderManage
	 * @return
	 */
	public Result<SenderManageResDto> querySenderManageById(SenderManage senderManage) {
		Result<SenderManageResDto> result = new Result<SenderManageResDto>();
		SenderManage sender = senderManageDao.queryEntityById(senderManage.getId());
		result.setItems(convertToResDto(sender));
		return result;
	}

	/**
	 * 编辑信息
	 * 
	 * @param senderManage
	 * @return
	 */
	public BaseResult updateSenderManage(SenderManage senderManage) {
		BaseResult result = new BaseResult();
		senderManageDao.queryEntityById(senderManage.getId());
		senderManageDao.update(senderManage);
		return result;
	}

	/**
	 * 删除数据
	 * 
	 * @param senderManage
	 * @return
	 */
	public BaseResult deleteSenderManage(SenderManage senderManage) {
		senderManageDao.queryEntityById(senderManage.getId());
		BaseResult result = new BaseResult();
		senderManage.setIsDelete(BaseConsts.ONE);
		senderManageDao.update(senderManage);
		return result;
	}

	/**
	 * 账期提醒定时任务业务处理
	 */
	public void adventMessageSchedule() {
		List<StlResDto> stlListThree = stlService.queryAllStlByAdvent(BaseConsts.THREE);// 临期3天
		if (stlListThree.size() > BaseConsts.ZERO) {
			dealMessage(stlListThree, BaseConsts.THREE);
		}
		List<StlResDto> stlListSeven = stlService.queryAllStlByAdvent(BaseConsts.SEVEN);// 临期7天
		if (stlListSeven.size() > BaseConsts.ZERO) {
			dealMessage(stlListSeven, BaseConsts.SEVEN);
		}
	}

	public void dealMessage(List<StlResDto> stlList, Integer type) {
		for (StlResDto stl : stlList) {
			SenderManageReqDto senderReqDto = new SenderManageReqDto();
			senderReqDto.setProjectId(stl.getProjectId());
			List<SenderManage> sendList = senderManageDao.querySenderByProject(senderReqDto);
			for (SenderManage senderManage : sendList) {
				String title = "账期提醒";
				String message = "项目：" + stl.getProjectName() + ",仓库:" + stl.getWarehouseName() + ",可用数量:"
						+ DecimalUtil.toQuantityString(stl.getAvailableNum()) + ",库存金额:" + stl.getAvailableModelStr()
						+ ",临期" + type + "天";
				auditService.sendWarnRtx(senderManage.getUserId(), title, message);
			}
		}
	}

	private List<SenderManageResDto> convertToResult(List<SenderManage> sendersList) {
		List<SenderManageResDto> senderManageResDtoList = new ArrayList<SenderManageResDto>();
		if (CollectionUtils.isEmpty(sendersList)) {
			return senderManageResDtoList;
		}
		for (SenderManage senderManage : sendersList) {
			SenderManageResDto restDto = convertToResDto(senderManage);
			restDto.setOpertaList(getOperList(senderManage.getStatus()));
			senderManageResDtoList.add(restDto);
		}
		return senderManageResDtoList;
	}

	public SenderManageResDto convertToResDto(SenderManage model) {
		SenderManageResDto result = new SenderManageResDto();
		result.setId(model.getId());
		result.setUserId(model.getUserId());
		result.setUserName(cacheService.getUserChineseNameByid(model.getUserId()));
		result.setBizSendType(model.getBizSendType());
		result.setBizSendTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.BIZ_SEND_TYPE, model.getBizSendType() + ""));
		result.setCreator(model.getCreator());
		result.setCreateAt(model.getCreateAt());
		result.setUpdateAt(model.getUpdateAt());
		result.setStatus(model.getStatus());
		result.setIsDelete(model.getIsDelete());
		return result;
	}

	private List<CodeValue> getOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				SenderManageResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getOperListByState(Integer state) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList(3);
		opertaList.add(OperateConsts.DETAIL);
		switch (state) {
		// 状态
		case BaseConsts.ZERO:
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.DELETE);
			break;
		}
		return opertaList;
	}
}
