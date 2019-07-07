package com.scfs.service.common;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.dao.fi.BankReceiptDao;
import com.scfs.dao.logistics.BillInStoreDao;
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.dao.project.ProjectPoolAssetDao;
import com.scfs.dao.project.ProjectPoolDao;
import com.scfs.dao.project.ProjectPoolFundDao;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.logistics.dto.req.BillOutStoreSearchReqDto;
import com.scfs.domain.logistics.entity.BillInStore;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.pay.dto.req.PayFeeRelationReqDto;
import com.scfs.domain.pay.dto.req.PayOrderSearchReqDto;
import com.scfs.domain.pay.entity.PayFeeRelationModel;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.project.dto.req.ProjectPoolSearchReqDto;
import com.scfs.domain.project.entity.ProjectPool;
import com.scfs.domain.project.entity.ProjectPoolAsset;
import com.scfs.service.fi.BankReceiptService;
import com.scfs.service.logistics.BillInStoreService;
import com.scfs.service.logistics.BillOutStoreService;
import com.scfs.service.pay.PayFeeRelationService;
import com.scfs.service.pay.PayService;
import com.scfs.service.project.ProjectPoolService;

/**
 * Created by Administrator on 2017年3月16日.
 */
@Service
public class RefreshPoolAssetDtlService {
	@Autowired
	private BillOutStoreDao billOutStoreDao;
	@Autowired
	private ProjectPoolAssetDao projectPoolAssetDao;
	@Autowired
	private ProjectPoolFundDao projectPoolFundDao;
	@Autowired
	private ProjectPoolDao projectPoolDao;
	@Autowired
	private PayOrderDao payOrderDao;
	@Autowired
	private ProjectPoolService projectPoolService;
	@Autowired
	private PayFeeRelationService payFeeRelationService;
	@Autowired
	private PayService payService;
	@Autowired
	private BankReceiptService bankReceiptService;
	@Autowired
	private BankReceiptDao bankReceiptDao;
	@Autowired
	private BillInStoreService billInStoreService;
	@Autowired
	private BillOutStoreService billOutStoreService;
	@Autowired
	private BillInStoreDao billInStoreDao;

	public void refreshPoolAssetDtl() {
		BillOutStoreSearchReqDto billOutStoreSearchReqDto = new BillOutStoreSearchReqDto();
		List<BillOutStore> billOutStoreList = billOutStoreDao.querySaleFinishedResults(billOutStoreSearchReqDto);
		if (!CollectionUtils.isEmpty(billOutStoreList)) {
			for (BillOutStore billOutStore : billOutStoreList) {
				ProjectPoolAsset projectPoolAsset = projectPoolAssetDao.queryResultByBillNo(billOutStore.getBillNo());
				if (null == projectPoolAsset) {
					ProjectPoolAsset ppf = new ProjectPoolAsset();
					ppf.setType(BaseConsts.TWO);
					ppf.setBillNo(billOutStore.getBillNo());
					ppf.setBillSource(BaseConsts.TWO);
					ppf.setProjectId(billOutStore.getProjectId());
					ppf.setCustomerId(billOutStore.getCustomerId());
					ppf.setBusinessDate(billOutStore.getDeliverTime());
					ppf.setBillAmount(billOutStore.getCostAmount());
					ppf.setBillCurrencyType(billOutStore.getCurrencyType());
					projectPoolService.addProjectPoolAsset(ppf, BaseConsts.TWO);
					projectPoolService.updateProjectPoolInfo(ppf.getProjectId());
				}
			}
		}
	}

	public void refreshProjectPool() {
		// projectPoolDao.deleteExculdeOfSunyou();
		projectPoolFundDao.deleteExculdeOfSunyou();
		projectPoolAssetDao.deleteExculdeOfSunyou();
		refreshFundIn();
		refreshFundOut();
		refreshAssetIn();
		refreshAssetOut();

		resetProjectPool();
	}

