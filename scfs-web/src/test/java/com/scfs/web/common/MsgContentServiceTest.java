package com.scfs.web.common;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.domain.common.entity.MsgContent;
import com.scfs.service.common.MsgContentService;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2016/12/15.
 */
public class MsgContentServiceTest extends BaseJUnitTest {

    @Autowired
    private MsgContentService msgContentService;

    @Test
    public void add(){
        msgContentService.addMsgContentByUserId(1,"aaaa","ssdsfds", BaseConsts.ONE);
    }
    @Test
    public void udpate(){
        MsgContent msgContent = new MsgContent();
        msgContent.setId(1);
        msgContent.setSendCount(1);
        msgContent.setIsSend(2);
        msgContentService.updateMsgById(msgContent);
    }

    @Test
    public void  testQuerySendMsgList(){
        LOGGER.info(JSONObject.toJSONString(msgContentService.querySendMsgList()));
    }

    @Test
    public void addMail(){
        //String[] tableTitle ={"编号","转交信息","转交人"};
        List<List<String>> lists = Lists.newArrayList();
        List<String> columns = Lists.newArrayList();
        columns.add("1312312");
        columns.add("nn你好色的该死的");
        columns.add("张三");
        List<String> columns2 = Lists.newArrayList();
        columns2.add("1111111111111111");
        columns2.add("sdfskdjklrwejr;ljw;是尽快付款圣诞节");
        columns2.add("李四");
        lists.add(columns);
        lists.add(columns2);
        //String content = msgContentService.convertMailTwoContent("采购单转交",tableTitle,lists,null);
        //msgContentService.addMsgContents("test@163.com","模板二。。。。。",content,BaseConsts.TWO);

    }
}
