package com.scfs.domain.base.dto.req;

import com.scfs.domain.BaseReqDto;

public class BaseGoodsReqDto extends BaseReqDto {

	private static final long serialVersionUID = -4606644312456857510L;

	/** 编号 */
	private String number;

	/** 名称 */
	private String name;

	/** 型号 */
	private String type;

	/** 规格 */
	private String specification;

	/** 条码 */
	private String barCode;

	/** 状态 */
	private Integer status;

	private Integer goodType;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getGoodType() {
		return goodType;
	}

	public void setGoodType(Integer goodType) {
		this.goodType = goodType;
	}

}