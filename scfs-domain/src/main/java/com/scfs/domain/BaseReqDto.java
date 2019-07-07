package com.scfs.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public class BaseReqDto implements Serializable {
	
    private static final long serialVersionUID = 1302448645483426061L;

    private List<Integer> ids;
    
    private Integer id;
    private Integer userId;
    //是否需要合计，1表示是，2表示否  key:IS_NEED
    private Integer needSum;
    /** 查询过滤项目**/
    private List<Integer> excludeProjectIdList;
    
    public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**第几页*/
    private int page = 0;
    
    /**每页显示数目*/
    private int per_page = 15;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

    public Integer getNeedSum() {
        return needSum;
    }

    public void setNeedSum(Integer needSum) {
        this.needSum = needSum;
    }

	public List<Integer> getExcludeProjectIdList() {
		return excludeProjectIdList;
	}

	public void setExcludeProjectIdList(List<Integer> excludeProjectIdList) {
		this.excludeProjectIdList = excludeProjectIdList;
	}
    
    
}
