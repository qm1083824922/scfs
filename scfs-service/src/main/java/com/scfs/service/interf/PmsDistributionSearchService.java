package com.scfs.service.interf;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.api.pms.PmsPayDao;
import com.scfs.dao.api.pms.PmsSeriesDao;
import com.scfs.dao.api.pms.PmsStoreInDao;
import com.scfs.dao.api.pms.PmsStoreOutDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.CodeValue;
import com.scfs.domain.api.pms.entity.PmsPay;
import com.scfs.domain.api.pms.entity.PmsStoreIn;
import com.scfs.domain.api.pms.model.PmsSeriesModel;
import com.scfs.domain.interf.dto.PmsDistributionSearchReqDto;
import com.scfs.domain.interf.dto.PmsDistributionSearchResDto;
import com.scfs.domain.interf.dto.PmsStoreResDto;
import com.scfs.domain.pay.entity.PmsStoreOut;
import com.scfs.domain.result.PageResult;
import com.scfs.service.api.pms.PmsSyncBillInStoreService;
import com.scfs.service.api.pms.PmsSyncBillOutStoreService;
import com.scfs.service.po.PurchaseOrderService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 * 
 *  File: PmsDistributionSearchController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月06日			Administrator
 *
 * </pre>
 */
@Service
public class PmsDistributionSearchService {

	@Autowired
	private PmsSeriesDao pmsSeriesDao;

	@Autowired
	private PmsStoreInDao storeInDao;

	@Autowired
	private PmsStoreOutDao storeOutDao;

	@Autowired
	private PmsSyncBillInStoreService billInStoreService;

	@Autowired
	private PmsSyncBillOutStoreService billOutStoreService;

	@Autowired
	PurchaseOrderService purchaseOrderService;
	@Autowired
	private PmsBillStoreInService billStoreInService;

	@Autowired
	private PmsPayDao pmsPayDao;

	@Autowired
	private PmsPayWaitService pmsPayWaitService;

