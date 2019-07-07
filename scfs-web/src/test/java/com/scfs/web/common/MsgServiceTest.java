package com.scfs.web.common;

import com.beust.jcommander.internal.Lists;
import com.scfs.common.utils.MsgPhpUrlBuilder;
import com.scfs.common.utils.http.HttpInvoker;
import com.scfs.domain.common.MsgData;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/10.
 */
public class MsgServiceTest {

    public static void main(String[] args) throws Exception{
        String url = "http://branched.msgcenter100.com/api-source/index";
        Map<String,Object > params = Maps.newHashMap();
        params.put("account","scfsuser01");//	应用对应的用户名
        params.put("password","scfspwd01");//	应用对应的密码
        params.put("api_key","kuGZ8MFvngF4hTDqDutb");//api_key
        List<MsgData> msgDataList = Lists.newArrayList();
        MsgData msgData = new MsgData();
        msgData.setContent("系统报警......");
        msgData.setChannel("rtx");
        msgData.setTitle("系统抓取汇率异常======");
        msgData.setTo("10828");
        msgDataList.add(msgData);
//        MsgData msgData2 = new MsgData();
//        msgData2.setContent("系统报警");
//        msgData2.setChannel("cheetahmail");
//        msgData2.setTitle("系统抓取汇率异常+++++++");
//        msgData2.setTo("");
//        msgDataList.add(msgData2);
//        params.put("data", JSONObject.toJSONString(msgDataList));
        List<Map<String,Object>> dataList = Lists.newArrayList();
        Map<String,Object> map = Maps.newHashMap();
        map.put("content","系统报警......");
        map.put("channel","rtx");
        map.put("to","10828");
        map.put("title","系统抓取汇率异常====");
        dataList.add(map);

        Map<String,Object> map1 = Maps.newHashMap();
        map1.put("content","系统报警......");
        map1.put("channel","sms");
        map1.put("to","18565659251");
        map1.put("title","系统抓取汇率异常====");
        dataList.add(map1);

        params.put("data",dataList);
        System.out.println( HttpInvoker.post(url, MsgPhpUrlBuilder.httpBuildQuery(params,"UTF-8")));
    }

}
