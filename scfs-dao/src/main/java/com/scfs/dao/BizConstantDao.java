package com.scfs.dao;

import com.scfs.domain.base.entity.BizConstant;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BizConstantDao {

    List<BizConstant> queryAllBizConstant(@Param("updateAt") String updateAt);

    /**
     * 查询最后更新时间
     * @return
     */
    Date queryLastUpdateAt();
}
