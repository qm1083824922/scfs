package com.scfs.domain.audit.model;

import java.io.Serializable;
import java.util.List;

import com.scfs.domain.logistics.dto.resp.BillOutStoreDtlResDto;
import com.scfs.domain.logistics.dto.resp.BillOutStoreFileResDto;
import com.scfs.domain.logistics.dto.resp.BillOutStoreResDto;

/**
 * Created by Administrator on 2016年11月2日.
 */
public class BillOutStoreAuditInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4711464494085287542L;

	/** 审核单头信息 */
	private BillOutStoreResDto billOutStoreResDto;
	/** 审核单行列表信息 */
	private List<BillOutStoreDtlResDto> billOutStoreDtlResDtoList;
	/** 附件信息 **/
	List<BillOutStoreFileResDto> billOutStoreFileList;

	public BillOutStoreResDto getBillOutStoreResDto() {
		return billOutStoreResDto;
	}

	public void setBillOutStoreResDto(BillOutStoreResDto billOutStoreResDto) {
		this.billOutStoreResDto = billOutStoreResDto;
	}

	public List<BillOutStoreDtlResDto> getBillOutStoreDtlResDtoList() {
		return billOutStoreDtlResDtoList;
	}

	public void setBillOutStoreDtlResDtoList(List<BillOutStoreDtlResDto> billOutStoreDtlResDtoList) {
		this.billOutStoreDtlResDtoList = billOutStoreDtlResDtoList;
	}

	public List<BillOutStoreFileResDto> getBillOutStoreFileList() {
		return billOutStoreFileList;
	}

	public void setBillOutStoreFileList(List<BillOutStoreFileResDto> billOutStoreFileList) {
		this.billOutStoreFileList = billOutStoreFileList;
	}

}
