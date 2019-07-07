package com.scfs.domain.invoice.dto.req;

import java.util.List;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.invoice.entity.InvoiceOverseasPo;

/**
 * <pre>
 * 	境外发票销售信息
 *  File: InvoiceOverseasPoReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年03月28日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceOverseasPoReqDto extends BaseReqDto {
	/** 境外收票id **/
	private Integer overseasId;
	/** 单据编号 **/
	private String orderNo;
	/** 商品编号 **/
	private String goodsNo;
	/** 商品名称 **/
	private String goodsName;

	private List<InvoiceOverseasPo> poList;

	public Integer getOverseasId() {
		return overseasId;
	}

	public void setOverseasId(Integer overseasId) {
		this.overseasId = overseasId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public List<InvoiceOverseasPo> getPoList() {
		return poList;
	}

	public void setPoList(List<InvoiceOverseasPo> poList) {
		this.poList = poList;
	}

}
