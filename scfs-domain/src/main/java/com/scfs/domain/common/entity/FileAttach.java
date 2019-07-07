package com.scfs.domain.common.entity;


import com.scfs.domain.base.entity.BaseEntity;

public class FileAttach extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer busId;

    private Integer busType;

    private String name;

    private String type;

    private String path;

    public Integer getBusId() {
        return busId;
    }

    public void setBusId(Integer busId) {
        this.busId = busId;
    }

    public Integer getBusType() {
        return busType;
    }

    public void setBusType(Integer busType) {
        this.busType = busType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}