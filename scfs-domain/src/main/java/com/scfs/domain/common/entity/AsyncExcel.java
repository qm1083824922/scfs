package com.scfs.domain.common.entity;

import com.scfs.domain.base.entity.BaseEntity;

public class AsyncExcel extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String className;

    private String methodName;

    private byte[] args;

    private String name;//excel文件名称
    private String templatePath;//excel模板文件路径
    private String excelPath;//生成的excel文件存放路径
    private Integer poType;
    private Integer yn;
    private String poTypeName;//excel文件名称

    private Integer result;

    public byte[] getArgs() {
        return args;
    }

    public void setArgs(byte[] args) {
        this.args = args;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
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

    public String getExcelPath() {
        return excelPath;
    }

    public void setExcelPath(String excelPath) {
        this.excelPath = excelPath;
    }

	public String getPoTypeName() {
		return poTypeName;
	}

	public void setPoTypeName(String poTypeName) {
		this.poTypeName = poTypeName;
	}
    
}