package com.scfs.service.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.invoice.InvoiceCollectOverseasPoDao;
import com.scfs.dao.po.PurchaseOrderLineDao;
import com.scfs.dao.po.PurchaseOrderTitleDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.invoice.dto.req.InvoiceCollectOverseasPoReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectOverseasPoFileResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectOverseasPoResDto;
import com.scfs.domain.invoice.entity.InvoiceCollectOverseas;
import com.scfs.domain.invoice.entity.InvoiceCollectOverseasPo;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.entity.PurchaseOrderLine;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.po.model.PoLineModel;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.po.PurchaseOrderService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.FileUploadService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 境外收票关联采购的相关业务
 *  File: InvoiceCollectOverseasPoService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月24日		Administrator
 *
 * </pre>
 */
@Service
public class InvoiceCollectOverseasPoService {

	@Autowired
	private InvoiceCollectOverseasPoDao collectOverseasPoDao;

	@Autowired
	CacheService cacheService;

	@Autowired
	private InvoiceCollectOverseasService collectOverseasService;

	@Autowired
	private PurchaseOrderLineDao purchaseOrderLineDao;

	@Autowired
	private PurchaseOrderTitleDao purchaseOrderTitleDao;

	@Autowired
	private PurchaseOrderService purchaseOrderService;// 订单信息
	@Autowired
	private FileUploadService fileUploadService;

