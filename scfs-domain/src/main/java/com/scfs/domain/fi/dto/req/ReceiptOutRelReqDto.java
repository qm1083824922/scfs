package com.scfs.domain.fi.dto.req;

import java.util.ArrayList;
import java.util.List;
import com.scfs.domain.BaseReqDto;
import com.scfs.domain.fi.entity.RecReceiptRel;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.po.entity.PurchaseOrderLine;

/**
 * <pre>
 * 
 *  File: ReceiptOutRelReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年06月03日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class ReceiptOutRelReqDto extends BaseReqDto  {
	/** 水单id **/
	private Integer receiptId;
	/**出库单id **/
	private Integer outStoreID;
	public List<BillOutStore> getRelList() {
		return relList;
	}
	public void setRelList(List<BillOutStore> relList) {
		this.relList = relList;
	}
	/**批量新增出库单 */
	private List<Integer> recOutRelId;
	private List<BillOutStore> relList = new ArrayList<BillOutStore>();

	/**批量新增铺货结算单**/
	private List<PurchaseOrderLine> orderLines=new  ArrayList<PurchaseOrderLine>();

   private  List<RecReceiptRel> rels=new ArrayList<RecReceiptRel>();
	public List<RecReceiptRel> getRels() {
	return rels;
}
public void setRels(List<RecReceiptRel> rels) {
	this.rels = rels;
}
	public List<Integer> getRecOutRelId() {
		return recOutRelId;
	}
	public void setRecOutRelId(List<Integer> recOutRelId) {
		this.recOutRelId = recOutRelId;
	}
	public Integer getReceiptId() {
		return receiptId;
	}
	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}
	public Integer getOutStoreID() {
		return outStoreID;
	}
	public void setOutStoreID(Integer outStoreID) {
		this.outStoreID = outStoreID;
	}
	
	public List<PurchaseOrderLine> getOrderLines() {
		return orderLines;
	}
	public void setOrderLines(List<PurchaseOrderLine> orderLines) {
		this.orderLines = orderLines;
	}
}
