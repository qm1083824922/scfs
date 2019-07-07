package com.scfs.service.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fee.FeeDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.invoice.InvoiceCollectOverseasFeeDao;
import com.scfs.domain.fee.dto.req.QueryFeeReqDto;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.invoice.dto.req.InvoiceCollectOverseasFeeReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectOverseasFeeResDto;
import com.scfs.domain.invoice.entity.InvoiceCollectOverseas;
import com.scfs.domain.invoice.entity.InvoiceCollectOverseasFee;
import com.scfs.domain.result.PageResult;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 境外收票关联采购的相关业务
 *  File: InvoiceCollectOverseasFeeService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月24日		Administrator
 *
 * </pre>
 */
@Service
public class InvoiceCollectOverseasFeeService {

	@Autowired
	private InvoiceCollectOverseasFeeDao invoiceCollectOverseasFeeDao;
	@Autowired
	private FeeDao feeDao;
	@Autowired
	private InvoiceCollectOverseasService collectOverseasService;

	/**
	 * 查询当前Invoice收票关联费用单数据列表
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<InvoiceCollectOverseasFeeResDto> queryInvoiceCollectFeeResult(
			InvoiceCollectOverseasFeeReqDto reqDto) {
		PageResult<InvoiceCollectOverseasFeeResDto> result = new PageResult<InvoiceCollectOverseasFeeResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<InvoiceCollectOverseasFeeResDto> overseasResDtos = new ArrayList<InvoiceCollectOverseasFeeResDto>();
		List<InvoiceCollectOverseasFee> collectOverseasPos = invoiceCollectOverseasFeeDao.queryInvoiceFeeResult(reqDto,
				rowBounds);
		overseasResDtos = convertToInvoiceCollectFeeResDto(collectOverseasPos);
		result.setItems(overseasResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(reqDto.getPage());
		result.setPer_page(reqDto.getPer_page());
		return result;
	}

	/**
	 * 查询费用列表数据
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<InvoiceCollectOverseasFeeResDto> queryFeeResultByCon(QueryFeeReqDto reqDto) {
		PageResult<InvoiceCollectOverseasFeeResDto> pageResult = new PageResult<InvoiceCollectOverseasFeeResDto>();
		InvoiceCollectOverseas overseas = collectOverseasService.queryInvoiceCollectOverEntityById(reqDto.getId());
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		reqDto.setProjectId(overseas.getProjectId());
		reqDto.setCustPayer(overseas.getSupplierId());
		reqDto.setState(BaseConsts.THREE);
		reqDto.setIsPayAll(BaseConsts.TWO);
		List<Fee> fees = feeDao.queryFeeByCond(reqDto, rowBounds);
		List<InvoiceCollectOverseasFeeResDto> feeResDtos = new ArrayList<InvoiceCollectOverseasFeeResDto>();
		if (!CollectionUtils.isEmpty(fees)) {
			feeResDtos = convertFeeToByInvoiceFeeResDto(fees);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(reqDto.getPage());
		pageResult.setPer_page(reqDto.getPer_page());
		pageResult.setItems(feeResDtos);
		return pageResult;
	}

	/**
	 * 删除费用单
	 * 
	 * @param reqDto
	 */
	public void deleteInvoiceCollectFeeByIds(InvoiceCollectOverseasFeeReqDto reqDto) {
		List<Integer> ids = reqDto.getIds();
		if (!CollectionUtils.isEmpty(ids)) {
			for (Integer id : ids) {
				this.deleteInvoiceFee(id, true);
			}
		}
	}

