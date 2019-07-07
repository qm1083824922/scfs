package com.scfs.domain.result;

import com.scfs.domain.BaseResult;

/**
 * Created by Administrator on 2016/9/28.
 */
public class Result<T> extends BaseResult {

	/** 数据集 */
	private T items;

	public T getItems() {
		return items;
	}

	public void setItems(T items) {
		this.items = items;
	}
}
