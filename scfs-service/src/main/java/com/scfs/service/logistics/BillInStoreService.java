package com.scfs.service.logistics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.testng.collections.Sets;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseAddressDao;
import com.scfs.dao.base.entity.BaseUserSubjectDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.logistics.BillInStoreDao;
import com.scfs.dao.logistics.BillInStoreDtlDao;
import com.scfs.dao.logistics.BillInStoreTallyDtlDao;
import com.scfs.dao.logistics.StlDao;
import com.scfs.dao.po.PurchaseOrderTitleDao;
import com.scfs.dao.sale.BillDeliveryDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.dto.req.BaseUserSubjectReqDto;
import com.scfs.domain.base.entity.BaseAddress;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.base.entity.BaseUserSubject;
import com.scfs.domain.common.MailTemplateTwo;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.fi.dto.req.FundPoolReqDto;
import com.scfs.domain.logistics.dto.req.BillInStoreDtlSearchReqDto;
import com.scfs.domain.logistics.dto.req.BillInStoreSearchReqDto;
import com.scfs.domain.logistics.dto.req.PoOrderReqDto;
import com.scfs.domain.logistics.dto.resp.BillInStoreFileResDto;
import com.scfs.domain.logistics.dto.resp.BillInStoreResDto;
import com.scfs.domain.logistics.dto.resp.PoOrderDtlResDto;
import com.scfs.domain.logistics.entity.BillInStore;
import com.scfs.domain.logistics.entity.BillInStoreDtl;
import com.scfs.domain.logistics.entity.BillInStoreSum;
import com.scfs.domain.logistics.entity.BillInStoreTallyDtl;
import com.scfs.domain.logistics.entity.Stl;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.project.entity.ProjectPoolAsset;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.domain.sale.entity.BillDelivery;
import com.scfs.service.bookkeeping.InStoreBookkeepingService;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.fi.ReceiptPoolAssestService;
import com.scfs.service.project.ProjectPoolService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.FileUploadService;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2016年10月17日.
 */
@Service
public class BillInStoreService {

	@Autowired
	private BillInStoreDao billInStoreDao;
	@Autowired
	private BillInStoreDtlDao billInStoreDtlDao;
	@Autowired
	private BillInStoreTallyDtlDao billInStoreTallyDtlDao;
	@Autowired
	private StlDao stlDao;
	@Autowired
	private PurchaseOrderTitleDao purchaseOrderTitleDao;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private BillOutStoreService billOutStoreService;
	@Autowired
	private InStoreBookkeepingService inStoreBookkeepingService;
	@Autowired
	private BillInStoreDtlService billInStoreDtlService;
	@Autowired
	private BillInStoreTallyDtlService billInStoreTallyDtlService;
	@Autowired
	private ProjectPoolService projectPoolService;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private MsgContentService msgContentService;
	@Autowired
	private AsyncExcelService asyncExcelService;
	@Autowired
	private BillDeliveryDao billDeliveryDao;
	@Autowired
	private BaseUserSubjectDao baseUserSubjectDao;
	@Autowired
	private ReceiptPoolAssestService poolAssestService;
	@Autowired
	private BaseAddressDao baseAddressDao;

