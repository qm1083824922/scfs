package com.scfs.service.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.invoice.InvoiceOverseasDao;
import com.scfs.dao.invoice.InvoiceOverseasPoDao;
import com.scfs.dao.sale.BillDeliveryDtlDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.invoice.dto.req.InvoiceOverseasPoReqDto;
import com.scfs.domain.invoice.dto.req.InvoiceSaleManagerReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceOverseasPoResDto;
import com.scfs.domain.invoice.entity.InvoiceOverseas;
import com.scfs.domain.invoice.entity.InvoiceOverseasPo;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.domain.sale.entity.BillDeliveryDtl;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 	境外发票销售单业务
 *  File: InvoiceOverseasPoService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年03月28日				Administrator
 *
 * </pre>
 */
@Service
public class InvoiceOverseasPoService {
	@Autowired
	private InvoiceOverseasPoDao invoiceOverseasPoDao;
	@Autowired
	private InvoiceOverseasDao invoiceOverseasDao;
	@Autowired
	private BillDeliveryDtlDao billDeliveryDtlDao;
	@Autowired
	private CacheService cacheService;

	/**
	 * 获取销售单数据
	 * 
	 * @param poReqDto
	 * @return
	 */
	public PageResult<InvoiceOverseasPoResDto> querySaleNotSelectByCon(InvoiceOverseasPoReqDto poReqDto) {
		PageResult<InvoiceOverseasPoResDto> pageResult = new PageResult<InvoiceOverseasPoResDto>();
		InvoiceOverseas invoiceOverseas = invoiceOverseasDao.queryEntityById(poReqDto.getId());
		int offSet = PageUtil.getOffSet(poReqDto.getPage(), poReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, poReqDto.getPer_page());
		InvoiceSaleManagerReqDto invoiceSaleReqDto = new InvoiceSaleManagerReqDto();
		invoiceSaleReqDto.setProjectId(invoiceSaleReqDto.getProjectId());
		invoiceSaleReqDto.setCustomerId(invoiceOverseas.getCustomerId());
		invoiceSaleReqDto.setCurrencyType(invoiceOverseas.getCurrnecyType());
		invoiceSaleReqDto.setBillNo(poReqDto.getOrderNo());
		invoiceSaleReqDto.setGoodNum(poReqDto.getGoodsNo());
		invoiceSaleReqDto.setGoodName(poReqDto.getGoodsName());
		invoiceSaleReqDto.setBillType(BaseConsts.ONE);
		List<BillDeliveryDtl> result = billDeliveryDtlDao.queryNotSelectByCon(invoiceSaleReqDto, rowBounds);
		List<InvoiceOverseasPoResDto> resultList = new ArrayList<InvoiceOverseasPoResDto>();
		if (result != null && result.size() > BaseConsts.ONE) {
			for (BillDeliveryDtl deliveryDtl : result) {
				resultList.add(conver(deliveryDtl));
			}
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), invoiceSaleReqDto.getPer_page());
		pageResult.setItems(resultList);
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(invoiceSaleReqDto.getPage());
		pageResult.setPer_page(invoiceSaleReqDto.getPer_page());
		return pageResult;
	}

	public InvoiceOverseasPoResDto conver(BillDeliveryDtl model) {
		InvoiceOverseasPoResDto result = new InvoiceOverseasPoResDto();
		result.setId(model.getId());
		result.setBillNo(model.getBillNo());
		result.setRequiredSendDate(model.getRequiredSendDate());
		result.setGoodsNumber(cacheService.getGoodsById(model.getGoodsId()).getNumber());
		result.setGoodsName(cacheService.getGoodsById(model.getGoodsId()).getName());
		if (model.getProvideInvoiceNum() != null) {
			BigDecimal sumNum = model.getRequiredSendNum().subtract(model.getProvideInvoiceNum());
			result.setProvideMaxNum(DecimalUtil.formatScale2(sumNum));
		} else {
			result.setProvideMaxNum(DecimalUtil.formatScale2(model.getRequiredSendNum()));
		}
		if (model.getProvideInvoiceAmount() != null) {
			BigDecimal sumAmount = model.getRequiredSendNum().multiply(model.getRequiredSendPrice());
			result.setProvideMaxAmount(DecimalUtil.formatScale2(sumAmount.subtract(model.getProvideInvoiceAmount())));
		} else {
			result.setProvideMaxAmount(
					DecimalUtil.formatScale2(model.getRequiredSendNum().multiply(model.getRequiredSendPrice())));
		}
		result.setCostPrice(model.getCostPrice());
		return result;
	}

	/**
	 * 添加数据
	 * 
	 * @param poReqDto
	 * @return
	 */
	public BaseResult createInvoiceOverseasPo(InvoiceOverseasPoReqDto poReqDto) {
		BaseResult baseResult = new BaseResult();

		Integer overseasId = poReqDto.getOverseasId();
		InvoiceOverseas invoiceOverseas = invoiceOverseasDao.queryEntityById(overseasId);
		BigDecimal sum = invoiceOverseas.getInvoiceAmount();

		List<InvoiceOverseasPo> poList = poReqDto.getPoList();
		for (InvoiceOverseasPo overseasPo : poList) {
			Integer deliveryId = overseasPo.getBillDeliveryId();
			InvoiceOverseasPoResDto billDeliveryDtl = conver(billDeliveryDtlDao.queryEntityById(deliveryId));
			BigDecimal invoiceNum = overseasPo.getInvoiceNum();
			if (DecimalUtil.gt(invoiceNum, billDeliveryDtl.getProvideMaxNum())) {
				baseResult.setMsg("数量不足！");
				return baseResult;
			}
			BigDecimal invoiceAmount = overseasPo.getInvoiceAmount();
			if (DecimalUtil.gt(invoiceAmount, billDeliveryDtl.getProvideMaxAmount())) {
				baseResult.setMsg("余额不足！");
				return baseResult;
			}
		}
		Date date = new Date();
		for (InvoiceOverseasPo overseasPo : poList) {
			BigDecimal invoiceNum = overseasPo.getInvoiceNum();
			BigDecimal invoiceAmount = overseasPo.getInvoiceAmount();
			Integer deliveryId = overseasPo.getBillDeliveryId();

			BillDeliveryDtl billDeliveryDtl = billDeliveryDtlDao.queryEntityById(deliveryId);

			BillDeliveryDtl upDeliveryDtl = new BillDeliveryDtl();// 修改销售单明细数据
			upDeliveryDtl.setId(deliveryId);
			upDeliveryDtl.setProvideInvoiceNum(DecimalUtil.add(billDeliveryDtl.getProvideInvoiceNum(), invoiceNum));
			upDeliveryDtl
					.setProvideInvoiceAmount(DecimalUtil.add(billDeliveryDtl.getProvideInvoiceAmount(), invoiceAmount));
			billDeliveryDtlDao.updateById(upDeliveryDtl);

			overseasPo.setOverseasId(overseasId); // 添加数据
			overseasPo.setCreateAt(date);
			overseasPo.setCreator(ServiceSupport.getUser().getChineseName());
			overseasPo.setCreatorId(ServiceSupport.getUser().getId());
			overseasPo.setIsDelete(BaseConsts.ZERO);
			invoiceOverseasPoDao.insert(overseasPo);
			sum = DecimalUtil.add(sum, invoiceAmount);
		}
		InvoiceOverseas upOverseas = new InvoiceOverseas();
		upOverseas.setId(overseasId);
		upOverseas.setInvoiceAmount(sum);
		invoiceOverseasDao.updateById(upOverseas);
		return baseResult;
	}

	/**
	 * 修改数据
	 * 
	 * @param poReqDto
	 * @return
	 */
	public BaseResult updateInvoiceOverseasPo(InvoiceOverseasPoReqDto poReqDto) {
		BaseResult baseResult = new BaseResult();
		List<InvoiceOverseasPo> poList = poReqDto.getPoList();
		for (InvoiceOverseasPo overseasPo : poList) {
			BigDecimal invoiceNum = overseasPo.getInvoiceNum();
			BigDecimal invoiceAmount = overseasPo.getInvoiceAmount();
			InvoiceOverseasPo entity = invoiceOverseasPoDao.queryEntityById(overseasPo.getId());

			InvoiceOverseasPoResDto billDeliveryDtl = conver(
					billDeliveryDtlDao.queryEntityById(entity.getBillDeliveryId()));

			BigDecimal diffNum = DecimalUtil.subtract(invoiceNum, entity.getInvoiceNum());
			if (DecimalUtil.gt(diffNum, billDeliveryDtl.getProvideMaxNum())) {
				baseResult.setMsg("开票数量不足！");
				return baseResult;
			}
			BigDecimal diffAmount = DecimalUtil.subtract(invoiceAmount, entity.getInvoiceAmount());
			if (DecimalUtil.gt(diffAmount, billDeliveryDtl.getProvideMaxAmount())) {
				baseResult.setMsg("开票金额不足！");
				return baseResult;
			}
		}
		for (InvoiceOverseasPo overseasPo : poList) {
			InvoiceOverseasPo entity = invoiceOverseasPoDao.queryEntityById(overseasPo.getId());
			BigDecimal invoiceNum = overseasPo.getInvoiceNum();
			BigDecimal invoiceAmount = overseasPo.getInvoiceAmount();
			BigDecimal diffNum = DecimalUtil.subtract(invoiceNum, entity.getInvoiceNum());
			BigDecimal diffAmount = DecimalUtil.subtract(invoiceAmount, entity.getInvoiceAmount());

			BillDeliveryDtl billDeliveryDtl = billDeliveryDtlDao.queryEntityById(entity.getBillDeliveryId());// 修改销售单详情
			BillDeliveryDtl upDeliveryDtl = new BillDeliveryDtl();
			upDeliveryDtl.setId(billDeliveryDtl.getId());
			upDeliveryDtl.setProvideInvoiceNum(DecimalUtil.add(billDeliveryDtl.getProvideInvoiceNum(), diffNum));
			upDeliveryDtl
					.setProvideInvoiceAmount(DecimalUtil.add(billDeliveryDtl.getProvideInvoiceAmount(), diffAmount));
			billDeliveryDtlDao.updateById(upDeliveryDtl);

			InvoiceOverseas overseas = invoiceOverseasDao.queryEntityById(entity.getOverseasId());// 修改境外收票金额
			InvoiceOverseas upOverseas = new InvoiceOverseas();
			upOverseas.setId(overseas.getId());
			upOverseas.setInvoiceAmount(DecimalUtil.add(overseas.getInvoiceAmount(), diffAmount)
					.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			invoiceOverseasDao.updateById(upOverseas);

			invoiceOverseasPoDao.updateById(overseasPo);
		}
		return baseResult;
	}

	/***
	 * 删除数据
	 * 
	 * @param poReqDto
	 * @return
	 */
	public BaseResult deleteInvoiceOverseasPo(InvoiceOverseasPoReqDto poReqDto) {
		BaseResult baseResult = new BaseResult();
		for (Integer id : poReqDto.getIds()) {
			InvoiceOverseasPo entity = invoiceOverseasPoDao.queryEntityById(id);
			BigDecimal invoiceNum = entity.getInvoiceNum();
			BigDecimal invoiceAmount = entity.getInvoiceAmount();

			BillDeliveryDtl billDeliveryDtl = billDeliveryDtlDao.queryEntityById(entity.getBillDeliveryId());// 修改销售单详情
			BillDeliveryDtl upDeliveryDtl = new BillDeliveryDtl();
			upDeliveryDtl.setId(billDeliveryDtl.getId());
			upDeliveryDtl
					.setProvideInvoiceNum(DecimalUtil.subtract(billDeliveryDtl.getProvideInvoiceNum(), invoiceNum));
			upDeliveryDtl.setProvideInvoiceAmount(
					DecimalUtil.subtract(billDeliveryDtl.getProvideInvoiceAmount(), invoiceAmount));
			billDeliveryDtlDao.updateById(upDeliveryDtl);

			InvoiceOverseas overseas = invoiceOverseasDao.queryEntityById(entity.getOverseasId());// 修改境外收票金额
			InvoiceOverseas upOverseas = new InvoiceOverseas();
			upOverseas.setId(overseas.getId());
			upOverseas.setInvoiceAmount(DecimalUtil.subtract(overseas.getInvoiceAmount(), invoiceAmount)
					.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			invoiceOverseasDao.updateById(upOverseas);

			InvoiceOverseasPo upOverseasPo = new InvoiceOverseasPo();
			upOverseasPo.setId(id);
			upOverseasPo.setIsDelete(BaseConsts.ONE);
			invoiceOverseasPoDao.updateById(upOverseasPo);
		}
		return baseResult;
	}

	/**
	 * 编辑
	 * 
	 * @param poReqDto
	 * @return
	 */
	public Result<InvoiceOverseasPoResDto> editInvoiceCollectPoById(InvoiceOverseasPoReqDto poReqDto) {
		Result<InvoiceOverseasPoResDto> result = new Result<InvoiceOverseasPoResDto>();
		result.setItems(convertInvoiceOverseasPoResDto(invoiceOverseasPoDao.queryEntityById(poReqDto.getId())));
		return result;
	}

	/**
	 * 获取所有数据
	 * 
	 * @param poReqDto
	 * @return
	 */
	public List<InvoiceOverseasPoResDto> queryInvoiceCollectPoResults(InvoiceOverseasPoReqDto poReqDto) {
		List<InvoiceOverseasPoResDto> poList = convertToInvoiceOverseasPoResDtos(
				invoiceOverseasPoDao.queryResultsByCon(poReqDto));
		return poList;
	}

	public List<InvoiceOverseasPoResDto> queryInvoiceCollectPoResultsByBill(InvoiceOverseasPoReqDto poReqDto) {
		List<InvoiceOverseasPoResDto> poList = convertToInvoiceOverseasPoResDtos(
				invoiceOverseasPoDao.queryAllResultsGroupByBillNo(poReqDto));
		return poList;
	}

	public PageResult<InvoiceOverseasPoResDto> queryAllInvoiceCollectPoResults(InvoiceOverseasPoReqDto poReqDto) {
		PageResult<InvoiceOverseasPoResDto> result = new PageResult<InvoiceOverseasPoResDto>();
		InvoiceOverseas overseas = invoiceOverseasDao.queryEntityById(poReqDto.getOverseasId());
		List<InvoiceOverseasPoResDto> poList = new ArrayList<InvoiceOverseasPoResDto>();
		if (overseas.getIsMerge() == BaseConsts.ONE) {
			poList = convertToInvoiceOverseasPoResDtos(invoiceOverseasPoDao.queryAllResultsByCon(poReqDto));

		} else {
			poList = queryInvoiceCollectPoResults(poReqDto);
		}
		result.setItems(poList);
		return result;
	}

	/**
	 * 列表分页
	 * 
	 * @param poReqDto
	 * @return
	 */
	public PageResult<InvoiceOverseasPoResDto> queryInvoiceCollectPoResultsByCon(InvoiceOverseasPoReqDto poReqDto) {
		PageResult<InvoiceOverseasPoResDto> pageResult = new PageResult<InvoiceOverseasPoResDto>();
		int offSet = PageUtil.getOffSet(poReqDto.getPage(), poReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, poReqDto.getPer_page());
		List<InvoiceOverseasPoResDto> overseasPoResDto = convertToInvoiceOverseasPoResDtos(
				invoiceOverseasPoDao.queryResultsByCon(poReqDto, rowBounds));
		pageResult.setItems(overseasPoResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), poReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(poReqDto.getPage());
		pageResult.setPer_page(poReqDto.getPer_page());

		List<InvoiceOverseasPoResDto> sumPo = convertToInvoiceOverseasPoResDtos(
				invoiceOverseasPoDao.queryResultsByCon(poReqDto, rowBounds));
		BigDecimal invoiceAmountSum = BigDecimal.ZERO;
		BigDecimal numSum = BigDecimal.ZERO;
		if (CollectionUtils.isNotEmpty(sumPo)) {
			for (InvoiceOverseasPoResDto po : sumPo) {
				invoiceAmountSum = DecimalUtil.add(invoiceAmountSum, po.getInvoiceAmount());
				numSum = DecimalUtil.add(numSum, po.getInvoiceNum());
			}
		}
		String totalStr = "数量  : " + DecimalUtil.formatScale2(numSum) + "  &nbsp;&nbsp;  开票金额: "
				+ DecimalUtil.formatScale2(invoiceAmountSum);
		pageResult.setTotalStr(totalStr);
		return pageResult;
	}

	public List<InvoiceOverseasPoResDto> convertToInvoiceOverseasPoResDtos(List<InvoiceOverseasPo> result) {
		List<InvoiceOverseasPoResDto> invoiceOverseasPoResDto = new ArrayList<InvoiceOverseasPoResDto>();
		if (ListUtil.isEmpty(result)) {
			return invoiceOverseasPoResDto;
		}
		for (InvoiceOverseasPo model : result) {
			InvoiceOverseasPoResDto overseasFeeResDto = convertInvoiceOverseasPoResDto(model);
			invoiceOverseasPoResDto.add(overseasFeeResDto);
		}
		return invoiceOverseasPoResDto;
	}

	public InvoiceOverseasPoResDto convertInvoiceOverseasPoResDto(InvoiceOverseasPo model) {
		InvoiceOverseasPoResDto result = new InvoiceOverseasPoResDto();
		result.setId(model.getId());
		result.setOverseasId(model.getOverseasId());
		result.setInvoiceNum(model.getInvoiceNum());
		result.setInvoiceAmount(model.getInvoiceAmount());
		result.setBillNo(model.getBillNo());
		result.setRequiredSendDate(model.getRequiredSendDate());
		result.setGoodsName(model.getGoodsName());
		result.setGoodsNumber(model.getGoodsNumber());
		result.setUnit(model.getUnit());
		result.setCostPrice(model.getRequiredSendPrice());
		result.setRegPlace(model.getRegPlace());
		if (model.getProvideInvoiceNum() != null) {
			BigDecimal sumNum = model.getRequiredSendNum().subtract(model.getProvideInvoiceNum());
			result.setProvideMaxNum(sumNum);
		} else {
			result.setProvideMaxNum(model.getRequiredSendNum());
		}
		if (model.getProvideInvoiceAmount() != null) {
			BigDecimal sumAmount = model.getRequiredSendNum().multiply(model.getRequiredSendPrice());
			result.setProvideMaxAmount(sumAmount.subtract(model.getProvideInvoiceAmount()));
		} else {
			result.setProvideMaxAmount(model.getRequiredSendNum().multiply(model.getRequiredSendPrice()));
		}
		return result;
	}
}
