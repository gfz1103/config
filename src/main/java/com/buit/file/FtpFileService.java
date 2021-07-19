package com.buit.file;

import com.buit.config.file.FtpServerProperties;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @Description
 * @Author yueyu
 * @Date 2021/4/19 10:58
 */
@Component
public class FtpFileService {

    static final Logger logger = LoggerFactory.getLogger(FtpFileService.class);

    @Autowired
    public FtpServerProperties properties;


    protected FTPClient login() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(properties.getHost(),properties.getPort());
        ftpClient.login(properties.getUser(),properties.getPwd());
        if(FTPReply.isPositiveCompletion(ftpClient.getReplyCode())){
            logger.info("ftp服务器连接成功");
        }
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setControlEncoding("UTF-8");
        return ftpClient;
    }

    /**
     * @param path = "/image(图片都为image)/system(模块名称)"
     * @param fileName
     * @param inputStream
     * @return
     */
    public String uploadFile(String path, String fileName, InputStream inputStream){
        logger.info("上传文件[{},{}]",path,fileName);
        try {
            FTPClient ftpClient = login();
            createDirectory(ftpClient,path);
            boolean success = ftpClient.storeFile(fileName,inputStream);
            inputStream.close();
            ftpClient.logout();
            ftpClient.disconnect();
            if(success) {
                return String.format("%s/%s",path,fileName);
            }else{
                return null;
            }
        } catch (IOException e) {
            logger.error("上传文件失败",e);
            return null;
        }
    }

    /**
     * @param fileName
     * @param inputStream
     * @param expireDay 过期天数
     * @return
     */
    public String uploadTempFile(String fileName, InputStream inputStream,Integer expireDay){
        LocalDate expireDate = LocalDate.now().plusDays(expireDay);
        String path = String.format("/temp/%d/%d/%d",expireDate.getYear(),expireDate.getMonthValue(),expireDate.getDayOfMonth());
        return uploadFile(path,fileName,inputStream);
    }

    public OutputStream downloadFile(String fileFullPath){
        logger.info("下载文件[{}]",fileFullPath);
        try{
            FTPClient ftpClient = login();
            FTPFile[] ftpFile = ftpClient.listFiles(fileFullPath);
            if(ftpFile==null){
                logger.info("文件不存在[{}]",fileFullPath);
                return null;
            }
            OutputStream out = new ByteArrayOutputStream();
            ftpClient.retrieveFile(fileFullPath,out);
            ftpClient.logout();
            ftpClient.disconnect();
            return out;
        } catch (IOException e) {
            logger.error("下载文件失败",e);
            return null;
        }
    }

    public InputStream getInputStream(String fileFullPath){
        logger.info("获取文件流[{}]",fileFullPath);
        try{
            FTPClient ftpClient = login();
            FTPFile[] ftpFile = ftpClient.listFiles(fileFullPath);
            if(ftpFile==null){
                logger.info("文件不存在[{}]",fileFullPath);
                return null;
            }
            InputStream out = ftpClient.retrieveFileStream(fileFullPath);
            ftpClient.completePendingCommand();
            ftpClient.logout();
            ftpClient.disconnect();
            return out;
        } catch (IOException e) {
            logger.error("下载文件失败",e);
            return null;
        }
    }

    public boolean deleteFile(String fileFullPath){
        logger.info("删除文件[{}]",fileFullPath);
        try {
            FTPClient ftpClient = login();
            FTPFile[] ftpFile = ftpClient.listFiles(fileFullPath);
            if(ftpFile==null){
                logger.info("文件不存在[{}]",fileFullPath);
                return false;
            }
            boolean success = ftpClient.deleteFile(fileFullPath);
            ftpClient.logout();
            ftpClient.disconnect();
            return success;
        } catch (IOException e) {
            logger.error("删除文件失败[{}]",fileFullPath);
            return false;
        }
    }


    protected void createDirectory(FTPClient ftpClient,String path) throws IOException {
        if(path.equals("/")){
            return;
        }
        String[] parentPaths = path.split("/");
        for(int i=1;i<parentPaths.length;i++){
            boolean isExist = ftpClient.changeWorkingDirectory(parentPaths[i]);
            if(!isExist){
                boolean isSuccess = ftpClient.makeDirectory(parentPaths[i]);
                if(!isSuccess){
                    throw new IOException("创建文件夹失败");
                }else{
                    ftpClient.changeWorkingDirectory(parentPaths[i]);
                }
            }
        }

    }
}
