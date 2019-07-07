package com.scfs.web.common;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.dao.common.FileAttachDao;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.service.support.FileUploadService;
import com.scfs.service.support.ServiceSupport;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2016/11/30.
 */
public class FileUploadServiceTest extends BaseJUnitTest {
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private FileAttachDao fileAttachDao;

    @Test
    public void testInsert() {
        FileAttach fileAttach = new FileAttach();
        fileAttach.setType("jpg");
        fileAttach.setName("照片");
        fileAttach.setBusId(1);
        fileAttach.setBusType(1);
        fileAttach.setPath("E:\\upload\\imgs\\2016-11-26\\QQ图片20161013180805.jpg");
        fileAttach.setIsDelete(BaseConsts.ZERO);
        fileAttach.setCreateAt(new Date());
        fileAttach.setCreator(ServiceSupport.getUser().getChineseName());
        fileAttach.setCreatorId(ServiceSupport.getUser().getId());
        fileAttachDao.insert(fileAttach);
    }

    @Test
    public void testQuery() {
        LOGGER.info(JSONObject.toJSONString(fileAttachDao.queryById(1)));
    }

    @Test
    public void deleteFile() {
        fileUploadService.deleteFileById(46);
    }





}
