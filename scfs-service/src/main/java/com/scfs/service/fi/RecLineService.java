package com.scfs.service.fi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fi.RecLineDao;
import com.scfs.dao.fi.ReceiveDao;
import com.scfs.dao.fi.VoucherLineDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseReqDto;
import com.scfs.domain.fi.dto.req.RecLineSearchReqDto;
import com.scfs.domain.fi.dto.resp.RecLineResDto;
import com.scfs.domain.fi.entity.RecDetail;
import com.scfs.domain.fi.entity.RecLine;
import com.scfs.domain.fi.entity.Receive;
import com.scfs.domain.fi.entity.VoucherLine;
import com.scfs.domain.result.PageResult;
import com.scfs.common.exception.BaseException;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: RecLineRelService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月29日			Administrator
 *
 * </pre>
 */

@Service
public class RecLineService {

	@Autowired
	RecLineDao recLineDao;

	@Autowired
	ReceiveDao receiveDao;

	@Autowired
	CacheService cacheService;

	@Autowired
	VoucherLineDao voucherLineDao;

	public void deleteRecLineById(Integer id) {
		RecLine recLine = queryEntityById(id);
		if (!DecimalUtil.eq(recLine.getWriteOffAmount(), DecimalUtil.ZERO)) {
			throw new BaseException(ExcMsgEnum.WRITE_OFF_AMOUNT_NOT_ZERO);
		}
		// 1.更新分录表的已对账金额
		VoucherLine voucherLine = voucherLineDao.queryEntityById(recLine.getVoucherLineId());
		voucherLine.setAmountChecked(DecimalUtil.subtract(voucherLine.getAmountChecked(), recLine.getAmountCheck()));
		voucherLineDao.updateById(voucherLine);

		// 2.更新应收表的应收金额
		Receive receive = receiveDao.queryEntityById(recLine.getRecId());
		receive.setAmountReceivable(DecimalUtil.subtract(receive.getAmountReceivable(), recLine.getAmountCheck()));
		receiveDao.updateById(receive);

		// 3.删除应收明细
		recLineDao.deleteById(id);
	}

	/**
	 * id : 应收id ids : 应收明细id列表 TODO.
	 *
	 * @param req
	 */
	public void batchDeleteRecLineById(BaseReqDto baseReqDto) {
		BigDecimal cutAmount = BigDecimal.ZERO;
		Integer recId = baseReqDto.getId();
		for (Integer id : baseReqDto.getIds()) {
			RecLine recLine = queryEntityById(id);
			if (!recLine.getRecId().equals(recId)) {
				throw new BaseException(ExcMsgEnum.REC_AND_LINE_NOT_MATCH);
			}
			if (!DecimalUtil.eq(recLine.getWriteOffAmount(), DecimalUtil.ZERO)) {
				throw new BaseException(ExcMsgEnum.WRITE_OFF_AMOUNT_NOT_ZERO);
			}
			cutAmount = DecimalUtil.add(cutAmount, recLine.getAmountCheck());
			VoucherLine voucherLine = voucherLineDao.queryEntityById(recLine.getVoucherLineId());
			// 1.更新分录已对账金额
			voucherLine
					.setAmountChecked(DecimalUtil.subtract(voucherLine.getAmountChecked(), recLine.getAmountCheck()));
			voucherLineDao.updateById(voucherLine);
			// 2.删除应收明细
			recLineDao.deleteById(id);
		}
		// 3.更新应收表的应收金额
		Receive receive = receiveDao.queryEntityById(recId);
		if (DecimalUtil.eq(receive.getAmountReceivable(), cutAmount)) {
			receiveDao.deleteById(recId);
		} else {
			receive.setAmountReceivable(DecimalUtil.subtract(receive.getAmountReceivable(), cutAmount));
			receiveDao.updateById(receive);
		}
	}

