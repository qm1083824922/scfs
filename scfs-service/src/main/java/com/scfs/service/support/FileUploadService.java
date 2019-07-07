package com.scfs.service.support;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.FileUploadUtil;
import com.scfs.common.utils.IOUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.common.FileAttachDao;
import com.scfs.dao.tx.IgnoreTransactionalMark;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.FileAttach;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * Created by Administrator on 2016/11/26.
 */
@Service
public class FileUploadService {
	private final static Logger LOGGER = LoggerFactory.getLogger(FileUploadService.class);

	@Value("${file.save.path}")
	private String savePath;
	@Value("${file.max.size}")
	private Integer fileSize = 10;
	@Value("${file.url.perfix}")
	private String urlPrefix;
	@Autowired
	private FileAttachDao fileAttachDao;

	@IgnoreTransactionalMark
	public List<FileAttach> queryFileAttList(FileAttachSearchReqDto fileAttReqDto) {
		int offSet = PageUtil.getOffSet(fileAttReqDto.getPage(), fileAttReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, fileAttReqDto.getPer_page());
		List<FileAttach> fileAttachList = fileAttachDao.queryFileAttList(fileAttReqDto, rowBounds);
		return fileAttachList;
	}

	@IgnoreTransactionalMark
	public void viewFileById(Integer id, HttpServletResponse response) {
		try {
			FileAttach fileAtt = fileAttachDao.queryById(id);
			String fileName = fileAtt.getName() + "." + fileAtt.getType();
			OutputStream os = response.getOutputStream();
			response.reset();
			response.addHeader("Content-Disposition",
					"attachment; filename=" + new String(fileName.getBytes("GBK"), "ISO8859-1"));
			String contentType = ServiceSupport.getValueByBizCode(BizCodeConsts.FILE_MIME_TYPE, fileAtt.getType());
			response.setHeader("Content-Type", contentType);
			os.write(FileUtils.readFileToByteArray(new File(fileAtt.getPath())));
			os.flush();
		} catch (Exception e) {
			LOGGER.error("查看文件异常：", e);
		}
	}

	@IgnoreTransactionalMark
	public void downLoadFileList(FileAttachSearchReqDto fileAttReqDto, HttpServletResponse response) {
		List<FileAttach> fileAttachList = fileAttachDao.queryFileAtts(fileAttReqDto);
		if (CollectionUtils.isNotEmpty(fileAttachList)) {
			FileOutputStream fos = null;
			ZipOutputStream zos = null;
			String zipName = "myfile.zip";
			File target = new File(zipName);
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			response.setContentType("application/zip");
			response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
			try {
				fos = new FileOutputStream(target);
				zos = new ZipOutputStream(response.getOutputStream());
				for (FileAttach fileAttach : fileAttachList) {
					// 添加对应的文件Entry
					FileUploadUtil.addEntry(fileAttach.getPath(), fileAttach.getName() + "." + fileAttach.getType(),
							zos);
				}
				response.flushBuffer();
			} catch (Exception e) {
				LOGGER.error("批量下载文件异常：", e);
			} finally {
				IOUtil.closeQuietly(bis, fis);
				IOUtil.closeQuietly(zos, fos);
			}
		}
	}

	public void deleteFileById(Integer id) {
		FileAttach fileAtt = new FileAttach();
		fileAtt.setId(id);
		fileAtt.setIsDelete(BaseConsts.ONE);
		FileAttach fileAttach = fileAttachDao.queryById(id);
		deleteFile(fileAttach.getPath());
		int count = fileAttachDao.updateById(fileAtt);
		if (count != BaseConsts.ONE) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "删除文件失败！");
		}
	}

	/**
	 * 批量上传文件
	 *
	 * @param fileList
	 * @param fileAttach
	 */
	public void uploadFileList(List<MultipartFile> fileList, FileAttach fileAttach) {
		if (CollectionUtils.isNotEmpty(fileList)) {
			for (MultipartFile file : fileList) {
				uploadFile(file, fileAttach);
			}
		}

	}

	/**
	 * 返回上传文件路径
	 *
	 * @param myFile
	 * @return
	 */
	public void uploadFile(MultipartFile myFile, FileAttach fileAttach) {
		if (fileAttach.getBusId() == null || fileAttach.getBusType() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "业务ID或业务类型不能为空");
		}
		String fileName = myFile.getOriginalFilename();
		fileAttach.setName(fileName.substring(0, fileName.lastIndexOf(".")).replaceAll(",", ""));
		fileAttach.setType(fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()));
		fileAttach.setCreateAt(new Date());
		fileAttach.setCreator(ServiceSupport.getUser().getChineseName());
		fileAttach.setCreatorId(ServiceSupport.getUser().getId());
		fileAttach.setIsDelete(BaseConsts.ZERO);
		String path = FileUploadUtil.uploadFile(myFile, savePath, fileSize, urlPrefix);
		fileAttach.setPath(path);
		fileAttachDao.insert(fileAttach);
	}

	/**
	 * @param myFile
	 * @return
	 */
	private void deleteFile(String myFile) {
		FileUploadUtil.deleteFileOfDir(myFile);
	}
}