	/**
	 * 新增入库单
	 * 
	 * @param billInStore
	 * @return
	 */
	public BillInStore addBillInStore(BillInStore billInStore) {
		billInStore.setBillNo(sequenceService.getNumDateByBusName(BaseConsts.PRE_BILL_IN_STORE,
				SeqConsts.BILL_IN_STORE_NO, BaseConsts.INT_13));
		billInStore.setBillType(BaseConsts.ONE); // 页面新增默认采购入库
		billInStore.setReceiveNum(BigDecimal.ZERO);
		billInStore.setReceiveAmount(BigDecimal.ZERO);
		billInStore.setTallyNum(BigDecimal.ZERO);
		billInStore.setTallyAmount(BigDecimal.ZERO);
		billInStore.setStatus(BaseConsts.ONE); // 待收货
		if (billInStore.getCreator() == null || "".equals(billInStore.getCreator())) {
			billInStore.setCreator(ServiceSupport.getUser().getChineseName());
		}
		if (billInStore.getCreatorId() == null) {
			billInStore.setCreatorId(ServiceSupport.getUser().getId());
		}
		billInStore.setPayAmount(BigDecimal.ZERO);
		int result = billInStoreDao.insert(billInStore);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.BILL_IN_STORE_ADD_ERROR);
		}
		return billInStore;
	}

	/**
	 * 更新入库单
	 * 
	 * @param billInStore
	 * @return
	 */
	public void updateBillInStore(BillInStore billInStore) {
		BillInStore billInStoreRes = billInStoreDao.queryAndLockEntityById(billInStore.getId());
		if (billInStoreRes.getStatus().equals(BaseConsts.ONE) && billInStoreRes.getIsDelete().equals(BaseConsts.ZERO)) { // 1-待提交
			billInStore.setCustomerId(null == billInStore.getCustomerId() ? -1 : billInStore.getCustomerId());
			int result = billInStoreDao.updateById(billInStore);
			if (result <= 0) {
				throw new BaseException(ExcMsgEnum.BILL_IN_STORE_UPDATE_ERROR);
			}
		} else {
			throw new BaseException(ExcMsgEnum.BILL_IN_STORE_UPDATE_ERROR);
		}

	}

	public void updateBillInStoreById(BillInStore billInStore) {
		int result = billInStoreDao.updateById(billInStore);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.BILL_IN_STORE_UPDATE_ERROR);
		}
	}

	/**
	 * 查询入库单列表
	 * 
	 * @param billInStoreSearchReqDto
	 * @return
	 */
	public PageResult<BillInStoreResDto> queryBillInStoreList(BillInStoreSearchReqDto billInStoreSearchReqDto) {
		PageResult<BillInStoreResDto> result = new PageResult<BillInStoreResDto>();

		int offSet = PageUtil.getOffSet(billInStoreSearchReqDto.getPage(), billInStoreSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, billInStoreSearchReqDto.getPer_page());

		billInStoreSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		BaseUserSubjectReqDto baseReq = new BaseUserSubjectReqDto();// 用户下是否有仓库
		baseReq.setUserId(ServiceSupport.getUser().getId());
		List<BaseUserSubject> userSubject = baseUserSubjectDao.queryUserSubjectByCon(baseReq);
		List<BillInStore> billInStoreList = null;
		if (!CollectionUtils.isEmpty(userSubject)) {
			billInStoreSearchReqDto.setUserSubject(userSubject);
			billInStoreList = billInStoreDao.queryResultsByCon(billInStoreSearchReqDto, rowBounds);
		}
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_BILL_IN_STORE);
		List<BillInStoreResDto> billInStoreResDtoList = convertToResDto(billInStoreList, isAllowPerm);
		result.setItems(billInStoreResDtoList);
		String totalStr = querySumBillInStore(billInStoreSearchReqDto, isAllowPerm);
		if (StringUtils.isNotBlank(totalStr)) {
			result.setTotalStr(totalStr);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), billInStoreSearchReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(billInStoreSearchReqDto.getPage());
		result.setPer_page(billInStoreSearchReqDto.getPer_page());

		return result;
	}

	/**
	 * 查询入库单列表(不分页)
	 * 
	 * @param billInStoreSearchReqDto
	 * @return
	 */
	public List<BillInStoreResDto> queryAllBillInStoreList(BillInStoreSearchReqDto billInStoreSearchReqDto) {
		if (null == billInStoreSearchReqDto.getUserId()) {
			billInStoreSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		BaseUserSubjectReqDto baseReq = new BaseUserSubjectReqDto();// 用户下是否有仓库
		baseReq.setUserId(ServiceSupport.getUser().getId());
		List<BaseUserSubject> userSubject = baseUserSubjectDao.queryUserSubjectByCon(baseReq);
		List<BillInStore> billInStoreList = null;
		if (!CollectionUtils.isEmpty(userSubject)) {
			billInStoreSearchReqDto.setUserSubject(userSubject);
			billInStoreList = billInStoreDao.queryResultsByCon(billInStoreSearchReqDto);
		}
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_BILL_IN_STORE,
				billInStoreSearchReqDto.getUserId());
		List<BillInStoreResDto> billInStoreResDtoList = convertToResDto(billInStoreList, isAllowPerm);
		return billInStoreResDtoList;
	}

	/**
	 * 根据ID查询入库单详情
	 * 
	 * @param billInStoreSearchReqDto
	 * @return
	 */
	public Result<BillInStoreResDto> queryBillInStoreById(BillInStoreSearchReqDto billInStoreSearchReqDto) {
		Result<BillInStoreResDto> result = new Result<BillInStoreResDto>();
		BillInStore billInStoreRes = billInStoreDao.queryEntityById(billInStoreSearchReqDto);
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_BILL_IN_STORE);
		BillInStoreResDto billInStoreResDto = convertToResDto(billInStoreRes, isAllowPerm);
		result.setItems(billInStoreResDto);
		return result;
	}

	/**
	 * 提交入库单
	 * 
	 * @param billInStore
	 * @return
	 */
	public void submitBillInStore(BillInStore billInStore, Date acceptTime) {
		// 检查是否存在入库明细和理货明细、收货数量是否等于理货数量、订单入库数量是否超过订单的销售数量
		validateSubmit(billInStore);

		billInStore = billInStoreDao.queryAndLockEntityById(billInStore.getId());
		if (billInStore.getStatus().equals(BaseConsts.ONE) && billInStore.getIsDelete().equals(BaseConsts.ZERO)) { // 待提交且未删除
			/**
			 * if (billInStore.getWmsStatus().equals(BaseConsts.ONE)) {
			 * //已调用wms入库单接口 throw new
			 * BaseException(ExcMsgEnum.BILL_IN_STORE_WMS_HAS_INVOKE); }
			 **/
			billInStore.setAcceptorId(ServiceSupport.getUser().getId());
			billInStore.setAcceptor(ServiceSupport.getUser().getChineseName());
			billInStore.setAcceptTime(acceptTime);
			billInStore.setStatus(BaseConsts.TWO);
		} else {
			throw new BaseException(ExcMsgEnum.BILL_IN_STORE_SUBMIT_ERROR);
		}

		// 更新明细和理货明细的入库时间
		BillInStoreSearchReqDto billInStoreSearchReqDto = new BillInStoreSearchReqDto();
		billInStoreSearchReqDto.setId(billInStore.getId());
		BillInStore billInStoreRes = billInStoreDao.queryEntityById(billInStoreSearchReqDto);
		Integer billType = billInStoreRes.getBillType();

		BillInStoreDtl billInStoreDtl = new BillInStoreDtl();
		billInStoreDtl.setAcceptTime(acceptTime);
		billInStoreDtl.setBillInStoreId(billInStore.getId());
		if (billType.equals(BaseConsts.ONE)) { // 类型：采购入库
			billInStoreDtl.setOriginAcceptTime(acceptTime);
		}
		billInStoreDtlDao.updateAcceptTime(billInStoreDtl);
		BillInStoreTallyDtl billInStoreTallyDtl = new BillInStoreTallyDtl();
		billInStoreTallyDtl.setAcceptTime(acceptTime);
		billInStoreTallyDtl.setBillInStoreId(billInStore.getId());
		if (billType.equals(BaseConsts.ONE)) { // 类型：采购入库
			billInStoreTallyDtl.setOriginAcceptTime(acceptTime);
		}
		billInStoreTallyDtlDao.updateAcceptTime(billInStoreTallyDtl);

		if (billType.equals(BaseConsts.TWO)) { // 类型：调拨入库
			Integer billOutStoreId = billInStoreRes.getBillOutStoreId();
			BaseSubject onwayWarehouse = billOutStoreService.getOnwayWarehouse();
			if (null != billOutStoreId && null != onwayWarehouse) {
				// 根据出库单ID和在途仓库ID查询在途仓的入库单(一般只有一个在途仓)
				BillInStoreSearchReqDto billInStoreSearchReqDto2 = new BillInStoreSearchReqDto();
				billInStoreSearchReqDto2.setBillOutStoreId(billOutStoreId);
				billInStoreSearchReqDto2.setWarehouseId(onwayWarehouse.getId());
				billInStoreSearchReqDto2.setIsDelete(BaseConsts.ZERO); // 有效入库单
				List<BillInStore> onwaybillInStoreList = billInStoreDao.selectList(billInStoreSearchReqDto2);
				for (BillInStore onwayBillInStore : onwaybillInStoreList) {
					// 作废在途仓的入库单
					onwayBillInStore.setDeleterId(ServiceSupport.getUser().getId());
					onwayBillInStore.setDeleter(ServiceSupport.getUser().getChineseName());
					onwayBillInStore.setDeleteAt(new Date());
					onwayBillInStore.setIsDelete(BaseConsts.ONE);
					billInStoreDao.updateById(onwayBillInStore);
					// 扣减在途仓库存
					subtractStl(onwayBillInStore);
				}
			} else {
				throw new BaseException(ExcMsgEnum.ONWAY_WAREHOUSE_NOT_EXIST);
			}
		} else if (billType.equals(BaseConsts.ONE)) {// 类型：采购入库
			createProjectPool(billInStore);
		}
		// 添加库存
		addStl(billInStoreRes);

		// 最后更新入库单
		int result = billInStoreDao.updateById(billInStore);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.BILL_IN_STORE_UPDATE_ERROR);
		}
		// 回写销售退货单的状态为已收货
		if (billType.equals(BaseConsts.THREE)) { // 3-销售退货
			BillDelivery billDelivery = new BillDelivery();
			billDelivery.setId(billInStore.getBillDeliveryId());
			billDelivery.setStatus(BaseConsts.FIVE);
			billDeliveryDao.updateById(billDelivery);
		}

		// 采购入库记账
		if (billInStore.getBillType() == BaseConsts.ONE) {
			/**
			 * BaseProject baseProject =
			 * baseProjectDao.queryEntityById(billInStore.getProjectId()); if
			 * (baseProject.getBizType() != BaseConsts.SIX) {// 融通质押项目不需要记账
			 * inStoreBookkeepingService.inStoreBookkeeping(billInStore.getId())
			 * ; }
			 **/
			inStoreBookkeepingService.inStoreBookkeeping(billInStore.getId());
			// 发送邮件
			if (billInStore.getAffiliateNo() != null && billInStore.getAffiliateNo().startsWith("QK")) {
				// QK开头的（保理单据）不发邮件提醒
			} else {
				sendWarnEmail(billInStore);
			}
		}
		// 销售退货记账
		if (billType == BaseConsts.THREE) {
			// 1.和销售出库 记账一样，金额为负数
			inStoreBookkeepingService.outStoreBookkeeping(billInStore.getId());
			// 4.融资池记账 和 销售出库融资池记账 记账一样，金额为负数
			createSaleReturnProjectPool(billInStore);
		}
		if (billType == BaseConsts.ONE) { // 采购入库类型的进行入池操作
			FundPoolReqDto poolReqDto = new FundPoolReqDto();
			poolReqDto.setId(billInStore.getId());
			poolAssestService.createPoolAssestIn(poolReqDto);
		}

	}

	public void createProjectPool(BillInStore billInStore) {
		ProjectPoolAsset ppf = new ProjectPoolAsset();
		ppf.setType(BaseConsts.ONE);
		ppf.setBillNo(billInStore.getBillNo());
		ppf.setBillSource(BaseConsts.ONE);
		ppf.setProjectId(billInStore.getProjectId());
		ppf.setSupplierId(billInStore.getSupplierId());
		ppf.setBusinessDate(billInStore.getAcceptTime());
		ppf.setBillAmount(billInStore.getPayAmount());
		// ppf.setBillAmount(billInStore.getReceiveAmount());
		ppf.setBillCurrencyType(billInStore.getCurrencyType());
		projectPoolService.addProjectPoolAsset(ppf, BaseConsts.ONE); // 1-入库单
		projectPoolService.updateProjectPoolInfo(ppf.getProjectId());
	}

	public void createSaleReturnProjectPool(BillInStore billInStore) {
		ProjectPoolAsset ppf = new ProjectPoolAsset();
		ppf.setType(BaseConsts.ONE);
		ppf.setBillNo(billInStore.getBillNo());
		ppf.setBillSource(BaseConsts.ONE);
		ppf.setProjectId(billInStore.getProjectId());
		ppf.setCustomerId(billInStore.getCustomerId());
		ppf.setBusinessDate(billInStore.getCreateAt());
		ppf.setBillAmount(billInStore.getPayAmount().multiply(new BigDecimal("-1")));
		// ppf.setBillAmount(billInStore.getReceiveAmount().multiply(new
		// BigDecimal("-1")));
		ppf.setBillCurrencyType(billInStore.getCurrencyType());
		projectPoolService.addProjectPoolAsset(ppf, BaseConsts.ONE);
		projectPoolService.updateProjectPoolInfo(ppf.getProjectId());
	}

	private void sendWarnEmail(BillInStore billInStore) {
		String[] tableTitle = { "订单编号", "商品编码", "商品描述", "数量", "金额" };
		BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto = new BillInStoreDtlSearchReqDto();
		billInStoreDtlSearchReqDto.setBillInStoreId(billInStore.getId());
		List<BillInStoreDtl> billInStoreDtlList = billInStoreDtlDao.queryResultsByCon(billInStoreDtlSearchReqDto);
		List<List<MailTemplateTwo>> lists = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(billInStoreDtlList)) {
			for (BillInStoreDtl billInStoreDtl : billInStoreDtlList) {
				List<MailTemplateTwo> columns = Lists.newArrayList();
				MailTemplateTwo mailTemplateTwo = new MailTemplateTwo();
				mailTemplateTwo.setContent(billInStoreDtl.getOrderNo());
				columns.add(mailTemplateTwo);

				BaseGoods baseGoods = cacheService.getGoodsById(billInStoreDtl.getGoodsId());

				String goodsNumber = "";
				String goodsName = "";
				String goodsBarCode = "";
				String goodsType = "";
				String goodsUnit = "";
				if (null != baseGoods) {
					goodsNumber = baseGoods.getNumber();
					goodsName = baseGoods.getName();
					goodsBarCode = baseGoods.getBarCode();
					goodsType = baseGoods.getType();
					goodsUnit = baseGoods.getUnit();
				}
				mailTemplateTwo = new MailTemplateTwo();
				mailTemplateTwo.setContent(goodsNumber);
				columns.add(mailTemplateTwo);

				mailTemplateTwo = new MailTemplateTwo();
				mailTemplateTwo.setContent("商品名称：" + goodsName + "；商品条码：" + goodsBarCode + "；商品型号：" + goodsType);
				columns.add(mailTemplateTwo);

				mailTemplateTwo = new MailTemplateTwo();
				mailTemplateTwo.setContent(
						DecimalUtil.toQuantityString(billInStoreDtl.getReceiveNum()) + "(" + goodsUnit + ")");
				columns.add(mailTemplateTwo);

				mailTemplateTwo = new MailTemplateTwo();
				mailTemplateTwo.setContent(DecimalUtil.toAmountString(billInStoreDtl.getReceiveAmount()));
				columns.add(mailTemplateTwo);
				lists.add(columns);
			}
		}

		StringBuilder bottom = new StringBuilder();
		MailTemplateTwo mailTemplateTwo = new MailTemplateTwo();
		bottom.append("<tr>");
		bottom.append(" <td style='text-align: right" + ";color:" + mailTemplateTwo.getColor() + ";font-size:"
				+ mailTemplateTwo.getFontSize() + ";' colspan='3'>合计：</td>");

		bottom.append(" <td style='text-align: " + mailTemplateTwo.getAlign() + ";color:" + mailTemplateTwo.getColor()
				+ ";font-size:" + mailTemplateTwo.getFontSize() + ";' colspan='1'>"
				+ DecimalUtil.toQuantityString(billInStore.getReceiveNum()) + "</td>");

		bottom.append(" <td style='text-align: " + mailTemplateTwo.getAlign() + ";color:" + mailTemplateTwo.getColor()
				+ ";font-size:" + mailTemplateTwo.getFontSize() + ";' colspan='1'>"
				+ DecimalUtil.toAmountString(billInStore.getReceiveAmount()) + " "
				+ ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
						Integer.toString(billInStore.getCurrencyType()))
				+ "</td>");

		bottom.append("</tr>");
		String content = msgContentService.convertMailTwoContent("采购到货提醒【单号：" + billInStore.getBillNo() + "】",
				tableTitle, lists, bottom.toString());

		String emailStr = "";
		Set<String> emailList = Sets.newHashSet();
		BaseUser baseUser = ServiceSupport.getBusDepartUser(billInStore.getProjectId());
		if (null != baseUser) {
			String email = baseUser.getEmail();
			if (StringUtils.isNotBlank(email)) {
				emailList.add(email);
			}
		}
		baseUser = ServiceSupport.getBusDepartUser(billInStore.getProjectId());
		if (null != baseUser) {
			String email = baseUser.getEmail();
			if (StringUtils.isNotBlank(email)) {
				emailList.add(email);
			}
		}
		baseUser = ServiceSupport.getFinanceDepartUser(billInStore.getProjectId());
		if (null != baseUser) {
			String email = baseUser.getEmail();
			if (StringUtils.isNotBlank(email)) {
				emailList.add(email);
			}
		}
		baseUser = ServiceSupport.getFinanceSpecialDepartUser(billInStore.getProjectId());
		if (null != baseUser) {
			String email = baseUser.getEmail();
			if (StringUtils.isNotBlank(email)) {
				emailList.add(email);
			}
		}
		baseUser = ServiceSupport.getOfficalUser(billInStore.getProjectId());
		if (null != baseUser) {
			String email = baseUser.getEmail();
			if (StringUtils.isNotBlank(email)) {
				emailList.add(email);
			}
		}
		if (!CollectionUtils.isEmpty(emailList)) {
			emailStr = StringUtils.join(emailList, ",");
		}
		if (StringUtils.isNotBlank(emailStr)) {
			msgContentService.addMsgContents(emailStr,
					"采购到货提醒【" + DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, billInStore.getAcceptTime())
							+ "；项目：" + cacheService.showProjectNameById(billInStore.getProjectId()) + "】",
					content, BaseConsts.TWO);
		}
	}

	/**
	 * 添加库存
	 */
	private void addStl(BillInStore billInStore) {
		if (null != billInStore) {
			BillInStoreTallyDtl billInStoreTallyDtl = new BillInStoreTallyDtl();
			billInStoreTallyDtl.setBillInStoreId(billInStore.getId());
			List<BillInStoreTallyDtl> billInStoreTallyDtlList = billInStoreTallyDtlDao
					.queryResultsByCon(billInStoreTallyDtl);
			if (!CollectionUtils.isEmpty(billInStoreTallyDtlList)) {
				for (BillInStoreTallyDtl tallyDtl : billInStoreTallyDtlList) {
					Stl stl = createStl(billInStore, tallyDtl);
					stlDao.insert(stl);
				}
			}
		}
	}

	/**
	 * 创建库存对象
	 * 
	 * @param billInStore
	 * @param billInStoreTallyDtl
	 * @return
	 */
	public Stl createStl(BillInStore billInStore, BillInStoreTallyDtl billInStoreTallyDtl) {
		Stl stl = new Stl();
		BeanUtils.copyProperties(billInStoreTallyDtl, stl);
		stl.setId(null);
		stl.setProjectId(billInStore.getProjectId());
		stl.setWarehouseId(billInStore.getWarehouseId());
		stl.setFlyOrderFlag(billInStore.getFlyOrderFlag());

		stl.setBillInStoreDtlTallyId(billInStoreTallyDtl.getId());
		stl.setInStoreNum(billInStoreTallyDtl.getTallyNum());
		stl.setStoreNum(billInStoreTallyDtl.getTallyNum());
		stl.setLockNum(BigDecimal.ZERO);
		stl.setSaleLockNum(BigDecimal.ZERO);
		stl.setCostPrice(billInStoreTallyDtl.getCostPrice());
		stl.setPoPrice(billInStoreTallyDtl.getPoPrice());
		stl.setCurrencyType(billInStoreTallyDtl.getCurrencyType());
		stl.setReceiveDate(billInStoreTallyDtl.getReceiveDate());
		stl.setCustomerId(billInStoreTallyDtl.getCustomerId());
		stl.setSupplierId(billInStoreTallyDtl.getSupplierId());
		stl.setOriginAcceptTime(billInStoreTallyDtl.getOriginAcceptTime());

		stl.setBillInStoreNo(billInStore.getBillNo());
		stl.setAffiliateNo(billInStore.getAffiliateNo());
		if (billInStore.getBillType().equals(BaseConsts.ONE) || billInStore.getBillType().equals(BaseConsts.TWO)) {
			if (null != billInStoreTallyDtl.getPoId()) {
				PurchaseOrderTitle purchaseOrderTitle = purchaseOrderTitleDao
						.queryEntityById(billInStoreTallyDtl.getPoId());
				if (null != purchaseOrderTitle) {
					stl.setOrderNo(purchaseOrderTitle.getOrderNo());
					stl.setAppendNo(purchaseOrderTitle.getAppendNo());
				}
			}
		} else if (billInStore.getBillType().equals(BaseConsts.THREE)) { // 3-销售退货
			if (null != billInStoreTallyDtl.getBillDeliveryId()) {
				BillDelivery billDelivery = new BillDelivery();
				billDelivery.setId(billInStoreTallyDtl.getBillDeliveryId());
				billDelivery = billDeliveryDao.queryEntityById(billDelivery);
				if (null != billDelivery) {
					stl.setOrderNo(billDelivery.getBillNo());
					stl.setAppendNo(billDelivery.getAffiliateNo());
				}
			}
		}
		stl.setPayPrice(billInStoreTallyDtl.getPayPrice());
		stl.setPayRate(billInStoreTallyDtl.getPayRate() == null ? BigDecimal.ZERO : billInStoreTallyDtl.getPayRate());
		stl.setPayRealCurrency(billInStoreTallyDtl.getPayRealCurrency());
		stl.setPayTime(billInStoreTallyDtl.getPayTime());
		return stl;
	}

	/**
	 * 扣减库存
	 */
	private void subtractStl(BillInStore billInStore) {
		if (null != billInStore) {
			BillInStoreTallyDtl billInStoreTallyDtl = new BillInStoreTallyDtl();
			billInStoreTallyDtl.setBillInStoreId(billInStore.getId());
			List<BillInStoreTallyDtl> billInStoreTallyDtlList = billInStoreTallyDtlDao
					.queryResultsByCon(billInStoreTallyDtl);
			if (!CollectionUtils.isEmpty(billInStoreTallyDtlList)) {
				for (BillInStoreTallyDtl tallyDtl : billInStoreTallyDtlList) {
					BigDecimal tallyNum = tallyDtl.getTallyNum();
					Stl stl = stlDao.queryStlByBillInStoreTallyDtlId(tallyDtl.getId());
					stl.setStoreNum(DecimalUtil.subtract(stl.getStoreNum(), tallyNum));
					stlDao.updateById(stl);
				}
			}
		}
	}

	/**
	 * 批量删除入库单
	 * 
	 * @param ids
	 * @return
	 */
	public void deleteBillInStoreByIds(List<Integer> ids) {
		for (Integer id : ids) {
			BillInStore billInStore = billInStoreDao.queryAndLockEntityById(id);
			if (billInStore.getStatus().equals(BaseConsts.ONE) && billInStore.getIsDelete().equals(BaseConsts.ZERO)) { // 待提交且未删除
				billInStore.setDeleterId(ServiceSupport.getUser().getId());
				billInStore.setDeleter(ServiceSupport.getUser().getChineseName());
				billInStore.setDeleteAt(new Date());
				billInStore.setIsDelete(BaseConsts.ONE);
			} else {
				throw new BaseException(ExcMsgEnum.BILL_IN_STORE_DELETE_ERROR);
			}
			if (billInStore.getBillType().equals(BaseConsts.ONE)) {
				// 还原订单入库数量
				releasePoWarehouseNum(billInStore);
			}

			// 最后更新入库单
			int result = billInStoreDao.updateById(billInStore);
			if (result <= 0) {
				throw new BaseException(ExcMsgEnum.BILL_IN_STORE_UPDATE_ERROR);
			}
		}
	}

	/**
	 * 查询可入库的订单
	 * 
	 * @param poOrderReqDto
	 * @return
	 */
	public PageResult<PoOrderDtlResDto> queryPoOrderDtlList(PoOrderReqDto poOrderReqDto) {
		PageResult<PoOrderDtlResDto> result = new PageResult<PoOrderDtlResDto>();
		if (null != poOrderReqDto.getBillInStoreId()) {
			BillInStoreSearchReqDto billInStoreSearchReqDto = new BillInStoreSearchReqDto();
			billInStoreSearchReqDto.setId(poOrderReqDto.getBillInStoreId());
			BillInStore billInStore = billInStoreDao.queryEntityById(billInStoreSearchReqDto);
			poOrderReqDto.setProjectId(billInStore.getProjectId());
			poOrderReqDto.setWarehouseId(billInStore.getWarehouseId());
			poOrderReqDto.setSupplierId(billInStore.getSupplierId());
			poOrderReqDto.setCurrencyId(billInStore.getCurrencyType());
			if (null != billInStore.getCustomerId()) {
				poOrderReqDto.setCustomerId(billInStore.getCustomerId());
				poOrderReqDto.setIsCustomerNullFlag(BaseConsts.ZERO);
			} else {
				poOrderReqDto.setIsCustomerNullFlag(BaseConsts.ONE);
			}

			int offSet = PageUtil.getOffSet(poOrderReqDto.getPage(), poOrderReqDto.getPer_page());
			RowBounds rowBounds = new RowBounds(offSet, poOrderReqDto.getPer_page());
			List<PoOrderDtlResDto> poOrderDtlResDtoList = billInStoreDtlDao.queryPoOrderDtlResults(poOrderReqDto,
					rowBounds);
			if (!CollectionUtils.isEmpty(poOrderDtlResDtoList)) {
				for (PoOrderDtlResDto poOrderDtlResDto : poOrderDtlResDtoList) {
					if (poOrderDtlResDto.getGoodsId() != null) {
						BaseGoods baseGoods = cacheService.getGoodsById(poOrderDtlResDto.getGoodsId());
						if (null != baseGoods) {
							poOrderDtlResDto.setGoodsName(baseGoods.getName());
							poOrderDtlResDto.setGoodsNumber(baseGoods.getNumber());
							poOrderDtlResDto.setGoodsType(baseGoods.getType());
							poOrderDtlResDto.setGoodsBarCode(baseGoods.getBarCode());
						}
					}
					if (poOrderDtlResDto.getCurrencyId() != null) {
						poOrderDtlResDto
								.setCurrencyName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
										Integer.toString(poOrderDtlResDto.getCurrencyId())));
					}
				}
			}
			result.setItems(poOrderDtlResDtoList);
			int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), poOrderReqDto.getPer_page());
			result.setLast_page(totalPage);
			result.setTotal(CountHelper.getTotalRow());
			result.setCurrent_page(poOrderReqDto.getPage());
			result.setPer_page(poOrderReqDto.getPer_page());
		}
		return result;
	}

	/**
	 * 更新打印次数
	 */
	public void updatePrintNum(Integer id) {
		BillInStore billInStore = billInStoreDao.queryAndLockEntityById(id);
		if (null != billInStore) {
			billInStore.setPrintNum(billInStore.getPrintNum() + 1);
			billInStoreDao.updateById(billInStore);
		}
	}

	/**
	 * 查询合计
	 * 
	 * @param billInStoreSearchReqDto
	 * @param isAllowPerm
	 * @return
	 */
	private String querySumBillInStore(BillInStoreSearchReqDto billInStoreSearchReqDto, boolean isAllowPerm) {
		String totalStr = "";
		if (billInStoreSearchReqDto.getNeedSum() != null && billInStoreSearchReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<BillInStoreSum> billInStoreSumList = billInStoreDao.querySumBillInStore(billInStoreSearchReqDto);
			if (!CollectionUtils.isEmpty(billInStoreSumList)) {
				BigDecimal totalReceiveNum = BigDecimal.ZERO;
				BigDecimal totalTallyNum = BigDecimal.ZERO;
				BigDecimal totalReceiveAmount = BigDecimal.ZERO;
				String totalReceiveAmountStr = "";

				for (BillInStoreSum billInStoreSum : billInStoreSumList) {
					totalReceiveNum = DecimalUtil.add(totalReceiveNum, null == billInStoreSum.getTotalReceiveNum()
							? BigDecimal.ZERO : billInStoreSum.getTotalReceiveNum());
					totalTallyNum = DecimalUtil.add(totalTallyNum, null == billInStoreSum.getTotalTallyNum()
							? BigDecimal.ZERO : billInStoreSum.getTotalTallyNum());
					BigDecimal cnyTotalReceiveAmount = BigDecimal.ZERO;
					if (null != billInStoreSum.getCurrencyType()) {
						cnyTotalReceiveAmount = ServiceSupport.amountNewToRMB(
								null == billInStoreSum.getTotalReceiveAmount() ? BigDecimal.ZERO
										: billInStoreSum.getTotalReceiveAmount(),
								billInStoreSum.getCurrencyType(), null);
					}
					totalReceiveAmount = DecimalUtil.add(totalReceiveAmount,
							null == cnyTotalReceiveAmount ? BigDecimal.ZERO : cnyTotalReceiveAmount);
				}
				if (isAllowPerm == true) {
					totalReceiveAmountStr = BaseConsts.NO_PERMISSION_HIT;
				} else {
					totalReceiveAmountStr = DecimalUtil.toAmountString(totalReceiveAmount);
				}
				totalStr = "收货数量：" + DecimalUtil.toQuantityString(totalReceiveNum) + "；理货数量："
						+ DecimalUtil.toQuantityString(totalTallyNum) + "；收货金额：" + totalReceiveAmountStr
						+ BaseConsts.STRING_BLANK_SPACE + BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE);
			}
		}
		return totalStr;
	}

	private List<BillInStoreResDto> convertToResDto(List<BillInStore> billInStoreList, boolean isAllowPerm) {
		List<BillInStoreResDto> billInStoreResDtoList = new ArrayList<BillInStoreResDto>(5);
		if (CollectionUtils.isEmpty(billInStoreList)) {
			return billInStoreResDtoList;
		}
		for (BillInStore billInStore : billInStoreList) {
			BillInStoreResDto billInStoreResDto = convertToResDto(billInStore, isAllowPerm);
			billInStoreResDto.setOpertaList(
					getOperList(billInStore.getStatus(), billInStore.getBillType(), billInStore.getWarehouseId()));

			billInStoreResDtoList.add(billInStoreResDto);
		}
		return billInStoreResDtoList;
	}

	private BillInStoreResDto convertToResDto(BillInStore billInStore, boolean isAllowPerm) {
		BillInStoreResDto billInStoreResDto = new BillInStoreResDto();
		if (null != billInStore) {
			BeanUtils.copyProperties(billInStore, billInStoreResDto);
			if (billInStore.getStatus() != null) {
				billInStoreResDto.setStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_IN_STORE_STATUS,
						Integer.toString(billInStore.getStatus())));
			}
			if (billInStore.getBillType() != null) {
				billInStoreResDto.setBillTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_IN_STORE_TYPE,
						Integer.toString(billInStore.getBillType())));
			}
			if (billInStore.getCurrencyType() != null) {
				billInStoreResDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.DEFAULT_CURRENCY_TYPE, Integer.toString(billInStore.getCurrencyType())));
			}

			billInStoreResDto.setProjectName(cacheService.showProjectNameById(billInStoreResDto.getProjectId()));
			billInStoreResDto.setSupplierName(
					cacheService.showSubjectNameByIdAndKey(billInStoreResDto.getSupplierId(), CacheKeyConsts.SUPPLIER));
			billInStoreResDto.setWarehouseName(cacheService
					.showSubjectNameByIdAndKey(billInStoreResDto.getWarehouseId(), CacheKeyConsts.WAREHOUSE));
			billInStoreResDto.setCustomerName(
					cacheService.showSubjectNameByIdAndKey(billInStoreResDto.getCustomerId(), CacheKeyConsts.CUSTOMER));
			billInStoreResDto.setFullSupplierName(
					cacheService.getBaseSubjectById(billInStoreResDto.getSupplierId()).getChineseName());
			if (null != billInStoreResDto.getWarehouseId()) {
				List<BaseAddress> baseAddresses = baseAddressDao
						.queryResultsyBySubjectId(billInStoreResDto.getWarehouseId());
				if (!CollectionUtils.isEmpty(baseAddresses)) {
					billInStoreResDto.setWarehouseAddressName(
							cacheService.getAddressById(baseAddresses.get(0).getId()).getShowValue());
				}
			}
			BaseProject baseProject = cacheService.getProjectById(billInStoreResDto.getProjectId());
			if (null != baseProject) {
				BaseSubject busiUnit = cacheService.getBusiUnitById(baseProject.getBusinessUnitId());
				if (null != busiUnit) {
					billInStoreResDto.setBusinessUnitName(busiUnit.getChineseName());
					billInStoreResDto.setBusinessUnitAddress(busiUnit.getOfficeAddress());
					billInStoreResDto.setBusinessUnitRegPhone(busiUnit.getRegPhone());
				}
			}

			BigDecimal receiveAmount = (null == billInStoreResDto.getReceiveAmount() ? BigDecimal.ZERO
					: billInStoreResDto.getReceiveAmount());
			BigDecimal tallyAmount = (null == billInStoreResDto.getTallyAmount() ? BigDecimal.ZERO
					: billInStoreResDto.getTallyAmount());
			billInStoreResDto.setReceiveAmountStr(DecimalUtil.toAmountString(receiveAmount));
			billInStoreResDto.setTallyAmountStr(DecimalUtil.toAmountString(tallyAmount));
			if (isAllowPerm) { // 不显示金额权限
				billInStoreResDto.setCurrencyTypeName(BaseConsts.NO_PERMISSION_HIT);
				billInStoreResDto.setReceiveAmountStr(BaseConsts.NO_PERMISSION_HIT);
				billInStoreResDto.setTallyAmountStr(BaseConsts.NO_PERMISSION_HIT);
			}
		}
		return billInStoreResDto;
	}

	private List<CodeValue> getOperList(Integer state, Integer billType, Integer warehouseId) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state, billType, warehouseId);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				BillInStoreResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getOperListByState(Integer state, Integer billType, Integer warehouseId) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList(5);

		BaseUserSubjectReqDto baseReq = new BaseUserSubjectReqDto();// 用户下是否有仓库
		baseReq.setUserId(ServiceSupport.getUser().getId());
		List<BaseUserSubject> userSubject = baseUserSubjectDao.queryUserSubjectByCon(baseReq);

		switch (state) {
		// 状态, 1表示待收货 2-已收货
		case BaseConsts.ONE:
			if (billType.equals(BaseConsts.TWO) || billType.equals(BaseConsts.THREE)) { // 2-调拨入库、3-销售退货
				opertaList.add(OperateConsts.DETAIL);
				opertaList.add(OperateConsts.EDIT);
				if (!CollectionUtils.isEmpty(userSubject)) {
					for (BaseUserSubject baseUserSubject : userSubject) {
						if (baseUserSubject.getSubjectId().equals(warehouseId)
								&& baseUserSubject.getOperater().equals(BaseConsts.ONE)) {
							opertaList.add(OperateConsts.SUBMIT);
						}
					}
				}
				if (billType.equals(BaseConsts.THREE)) {
					opertaList.add(OperateConsts.REJECT);
				}
			} else { // 1-采购入库
				opertaList.add(OperateConsts.DETAIL);
				opertaList.add(OperateConsts.EDIT);
				if (!CollectionUtils.isEmpty(userSubject)) {
					for (BaseUserSubject baseUserSubject : userSubject) {
						if (baseUserSubject.getSubjectId().equals(warehouseId)
								&& baseUserSubject.getOperater().equals(BaseConsts.ONE)) {
							opertaList.add(OperateConsts.SUBMIT);
						}
					}
				}
				opertaList.add(OperateConsts.DELETE);
			}
			break;
		case BaseConsts.TWO:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT);
			break;
		}
		return opertaList;
	}

	/**
	 * 还原订单入库数量
	 * 
	 * @param billInStore
	 * @return
	 */
	private void releasePoWarehouseNum(BillInStore billInStore) {
		BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto = new BillInStoreDtlSearchReqDto();
		billInStoreDtlSearchReqDto.setBillInStoreId(billInStore.getId());
		List<BillInStoreDtl> billInStoreDtls = billInStoreDtlDao.selectList(billInStoreDtlSearchReqDto);
		if (!CollectionUtils.isEmpty(billInStoreDtls)) {
			for (BillInStoreDtl billInStoreDtl : billInStoreDtls) {
				billInStoreDtlService.updatePoWarehouseNum(billInStoreDtl, billInStoreDtl, null, BaseConsts.THREE);
			}
		}
	}

	/**
	 * 验证入库单，检查是否存在入库明细和理货明细、收货数量是否等于理货数量、订单入库数量是否超过订单的销售数量等
	 * 
	 * @param billInStore
	 * @return
	 */
	private void validateSubmit(BillInStore billInStore) {
		BillInStoreSearchReqDto billInStoreSearchReqDto = new BillInStoreSearchReqDto();
		billInStoreSearchReqDto.setId(billInStore.getId());
		BillInStore billInStoreRes = billInStoreDao.queryEntityById(billInStoreSearchReqDto);
		/**
		 * if (billInStoreRes.getBillType().equals(BaseConsts.THREE)) { //3-销售退货
		 * Integer billDeliveryId = billInStoreRes.getBillDeliveryId(); if (null
		 * == billDeliveryId) { throw new
		 * BaseException(ExcMsgEnum.ERROR_GENERAL, "对应销售退货单不存在，请联系管理员！"); } else
		 * { BillDelivery billDelivery =
		 * billDeliveryDao.queryAndLockEntityById(billDeliveryId); if
		 * (billDelivery.getStatus().equals(BaseConsts.INT_25)) { throw new
		 * BaseException(ExcMsgEnum.ERROR_GENERAL, "对应销售退货单【" +
		 * billDelivery.getBillNo() + "】待财务专员审核，请稍后提交！"); } else if
		 * (billDelivery.getStatus().equals(BaseConsts.INT_30)) { throw new
		 * BaseException(ExcMsgEnum.ERROR_GENERAL, "对应销售退货单【" +
		 * billDelivery.getBillNo() + "】待财务主管审核，请稍后提交！"); } } }
		 **/

		int dtlsCount = billInStoreDao.queryDtlsCount(billInStore);
		if (dtlsCount <= 0) {
			throw new BaseException(ExcMsgEnum.BILL_IN_STORE_DTL_NOT_ADD);
		}
		int tallyDtlsCount = billInStoreDao.queryTallyDtlsCount(billInStore);
		if (tallyDtlsCount <= 0) {
			throw new BaseException(ExcMsgEnum.BILL_IN_STORE_TALLY_DTL_NOT_ADD);
		}

		BigDecimal receiveNum = (null == billInStoreRes.getReceiveNum() ? BigDecimal.ZERO
				: billInStoreRes.getReceiveNum());
		BigDecimal tallyNum = (null == billInStoreRes.getTallyNum() ? BigDecimal.ZERO : billInStoreRes.getTallyNum());
		if (DecimalUtil.ne(receiveNum, tallyNum)) {
			throw new BaseException(ExcMsgEnum.RECEIVE_NUM_NOT_EQUAL_TALLY_NUM);
		}

		BillInStore inStore = billInStoreDao.queryDtlsTotalInfo(billInStore);
		BillInStore tallyInStore = billInStoreDao.queryTallyDtlsTotalInfo(billInStore);
		if (null != inStore) {
			if (DecimalUtil.ne(receiveNum, inStore.getReceiveNum())) {
				throw new BaseException(ExcMsgEnum.TOTAL_RECEIVE_NUM_NOT_EQUAL);
			}
		}
		if (null != inStore && null != tallyInStore) {
			if (DecimalUtil.ne(tallyNum, inStore.getTallyNum())
					|| DecimalUtil.ne(receiveNum, tallyInStore.getTallyNum())) {
				throw new BaseException(ExcMsgEnum.TOTAL_TALLY_NUM_NOT_EQUAL);
			}
		}
	}

	public BillInStore autoReceive(PurchaseOrderTitle purchaseOrderTitle) {
		PoOrderReqDto poOrderReqDto = new PoOrderReqDto();
		poOrderReqDto.setPoId(purchaseOrderTitle.getId());
		List<PoOrderDtlResDto> poOrderDtlResDtoList = billInStoreDtlDao.queryPoOrderDtlResults(poOrderReqDto);
		if (CollectionUtils.isEmpty(poOrderDtlResDtoList)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购订单【" + purchaseOrderTitle.getOrderNo() + "】无可收货明细");
		}

		BillInStore billInStore = new BillInStore();
		billInStore.setProjectId(purchaseOrderTitle.getProjectId());
		billInStore.setAffiliateNo(purchaseOrderTitle.getAppendNo());
		billInStore.setSupplierId(purchaseOrderTitle.getSupplierId());
		billInStore.setCustomerId(purchaseOrderTitle.getCustomerId());
		billInStore.setWarehouseId(purchaseOrderTitle.getWarehouseId());
		billInStore.setReceiveDate(
				purchaseOrderTitle.getPerdictTime() == null ? new Date() : purchaseOrderTitle.getPerdictTime());
		billInStore.setCurrencyType(purchaseOrderTitle.getCurrencyId());
		billInStore.setRemark(purchaseOrderTitle.getRemark());

		BaseProject baseProject = cacheService.getProjectById(purchaseOrderTitle.getProjectId());// 获取商务信息
		billInStore.setCreator(ServiceSupport.getUser() == null ? BaseConsts.SYSTEM_ROLE_NAME
				: cacheService.getUserByid(baseProject.getBusinessManagerId()).getChineseName());
		billInStore.setCreatorId(baseProject.getBusinessManagerId());
		billInStore.setFlyOrderFlag(purchaseOrderTitle.getFlyOrderFlag());

		// 1.生成入库单
		addBillInStore(billInStore);
		// 2.生成入库明细
		billInStoreDtlService.addBillInStoreDtlsByPoLine(billInStore, purchaseOrderTitle, poOrderDtlResDtoList);
		// 3.整单自动理货
		billInStoreTallyDtlService.autoTally(billInStore);
		return billInStore;
	}

	/**
	 * 获取文件操作列表
	 * 
	 * @param state
	 * @return
	 */
	public PageResult<BillInStoreFileResDto> queryFileList(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<BillInStoreFileResDto> pageResult = new PageResult<BillInStoreFileResDto>();
		fileAttReqDto.setBusType(BaseConsts.THREE);
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<BillInStoreFileResDto> list = convertToFileResDto(fielAttach);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), fileAttReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(fileAttReqDto.getPage());
		pageResult.setPer_page(fileAttReqDto.getPer_page());
		pageResult.setItems(list);
		return pageResult;
	}

	private List<BillInStoreFileResDto> convertToFileResDto(List<FileAttach> fileAttach) {
		List<BillInStoreFileResDto> list = new LinkedList<BillInStoreFileResDto>();
		if (ListUtil.isEmpty(fileAttach)) {
			return list;
		}
		for (FileAttach model : fileAttach) {
			BillInStoreFileResDto result = new BillInStoreFileResDto();
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
				BillInStoreFileResDto.Operate.operMap);
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

	/**
	 * 查询有效入库单
	 * 
	 * @param billDeliveryId
	 * @return
	 */
	public BillInStore queryValidBillInStoreByBillDeliveryId(Integer billDeliveryId) {
		BillInStore billInStore = new BillInStore();
		billInStore.setBillDeliveryId(billDeliveryId);
		billInStore.setIsDelete(BaseConsts.ZERO);
		List<BillInStore> list = billInStoreDao.queryByBillDeliveryId(billInStore);
		if (!CollectionUtils.isEmpty(list)) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 驳回入库单（销售退货）
	 * 
	 * @param billInStore
	 * @return
	 * @throws Exception
	 */
	public void rejectBillInStore(BillInStore billInStore) {
		billInStore = billInStoreDao.queryAndLockEntityById(billInStore.getId());

		if (billInStore.getStatus().equals(BaseConsts.ONE) && billInStore.getIsDelete().equals(BaseConsts.ZERO)) { // 待收货且未删除
			if (billInStore.getBillType().equals(BaseConsts.THREE)) { // 3-销售退货
				billInStore.setDeleterId(ServiceSupport.getUser().getId());
				billInStore.setDeleter(ServiceSupport.getUser().getChineseName());
				billInStore.setDeleteAt(new Date());
				billInStore.setIsDelete(BaseConsts.ONE);

				BillDelivery billDelivery = new BillDelivery();
				billDelivery.setId(billInStore.getBillDeliveryId());
				billDelivery.setStatus(BaseConsts.ONE);
				billDeliveryDao.updateById(billDelivery);

				int result = billInStoreDao.updateById(billInStore);
				if (result <= 0) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "驳回入库单失败");
				}
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "驳回入库单失败");
		}
	}

	public boolean isOverBillInStoreMaxLine(BillInStoreSearchReqDto billInStoreSearchReqDto) {
		billInStoreSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		int count = billInStoreDao.queryCountByCon(billInStoreSearchReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("入库单单据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncBillInStoreExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/logistics/bill_in_store_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.THREE);
			asyncExcelService.addAsyncExcel(billInStoreSearchReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncBillInStoreExport(BillInStoreSearchReqDto billInStoreSearchReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<BillInStoreResDto> billInStoreResDtoList = queryAllBillInStoreList(billInStoreSearchReqDto);
		model.put("billInStoreList", billInStoreResDtoList);
		return model;
	}

	public List<BillInStore> queryFinishedBillInStoreByAffiliateNo(String affiliateNo) {
		return billInStoreDao.queryFinishedBillInStoreByAffiliateNo(affiliateNo);
	}
}
