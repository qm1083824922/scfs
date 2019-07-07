package com.scfs.domain.base.dto.req;

import java.util.ArrayList;
import java.util.List;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.DistributionGoods;

/**
 * <pre>
 *  铺货商品
 *  File: DistributionGoodsReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月02日				Administrator
 *
 * </pre>
 */
/**
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class DistributionGoodsReqDto extends BaseReqDto {

	/** 部门 **/
	private Integer departmentId;

	/** 供应商 **/
	private Integer supplierId;

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

	List<CodeValue> codeList;
	/** 项目id **/
	private Integer projectId;
	/** 商品的iD */
	private Integer goodId;

	private String updateAt;

	private List<DistributionGoods> disList = new ArrayList<DistributionGoods>();

	public List<DistributionGoods> getDisList() {
		return disList;
	}

	public void setDisList(List<DistributionGoods> disList) {
		this.disList = disList;
	}

	public Integer getGoodId() {
		return goodId;
	}

	public void setGoodId(Integer goodId) {
		this.goodId = goodId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

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

	public List<CodeValue> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<CodeValue> codeList) {
		this.codeList = codeList;
	}

	public String getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(String updateAt) {
		this.updateAt = updateAt;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

}