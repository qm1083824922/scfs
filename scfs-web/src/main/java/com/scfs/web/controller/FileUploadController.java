package com.scfs.web.controller;

import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.domain.BaseResult;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.service.support.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Controller
public class FileUploadController {
	@Autowired
	private FileUploadService fileUploadService;

	@RequestMapping(value = BaseUrlConsts.FILE_VIEW, method = RequestMethod.GET)
	public void viewFileById(Integer id, HttpServletResponse response) {
		fileUploadService.viewFileById(id, response);
	}

	@RequestMapping(value = BaseUrlConsts.FILE_UPLOAD, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult uploadFile(MultipartFile myFile, FileAttach fileAttach) {
		BaseResult result = new BaseResult();
		fileUploadService.uploadFile(myFile, fileAttach);
		return result;
	}

	@RequestMapping(value = "/file/download", method = RequestMethod.GET)
	public void downFileList(FileAttachSearchReqDto fileAttReqDto, HttpServletResponse response) {
		fileUploadService.downLoadFileList(fileAttReqDto, response);
	}

}
