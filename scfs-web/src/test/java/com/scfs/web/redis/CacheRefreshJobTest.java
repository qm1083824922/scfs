package com.scfs.web.redis;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.domain.base.entity.BaseUserProject;
import com.scfs.rpc.cache.ObjectRedisTemplate;
import com.scfs.service.schedule.CacheRefreshJob;
import com.scfs.web.base.BaseJUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */
public class CacheRefreshJobTest extends BaseJUnitTest {
    @SuppressWarnings("rawtypes")
	@Autowired
    private ObjectRedisTemplate objectRedisTemplate;
    @Autowired
    private CacheRefreshJob cacheRefreshJob;
    @Test
    public void testCache(){
        cacheRefreshJob.refreshCache();
//        BoundHashOperations accHash = objectRedisTemplate.boundHashOps(CacheKeyConsts.ADMIN_ROLE);
//        LOGGER.info(JSONObject.toJSONString(accHash.get("1"))+"===============");
//        LOGGER.info(JSONObject.toJSONString(ServiceSupport.isAdminUser(1))+"===============");
    }

    @SuppressWarnings("unchecked")
	@Test
    public void testQuery(){
//        Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.USER_PERMISSIONS).get("18");
//        List<BasePermission> permissionList = (List<BasePermission>)obj;
//        LOGGER.info(JSONObject.toJSONString(permissionList)+"===============");

        Object list = objectRedisTemplate.boundHashOps(CacheKeyConsts.USER_PROJECT).get(String.valueOf(1));
        if (list != null) {
            List<BaseUserProject> subList = (List<BaseUserProject>) list;
            LOGGER.info(JSONObject.toJSONString(subList)+"===============");
        }
        Object  obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.PROJECT).get("18");
        LOGGER.info(JSONObject.toJSONString(obj)+"++++++++++");
    }

}