	/**
	 * 查询当前PMS铺货业务的接口数据
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<PmsDistributionSearchResDto> queryDistributionResultByCon(PmsDistributionSearchReqDto reqDto) {
		// 进行分页查询
		PageResult<PmsDistributionSearchResDto> pageResult = new PageResult<PmsDistributionSearchResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		// 查询当前所有的PMS铺货接口的数据
		List<PmsSeriesModel> list = pmsSeriesDao.queryResultByCon(reqDto, rowBounds);
		List<PmsDistributionSearchResDto> distributionSearchResDtos = convertSearchResDtos(list, BaseConsts.FLAG_NO);
		pageResult.setItems(distributionSearchResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(reqDto.getPage());
		pageResult.setPer_page(reqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 查询PMS铺货成功的接口数据
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<PmsDistributionSearchResDto> queryDistributionSuccessResultByCon(
			PmsDistributionSearchReqDto reqDto) {
		// 进行分页查询
		PageResult<PmsDistributionSearchResDto> pageResult = new PageResult<PmsDistributionSearchResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		// 查询当前所有的PMS铺货接口的数据
		List<PmsSeriesModel> list = pmsSeriesDao.querySuccessResultByCon(reqDto, rowBounds);
		List<PmsDistributionSearchResDto> distributionSearchResDtos = convertSearchResDtos(list, BaseConsts.FLAG_YES);
		pageResult.setItems(distributionSearchResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(reqDto.getPage());
		pageResult.setPer_page(reqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 查询当前PMS铺货关联的同步明细
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<PmsStoreResDto> detailDistributionResultByCon(PmsDistributionSearchReqDto reqDto) {
		PageResult<PmsStoreResDto> pageResult = new PageResult<PmsStoreResDto>();
		List<PmsStoreResDto> dtos = new ArrayList<PmsStoreResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		if (reqDto.getType() == BaseConsts.ONE) {// 为1,PMS 入库的接口
			dtos = billInStoreService.convertStoreInResDtos(storeInDao.queryPmsStoreInBySeries(reqDto, rowBounds),
					reqDto);
		} else if (reqDto.getType() == BaseConsts.TWO) {// 为2，PMS 出库的接口
			dtos = billOutStoreService.convertStoreOutResDtos(storeOutDao.queryStoreBySeries(reqDto, rowBounds),
					reqDto);
		} else if (reqDto.getType() == BaseConsts.THREE) {// 为3，pms请款单接口
			dtos = pmsPayWaitService.converPmsPayResDtos(pmsPayDao.queryPayBySeries(reqDto, rowBounds));
		}
		pageResult.setItems(dtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(reqDto.getPage());
		pageResult.setPer_page(reqDto.getPer_page());
		return pageResult;
	}

	/**
	 * - PMS 接口重新调用
	 * 
	 * @param reqDto
	 */
	public void reinvokeDistributionByCon(PmsDistributionSearchReqDto reqDto) {
		// 根据传入的接口类型调用相应的接口数据
		if (reqDto.getType() == BaseConsts.ONE) {// 为1,PMS 入库的接口
			List<PmsStoreIn> list = storeInDao.queryPmsStoreInFailure(reqDto);
			if (!ListUtil.isEmpty(list)) {
				for (PmsStoreIn pmsStoreIn : list) {
					try {
						// 插入头信息和明细信息
						billStoreInService.dealPmsStoreIn(pmsStoreIn.getId(), false);
						billInStoreService.dealSuccess(pmsStoreIn.getId(), "PMS入库调用成功");
					} catch (BaseException e) {
						billInStoreService.dealFail(pmsStoreIn.getId(), e.getMsg(), BaseConsts.TWO);
					} catch (Exception e) {
						billInStoreService.dealFail(pmsStoreIn.getId(), e.getMessage(), BaseConsts.TWO);
					}
				}
			}
		} else if (reqDto.getType() == BaseConsts.TWO) {// 为2，PMS 出库的接口
			List<PmsStoreOut> list = storeOutDao.queryStoreOutFailure(reqDto);
			if (!ListUtil.isEmpty(list)) {
				for (PmsStoreOut pmsStoreOut : list) {
					try {
						purchaseOrderService
								.updatePurchaseOrderLineByCon(storeOutDao.queryEntityById(pmsStoreOut.getId()));
						billOutStoreService.dealSuccess(pmsStoreOut.getId(), "PMS出库调用成功");
					} catch (BaseException e) {
						billOutStoreService.dealFail(pmsStoreOut.getId(), e.getMsg(), BaseConsts.TWO);
					} catch (Exception e) {
						billOutStoreService.dealFail(pmsStoreOut.getId(), e.getMessage(), BaseConsts.TWO);
					}
				}
			}
		} else if (reqDto.getType() == BaseConsts.THREE) {// 为3，pms请款单接口
			// 查询为当前驳回的数据
			List<PmsPay> pmsPays = pmsPayDao.queryPayFailureByStatu(reqDto);
			if (!ListUtil.isEmpty(pmsPays)) {
				for (PmsPay pmsPay : pmsPays) {
					try {
						pmsPayWaitService.dealPmsPayRebut(pmsPay.getId());
					} catch (BaseException e) {
						pmsPayWaitService.dealFail(pmsPay.getId(), e.getMsg());
					} catch (Exception e) {
						pmsPayWaitService.dealFail(pmsPay.getId(), e.getMessage());
					}
				}
			}
			List<PmsPay> pays = pmsPayDao.queryPayFailureByStatuBy(reqDto);
			if (!ListUtil.isEmpty(pays)) {
				for (PmsPay pmsPay : pays) {
					try {
						pmsPayWaitService.dealPmsPayWait(pmsPay, false);
					} catch (BaseException e) {
						pmsPayWaitService.dealFail(pmsPay.getId(), e.getMsg());
					} catch (Exception e) {
						pmsPayWaitService.dealFail(pmsPay.getId(), e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * 查询当前PMS铺货接口调用失败的原因
	 * 
	 * @param reqDto
	 * @return
	 */
	public String queryDistributionFailureDetail(PmsDistributionSearchReqDto reqDto) {
		if (reqDto == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "PMS铺货失败详情查询数据为空");
		}
		StringBuffer buffer = new StringBuffer();
		if (reqDto.getType() != null) {
			if (reqDto.getType() == BaseConsts.ONE) {// PMS入库接口
				List<PmsStoreIn> storeIns = storeInDao.queryPmsStoreInFailure(reqDto);
				if (!ListUtil.isEmpty(storeIns)) {
					for (PmsStoreIn pmsStoreIn : storeIns) {
						buffer.append("采购单号:" + pmsStoreIn.getPurchase_sn() + ";处理结果:" + pmsStoreIn.getDealFlag()
								+ ";返回信息:" + pmsStoreIn.getDealMsg() + ";");
					}
				}
			} else if (reqDto.getType() == BaseConsts.TWO) {// PMS出库接口
				List<PmsStoreOut> outList = storeOutDao.queryStoreOutFailure(reqDto);
				if (!ListUtil.isEmpty(outList)) {
					for (PmsStoreOut pmsStoreOut : outList) {
						buffer.append("采购单号:" + pmsStoreOut.getPurchase_sn() + ";处理结果:" + pmsStoreOut.getDealFlag()
								+ ";返回信息:" + pmsStoreOut.getDealMsg() + ";");
					}
				}
			} else if (reqDto.getType() == BaseConsts.THREE) {// PMS请款单接口
				List<PmsPay> pmsPays = pmsPayDao.queryPayFailure(reqDto);
				if (!ListUtil.isEmpty(pmsPays)) {
					for (PmsPay pmsPay : pmsPays) {
						buffer.append("请款单号:" + pmsPay.getPay_sn() + ";处理结果:" + pmsPay.getDealFlag() + ";返回信息:"
								+ pmsPay.getDealMsg() + ";");
					}
				}
			}
		}
		return buffer.toString();

	}

	/**
	 * 封装当前PMS铺货对象到ResDto
	 * 
	 * @param pmsSeries
	 * @return
	 */
	public List<PmsDistributionSearchResDto> convertSearchResDtos(List<PmsSeriesModel> models, String flag) {
		List<PmsDistributionSearchResDto> distributionSearchResDtos = new ArrayList<PmsDistributionSearchResDto>();
		if (ListUtil.isEmpty(models)) {
			return distributionSearchResDtos;
		} else {
			for (PmsSeriesModel model : models) {
				PmsDistributionSearchResDto searchResDto = convertToPmsDistributionResDto(model);
				List<CodeValue> operList = getOperList(model, flag);
				searchResDto.setOpertaList(operList);
				distributionSearchResDtos.add(searchResDto);
			}
		}
		return distributionSearchResDtos;
	}

	/**
	 * PMS铺货数据的封装
	 * 
	 * @param series
	 * @return
	 */
	public PmsDistributionSearchResDto convertToPmsDistributionResDto(PmsSeriesModel model) {
		PmsDistributionSearchResDto resDto = new PmsDistributionSearchResDto();
		if (null != model) {
			BeanUtils.copyProperties(model, resDto);
			resDto.setId(model.getId());// PMS铺货id
			resDto.setMessage(model.getMessage());// 描述
			resDto.setInvokeTime(model.getInvokeTime());// 调用时间
			resDto.setUpdateAt(model.getUpdateAt());// 修改时间
			resDto.setCreateAt(model.getCreateAt());// 创建时间
			resDto.setType(model.getType());// 接口类型
			resDto.setPurchaseSn(model.getPurchaseSn());// 采购单单号
			resDto.setDealflag(model.getDealflag());// 处理状态
			resDto.setTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.PMS_SERIES_TYPE, model.getType() + ""));
		}
		return resDto;

	}

	/**
	 * 获取操作列表
	 * 
	 * @param pmsSeries
	 * @return
	 */
	private List<CodeValue> getOperList(PmsSeriesModel models, String flag) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(models, flag);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				PmsDistributionSearchResDto.Operate.operMap);
		return oprResult;
	}

	/**
	 * 根据状态得到操作列表
	 *
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState(PmsSeriesModel models, String flag) {
		List<String> opertaList = Lists.newArrayList();
		if (flag.equals(BaseConsts.FLAG_YES)) {
			opertaList.add(OperateConsts.DETAIL);
		} else {
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.REINVOKE);
		}
		return opertaList;
	}

}
