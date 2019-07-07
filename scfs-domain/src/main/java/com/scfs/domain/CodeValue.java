package com.scfs.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/28.
 *
 */
public class CodeValue implements Comparable<CodeValue>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;

	private String value;

	public CodeValue() {

	}

	public CodeValue(String code, String value) {
		this.code = code;
		this.value = value;
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

	@Override
	public int compareTo(CodeValue o) {
		return this.getValue().compareTo(o.getValue());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof CodeValue))
			return false;
		CodeValue codeValue = (CodeValue) o;
		if (code != null && code.equals(codeValue.code))
			return true;
		return false;
	}

	@Override
	public int hashCode() {
		int result = code != null ? code.hashCode() : 0;
		result = 31 * result + (value != null ? value.hashCode() : 0);
		return result;
	}
}
