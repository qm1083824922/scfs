package com.scfs.domain.common.entity;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * Created by Administrator on 2016/10/29.
 */
public class SeqManage  extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String seqName;

    private Integer seqVal;

    public String getSeqName() {
        return seqName;
    }

    public void setSeqName(String seqName) {
        this.seqName = seqName;
    }

    public Integer getSeqVal() {
        return seqVal;
    }

    public void setSeqVal(Integer seqVal) {
        this.seqVal = seqVal;
    }
}
