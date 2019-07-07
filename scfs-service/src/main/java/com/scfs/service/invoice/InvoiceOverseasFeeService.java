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
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.invoice.InvoiceOverseasDao;
import com.scfs.dao.invoice.InvoiceOverseasFeeDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fee.dto.req.QueryFeeReqDto;
import com.scfs.domain.fee.dto.resp.FeeQueryResDto;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.fee.entity.FeeQueryModel;
import com.scfs.domain.invoice.dto.req.InvoiceOverseasFeeReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceOverseasFeeResDto;
import com.scfs.domain.invoice.entity.InvoiceOverseas;
import com.scfs.domain.invoice.entity.InvoiceOverseasFee;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fee.impl.FeeServiceImpl;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 	境外发票费用业务
 *  File: InvoiceOverseasFeeService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年03月29日				Administrator
 *
 * </pre>
 */
@Service
public class InvoiceOverseasFeeService {
	@Autowired
	InvoiceOverseasFeeDao invoiceOverseasFeeDao;
	@Autowired
	InvoiceOverseasDao invoiceOverseasDao;
	@Autowired
	FeeServiceImpl feeService;
	@Autowired
	CacheService cacheService;

	/**
	 * 获取费用信息
	 * 
	 * @param overseasFeeResDto
	 * @return
	 */
	public PageResult<FeeQueryResDto> queryFeeByCond(InvoiceOverseasFeeReqDto overseasFeeResDto) {
		PageResult<FeeQueryResDto> model = new PageResult<FeeQueryResDto>();
		InvoiceOverseas invoiceOverseas = invoiceOverseasDao.queryEntityById(overseasFeeResDto.getId());
		QueryFeeReqDto queryFeeReqDto = new QueryFeeReqDto();
		queryFeeReqDto.setFeeNo(overseasFeeResDto.getFeeNo());
		queryFeeReqDto.setProjectId(invoiceOverseas.getProjectId());
		queryFeeReqDto.setBusiUnit(invoiceOverseas.getBusinessUnit());
		queryFeeReqDto.setCustPayer(invoiceOverseas.getCustomerId());
		queryFeeReqDto.setState(BaseConsts.THREE);
		queryFeeReqDto.setCurrencyType(invoiceOverseas.getCurrnecyType());
		model = feeService.queryFeeByCond(queryFeeReqDto);
		PageResult<FeeQueryResDto> result = new PageResult<FeeQueryResDto>();
		List<FeeQueryResDto> recFeeQueryResDtos = new ArrayList<FeeQueryResDto>();
		if (model.getItems() != null && model.getItems().size() > BaseConsts.ZERO) {// 费用税率与收票税率一致
			for (FeeQueryResDto fee : model.getItems()) {
				fee.setBlanceInvoiceAmount(DecimalUtil.subtract(fee.getRecAmount(), fee.getAcceptInvoiceAmount()));
				if (DecimalUtil.gt(fee.getBlanceInvoiceAmount(), BigDecimal.ZERO)) {
					recFeeQueryResDtos.add(fee);
				}

			}
		}
		result.setItems(recFeeQueryResDtos);
		return result;
	}

