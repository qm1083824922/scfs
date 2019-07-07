package com.scfs.domain.common.dto.req;

import java.util.List;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2016/11/30.
 */
public class FileAttachSearchReqDto  extends BaseReqDto{

    /**
     * Comment for &lt;code&gt;serialVersionUID&lt;/code&gt;
     */
    private static final long serialVersionUID = -2602252145407623128L;

    private Integer busId;

    private Integer busType;

    private String fileName;
    
    private List<Integer> busIds;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<Integer> getBusIds()
    {
        return busIds;
    }

    public void setBusIds(List<Integer> busIds)
    {
        this.busIds = busIds;
    }
}