	private void refreshFundIn() {
		PayOrderSearchReqDto payOrderSearchReqDto = new PayOrderSearchReqDto();
		payOrderSearchReqDto.setState(BaseConsts.SIX);
		payOrderSearchReqDto.setPayType(BaseConsts.ONE); // 付货款
		payOrderSearchReqDto.setPayWayType(BaseConsts.TWO);
		List<PayOrder> goodPayOrderList = payOrderDao.queryResultsByCon(payOrderSearchReqDto);
		if (!CollectionUtils.isEmpty(goodPayOrderList)) {
			for (PayOrder payOrder : goodPayOrderList) {
				payService.createProjectPool(payOrder, BigDecimal.ZERO);
			}
		}

		payOrderSearchReqDto.setState(BaseConsts.SIX);
		payOrderSearchReqDto.setPayType(BaseConsts.TWO); // 付费用
		payOrderSearchReqDto.setPayWayType(BaseConsts.TWO);
		List<PayOrder> feePayOrderList = payOrderDao.queryResultsByCon(payOrderSearchReqDto);
		if (!CollectionUtils.isEmpty(feePayOrderList)) {
			for (PayOrder payOrder : feePayOrderList) {
				PayFeeRelationReqDto payFeeRelationReqDto = new PayFeeRelationReqDto();
				payFeeRelationReqDto.setPayId(payOrder.getId());
				List<PayFeeRelationModel> ll = payFeeRelationService.queryPayFeeRelatioByCon(payFeeRelationReqDto);
				BigDecimal mm = DecimalUtil.ZERO;
				for (int i = 0; i < ll.size(); i++) {
					PayFeeRelationModel lv = ll.get(i);
					// 费用为资金占用类型入池
					if (lv.getPayFeeType() == BaseConsts.ONE) {
						mm = DecimalUtil.formatScale2(DecimalUtil.add(mm, lv.getPayAmount()));
					}
				}
				mm = DecimalUtil.formatScale2(mm);
				if (mm.compareTo(DecimalUtil.ZERO) != 0) {
					payService.createProjectPool(payOrder, mm);
				}
			}
		}
	}

	private void refreshFundOut() {
		List<BankReceipt> bankReceiptList = bankReceiptDao.queryRefreshProjectPoolResults();
		if (!CollectionUtils.isEmpty(bankReceiptList)) {
			for (BankReceipt bankReceipt : bankReceiptList) {
				bankReceiptService.createProjectPool(bankReceipt);
			}
		}
	}

	private void refreshAssetIn() {
		List<BillInStore> billInStoreList = billInStoreDao.queryRefreshProjectPoolResults();
		if (!CollectionUtils.isEmpty(billInStoreList)) {
			for (BillInStore billInStore : billInStoreList) {
				billInStoreService.createProjectPool(billInStore);
			}
		}
	}

	private void refreshAssetOut() {
		List<BillOutStore> billOutStoreList = billOutStoreDao.queryRefreshProjectPoolResults();
		if (!CollectionUtils.isEmpty(billOutStoreList)) {
			for (BillOutStore billOutStore : billOutStoreList) {
				billOutStoreService.createProjectPool(billOutStore);
			}
		}
	}

	private void resetProjectPool() {
		ProjectPoolSearchReqDto projectReqDto = new ProjectPoolSearchReqDto();
		List<ProjectPool> list = projectPoolDao.queryProjectPoolResultsByCon(projectReqDto);
		if (!CollectionUtils.isEmpty(list)) {
			for (ProjectPool projectPool : list) {
				int count = projectPoolDao.queryProjectPoolDtlsCountByProjectId(projectPool.getProjectId());
				if (count <= 0) {
					projectPool.setUsedFundAmount(BigDecimal.ZERO);
					projectPool.setUsedFundAmountCny(BigDecimal.ZERO);
					projectPool.setRemainFundAmount(projectPool.getProjectAmount());
					projectPool.setRemainFundAmountCny(projectPool.getProjectAmountCny());
					projectPool.setUsedAssetAmount(BigDecimal.ZERO);
					projectPool.setUsedAssetAmountCny(BigDecimal.ZERO);
					projectPool.setRemainAssetAmount(projectPool.getProjectAmount());
					projectPool.setRemainAssetAmountCny(projectPool.getProjectAmountCny());
					projectPoolDao.updateById(projectPool);
				}
			}
		}
	}
}
