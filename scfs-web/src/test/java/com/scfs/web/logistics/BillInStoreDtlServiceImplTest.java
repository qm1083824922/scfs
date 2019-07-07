package com.scfs.web.logistics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.dao.logistics.BillInStoreDtlDao;
import com.scfs.domain.logistics.dto.req.BillInStoreDtlSearchReqDto;
import com.scfs.domain.logistics.dto.req.BillInStoreSearchReqDto;
import com.scfs.domain.logistics.dto.req.PoOrderDtlReqDto;
import com.scfs.domain.logistics.dto.resp.BillInStoreDtlResDto;
import com.scfs.domain.logistics.entity.BillInStore;
import com.scfs.domain.logistics.entity.BillInStoreDtl;
import com.scfs.domain.po.dto.req.PoLineReqDto;
import com.scfs.domain.po.entity.PurchaseOrderLine;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.logistics.BillInStoreDtlService;
import com.scfs.service.po.PurchaseOrderService;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2016年10月19日.
 */
public class BillInStoreDtlServiceImplTest extends BaseJUnitTest{
	@Autowired
	private BillInStoreDtlService billInStoreDtlService;
	@Autowired
	private BillInStoreDtlDao billInStoreDtlDao;
	@Autowired
	private PurchaseOrderService purchaseOrderService;

	
	@Test
	public void testAddBillInStoreDtl() {
		try {
			BillInStoreSearchReqDto billInStoreReqDto = new BillInStoreSearchReqDto();
			billInStoreReqDto.setId(4);
			
			List<PoOrderDtlReqDto> dtos = new ArrayList<PoOrderDtlReqDto>(5);
			
			PoOrderDtlReqDto poOrderDtlReqDto = new PoOrderDtlReqDto();
			poOrderDtlReqDto.setPoId(4);
			poOrderDtlReqDto.setPoDtlId(13);
			poOrderDtlReqDto.setPoNum(new BigDecimal("2"));
			poOrderDtlReqDto.setPoPrice(new BigDecimal("50"));
			poOrderDtlReqDto.setReceivePrice(new BigDecimal("50"));
			poOrderDtlReqDto.setOrderNo("00000021");
			poOrderDtlReqDto.setOrderTime(DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD, "2016-10-29 11:01:03"));
			poOrderDtlReqDto.setAppendNo("1111");
			poOrderDtlReqDto.setGoodsId(1);
			poOrderDtlReqDto.setGoodsName("小米5");
			poOrderDtlReqDto.setGoodsNumber("xx-001");
			poOrderDtlReqDto.setGoodsType("手机");
			poOrderDtlReqDto.setReceiveNum(new BigDecimal("2"));
			dtos.add(poOrderDtlReqDto);
			billInStoreReqDto.setPoOrderDtlReqDtoList(dtos);
			
	        LOGGER.info("[参数]" + JSONObject.toJSONString(billInStoreReqDto,SerializerFeature.WriteMapNullValue)+"");

			billInStoreDtlService.addBillInStoreDtls(billInStoreReqDto);
			
		} catch (Exception e) {
	        LOGGER.error("", e);

		}
	}
	
	@Test
	public void testAddOrder() {
		try {
			PurchaseOrderTitle purchaseOrderTitle = new PurchaseOrderTitle();
			purchaseOrderTitle.setProjectId(3);
			purchaseOrderTitle.setCustomerId(52);
			purchaseOrderTitle.setWarehouseId(33);
			purchaseOrderTitle.setSupplierId(35);
			purchaseOrderTitle.setAppendNo("1111");
			purchaseOrderTitle.setBusinessUnitId(1);
			purchaseOrderTitle.setPerdictTime(new Date());
			purchaseOrderTitle.setArrivalNum(new BigDecimal("2"));
			purchaseOrderTitle.setArrivalAmount(new BigDecimal("100"));
			purchaseOrderTitle.setOrderTotalNum(new BigDecimal("2"));
			purchaseOrderTitle.setOrderTotalAmount(new BigDecimal("100"));
			purchaseOrderTitle.setOrderType(1);
			purchaseOrderTitle.setOrderTime(new Date());
			purchaseOrderTitle.setPayAmount(new BigDecimal("100"));
			purchaseOrderTitle.setPayWay(1);
			purchaseOrderTitle.setCurrencyId(1);
			purchaseOrderTitle.setAccountId(1);
			purchaseOrderTitle.setRequestPayTime(new Date());
			purchaseOrderTitle.setRemark("yjp测试");
			purchaseOrderService.addPurchaseOrderTitle(purchaseOrderTitle);
			
			List<PurchaseOrderLine> purchaseOrderLineList = new ArrayList<PurchaseOrderLine>();
			PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();
			purchaseOrderLine.setBatchNum("11111");
			purchaseOrderLine.setCostPrice(new BigDecimal("40"));
			purchaseOrderLine.setGoodsId(1);
			purchaseOrderLine.setGoodsNo("xx-001");
			purchaseOrderLine.setGoodsPrice(new BigDecimal("50"));
			purchaseOrderLine.setGoodsNum(new BigDecimal("2"));
			purchaseOrderLine.setPoId(purchaseOrderTitle.getId());
			purchaseOrderLineList.add(purchaseOrderLine);
			
			PoLineReqDto poLineReqDto = new PoLineReqDto();
			poLineReqDto.setId(purchaseOrderTitle.getId());
			poLineReqDto.setPoLines(purchaseOrderLineList);
			purchaseOrderService.addPoLines(poLineReqDto);
		} catch (Exception e) {
	        LOGGER.error("", e);

		}
	}
	
	@Test
	public void testQuery() {
		BillInStoreDtlSearchReqDto billInStoreDtlReqDto = new BillInStoreDtlSearchReqDto();
		billInStoreDtlReqDto.setId(3);
		try {
			Result<BillInStoreDtlResDto> result = billInStoreDtlService.queryBillInStoreDtlById(billInStoreDtlReqDto);
	        LOGGER.info("[结果1]" + JSONObject.toJSONString(result,SerializerFeature.WriteMapNullValue)+"");

	        List<Integer> ids = new ArrayList<Integer>();
	        ids.add(3);
	        billInStoreDtlReqDto.setIds(ids);
	        PageResult<BillInStoreDtlResDto> result2 = billInStoreDtlService.queryBillInStoreDtlByIds(billInStoreDtlReqDto);
	        LOGGER.info("[结果2]" + JSONObject.toJSONString(result2,SerializerFeature.WriteMapNullValue)+"");

	        BillInStoreDtlSearchReqDto billInStoreDtlReqDto2 = new BillInStoreDtlSearchReqDto();
	        billInStoreDtlReqDto2.setBillInStoreId(4);
	        PageResult<BillInStoreDtlResDto> rs = billInStoreDtlService.queryBillInStoreDtlsByBillInStoreId(billInStoreDtlReqDto2, false);
	        LOGGER.info("[结果3]" + JSONObject.toJSONString(rs,SerializerFeature.WriteMapNullValue)+"");

	        BillInStoreDtlSearchReqDto billInStoreDtlReqDto3 = new BillInStoreDtlSearchReqDto();
	        billInStoreDtlReqDto3.setBillInStoreId(3);
	        List<BillInStoreDtl> list = billInStoreDtlDao.queryResultsByCon(billInStoreDtlReqDto3);
	        LOGGER.info("[结果4]" + JSONObject.toJSONString(list,SerializerFeature.WriteMapNullValue)+"");
		} catch (Exception e) {
	        LOGGER.error("", e);

		}
	}
	
	@Test
	public void testDel() {	
		BillInStoreDtlSearchReqDto billInStoreDtlReqDto = new BillInStoreDtlSearchReqDto();
		billInStoreDtlReqDto.setId(3);
		try {
			List<Integer> ids = new ArrayList<Integer>(5);
			ids.add(3);
			billInStoreDtlReqDto.setIds(ids);
			billInStoreDtlService.deleteBillInStoreDtlsByIds(billInStoreDtlReqDto);	        
		} catch (Exception e) {
	        LOGGER.error("", e);
		}
	}
	
	@Test
	public void testUpdate() {
		try {
			BillInStore billInStore = new BillInStore();
			billInStore.setId(4);
			
			List<BillInStoreDtl> billInStoreDtls = new ArrayList<BillInStoreDtl>();
			BillInStoreDtl billInStoreDtl = new BillInStoreDtl();
			billInStoreDtl.setId(5);
			billInStoreDtl.setReceiveNum(new BigDecimal("2"));
			billInStoreDtls.add(billInStoreDtl);
			billInStore.setBillInStoreDtlList(billInStoreDtls);
	        LOGGER.info("[参数]" + JSONObject.toJSONString(billInStore,SerializerFeature.WriteMapNullValue)+"");

			billInStoreDtlService.updateBillInStoreDtls(billInStore);        
		} catch (Exception e) {
	        LOGGER.error("", e);

		}
	}

}