	/**
	 * 查询当前Invoice收票关联采购单数据列表
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<InvoiceCollectOverseasPoResDto> queryInvoiceCollectPoResult(
			InvoiceCollectOverseasPoReqDto reqDto) {
		PageResult<InvoiceCollectOverseasPoResDto> result = new PageResult<InvoiceCollectOverseasPoResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<InvoiceCollectOverseasPoResDto> overseasResDtos = new ArrayList<InvoiceCollectOverseasPoResDto>();
		List<InvoiceCollectOverseasPo> collectOverseasPos = collectOverseasPoDao.queryInvoicePoByInvoiceIdResult(reqDto,
				rowBounds);
		overseasResDtos = convertToInvoiceCollectPoResDto(collectOverseasPos);
		result.setItems(overseasResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(reqDto.getPage());
		result.setPer_page(reqDto.getPer_page());
		return result;
	}

	/**
	 * 获取采购单明细数据
	 * 
	 * @param poTitleReqDto
	 * @return
	 */
	public PageResult<PoLineModel> queryPoLinesResultByCon(PoTitleReqDto poTitleReqDto) {
		PageResult<PoLineModel> result = new PageResult<PoLineModel>();
		InvoiceCollectOverseas overseas = collectOverseasService
				.queryInvoiceCollectOverEntityById(poTitleReqDto.getId());
		poTitleReqDto.setProjectId(overseas.getProjectId());
		poTitleReqDto.setSupplierId(overseas.getSupplierId());
		poTitleReqDto.setState(BaseConsts.FIVE);
		poTitleReqDto.setOrderType(BaseConsts.ZERO);
		int offSet = PageUtil.getOffSet(poTitleReqDto.getPage(), poTitleReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, poTitleReqDto.getPer_page());
		List<PoLineModel> poLineList = purchaseOrderLineDao.queryLineResultAndPay(poTitleReqDto, rowBounds);
		purchaseOrderService.processPoLineModels(poLineList);
		// 获取转换后的收票金额
		List<PoLineModel> poLineModels = new ArrayList<PoLineModel>();
		for (PoLineModel poLine : poLineList) {
			BigDecimal payRate = poLine.getPayRate() == null ? BigDecimal.ONE : poLine.getPayRate();
			poLine.setPayRate(payRate);
			poLine.setPriceCountAmout(
					DecimalUtil.subtract(
							DecimalUtil.formatScale2(DecimalUtil.multiply(
									DecimalUtil.multiply(null == poLine.getDiscountPrice() ? DecimalUtil.ZERO
											: poLine.getDiscountPrice(), payRate),
									null == poLine.getGoodsNum() ? DecimalUtil.ZERO : poLine.getGoodsNum())),
							poLine.getInvoiceTotalAmount()));
			Integer payCurrencyType = poLine.getPayRealCurrency();
			if (payCurrencyType != null) {
				poLine.setRealCurrencyType(payCurrencyType);// 实际支付币种收票币种
				poLine.setRealCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
						payCurrencyType.toString()));
			}
			poLineModels.add(poLine);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), poTitleReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(poTitleReqDto.getPage());
		result.setPer_page(poTitleReqDto.getPer_page());
		result.setItems(poLineModels);
		return result;
	}

	/**
	 * 删除Invoice收票采购明细数据
	 * 
	 * @param reqDto
	 */
	public void deleteInvoiceCollectPoByIds(InvoiceCollectOverseasPoReqDto reqDto) {
		List<Integer> ids = reqDto.getIds();
		if (!CollectionUtils.isEmpty(ids)) {
			for (Integer id : ids) {
				this.deleteInvoiceCollectPo(id, true);
			}
		}
	}

	/**
	 * 删除采购单和收票的关系
	 * 
	 * @param id
	 */
	public void deleteInvoiceCollectPo(Integer id, boolean type) {
		// 查询Invoices收票采购数据
		InvoiceCollectOverseasPo overseasPo = queryInvoiceOverseasEntityById(id);
		if (overseasPo == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "Invoice收票采购明细为空");
		}
		// 获取要修改的收票数量和金额
		BigDecimal invoicePonum = overseasPo.getInvoiceNum() == null ? BigDecimal.ZERO : overseasPo.getInvoiceNum();
		BigDecimal invoicePoAmount = overseasPo.getInvoiceAmoun() == null ? BigDecimal.ZERO
				: overseasPo.getInvoiceAmoun();
		BigDecimal realInvoicePoAmount = overseasPo.getRealInvoiceAmount() == null ? BigDecimal.ZERO
				: overseasPo.getRealInvoiceAmount();
		if (type == true) {
			// 修改Invoice收票头信息的收票金额
			InvoiceCollectOverseas collectOverseas = collectOverseasService
					.queryInvoiceCollectOverEntityById(overseasPo.getCollectOverseasId());
			InvoiceCollectOverseas overseas = new InvoiceCollectOverseas();
			overseas.setId(collectOverseas.getId());
			overseas.setInvoiceAmount(DecimalUtil.subtract(
					collectOverseas.getInvoiceAmount() == null ? BigDecimal.ZERO : collectOverseas.getInvoiceAmount(),
					realInvoicePoAmount));
			overseas.setInvoiceNum(DecimalUtil.subtract(
					collectOverseas.getInvoiceNum() == null ? BigDecimal.ZERO : collectOverseas.getInvoiceNum(),
					invoicePonum));
			// 更新Invoice收票的收票金额
			collectOverseasService.updateInvoiceCollectOverseasById(overseas);
		}
		// 修改当前采购单明细的数据
		PurchaseOrderLine orderLine = purchaseOrderLineDao.queryPurchaseOrderLineById(overseasPo.getPoLineId());
		PurchaseOrderLine upOrderLine = new PurchaseOrderLine();
		upOrderLine.setId(orderLine.getId());
		upOrderLine
				.setInvoiceNum(DecimalUtil.formatScale2(DecimalUtil.subtract(orderLine.getInvoiceNum(), invoicePonum)));
		upOrderLine.setInvoiceAmount(
				DecimalUtil.formatScale2(DecimalUtil.subtract(orderLine.getInvoiceAmount(), invoicePoAmount)));
		purchaseOrderLineDao.updatePurchaseOrderLineById(upOrderLine);

		// 修改采购单订单头信息的数据
		// 修改订单头
		PurchaseOrderTitle orderTitle = purchaseOrderTitleDao.queryEntityById(orderLine.getPoId());
		PurchaseOrderTitle upOrderTitle = new PurchaseOrderTitle();
		upOrderTitle.setId(orderTitle.getId());
		upOrderTitle.setInvoiceTotalNum(
				DecimalUtil.formatScale2(DecimalUtil.subtract(orderTitle.getInvoiceTotalNum(), invoicePonum)));
		upOrderTitle.setInvoiceTotalAmount(
				DecimalUtil.formatScale2(DecimalUtil.subtract(orderTitle.getInvoiceTotalAmount(), invoicePoAmount)));
		purchaseOrderTitleDao.updatePurchaseOrderTitleById(upOrderTitle);
		// 更新invoice收票采购信息
		overseasPo.setIsDelete(BaseConsts.ONE);
		this.updateInvoiceCollectOverseasPoById(overseasPo);
	}

	/**
	 * 添加采购单个Invoice收票的关联
	 * 
	 * @param reqDto
	 */
	public void createInvoiceCollectOverseasPo(InvoiceCollectOverseasPoReqDto reqDto) {
		this.updateInvoiceOverseasPoBycon(reqDto);
	}

	/**
	 * 修改和添加的业务处理操作
	 * 
	 * @param reqDto
	 */
	public void updateInvoiceOverseasPoBycon(InvoiceCollectOverseasPoReqDto reqDto) {
		// 获取invoice收票信息
		Integer collectOverseasId = reqDto.getCollectOverseasId();
		InvoiceCollectOverseas invoiceCollectOverseas = collectOverseasService
				.queryInvoiceCollectOverEntityById(collectOverseasId);
		BigDecimal countInvoiceAmount = BigDecimal.ZERO;
		BigDecimal countInvoiceNum = BigDecimal.ZERO;
		Date invoiceDate = null;
		Integer invoiceCurrnecyType = null;
		// 业务操作
		List<InvoiceCollectOverseasPo> posList = reqDto.getOverseasPos();
		if (!CollectionUtils.isEmpty(posList)) {
			for (InvoiceCollectOverseasPo invoiceCollectOverseasPo : posList) {
				Integer poLineId = invoiceCollectOverseasPo.getPoLineId();// 采购明细ID
				PurchaseOrderLine line = purchaseOrderLineDao.queryPurchaseOrderLineById(poLineId);// 查询明细数据
				if (line == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购单明细为空");
				}
				// 获取采购单的可收票金额
				BigDecimal lineAmount = DecimalUtil.subtract(
						DecimalUtil.multiply(line.getGoodsNum(), line.getDiscountPrice()), line.getInvoiceAmount());
				BigDecimal payRate = invoiceCollectOverseasPo.getPayRate();
				if (payRate == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单转换汇率为空");
				}
				// 获取转换后的可收票金额
				BigDecimal afterDiscountAmount = DecimalUtil.multiply(payRate, lineAmount);
				if (invoiceCurrnecyType == null) {
					invoiceCurrnecyType = invoiceCollectOverseasPo.getRealCurrnecyType();
				} else {
					if (!invoiceCurrnecyType.equals(invoiceCollectOverseasPo.getRealCurrnecyType())) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "币种不一致");
					}
				}
				// 获取采购单的收票金额
				BigDecimal lineInvoiceAmount = line.getInvoiceAmount() == null ? BigDecimal.ZERO
						: line.getInvoiceAmount();
				// 金额的校验
				if (DecimalUtil.gt(DecimalUtil.add(lineInvoiceAmount, lineAmount), line.getAmount())) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "金额不足");
				}
				// 修改订单明细
				PurchaseOrderLine upOrderLine = new PurchaseOrderLine();
				upOrderLine.setId(line.getId());
				upOrderLine.setInvoiceNum(line.getGoodsNum());// 收票数量全部回写
				upOrderLine.setInvoiceAmount(
						DecimalUtil.formatScale2(DecimalUtil.add(line.getInvoiceAmount(), lineAmount)));
				purchaseOrderLineDao.updatePurchaseOrderLineById(upOrderLine);

				// 修改订单头
				PurchaseOrderTitle orderTitle = purchaseOrderTitleDao.queryEntityById(line.getPoId());
				if (orderTitle == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购单头信息为空");
				}
				PurchaseOrderTitle upOrderTitle = new PurchaseOrderTitle();
				upOrderTitle.setId(orderTitle.getId());
				upOrderTitle.setInvoiceTotalNum(DecimalUtil.add(orderTitle.getInvoiceTotalNum(),
						DecimalUtil.subtract(line.getGoodsNum(), line.getInvoiceNum())));
				upOrderTitle.setInvoiceTotalAmount(
						DecimalUtil.formatScale2(DecimalUtil.add(orderTitle.getInvoiceTotalAmount(), lineAmount)));
				purchaseOrderTitleDao.updatePurchaseOrderTitleById(upOrderTitle);

				// 新增invoice收票关联采购单的数据
				// 保存实际的收票数量和金额
				invoiceCollectOverseasPo.setCurrnecyType(orderTitle.getCurrencyId());
				invoiceCollectOverseasPo.setRealCurrnecyType(invoiceCurrnecyType);
				invoiceCollectOverseasPo.setRate(payRate);
				invoiceCollectOverseasPo.setRealInvoiceAmount(afterDiscountAmount);
				invoiceCollectOverseasPo.setInvoiceNum(DecimalUtil.subtract(line.getGoodsNum(), line.getInvoiceNum()));
				invoiceCollectOverseasPo.setInvoiceAmoun(DecimalUtil.formatScale2(lineAmount));
				this.createInvoiceOverseasPo(invoiceCollectOverseasPo, collectOverseasId);
				// 金额和数量的总计
				countInvoiceAmount = DecimalUtil.add(countInvoiceAmount, afterDiscountAmount);
				countInvoiceNum = DecimalUtil.add(countInvoiceNum,
						DecimalUtil.subtract(line.getGoodsNum(), line.getInvoiceNum()));
				invoiceDate = orderTitle.getOrderTime();
			}
			// 更新invoice收票数据
			invoiceCollectOverseas.setInvoiceAmount(
					DecimalUtil.formatScale2(DecimalUtil.add(invoiceCollectOverseas.getInvoiceAmount() == null
							? BigDecimal.ZERO : invoiceCollectOverseas.getInvoiceAmount(), countInvoiceAmount)));
			invoiceCollectOverseas.setInvoiceNum(DecimalUtil.add(invoiceCollectOverseas.getInvoiceNum() == null
					? BigDecimal.ZERO : invoiceCollectOverseas.getInvoiceNum(), countInvoiceNum));
			invoiceCollectOverseas.setInvoiceDate(invoiceDate);
			invoiceCollectOverseas.setCurrnecyType(invoiceCurrnecyType);
			collectOverseasService.updateInvoiceCollectOverseasById(invoiceCollectOverseas);
		}
	}

	/**
	 * 新增invoice收票关联采购单的数据
	 * 
	 * @param invoiceCollectOverseasPo
	 * @param collectOverseasId
	 */
	public void createInvoiceOverseasPo(InvoiceCollectOverseasPo invoiceCollectOverseasPo, Integer collectOverseasId) {
		// 新增invoice收票和采购的关系数据
		invoiceCollectOverseasPo.setCollectOverseasId(collectOverseasId);
		invoiceCollectOverseasPo.setCreateAt(new Date());
		invoiceCollectOverseasPo.setCreator(ServiceSupport.getUser().getChineseName());
		invoiceCollectOverseasPo.setCreatorId(ServiceSupport.getUser().getId());
		invoiceCollectOverseasPo.setIsDelete(BaseConsts.ZERO);
		this.updateInvoiceCollectOverseasPoById(invoiceCollectOverseasPo);
	}

	/**
	 * 编辑收票采购的信息
	 * 
	 * @param reqDto
	 * @return
	 */
	public Result<InvoiceCollectOverseasPoResDto> editInvoiceCollectOverseasPoById(
			InvoiceCollectOverseasPoReqDto reqDto) {
		Result<InvoiceCollectOverseasPoResDto> result = new Result<InvoiceCollectOverseasPoResDto>();
		InvoiceCollectOverseasPoResDto overseasPoResDto = invoiceCollectOverseasPoToRes(
				this.queryInvoiceOverseasEntityById(reqDto.getId()));
		result.setItems(overseasPoResDto);
		return result;
	}

	/**
	 * 修改当前 作废
	 * 
	 * @param poReqDto
	 */
	public void updateInvoiceOverseasPoById(InvoiceCollectOverseasPoReqDto poReqDto) {
		this.updateInvoiceOverseasPoBycon(poReqDto);
	}

	/**
	 * 根据收票采购ID查询信息
	 * 
	 * @param Id
	 * @return
	 */
	public InvoiceCollectOverseasPo queryInvoiceOverseasEntityById(Integer id) {
		InvoiceCollectOverseasPo overseasPo = collectOverseasPoDao.queryEntityById(id);
		return overseasPo;
	}

	/**
	 * 更新Invoice收票采购信息
	 * 
	 * @param invoiceCollect
	 * @return
	 */
	public void updateInvoiceCollectOverseasPoById(InvoiceCollectOverseasPo overseasPo) {
		int result = BaseConsts.ONE;
		if (overseasPo.getId() != null) {
			result = collectOverseasPoDao.updateById(overseasPo);
		} else {
			result = collectOverseasPoDao.insert(overseasPo);
		}
		if (result == BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "更新失败:" + JSONObject.toJSON(overseasPo));
		}
	}

	/**
	 * 根据收票id查询采购收票的数据
	 * 
	 * @param overId
	 * @return
	 */
	public List<InvoiceCollectOverseasPoResDto> queryInvoiceCollectByOverId(Integer overId) {
		InvoiceCollectOverseasPoReqDto collectOverseasPoReqDto = new InvoiceCollectOverseasPoReqDto();
		collectOverseasPoReqDto.setCollectOverseasId(overId);
		return convertToInvoiceCollectPoResDto(
				collectOverseasPoDao.queryInvoicePoByInvoiceIdResult(collectOverseasPoReqDto));
	}

	/**
	 * 封装当前Invoice收票关联采购单的数据
	 * 
	 * @param collectOverseasPos
	 * @return
	 */
	public List<InvoiceCollectOverseasPoResDto> convertToInvoiceCollectPoResDto(
			List<InvoiceCollectOverseasPo> collectOverseasPos) {
		List<InvoiceCollectOverseasPoResDto> poResDtos = new ArrayList<InvoiceCollectOverseasPoResDto>();
		if (CollectionUtils.isEmpty(collectOverseasPos)) {
			return poResDtos;
		} else {
			for (InvoiceCollectOverseasPo collectOverseasPo : collectOverseasPos) {
				InvoiceCollectOverseasPoResDto poResDtot = invoiceCollectOverseasPoToRes(collectOverseasPo);
				poResDtos.add(poResDtot);
			}
		}
		return poResDtos;
	}

	/**
	 * 封装当前Invoice收票关联采购单的明细数据
	 * 
	 * @param collectOverseasPo
	 * @return
	 */
	public InvoiceCollectOverseasPoResDto invoiceCollectOverseasPoToRes(InvoiceCollectOverseasPo collectOverseasPo) {
		InvoiceCollectOverseasPoResDto result = new InvoiceCollectOverseasPoResDto();
		result.setId(collectOverseasPo.getId());// 主键ID
		result.setCollectOverseasId(collectOverseasPo.getCollectOverseasId());// Invoice收票主ID
		result.setOrderNo(collectOverseasPo.getOrderNo());// 采购编号
		result.setOrderTime(collectOverseasPo.getOrderTime());// 订单时间
		if (collectOverseasPo.getGoodsId() != null) {
			BaseGoods baseGoods = cacheService.getGoodsById(collectOverseasPo.getGoodsId());
			if (baseGoods != null) {
				result.setGoodsNo(baseGoods.getNumber());
				result.setGoodsId(baseGoods.getId());
				result.setGoodsDescribe(baseGoods.getName());
			}
		}
		result.setPoLineId(collectOverseasPo.getPoLineId());// 采购明细ID
		result.setInvoiceAmoun(collectOverseasPo.getInvoiceAmoun());// 收票金额
		result.setInvoiceNum(collectOverseasPo.getInvoiceNum());// 收票数量
		result.setRate(collectOverseasPo.getRate());// 汇率
		result.setRealInvoiceAmount(collectOverseasPo.getRealInvoiceAmount());// 实际收票金额
		result.setCurrnecyType(collectOverseasPo.getCurrnecyType());// 收票币种
		result.setCurrnecyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				collectOverseasPo.getCurrnecyType() + ""));
		result.setRealCurrnecyType(collectOverseasPo.getRealCurrnecyType());// 实际收票币种
		result.setRealCurrnecyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				collectOverseasPo.getRealCurrnecyType() + ""));
		result.setCreator(collectOverseasPo.getCreator());// 创建人
		result.setCreatorId(collectOverseasPo.getCreatorId());
		result.setCreateAt(collectOverseasPo.getCreateAt());
		return result;
	}

	/**
	 * 获取文件操作列表
	 * 
	 * @param state
	 * @return
	 */
	public PageResult<InvoiceCollectOverseasPoFileResDto> queryFileList(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<InvoiceCollectOverseasPoFileResDto> pageResult = new PageResult<InvoiceCollectOverseasPoFileResDto>();
		fileAttReqDto.setBusType(BaseConsts.INT_38);// 附件类型
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<InvoiceCollectOverseasPoFileResDto> list = convertToFileResDto(fielAttach);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), fileAttReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(fileAttReqDto.getPage());
		pageResult.setPer_page(fileAttReqDto.getPer_page());
		pageResult.setItems(list);
		return pageResult;
	}

	public List<InvoiceCollectOverseasPoFileResDto> queryFileList(Integer overId) {
		FileAttachSearchReqDto fileAttReqDto = new FileAttachSearchReqDto();
		fileAttReqDto.setBusId(overId);
		fileAttReqDto.setBusType(BaseConsts.INT_38);
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<InvoiceCollectOverseasPoFileResDto> list = convertToFileResDto(fielAttach);
		return list;
	}

	private List<InvoiceCollectOverseasPoFileResDto> convertToFileResDto(List<FileAttach> fileAttach) {
		List<InvoiceCollectOverseasPoFileResDto> list = new LinkedList<InvoiceCollectOverseasPoFileResDto>();
		if (ListUtil.isEmpty(fileAttach)) {
			return list;
		}
		for (FileAttach model : fileAttach) {
			InvoiceCollectOverseasPoFileResDto result = new InvoiceCollectOverseasPoFileResDto();
			result.setId(model.getId());
			result.setBusId(model.getBusId());
			result.setBusTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.FILE_BUS_TYPE, model.getBusType() + ""));
			result.setName(model.getName());
			result.setType(model.getType());
			result.setCreateAt(model.getCreateAt());
			result.setCreator(model.getCreator());
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
				InvoiceCollectOverseasPoFileResDto.Operate.operMap);
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
