package com.scfs.domain.audit.model;

import java.io.Serializable;
import java.util.List;

import com.scfs.domain.sale.dto.resp.BillDeliveryDtlResDto;
import com.scfs.domain.sale.dto.resp.BillDeliveryFileResDto;
import com.scfs.domain.sale.dto.resp.BillDeliveryResDto;

/**
 * Created by Administrator on 2016年11月2日.
 */
public class BillDeliveryAuditInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1523503650992671749L;
	/** 审核单头信息 */
	private BillDeliveryResDto billDeliveryResDto;
	/** 审核单行列表信息 */
	private List<BillDeliveryDtlResDto> billDeliveryDtlResDtoList;

	/** 附件信息 **/
	List<BillDeliveryFileResDto> billDeliveryFileList;

	public BillDeliveryResDto getBillDeliveryResDto() {
		return billDeliveryResDto;
	}

	public void setBillDeliveryResDto(BillDeliveryResDto billDeliveryResDto) {
		this.billDeliveryResDto = billDeliveryResDto;
	}

	public List<BillDeliveryDtlResDto> getBillDeliveryDtlResDtoList() {
		return billDeliveryDtlResDtoList;
	}

	public void setBillDeliveryDtlResDtoList(List<BillDeliveryDtlResDto> billDeliveryDtlResDtoList) {
		this.billDeliveryDtlResDtoList = billDeliveryDtlResDtoList;
	}

	public List<BillDeliveryFileResDto> getBillDeliveryFileList() {
		return billDeliveryFileList;
	}

	public void setBillDeliveryFileList(List<BillDeliveryFileResDto> billDeliveryFileList) {
		this.billDeliveryFileList = billDeliveryFileList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
