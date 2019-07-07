package com.scfs.rpc.cms.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by Administrator on 2016年12月19日.
 */
public class PayCmsHttpResDto extends CmsHttpResDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -215845495505350865L;

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
