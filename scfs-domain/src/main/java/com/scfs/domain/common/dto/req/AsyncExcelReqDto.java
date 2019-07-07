package com.scfs.domain.common.dto.req;

import com.scfs.domain.BaseReqDto; 

public class AsyncExcelReqDto extends BaseReqDto{

    /***/
	private static final long serialVersionUID = 5215237362248092442L;
	private String name;//excel文件名称  
    private Integer poType;  
    private Integer result;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
 
    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getPoType() {
        return poType;
    }

    public void setPoType(Integer poType) {
        this.poType = poType;
    } 
    
}