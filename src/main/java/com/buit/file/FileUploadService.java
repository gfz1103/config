package com.buit.file;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

/**
 * 文件系统存储实现
 * 
 * @author
 */
@Service
public class FileUploadService {

	static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);

	/**
	 * 文件目录
	 */
	public String fileBaseDir;

	public void setFileBaseDir(String fileBaseDir) {
		this.fileBaseDir = fileBaseDir;
	}

	public String getBaseDir() {
		return fileBaseDir + File.separator;
	}

	private String dateFormat = "yyMMddHHmmssSSS";

	/**
	 * 返回默认文件名称
	 */
	public String getFileName() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String filenName = formatter.format(cal.getTime()) + ThreadLocalRandom.current().nextInt(10, 300);
		return filenName;
	}

	/**
	 * 文件上传：默认上传到服务 指定路径 path 为空时上传到 临时目录
	 */
	public String upload(MultipartFile multipartFile, String path) {
		if (multipartFile == null || multipartFile.isEmpty()) {
			logger.error(" multipartFile isEmpty ");
			return null;
		}

		if (StringUtils.isEmpty(path)) {
			logger.error(" path isEmpty ");
			return null;
		}
		String a1 = "/";
		char a2 = '/';
		char a3 = '\\';
		char lastChar = path.charAt(path.length() - 1);
		if (lastChar != a3 && lastChar != a2) {
			path += a1;
		}

		File baseFile = new File(getBaseDir() + path);
		if (!baseFile.exists()) {
			baseFile.mkdirs();
		}

		// 将上传的文件重新命名
		String oFileName = multipartFile.getOriginalFilename();
		// 获取文件后缀
		String fileSuffix = oFileName.indexOf(".") != -1
				? oFileName.substring(oFileName.lastIndexOf(".") + 1, oFileName.length())
				: null;
		String fileName = getFileName() + "." + fileSuffix;

		// 上传文件
		try {
			multipartFile.transferTo(new File(baseFile, fileName));
		} catch (IllegalStateException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return path + fileName;
	}

	/**
	 * 多文件上传到 临时目录
	 */
	public List<String> upload(MultipartFile[] multipartFiles) {
		return this.upload(multipartFiles, null);
	}

	/**
	 * 多文件上传 path 为空时上传到 临时目录
	 */
	public List<String> upload(MultipartFile[] multipartFiles, String path) {
		if (multipartFiles == null || multipartFiles.length == 0) {
			logger.error(" multipartFiles==null 或  multipartFiles.length==0 ");
			return null;
		}
		List<String> fileNames = new ArrayList<>();
		for (MultipartFile multipartFile : multipartFiles) {
			if (multipartFile.isEmpty()) {
				continue;
			}
			String retFileName = this.upload(multipartFile, path);
			fileNames.add(retFileName);
		}
		return fileNames;
	}

	/**
	 * 多张张 图片移动 返回 移动后的路径
	 */
	public List<String> move(List<String> fromFileNames, String toFilePath) {
		if (fromFileNames == null || fromFileNames.size() == 0) {
			logger.error("fromFileNames 不可为空");
			return null;
		}
		List<String> fileNames = new ArrayList<>();
		for (String fromFileName : fromFileNames) {
			String retpath = this.move(fromFileName, toFilePath);
			fileNames.add(retpath);
		}
		return fileNames;
	}

	/**
	 * 单张 图片移动 返回 移动后的路径
	 */
	public String move(String fromFileName, String toFilePath) {
		// 校验判断
		if (StringUtils.isEmpty(fromFileName)) {
			logger.warn("fromFileName为空");
			return null;
		}
		if (StringUtils.isEmpty(toFilePath)) {
			logger.warn("toFilePath为空");
			return null;
		}
		File fromFiler = new File(getBaseDir() + fromFileName);
		File toFileDri = new File(getBaseDir() + toFilePath);

		// 不存在
		if (!fromFiler.exists()) {
			logger.error("fromFiler不存在 ==" + fromFiler.getPath());
		}
		// 如果路径不存，就创建
		if (!toFileDri.exists()) {
			toFileDri.mkdirs();
		}
		// 提取文件名
		String[] fileArray = fromFileName.split(Pattern.quote("/"));
		String toFileName = fileArray[fileArray.length - 1];
		File toFile = new File(toFileDri, toFileName);
		copyFileUsingFileChannels(fromFiler, toFile);
		return toFilePath + toFileName;
	}

	/**
	 * 图片copy
	 * 
	 * @param source
	 * @param dest
	 * @throws IOException
	 */
	private static void copyFileUsingFileChannels(File source, File dest) {
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(source);
			fos = new FileOutputStream(dest);
			inputChannel = fis.getChannel();
			outputChannel = fos.getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				fis.close();
				fos.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

}
