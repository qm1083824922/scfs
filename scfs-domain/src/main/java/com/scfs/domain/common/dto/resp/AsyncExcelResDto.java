package com.scfs.domain.common.dto.resp;

import java.util.List;
import java.util.Map;
import com.scfs.common.consts.BaseUrlConsts; 
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue; 
import com.scfs.domain.common.entity.AsyncExcel;
import com.google.common.collect.Maps;

public class AsyncExcelResDto extends AsyncExcel{
	private static final long serialVersionUID = -6760647234939173247L;
	private String arg1;
	private String arg2;
	private String arg3;
	/**操作集合*/
    private List<CodeValue> opertaList;
    
    public static class Operate{
  		public static Map<String,String> operMap = Maps.newHashMap();
  		static {
  			operMap.put(OperateConsts.DOWNLOAD, BaseUrlConsts.DOWNLOADEXCELLIST);
  		}
  	}

	public String getArg1() {
		return arg1;
	}

	public void setArg1(String arg1) {
		this.arg1 = arg1;
	}

	public String getArg2() {
		return arg2;
	}

	public void setArg2(String arg2) {
		this.arg2 = arg2;
	}

	public String getArg3() {
		return arg3;
	}

	public void setArg3(String arg3) {
		this.arg3 = arg3;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

    
}