package com.scfs.service.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fee.FeeDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.invoice.InvoiceApplyDao;
import com.scfs.dao.invoice.InvoiceFeeDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.invoice.dto.req.InvoiceApplyManagerReqDto;
import com.scfs.domain.invoice.dto.req.InvoiceFeeManagerReqDto;
import com.scfs.domain.invoice.dto.req.InvoiceReqList;
import com.scfs.domain.invoice.dto.resp.InvoiceFeeManagerResDto;
import com.scfs.domain.invoice.entity.InvoiceApplyManager;
import com.scfs.domain.invoice.entity.InvoiceFeeManager;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.common.SequenceService;
import com.scfs.service.support.ServiceSupport;

@Service
public class InvoiceFeeService {

	@Autowired
	private InvoiceFeeDao invoiceDao;
	@Autowired
	private InvoiceInfoService invoiceInfoService;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private FeeDao feeDao;
	@Autowired
	private InvoiceApplyDao invoiceApplyDao;

	/**
	 * 查询费用信息(已选择)
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PageResult<InvoiceFeeManagerResDto> queryInvoiceResultsByCon(InvoiceApplyManagerReqDto queryInvoiceFeeDto)
			throws Exception {

		PageResult<InvoiceFeeManagerResDto> pageResult = new PageResult<InvoiceFeeManagerResDto>();
		int offSet = PageUtil.getOffSet(queryInvoiceFeeDto.getPage(), queryInvoiceFeeDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, queryInvoiceFeeDto.getPer_page());
		List<InvoiceFeeManager> result = invoiceDao.selectByInvoiceId(queryInvoiceFeeDto.getId(), rowBounds);
		List<InvoiceFeeManagerResDto> vos = new ArrayList<InvoiceFeeManagerResDto>();
		if (result != null) {
			for (InvoiceFeeManager inv : result) {
				InvoiceFeeManagerResDto res = convertToResDto(inv);
				vos.add(res);
				if (pageResult.getFeeTotalAmount() != null)
					pageResult.setFeeTotalAmount(res.getProvideInvoiceAmount().add(pageResult.getFeeTotalAmount()));
				else
					pageResult.setFeeTotalAmount(res.getProvideInvoiceAmount());
				if (pageResult.getRateTotalAmount() != null)
					pageResult.setRateTotalAmount(res.getRateAmount().add(pageResult.getRateTotalAmount()));
				else
					pageResult.setRateTotalAmount(res.getRateAmount());
				if (pageResult.getExRateTotalAmount() != null)
					pageResult.setExRateTotalAmount(res.getExRateAmount().add(pageResult.getExRateTotalAmount()));
				else
					pageResult.setExRateTotalAmount(res.getExRateAmount());
			}
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), queryInvoiceFeeDto.getPer_page());
		pageResult.setItems(vos);
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(queryInvoiceFeeDto.getPage());
		pageResult.setPer_page(queryInvoiceFeeDto.getPer_page());
		return pageResult;
	}

	/**
	 * 编辑/浏览
	 * 
	 * @param baseGoods
	 * @return
	 */
	public Result<InvoiceFeeManagerResDto> queryInvoiceResultsById(InvoiceFeeManagerReqDto invoiceFeeManagerReqDto) {
		Result<InvoiceFeeManagerResDto> result = new Result<InvoiceFeeManagerResDto>();
		InvoiceFeeManager invoiceRes = invoiceDao.selectInvoiceFeeById(invoiceFeeManagerReqDto.getId());
		InvoiceApplyManager apply = invoiceApplyDao.queryEntityById(invoiceRes.getInvoiceApplyId());
		if (apply.getStatus().compareTo(BaseConsts.ONE) != 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "状态不正确");
		}
		InvoiceFeeManagerResDto res = convertToResDto(invoiceRes);
		result.setItems(res);
		return result;
	}

	/**
	 * 查询费用信息(未选择)
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PageResult<InvoiceFeeManagerResDto> queryInvoiceByCon(InvoiceFeeManagerReqDto queryInvoiceFeeDto) {
		PageResult<InvoiceFeeManagerResDto> pageResult = new PageResult<InvoiceFeeManagerResDto>();
		InvoiceApplyManager vo = invoiceApplyDao.queryEntityById(queryInvoiceFeeDto.getId());
		InvoiceApplyManager applyManager = new InvoiceApplyManager();
		applyManager.setProjectId(vo.getProjectId());
		applyManager.setCustomerId(vo.getCustomerId());
		applyManager.setApplyNo(queryInvoiceFeeDto.getFeeNo());
		applyManager.setInvoiceType(vo.getInvoiceType());
		applyManager.setInvoiceTaxRate(vo.getInvoiceTaxRate());
		List<Fee> result = feeDao.selectNotByCon(applyManager);
		List<InvoiceFeeManagerResDto> vos = new ArrayList<InvoiceFeeManagerResDto>();
		if (result != null) {
			for (Fee inv : result) {
				InvoiceFeeManagerResDto res = new InvoiceFeeManagerResDto();
				res.setId(inv.getId());
				res.setFeeNo(inv.getFeeNo());
				res.setFeeType(inv.getFeeType());
				res.setProvideInvoiceAmount(inv.getProvideInvoiceAmount());
				if (inv.getProvideInvoiceAmount() != null) {
					res.setProvideMaxAmount(inv.getRecAmount().subtract(inv.getProvideInvoiceAmount()).setScale(2,
							BigDecimal.ROUND_HALF_UP));
					res.setProvideInvoiceAmount(inv.getRecAmount().subtract(inv.getProvideInvoiceAmount()).setScale(2,
							BigDecimal.ROUND_HALF_UP));
				} else {
					res.setProvideMaxAmount(inv.getRecAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
					res.setProvideInvoiceAmount(inv.getRecAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
				}

				res.setFeeTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.FEE_TYPE, inv.getFeeType() + ""));
				res.setFeeId(inv.getId());
				res.setFeeDate(inv.getRecDate());
				vos.add(res);
			}
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), queryInvoiceFeeDto.getPer_page());
		pageResult.setItems(vos);
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(queryInvoiceFeeDto.getPage());
		pageResult.setPer_page(queryInvoiceFeeDto.getPer_page());
		return pageResult;
	}

	/**
	 * 批量删除费用信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void deleteInvoiceByIds(List<Integer> ids) throws Exception {

		if (!CollectionUtils.isEmpty(ids)) {
			Integer invoiceApplyId = 0;
			for (Integer id : ids) {
				InvoiceFeeManager vo = invoiceDao.selectInvoiceFeeById(id);
				InvoiceApplyManager apply = invoiceApplyDao.queryEntityById(vo.getInvoiceApplyId());
				if (apply.getStatus().compareTo(BaseConsts.ONE) != 0) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "状态不正确");
				}
				invoiceApplyId = vo.getInvoiceApplyId();
				invoiceDao.deleteByCon(vo);
				Fee feeEntity = new Fee();
				Fee fee = feeDao.queryEntityById(vo.getFeeId());
				feeEntity.setId(fee.getId());
				feeEntity.setProvideInvoiceAmount(fee.getProvideInvoiceAmount().subtract(vo.getProvideInvoiceAmount()));
				feeDao.updateById(feeEntity);
			}
			invoiceInfoService.deleteSimulateInvoiceInfo(invoiceApplyId);
		}
	}

	/**
	 * 删除费用信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void deleteInvoiceById(Integer id) throws Exception {
		InvoiceFeeManager invoiceFee = new InvoiceFeeManager();
		invoiceFee.setId(id);
		invoiceDao.deleteByCon(invoiceFee);
	}

	/**
	 * 批量修改费用信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public BaseResult updateInvoice(InvoiceFeeManagerReqDto invoiceFeeManager) {
		BaseResult baseResult = new BaseResult();
		InvoiceFeeManager vo = invoiceDao.selectInvoiceFeeById(invoiceFeeManager.getId());
		InvoiceApplyManager apply = invoiceApplyDao.queryEntityById(vo.getInvoiceApplyId());
		if (apply.getStatus().compareTo(BaseConsts.ONE) != 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "状态不正确");
		} else {
			Fee fee = feeDao.queryEntityById(vo.getFeeId());
			if (fee.getProvideInvoiceAmount() == null) {
				fee.setProvideInvoiceAmount(BigDecimal.ZERO);
			}
			if (invoiceFeeManager.getProvideInvoiceAmount().compareTo(fee.getRecAmount()
					.subtract(fee.getProvideInvoiceAmount().subtract(vo.getProvideInvoiceAmount()))) > 0) {
				throw new BaseException(ExcMsgEnum.INVOICE_AMOUNT_MAX_EXCEPTION);
			} else {
				// 更新费用表开票金额
				Fee fee1 = new Fee();
				fee1.setId(vo.getFeeId());
				fee1.setProvideInvoiceAmount(fee.getProvideInvoiceAmount()
						.add(invoiceFeeManager.getProvideInvoiceAmount().subtract(vo.getProvideInvoiceAmount())));
				feeDao.updateById(fee1);
				vo.setProvideInvoiceAmount(invoiceFeeManager.getProvideInvoiceAmount());
				invoiceDao.updateById(vo);
			}
		}
		return baseResult;
	}

	/**
	 * 批量插入费用信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public BaseResult addBactchInvoice(InvoiceReqList invoiceFeeManager) {
		BaseResult baseResult = new BaseResult();
		InvoiceApplyManager vo = invoiceApplyDao.queryEntityById(invoiceFeeManager.getId());
		if (vo.getStatus().compareTo(BaseConsts.ONE) != 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "状态不正确");
		}
		List<InvoiceFeeManager> lists = invoiceFeeManager.getInvoiceFeeManagerReqDto();
		if (!CollectionUtils.isEmpty(lists)) {
			for (InvoiceFeeManager list : lists) {
				Fee fee = feeDao.queryEntityById(list.getFeeId());
				if (fee.getProvideInvoiceAmount() == null) {
					fee.setProvideInvoiceAmount(BigDecimal.ZERO);
				}
				if (list.getProvideInvoiceAmount()
						.compareTo(fee.getRecAmount().subtract(fee.getProvideInvoiceAmount())) > 0) {
					throw new BaseException(ExcMsgEnum.INVOICE_AMOUNT_MAX_EXCEPTION);
				} else {
					list.setCreator(ServiceSupport.getUser().getChineseName());
					list.setCreatorId(ServiceSupport.getUser().getId());
					list.setCreateAt(new Date());
					list.setInRateAmount(list.getProvideInvoiceAmount());
					list.setRateAmount(DecimalUtil.multiply(DecimalUtil
							.divide(list.getProvideInvoiceAmount(), DecimalUtil.ONE.add(vo.getInvoiceTaxRate()))
							.setScale(2, BigDecimal.ROUND_HALF_UP), vo.getInvoiceTaxRate()));
					list.setExRateAmount(DecimalUtil
							.divide(list.getProvideInvoiceAmount(), DecimalUtil.ONE.add(vo.getInvoiceTaxRate()))
							.setScale(2, BigDecimal.ROUND_HALF_UP));
					list.setInvoiceApplyId(vo.getId());
					invoiceDao.insert(list);
					// 更新费用表开票金额
					Fee fee1 = new Fee();
					fee1.setId(list.getFeeId());
					fee1.setProvideInvoiceAmount(list.getProvideInvoiceAmount().add(fee.getProvideInvoiceAmount()));
					feeDao.updateById(fee1);
				}
			}
		}
		return baseResult;
	}

	/**
	 * 插入费用信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Integer addInvoice(InvoiceFeeManager list) {
		BigDecimal sum = BigDecimal.ZERO;
		list.setCreator(ServiceSupport.getUser().getChineseName());
		list.setCreatorId(ServiceSupport.getUser().getId());
		list.setFeeNo(sequenceService.getNumDateByBusName(BaseConsts.FE_NO_PREFIX, SeqConsts.INVOICE_FEE_NO,
				BaseConsts.INT_13));
		invoiceDao.insert(list);
		sum = sum.add(list.getProvideInvoiceAmount());
		// 更新费用表开票金额
		Fee fee = new Fee();
		fee.setId(list.getFeeId());
		fee.setProvideInvoiceAmount(list.getProvideInvoiceAmount());
		feeDao.updateById(fee);
		return 1;
	}

	/**
	 * 转换响应结果
	 * 
	 * @param inv
	 * @return
	 */
	private InvoiceFeeManagerResDto convertToResDto(InvoiceFeeManager inv) {
		// TODO Auto-generated method stub
		InvoiceFeeManagerResDto res = new InvoiceFeeManagerResDto();
		InvoiceApplyManager vo = invoiceApplyDao.queryEntityById(inv.getInvoiceApplyId());
		Fee fee = feeDao.queryEntityById(inv.getFeeId());
		res.setId(inv.getId());
		res.setFeeType(inv.getFeeType());
		res.setFeeTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.FEE_TYPE, inv.getFeeType() + ""));
		res.setFeeDate(inv.getFeeDate());
		res.setFeeDateValue(inv.getFeeDate());
		res.setFeeNo(inv.getFeeNo());
		res.setProvideInvoiceAmount(inv.getProvideInvoiceAmount());
		res.setRateAmount(DecimalUtil
				.multiply(DecimalUtil.divide(inv.getProvideInvoiceAmount(), DecimalUtil.ONE.add(vo.getInvoiceTaxRate()))
						.setScale(2, BigDecimal.ROUND_HALF_UP), vo.getInvoiceTaxRate()));
		res.setRateAmount(res.getRateAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
		res.setExRateAmount(
				DecimalUtil.divide(inv.getProvideInvoiceAmount(), DecimalUtil.ONE.add(vo.getInvoiceTaxRate()))
						.setScale(2, BigDecimal.ROUND_HALF_UP));
		res.setExRateAmount(res.getExRateAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
		res.setDiscountInRateAmount(inv.getDiscountInRateAmount());
		res.setDiscountRateAmount(DecimalUtil
				.multiply(DecimalUtil.divide(inv.getDiscountInRateAmount(), DecimalUtil.ONE.add(vo.getInvoiceTaxRate()))
						.setScale(2, BigDecimal.ROUND_HALF_UP), vo.getInvoiceTaxRate()));
		res.setRecAmount(fee.getRecAmount());
		res.setInvoiceAmount(fee.getProvideInvoiceAmount());
		res.setUseAmount(
				fee.getRecAmount().subtract(fee.getProvideInvoiceAmount().subtract(inv.getProvideInvoiceAmount())));
		res.setOpertaList(getOperList());
		return res;
	}

	/**
	 * 根据状态获取操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState() {
		List<String> opertaList = Lists.newArrayList(5);
		opertaList.add(OperateConsts.EDIT);
		return opertaList;
	}

	private List<CodeValue> getOperList() {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState();
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				InvoiceFeeManagerResDto.Operate.operMap);
		return oprResult;
	}
}
