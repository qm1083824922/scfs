package com.scfs.common.utils;

import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件上传
 * @author Administrator
 *
 */
public class FileUploadUtil {

    /**
     * 上传文件
     *
     * @param myFile   文件
     * @param savePath 物理路径
     * @param fileSize 文件尺寸限制大小（单位：KB）
     * @return 文件地址（存于数据库）
     */
    public static String uploadFile(MultipartFile myFile, String savePath, Integer fileSize, String urlPrefix) {

        //文件大小验证，默认5M
        if (fileSize == null) {
            fileSize = 10;
        }
        Long size = myFile.getSize();
        Long limitedSize = 1024L * 1024L * fileSize;
        if (limitedSize < size) {
            throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "文件大小不能超过" + fileSize + "M!");
        }

        //服务器存文件路径，每天一个目录
        Date date = new Date();
        String d = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, date);
        File saveFileDir = new File(savePath + File.separator + d);
        if (!saveFileDir.exists()) {
            saveFileDir.mkdirs();
        }
        try {
            String fileName = MD5Util.getMD5String(myFile.getOriginalFilename().replaceAll(",",""))+"_"+System.currentTimeMillis();
            //保存文件
            String filePath = savePath + File.separator + d + File.separator + fileName;
            File file = new File(filePath);
            if (file.exists()) {
                throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "上传文件已存在！");
            }
            myFile.transferTo(file);
            //文件存放路径(持久化到数据库)
            String path = urlPrefix + d + File.separator + fileName;

            return path;
        } catch (Exception e) {
            throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "上传文件失败！");
        }

    }

    /**
     * 删除文件/文件夹
     *
     * @param filePath
     */
    public static void deleteFileOfDir(String filePath) {
        try {
            File file = new File(filePath);

            if (!file.exists()) return;

            if (file.isFile()) {
                file.delete();
                return;
            }
//            File[] files = file.listFiles();
//            for (int i = 0; i < files.length; i++) {
//                deleteAllFilesOfDir(files[i].getPath());
//            }
//            file.delete();
        } catch (Exception e) {
            throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "删除文件失败！[" + filePath + "]");
        }
    }

    public static void addEntry(String filePath,String zipFileName, ZipOutputStream zos)
            throws IOException {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            byte[] buffer = new byte[1024 * 10];
            fis = new FileInputStream(new File(filePath));
            bis = new BufferedInputStream(fis, buffer.length);
            int read = 0;
            zos.putNextEntry(new ZipEntry(zipFileName));
            while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
                zos.write(buffer, 0, read);
            }
            zos.flush();
            zos.closeEntry();
        } finally {
            IOUtil.closeQuietly(bis, fis);
        }
    }



}
