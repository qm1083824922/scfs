package com.scfs.domain.api.pms.dto.res;

import java.io.Serializable;
import java.util.List;

/**
 * 退货订单完成发送接口返回数据结构 PmsPurchaseResDto
 * 
 * @author 
 *
 */
public class PmsPurchaseResDto implements Serializable {

	private static final long serialVersionUID = 1380552103720355758L;

	private String flag; // 0: 成功 -1: 失败
	private String msg;

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * 返回数据
	 */
	private List<PmsSyncPurchaseSendResDto> data;

	public List<PmsSyncPurchaseSendResDto> getData() {
		return data;
	}

	public void setData(List<PmsSyncPurchaseSendResDto> data) {
		this.data = data;
	}

}