	/**
	 * 添加境外收票
	 * 
	 * @param overseasFeeResDto
	 * @return
	 */
	public BaseResult createInvoiceOverseasFee(InvoiceOverseasFeeReqDto overseasFeeResDto) {
		BaseResult baseResult = new BaseResult();
		Integer overseasId = overseasFeeResDto.getOverseasId();
		InvoiceOverseas overseas = invoiceOverseasDao.queryEntityById(overseasId);
		BigDecimal sum = overseas.getInvoiceAmount();

		List<InvoiceOverseasFee> feeList = overseasFeeResDto.getFeeList();
		for (InvoiceOverseasFee overseasFee : feeList) {
			FeeQueryModel oldEntity = feeService.queryEntityById(overseasFee.getFeeId()).getItems();
			BigDecimal blance = DecimalUtil.subtract(oldEntity.getRecAmount(), oldEntity.getAcceptInvoiceAmount());
			BigDecimal invoiceAmount = overseasFee.getInvoiceAmount(); // 开票金额
			if (DecimalUtil.gt(invoiceAmount, blance)) {
				baseResult.setMsg("余额不足！");
				return baseResult;
			}
		}

		Date date = new Date();
		for (InvoiceOverseasFee overseasFee : feeList) {
			BigDecimal invoiceAmount = overseasFee.getInvoiceAmount(); // 开票金额
			overseasFee.setOverseasId(overseasId);
			overseasFee.setCreateAt(date);
			overseasFee.setCreator(ServiceSupport.getUser().getChineseName());
			overseasFee.setCreatorId(ServiceSupport.getUser().getId());
			overseasFee.setIsDelete(BaseConsts.ZERO);
			invoiceOverseasFeeDao.insert(overseasFee);
			sum = DecimalUtil.add(sum, invoiceAmount);

			FeeQueryModel oldEntity = feeService.queryEntityById(overseasFee.getFeeId()).getItems();
			Fee upFee = new Fee();
			upFee.setId(oldEntity.getId());
			upFee.setAcceptInvoiceAmount(DecimalUtil.add(oldEntity.getAcceptInvoiceAmount(), invoiceAmount)
					.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			feeService.updateFeeById(upFee);// 修改费用收票金额
		}
		InvoiceOverseas upEntity = new InvoiceOverseas();
		upEntity.setId(overseasId);
		upEntity.setInvoiceAmount(sum);
		invoiceOverseasDao.updateById(upEntity);
		return baseResult;
	}

	/**
	 * 修改数据
	 * 
	 * @param overseasFeeResDto
	 * @return
	 */
	public BaseResult updateInvoiceOverseasFee(InvoiceOverseasFeeReqDto overseasFeeResDto) {
		BaseResult baseResult = new BaseResult();
		List<InvoiceOverseasFee> feeList = overseasFeeResDto.getFeeList();
		for (InvoiceOverseasFee invoiceOverseasFee : feeList) {
			InvoiceOverseasFee overseasFee = invoiceOverseasFeeDao.queryEntityById(invoiceOverseasFee.getId());
			FeeQueryModel oldEntity = feeService.queryEntityById(overseasFee.getFeeId()).getItems();
			BigDecimal diffAmount = DecimalUtil.subtract(invoiceOverseasFee.getInvoiceAmount(),
					overseasFee.getInvoiceAmount());// 差额
			BigDecimal sum = DecimalUtil.add(diffAmount, oldEntity.getAcceptInvoiceAmount());// 加上以前
			if (DecimalUtil.gt(sum, oldEntity.getRecAmount())) {// 是否大于总额
				baseResult.setMsg("余额不足！");
				return baseResult;
			}
		}

		for (InvoiceOverseasFee invoiceOverseasFee : feeList) {
			BigDecimal invoiceAmount = invoiceOverseasFee.getInvoiceAmount(); // 开票金额
			InvoiceOverseasFee overseasFee = invoiceOverseasFeeDao.queryEntityById(invoiceOverseasFee.getId());

			Integer overseasId = overseasFee.getOverseasId();// 获取开票基本信息
			InvoiceOverseas overseas = invoiceOverseasDao.queryEntityById(overseasId);

			BigDecimal diffAmount = DecimalUtil.subtract(invoiceAmount, overseasFee.getInvoiceAmount());
			InvoiceOverseas upEntity = new InvoiceOverseas();
			upEntity.setId(overseasId);
			upEntity.setInvoiceAmount(DecimalUtil.add(diffAmount, overseas.getInvoiceAmount()).setScale(BaseConsts.TWO,
					BigDecimal.ROUND_HALF_UP));
			invoiceOverseasDao.updateById(upEntity);// 修改境外收票金额

			FeeQueryModel oldEntity = feeService.queryEntityById(overseasFee.getFeeId()).getItems();
			Fee upFee = new Fee();
			upFee.setId(oldEntity.getId());
			upFee.setAcceptInvoiceAmount(DecimalUtil.add(diffAmount, oldEntity.getAcceptInvoiceAmount())
					.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			feeService.updateFeeById(upFee);// 修改费用收票金额

			invoiceOverseasFeeDao.updateById(invoiceOverseasFee);
		}
		return baseResult;
	}

	/**
	 * 删除数据
	 * 
	 * @param feeReqDto
	 * @return
	 */
	public BaseResult deleteInvoiceOverseasFee(InvoiceOverseasFeeReqDto feeReqDto) {
		BaseResult baseResult = new BaseResult();
		for (Integer id : feeReqDto.getIds()) {
			InvoiceOverseasFee oldEntity = invoiceOverseasFeeDao.queryEntityById(id);
			InvoiceOverseas overseas = invoiceOverseasDao.queryEntityById(oldEntity.getOverseasId());
			InvoiceOverseas upEntity = new InvoiceOverseas();
			upEntity.setId(oldEntity.getOverseasId());
			upEntity.setInvoiceAmount(DecimalUtil.subtract(overseas.getInvoiceAmount(), oldEntity.getInvoiceAmount())
					.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP)); // 除掉开票金额
			invoiceOverseasDao.updateById(upEntity);// 修改收票金额

			FeeQueryModel oldFeeEntity = feeService.queryEntityById(oldEntity.getFeeId()).getItems();
			Fee upFee = new Fee();
			upFee.setId(oldFeeEntity.getId());
			upFee.setAcceptInvoiceAmount(
					DecimalUtil.subtract(oldFeeEntity.getAcceptInvoiceAmount(), oldEntity.getInvoiceAmount())
							.setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
			feeService.updateFeeById(upFee);// 修改费用收票金额

			InvoiceOverseasFee invoiceOverseasFee = new InvoiceOverseasFee();
			invoiceOverseasFee.setId(id);
			invoiceOverseasFee.setIsDelete(BaseConsts.ONE);
			invoiceOverseasFeeDao.updateById(invoiceOverseasFee);
		}
		return baseResult;
	}

	/**
	 * 编辑
	 * 
	 * @param feeReqDto
	 * @return
	 */
	public Result<InvoiceOverseasFeeResDto> editInvoiceCollectFeeById(InvoiceOverseasFeeReqDto feeReqDto) {
		Result<InvoiceOverseasFeeResDto> result = new Result<InvoiceOverseasFeeResDto>();
		result.setItems(convertInvoiceOverseasFeeResDto(invoiceOverseasFeeDao.queryEntityById(feeReqDto.getId())));
		return result;
	}

	/**
	 * 获取所有数据
	 * 
	 * @param feeReqDto
	 * @return
	 */
	public List<InvoiceOverseasFeeResDto> queryInvoiceCollectFeeResults(InvoiceOverseasFeeReqDto feeReqDto) {
		return convertToInvoiceOverseasFeeResDtos(invoiceOverseasFeeDao.queryResultsByCon(feeReqDto));
	}

	public PageResult<InvoiceOverseasFeeResDto> queryAllInvoiceCollectFeeResults(InvoiceOverseasFeeReqDto feeReqDto) {
		PageResult<InvoiceOverseasFeeResDto> pageResult = new PageResult<InvoiceOverseasFeeResDto>();
		pageResult.setItems(convertToInvoiceOverseasFeeResDtos(invoiceOverseasFeeDao.queryResultsByCon(feeReqDto)));
		return pageResult;
	}

	/**
	 * 获取列表数据
	 * 
	 * @param feeReqDto
	 * @return
	 */
	public PageResult<InvoiceOverseasFeeResDto> queryInvoiceCollectFeeResultsByCon(InvoiceOverseasFeeReqDto feeReqDto) {
		PageResult<InvoiceOverseasFeeResDto> pageResult = new PageResult<InvoiceOverseasFeeResDto>();
		int offSet = PageUtil.getOffSet(feeReqDto.getPage(), feeReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, feeReqDto.getPer_page());
		List<InvoiceOverseasFeeResDto> overseasFeeResDto = convertToInvoiceOverseasFeeResDtos(
				invoiceOverseasFeeDao.queryResultsByCon(feeReqDto, rowBounds));
		pageResult.setItems(overseasFeeResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), feeReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(feeReqDto.getPage());
		pageResult.setPer_page(feeReqDto.getPer_page());

		List<InvoiceOverseasFeeResDto> sumFee = convertToInvoiceOverseasFeeResDtos(
				invoiceOverseasFeeDao.queryResultsByCon(feeReqDto, rowBounds));
		BigDecimal invoiceAmountSum = BigDecimal.ZERO;
		if (CollectionUtils.isNotEmpty(sumFee)) {
			for (InvoiceOverseasFeeResDto fee : sumFee) {
				invoiceAmountSum = DecimalUtil.add(invoiceAmountSum, fee.getInvoiceAmount());
			}
		}
		String totalStr = "开票金额  : " + DecimalUtil.formatScale2(invoiceAmountSum);
		pageResult.setTotalStr(totalStr);
		return pageResult;
	}

	public List<InvoiceOverseasFeeResDto> convertToInvoiceOverseasFeeResDtos(List<InvoiceOverseasFee> result) {
		List<InvoiceOverseasFeeResDto> invoiceOverseasFeeResDto = new ArrayList<InvoiceOverseasFeeResDto>();
		if (ListUtil.isEmpty(result)) {
			return invoiceOverseasFeeResDto;
		}
		for (InvoiceOverseasFee model : result) {
			InvoiceOverseasFeeResDto overseasFeeResDto = convertInvoiceOverseasFeeResDto(model);
			invoiceOverseasFeeResDto.add(overseasFeeResDto);
		}
		return invoiceOverseasFeeResDto;
	}

	public InvoiceOverseasFeeResDto convertInvoiceOverseasFeeResDto(InvoiceOverseasFee model) {
		InvoiceOverseasFeeResDto result = new InvoiceOverseasFeeResDto();
		result.setId(model.getId());
		result.setOverseasId(model.getOverseasId());
		result.setFeeId(model.getFeeId());
		result.setInvoiceAmount(model.getInvoiceAmount());
		result.setProjectId(model.getProjectId());
		result.setProjectName(cacheService.getProjectNameById(model.getProjectId()));
		result.setFeeNo(model.getFeeNo());
		result.setFeeType(model.getFeeType());
		result.setFeeTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.FEE_TYPE, model.getFeeType() + ""));
		result.setRecDate(model.getRecDate());
		result.setBlanceInvoiceAmount(model.getBlanceInvoiceAmount());
		result.setCurrencyType(model.getCurrencyType());
		result.setCurrencyTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, model.getCurrencyType() + ""));
		return result;
	}
}
