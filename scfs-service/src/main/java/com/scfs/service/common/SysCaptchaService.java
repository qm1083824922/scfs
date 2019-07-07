package com.scfs.service.common;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.utils.UUIDHexGenerator;

/**
 * 验证码
 *
 */
@Service("sysCaptchaService")
public class SysCaptchaService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public Map<String, Object> getUuid(String captcha) {
		String uuid = UUIDHexGenerator.generate();

        //5分钟后过期
        stringRedisTemplate.opsForValue().set(CacheKeyConsts.CACHE_PREFIX + uuid, captcha, 60*5, TimeUnit.SECONDS);
        Map<String, Object> map = Maps.newHashMap();
        map.put("uuid", uuid);
        map.put("expire", uuid);
        return map;
    }

    public boolean isExpired(String uuid){
    	 String captcha = stringRedisTemplate.opsForValue().get(CacheKeyConsts.CACHE_PREFIX + uuid);
         if (StringUtils.isBlank(captcha)) {
         	return true;
         } else {
        	 return false;
         }
	}
    
    public boolean validate(String uuid, String code) {
        String currCaptcha = stringRedisTemplate.opsForValue().get(CacheKeyConsts.CACHE_PREFIX + uuid);
        if (StringUtils.isBlank(currCaptcha)) {
            return false;
        }
        if (code.equalsIgnoreCase(currCaptcha)){
            return true;
        }
        return false;
    }
    
    public void clearCaptcha(String uuid) {
        stringRedisTemplate.delete(CacheKeyConsts.CACHE_PREFIX + uuid);
    }
}
