package com.scfs.web.wechat;

import com.scfs.common.utils.http.HttpInvoker;
import com.scfs.service.wechat.WechatEventService;
import com.scfs.web.base.BaseJUnitTest;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by Administrator on 2017/1/3.
 */
public class WechatEventServiceTest extends BaseJUnitTest {
    @Autowired
    private WechatEventService wechatEventService;

    @Test
    public void testInsert(){
        Map<String,String> wParam = Maps.newHashMap();
        wParam.put("FromUserName","oANNHwn813Zl_wPvL9ZcSXyo6Gxs");
        wechatEventService.subscribe(wParam);
    }

    public static void main(String[] args) throws Exception{
        String url = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=FaiE3y1OYwtLSQ4AoD7nD2sVPoPpOcG1kbRp3-0q5bdcEXGMok-klTk_uo9sdcfR4xIldl6X_YB-Dece4SbDlC3T9qLYuUfIvkmzXyNqe7J1206RDG3x4rBlTwIqvMzYSJSjAJAWNW";
        System.out.println(HttpInvoker.get(url));
    }
}
