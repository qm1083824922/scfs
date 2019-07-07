package com.scfs.domain.base.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/28.
 */
public class BizConstant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8488603794435074497L;
	private int id;
	/** 业务编码 */
	private String bizCode;
	private String pBizCodeCode;
	/** code */
	private String code;
	/** code对应的中文含义 */
	private String value;
	/** 显示顺序 */
	private Integer order;
	/** 删除标记 0 : 有效 1 : 删除 */
	private Integer isDelete;
	/** 创建时间 */
	private Date createAt;
	/** 备注 */
	private String remark;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBizCode() {
		return bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getpBizCodeCode() {
		return pBizCodeCode;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public void setpBizCodeCode(String pBizCodeCode) {
		this.pBizCodeCode = pBizCodeCode;
	}
}
