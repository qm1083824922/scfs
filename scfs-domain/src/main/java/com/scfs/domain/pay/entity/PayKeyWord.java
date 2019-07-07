package com.scfs.domain.pay.entity;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 
 *  File: PayKeyWord.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月30日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class PayKeyWord extends BaseEntity {
	/** 关键词 **/
	private String word;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

}