	public void updateRecLineById(RecLine recLine) {
		Integer id = recLine.getId();
		RecLine oldEntity = queryEntityById(id);
		// 1.校验已核销金额只能为0
		if (!DecimalUtil.eq(oldEntity.getWriteOffAmount(), DecimalUtil.ZERO)) {
			throw new BaseException(ExcMsgEnum.WRITE_OFF_AMOUNT_NOT_ZERO);
		}
		VoucherLine voucherLine = voucherLineDao.queryEntityById(oldEntity.getVoucherLineId());
		BigDecimal amount_check = recLine.getAmountCheck();
		if (DecimalUtil.gt(DecimalUtil.ZERO, amount_check)
				&& DecimalUtil.lt(DecimalUtil.ZERO, voucherLine.getAmount())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "分录金额为正数，对账金额必须为正数");
		}
		if (DecimalUtil.lt(DecimalUtil.ZERO, amount_check)
				&& DecimalUtil.gt(DecimalUtil.ZERO, voucherLine.getAmount())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "分录金额为负数，对账金额必须为负数");
		}
		// 分录金额 - 已对账金额 = 待对账金额
		BigDecimal writingOffAmount = DecimalUtil.subtract(voucherLine.getAmount(), voucherLine.getAmountChecked());
		BigDecimal diffAmount = DecimalUtil.subtract(amount_check, oldEntity.getAmountCheck());
		// 2.校验
		if (DecimalUtil.gt(diffAmount.abs(), writingOffAmount.abs())) {
			throw new BaseException(ExcMsgEnum.CHECK_AMOUNT_EXCCED, amount_check, writingOffAmount);
		}

		// 3.更新分录已对账金额
		voucherLine.setAmountChecked(DecimalUtil.add(voucherLine.getAmountChecked(), diffAmount));
		voucherLineDao.updateById(voucherLine);
		// 4.更新应收表应收金额
		Receive receive = receiveDao.queryEntityById(recLine.getRecId());
		receive.setAmountReceivable(DecimalUtil.add(receive.getAmountReceivable(), diffAmount));
		// 5.更新应收明细对账金额
		recLineDao.updateById(recLine);

	}

	public void batchUpdateRecLineById(RecDetail recDetail) {
		BigDecimal sumDiffAmount = BigDecimal.ZERO;
		// 获取前台传过来得应收id
		int recId = recDetail.getReceive().getId();
		for (RecLine recLine : recDetail.getRecLines()) {
			RecLine oldEntity = queryEntityById(recLine.getId());
			if (recId != oldEntity.getRecId()) {
				throw new BaseException(ExcMsgEnum.REC_AND_LINE_NOT_MATCH);
			}
			// 1.校验已核销金额只能为0
			if (!DecimalUtil.eq(oldEntity.getWriteOffAmount(), DecimalUtil.ZERO)) {
				throw new BaseException(ExcMsgEnum.WRITE_OFF_AMOUNT_NOT_ZERO);
			}
			VoucherLine voucherLine = voucherLineDao.queryEntityById(oldEntity.getVoucherLineId());
			BigDecimal amount_check = recLine.getAmountCheck();
			if (DecimalUtil.gt(DecimalUtil.ZERO, amount_check)
					&& DecimalUtil.lt(DecimalUtil.ZERO, voucherLine.getAmount())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "分录金额为正数，对账金额必须为正数");
			}
			if (DecimalUtil.lt(DecimalUtil.ZERO, amount_check)
					&& DecimalUtil.gt(DecimalUtil.ZERO, voucherLine.getAmount())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "分录金额为负数，对账金额必须为负数");
			}
			// 分录金额 - 已对账金额 = 待对账金额
			BigDecimal writingOffAmount = DecimalUtil.subtract(voucherLine.getAmount(), voucherLine.getAmountChecked());
			BigDecimal diffAmount = DecimalUtil.subtract(amount_check, oldEntity.getAmountCheck());
			// 2.校验
			if (DecimalUtil.gt(diffAmount.abs(), writingOffAmount.abs())) {
				throw new BaseException(ExcMsgEnum.CHECK_AMOUNT_EXCCED, amount_check, writingOffAmount);
			}

			sumDiffAmount = DecimalUtil.add(sumDiffAmount, diffAmount);
			// 3.更新分录已对账金额
			voucherLine.setAmountChecked(DecimalUtil.add(voucherLine.getAmountChecked(), diffAmount));
			voucherLineDao.updateById(voucherLine);
			// 4.更新应收明细对账金额
			recLineDao.updateById(recLine);
		}
		// 从数据库中获取应收对象,并更新应收金额
		Receive receive = receiveDao.queryEntityById(recId);
		receive.setAmountReceivable(DecimalUtil.add(receive.getAmountReceivable(), sumDiffAmount));
		receiveDao.updateById(receive);
	}

	public RecLine queryEntityById(Integer id) {
		RecLine recLineRel = recLineDao.queryEntityById(id);
		return recLineRel;
	}

	public PageResult<RecLineResDto> queryResultsByRecId(RecLineSearchReqDto req) {
		PageResult<RecLineResDto> pageResult = new PageResult<RecLineResDto>();
		int offSet = PageUtil.getOffSet(req.getPage(), req.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, req.getPer_page());
		List<RecLine> recLines = recLineDao.queryResultsByRecId(req, rowBounds);
		List<RecLineResDto> recLineResDtos = new ArrayList<RecLineResDto>();
		for (RecLine recLine : recLines) {
			RecLineResDto recLineResDto = convertToRecLineRes(recLine);
			recLineResDtos.add(recLineResDto);
		}
		pageResult.setItems(recLineResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), req.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(req.getPage());
		pageResult.setPer_page(req.getPer_page());
		return pageResult;
	}

	private RecLineResDto convertToRecLineRes(RecLine recLine) {
		RecLineResDto resDto = new RecLineResDto();
		resDto.setId(recLine.getId());
		resDto.setBusiUnitName(cacheService.getSubjectNcByIdAndKey(recLine.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));
		resDto.setCustName(cacheService.getSubjectNcByIdAndKey(recLine.getCustId(), CacheKeyConsts.CUSTOMER));
		resDto.setProjectName(cacheService.getProjectNameById(recLine.getProjectId()));
		resDto.setAmountCheck(recLine.getAmountCheck());
		resDto.setWriteOffAmount(recLine.getWriteOffAmount());
		resDto.setBillDate(recLine.getBillDate());
		resDto.setCurrencyTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, recLine.getCurrencyType() + ""));
		resDto.setBillNo(recLine.getBillNo());
		return resDto;
	}

}
