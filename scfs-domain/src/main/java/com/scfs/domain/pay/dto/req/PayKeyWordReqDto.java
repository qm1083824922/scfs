package com.scfs.domain.pay.dto.req;

import com.scfs.domain.BaseReqDto;

@SuppressWarnings("serial")
public class PayKeyWordReqDto extends BaseReqDto {
	/** 关键词 **/
	private String word;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

}
