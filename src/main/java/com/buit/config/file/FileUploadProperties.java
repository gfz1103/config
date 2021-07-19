package com.buit.config.file;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
* @ClassName: FileUploadConfig
* @Description: 静态文件上传配置类
* @author 神算子
* @date 2020年4月26日 下午3:49:14
 */
@Component
@ConfigurationProperties("his.filepath")
public class FileUploadProperties {
	/** 外网路径 */
	public  String httpUrl;
	/** 临时目录 */
	public  String tempFile ;
	/** 正式目录 */
	public  String formalFile ;
	/** 编辑器图片的正式目录 */
	public  String fileBaseDir;
	public String getHttpUrl() {
		return httpUrl;
	}
	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}
	public String getTempFile() {
		return tempFile;
	}
	public void setTempFile(String tempFile) {
		this.tempFile = tempFile;
	}
	public String getFormalFile() {
		return formalFile;
	}
	public void setFormalFile(String formalFile) {
		this.formalFile = formalFile;
	}
	public String getFileBaseDir() {
		return fileBaseDir;
	}
	public void setFileBaseDir(String fileBaseDir) {
		this.fileBaseDir = fileBaseDir;
	}
}