	/**
	 * 业务删除费用和收票的关系
	 * 
	 * @param id
	 * @param type
	 */
	public void deleteInvoiceFee(Integer id, boolean type) {
		InvoiceCollectOverseasFee collectOverseasFee = this.queryInvoiceFeeEntityById(id);
		if (collectOverseasFee == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "Invoice收票费用明细为空");
		}
		// 根据当前删除的数据进行会写收票金额
		BigDecimal invoiceAmount = collectOverseasFee.getInvoiceAmount() == null ? BigDecimal.ZERO
				: collectOverseasFee.getInvoiceAmount();
		// 查询费用单数据
		Fee fee = feeDao.queryEntityById(collectOverseasFee.getFeeId());
		if (fee == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "费用明细为空");
		}
		// 修改费用单
		Fee fee2 = new Fee();
		fee2.setId(fee.getId());
		fee2.setAcceptInvoiceAmount(DecimalUtil.subtract(fee.getAcceptInvoiceAmount(), invoiceAmount));
		feeDao.updateById(fee2);
		if (type == true) {
			// 修改Invoice收票头信息的收票金额
			InvoiceCollectOverseas collectOverseas = collectOverseasService
					.queryInvoiceCollectOverEntityById(collectOverseasFee.getCollectOverseasId());
			collectOverseas.setInvoiceAmount(DecimalUtil.subtract(
					collectOverseas.getInvoiceAmount() == null ? BigDecimal.ZERO : collectOverseas.getInvoiceAmount(),
					invoiceAmount));
			// 更新Invoice收票的收票金额
			collectOverseasService.updateInvoiceCollectOverseasById(collectOverseas);
		}
		// 修改当前费用收票的状态
		collectOverseasFee.setIsDelete(BaseConsts.ONE);
		this.updateInvoiceCollectOverseasFeeById(collectOverseasFee);
	}

	/**
	 * 新增费用收票的业务
	 * 
	 * @param reqDto
	 */
	public void createInvoiceFeeByCon(InvoiceCollectOverseasFeeReqDto reqDto) {
		// 获取invoice收票信息
		Integer collectOverseasId = reqDto.getCollectOverseasId();
		InvoiceCollectOverseas invoiceCollectOverseas = collectOverseasService
				.queryInvoiceCollectOverEntityById(collectOverseasId);
		// 费用收票的总金额
		BigDecimal countInvoiceFeeAmount = BigDecimal.ZERO;
		Integer invoiceCurrnecyType = null;
		// 业务操作
		List<InvoiceCollectOverseasFee> overseasFees = reqDto.getOverseasFees();
		if (!CollectionUtils.isEmpty(overseasFees)) {
			for (InvoiceCollectOverseasFee overseasFee : overseasFees) {
				Fee fee = feeDao.queryEntityById(overseasFee.getFeeId());
				if (fee == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "费用明细为空");
				}
				// 获取费用单的应收金额和收票金额
				BigDecimal payAmount = fee.getPayAmount() == null ? BigDecimal.ZERO : fee.getPayAmount();// 应付金额
				BigDecimal acceptInvoiceAmount = fee.getAcceptInvoiceAmount() == null ? BigDecimal.ZERO
						: fee.getAcceptInvoiceAmount();
				BigDecimal overInvoiceAmount = DecimalUtil.subtract(payAmount, acceptInvoiceAmount);
				if (DecimalUtil.lt(overInvoiceAmount, BigDecimal.ZERO)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "可收票金额有误");
				}
				// 币种的校验
				if (invoiceCurrnecyType == null) {
					invoiceCurrnecyType = fee.getCurrencyType();
				} else {
					if (!invoiceCurrnecyType.equals(fee.getCurrencyType())) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "币种不一致");
					}
				}
				// 新增费用收票的数据
				InvoiceCollectOverseasFee collectOverseasFee = new InvoiceCollectOverseasFee();
				collectOverseasFee.setInvoiceAmount(overInvoiceAmount);
				collectOverseasFee.setFeeId(fee.getId());
				collectOverseasFee.setCurrencyType(fee.getCurrencyType());
				this.createInvoiceFee(collectOverseasFee, collectOverseasId);
				// 修改费用单的收票金额
				Fee feeEntity = new Fee();
				feeEntity.setId(fee.getId());
				feeEntity.setAcceptInvoiceAmount(DecimalUtil.add(fee.getAcceptInvoiceAmount(), overInvoiceAmount));
				feeDao.updateById(feeEntity);
				// 保存总共的收票金额
				countInvoiceFeeAmount = DecimalUtil.add(countInvoiceFeeAmount, overInvoiceAmount);
			}
			// 修改invoice收票的头信息
			invoiceCollectOverseas.setInvoiceAmount(
					DecimalUtil.add(invoiceCollectOverseas.getInvoiceAmount(), countInvoiceFeeAmount));
			invoiceCollectOverseas.setCurrnecyType(invoiceCurrnecyType);
			collectOverseasService.updateInvoiceCollectOverseasById(invoiceCollectOverseas);
		}
	}

	/**
	 * 封装费用收票的新增数据
	 * 
	 * @param collectOverseasFee
	 * @return
	 */
	private void createInvoiceFee(InvoiceCollectOverseasFee collectOverseasFee, Integer collectOverseasId) {
		collectOverseasFee.setIsDelete(BaseConsts.ZERO);
		collectOverseasFee.setCreateAt(new Date());
		collectOverseasFee.setCreator(ServiceSupport.getUser().getChineseName());
		collectOverseasFee.setCreatorId(ServiceSupport.getUser().getId());
		collectOverseasFee.setCollectOverseasId(collectOverseasId);
		this.updateInvoiceCollectOverseasFeeById(collectOverseasFee);
	}

	/**
	 * 查询费用收票数据
	 * 
	 * @param id
	 * @return
	 */
	public InvoiceCollectOverseasFee queryInvoiceFeeEntityById(Integer id) {
		return invoiceCollectOverseasFeeDao.queryEntityById(id);
	}

	/**
	 * 根据收票Id查询列表数据
	 * 
	 * @param overId
	 * @return
	 */
	public List<InvoiceCollectOverseasFeeResDto> queryInvoiceCollectOverseasByResut(Integer overId) {
		InvoiceCollectOverseasFeeReqDto reqDto = new InvoiceCollectOverseasFeeReqDto();
		reqDto.setCollectOverseasId(overId);
		return convertToInvoiceCollectFeeResDto(invoiceCollectOverseasFeeDao.queryInvoiceFeeResult(reqDto));
	}

	/**
	 * 更新Invoice收票采购信息
	 * 
	 * @param invoiceCollect
	 * @return
	 */
	public void updateInvoiceCollectOverseasFeeById(InvoiceCollectOverseasFee invoiceCollectOverseasFee) {
		int result = BaseConsts.ONE;
		if (invoiceCollectOverseasFee.getId() != null) {
			result = invoiceCollectOverseasFeeDao.updateById(invoiceCollectOverseasFee);
		} else {
			result = invoiceCollectOverseasFeeDao.insert(invoiceCollectOverseasFee);
		}
		if (result == BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "更新失败:" + JSONObject.toJSON(invoiceCollectOverseasFee));
		}
	}

	/**
	 * 封装当前Invoice收票关联费用的数据
	 * 
	 * @param collectOverseasPos
	 * @return
	 */
	public List<InvoiceCollectOverseasFeeResDto> convertToInvoiceCollectFeeResDto(
			List<InvoiceCollectOverseasFee> collectOverseasFees) {
		List<InvoiceCollectOverseasFeeResDto> poResDtos = new ArrayList<InvoiceCollectOverseasFeeResDto>();
		if (CollectionUtils.isEmpty(collectOverseasFees)) {
			return poResDtos;
		} else {
			for (InvoiceCollectOverseasFee collectOverseasFee : collectOverseasFees) {
				InvoiceCollectOverseasFeeResDto poResDtot = invoiceCollectOverseasFeeToRes(collectOverseasFee);
				poResDtos.add(poResDtot);
			}
		}
		return poResDtos;
	}

	/**
	 * 封装当前Invoice收票关联费用的明细数据
	 * 
	 * @param collectOverseasPo
	 * @return
	 */
	public InvoiceCollectOverseasFeeResDto invoiceCollectOverseasFeeToRes(
			InvoiceCollectOverseasFee collectOverseasFee) {
		InvoiceCollectOverseasFeeResDto result = new InvoiceCollectOverseasFeeResDto();
		result.setId(collectOverseasFee.getId());// 主键ID
		result.setCollectOverseasId(collectOverseasFee.getCollectOverseasId());// Invoice收票主ID
		result.setFeeNo(collectOverseasFee.getFeeNo());// 费用编号
		result.setPayDate(collectOverseasFee.getPayDate());// 费用时间
		result.setFeeId(collectOverseasFee.getFeeId());// 费用ID
		result.setInvoiceAmount(collectOverseasFee.getInvoiceAmount());
		result.setCurrencyType(collectOverseasFee.geyCurrencyType());// 收票币种
		result.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				collectOverseasFee.geyCurrencyType() + ""));
		result.setFeeType(collectOverseasFee.getFeeType());
		result.setFeeTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.FEE_TYPE, collectOverseasFee.getFeeType() + ""));
		result.setCreator(collectOverseasFee.getCreator());// 创建人
		result.setCreatorId(collectOverseasFee.getCreatorId());
		result.setCreateAt(collectOverseasFee.getCreateAt());
		return result;
	}

	/**
	 * 将费用的数据封装成费用收票的数据
	 * 
	 * @param collectOverseasPos
	 * @return
	 */
	public List<InvoiceCollectOverseasFeeResDto> convertFeeToByInvoiceFeeResDto(List<Fee> fees) {
		List<InvoiceCollectOverseasFeeResDto> poResDtos = new ArrayList<InvoiceCollectOverseasFeeResDto>();
		if (CollectionUtils.isEmpty(fees)) {
			return poResDtos;
		} else {
			for (Fee fee : fees) {
				InvoiceCollectOverseasFeeResDto poResDtot = invoiceFeeResDto(fee);
				poResDtos.add(poResDtot);
			}
		}
		return poResDtos;
	}

	/**
	 * 封装费用的数据格式
	 * 
	 * @param fee
	 * @return
	 */
	public InvoiceCollectOverseasFeeResDto invoiceFeeResDto(Fee fee) {
		InvoiceCollectOverseasFeeResDto feeResDto = new InvoiceCollectOverseasFeeResDto();
		feeResDto.setId(fee.getId());// 费用ID
		feeResDto.setFeeNo(fee.getFeeNo());// 费用编号
		feeResDto.setFeeType(fee.getFeeType());// 费用类型
		feeResDto.setFeeTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.FEE_TYPE, fee.getFeeType() + ""));
		feeResDto.setPayDate(fee.getPayDate());
		feeResDto.setInvoiceAmount(DecimalUtil.subtract(fee.getPayAmount(), fee.getAcceptInvoiceAmount()));
		feeResDto.setCurrencyType(fee.getCurrencyType());
		feeResDto.setCurrencyTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, fee.getCurrencyType() + ""));
		return feeResDto;
	}
}
