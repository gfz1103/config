package com.buit.file;

import com.buit.config.file.FileUploadProperties;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.linuxense.javadbf.DBFDataType;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFWriter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 文件系统存储实现
 * 
 * @author
 */
@Service
public class IReportExportFileSer {

	static final Logger logger = LoggerFactory.getLogger(IReportExportFileSer.class);
	@Autowired
	private FileUploadProperties fileUploadConfig;
	@Autowired
	private FileUploadService fileUploadSer;

	/**
	 * 从jar包中取文件
	 */
	public File getFile(String fileName) {
		// 编译jrxml文件，生成jasper文件
//	     URL url = this.getClass().getClassLoader().getResource("jasperreport/test.jrxml");
//	     JasperCompileManager.compileReportToFile(url.getPath());
		if(fileName.contains("/")||fileName.contains("\\")) {
			return new File(fileName);
		}
		String folder = System.getProperty("java.io.tmpdir");
		File jasperFile = new File(folder + File.separator + fileName);
		try {
			jasperFile.createNewFile();
			ClassPathResource classPathResource = new ClassPathResource("jrxml/" + fileName);
			FileUtils.copyInputStreamToFile(classPathResource.getInputStream(), jasperFile);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
//	     File jasperFile = ResourceUtils.getFile("classpath:jrxml/GhfkstjView.jasper");
		return jasperFile;
	}

	public InputStream getFileInputStream(String fileName) {
		return this.getClass().getClassLoader().getResourceAsStream(fileName);
	}
	

	/**
	 * 导出到html文件.
	 * 
	 * @param list   数据集
	 * @param params 报表其他参数
	 * @return 返回htmlURL路径
	 */
	public String reportHtml(List<Map<String, Object>> list, Map<String, Object> params, String jasperName) {
		JRDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(list);
		String tempFile = fileUploadConfig.getTempFile() + fileUploadSer.getFileName() + ".html";
		try {
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getFile(jasperName));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, jrBeanCollectionDataSource);
			JasperExportManager.exportReportToHtmlFile(jasperPrint, fileUploadConfig.getFileBaseDir() + tempFile);
		} catch (JRException e) {
			logger.error(e.getMessage(), e);
		}
		return fileUploadConfig.getHttpUrl() + tempFile;
	}

	public void reportHtmlForStream(List<Map<String, Object>> list, Map<String, Object> params, String jasperName,HttpServletResponse response) {
		JRDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(list);
		response.setContentType("text/html;charset=UTF-8");
		try {
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getFileInputStream(jasperName));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, jrBeanCollectionDataSource);
			HtmlExporter exporter = new HtmlExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getOutputStream()));
			exporter.exportReport();
		} catch (JRException | IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void reportHtmlForStream(Map<String, Object> params, String jasperName,
			HttpServletResponse response) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.add(new HashMap<String, Object>());
		reportHtmlForStream(list, params, jasperName, response);
	}
	
	
	
	/**
	 * 带子报表导出 html文件.
	 * 
	 * @param list   数据集
	 * @param params 报表其他参数
	 * @return 返回htmlURL路径
	 */
	public String reportHtml(List<Map<String, Object>> list, Map<String, Object> params, 
			String jasperName,List<String> sonjasperList, List<String> subkeyList) {
		for(Map<String, Object> map : list) {
			for(String key : map.keySet()) {
				for(String subkey : subkeyList) {
					if(key.equals(subkey)) {
						map.put(subkey, conVerDataSource((Collection) map.get(subkey)));
					}
				}
			}
		}
		JRDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(list);
		params.put("SUBREPORT_DIR", System.getProperty("java.io.tmpdir")+ File.separator );
		for (String sonjasper : sonjasperList) {
			getFile(sonjasper);
		}
		String tempFile = fileUploadConfig.getTempFile() + fileUploadSer.getFileName() + ".html";
		try {
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getFile(jasperName));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, jrBeanCollectionDataSource);
			JasperExportManager.exportReportToHtmlFile(jasperPrint, fileUploadConfig.getFileBaseDir() + tempFile);
		} catch (JRException e) {
			logger.error(e.getMessage(), e);
		}
		return fileUploadConfig.getHttpUrl() + tempFile;
	}

	public String reportHtml(Map<String, Object> params, String jasperName) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.add(new HashMap<String, Object>());
		return reportHtml(list, params, jasperName);
	}

	/**
	 * PDF导出.把文件直接输出到response流中
	 * 
	 * @param list   数据集
	 * @param params 报表其他参数
	 */
	public void pdfExport(Map<String, Object> params, String jasperName, HttpServletResponse response) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.add(new HashMap<String, Object>());
		this.pdfExport(list, params, jasperName, response);

	}

	public void pdfExport(List<Map<String, Object>> list, Map<String, Object> params, String jasperName,
			HttpServletResponse response) {
		try (ServletOutputStream ouputStream = response.getOutputStream()) {
			JRDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(list);
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getFile(jasperName));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, jrBeanCollectionDataSource);
			JRPdfExporter pdf = new JRPdfExporter();
			pdf.setExporterOutput(new SimpleOutputStreamExporterOutput(ouputStream));
			pdf.setExporterInput(new SimpleExporterInput(jasperPrint));
			String fileName = new String((jasperName.replace(".jasper", "") + ".pdf").getBytes("utf-8"), "ISO_8859_1");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.setContentType("application/pdf");
			response.setCharacterEncoding("utf-8");
			pdf.exportReport();
		} catch (IOException | JRException e) {
			logger.error(e.getMessage(), e);
		}

	}

	/**
	 * PDF导出到本地文件
	 * 
	 * @param list   数据集
	 * @param params 报表其他参数
	 * @return 返回PDF URL路径
	 */

	public String pdfExportToUrl(List<Map<String, Object>> list, Map<String, Object> params, String jasperName) {
		String tempFile = fileUploadConfig.getTempFile() + fileUploadSer.getFileName() + ".pdf";
		try (FileOutputStream ouputStream = new FileOutputStream(fileUploadConfig.getFileBaseDir() + tempFile);) {
			JRDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(list);
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getFile(jasperName));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, jrBeanCollectionDataSource);
			JRPdfExporter pdf = new JRPdfExporter();
			pdf.setExporterOutput(new SimpleOutputStreamExporterOutput(ouputStream));
			pdf.setExporterInput(new SimpleExporterInput(jasperPrint));
			pdf.exportReport();
		} catch (IOException | JRException e) {
			logger.error(e.getMessage(), e);
		}
		return fileUploadConfig.getHttpUrl() + tempFile;
	}

	public String pdfExportToUrl(Map<String, Object> params, String jasperName) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.add(new HashMap<String, Object>());
		return pdfExportToUrl(list, params, jasperName);
	}

	/**
	 * 利用PDF脚本实现 自动弹出打印预览的效果
	 * 
	 * @param list   数据集
	 * @param params 报表其他参数
	 */

	public void pdfPreviewPrint(List<Map<String, Object>> list, Map<String, Object> params, String jasperName,
			HttpServletResponse response) {
		JRDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(list);
		try (ServletOutputStream ouputStream = response.getOutputStream()) {
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getFile(jasperName));
			byte[] pdfStream = JasperRunManager.runReportToPdf(jasperReport, params, jrBeanCollectionDataSource);
			PdfReader reader = new PdfReader(pdfStream);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(pdfStream.length);
			// 给pdf加上脚本实现自动打印
			StringBuffer script = new StringBuffer();
			script.append("this.print({bUI:false,bSilent:true,bShrinkToFit:false});");
//			script.append("\r\nthis.closeDoc();");
			PdfStamper stamp = new PdfStamper(reader, bos);
			stamp.setViewerPreferences(PdfWriter.HideMenubar | PdfWriter.HideToolbar | PdfWriter.HideWindowUI);
			stamp.addJavaScript(script.toString());
			stamp.setFormFlattening(true);
			stamp.close();
			// 输出pdf
			byte[] bytes = bos.toByteArray();
			if (bytes != null && bytes.length > 0) {
				response.setContentType("application/pdf");
				response.setContentLength(bytes.length);
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				ouputStream.write(bytes, 0, bytes.length);
				ouputStream.flush();
			}
		} catch (DocumentException | IOException | JRException e) {
			logger.error(e.getMessage(), e);
		}
	}

	// 连接打印机打印

//	public void print1(List<Map<String, Object>> list, Map<String, Object> params, String jasperName,HttpServletResponse response)  {
//		try {
//			JRDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(list);
//			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getFile(jasperName));
//			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, jrBeanCollectionDataSource);
//			// 设置打印方向 LANDSCAPE横向 PORTRAIT竖向
//			jasperPrint.setOrientation(OrientationEnum.LANDSCAPE);
//			// withPrintDialog表示在打印的时候是否显示打印机设置对话框
//			JasperPrintManager.printReport(jasperPrint, false);
//		}catch (JRException e) {
//			logger.error(e.getMessage(),e);	
//		}
//	}

	/**
	 * 导出到excel文件.
	 * 
	 * @param list   数据集
	 * @param params 报表其他参数
	 * @return 返回excel URL路径
	 */
	public String exportExcel(List<Map<String, Object>> list, Map<String, Object> params, String jasperName) {
		JRDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(list);
//		ByteArrayOutputStream outPut = new ByteArrayOutputStream();
//		FileOutputStream outputStream = null;
		String tempFile = fileUploadConfig.getTempFile() + fileUploadSer.getFileName() + ".xls";
//		File file = new File(fileUploadConfig.getFileBaseDir()+tempFile);
		try (FileOutputStream ouputStream = new FileOutputStream(fileUploadConfig.getFileBaseDir() + tempFile);) {
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getFile(jasperName));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, jrBeanCollectionDataSource);
			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));// 创建jasperPrint
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(ouputStream));// 生成输出流
			SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
			configuration.setOnePagePerSheet(false);// 设置所有页只打印到一个Sheet中
			configuration.setWhitePageBackground(false);// 设置Excel表格的背景颜色为默认的白色
			configuration.setIgnorePageMargins(true);
			configuration.setRemoveEmptySpaceBetweenRows(true);
			String[] sheetNames = { "sheet1" }; // 自定义sheet名称
			configuration.setSheetNames(sheetNames);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
//			outputStream = new FileOutputStream(file);
//			outputStream.write(outPut.toByteArray());
		} catch (IOException | JRException e) {
			logger.error(e.getMessage(), e);
		}
//		finally {
//			try {
//				outPut.flush();
//				outPut.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		return fileUploadConfig.getHttpUrl() + tempFile;
	}

	public String exportExcel(Map<String, Object> params, String jasperName) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.add(new HashMap<String, Object>());
		return exportExcel(list, params, jasperName);
	}

	/**
	 * excel导出.把文件直接输出到response流中
	 * 
	 * @param list   数据集
	 * @param params 报表其他参数
	 * @return
	 */
	public void exportDownload(List<Map<String, Object>> list, Map<String, Object> params, String jasperName,
			HttpServletResponse response) {
		JRDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(list);
		response.setContentType("application/vnd.ms-excel");
		String fileName = jasperName.replace(".jasper", "") + ".xlsx";
		response.setCharacterEncoding("utf-8");
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		try (ServletOutputStream ouputStream = response.getOutputStream()) {
			fileName = new String(fileName.getBytes("utf-8"), "ISO8859-1");
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getFile(jasperName));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, jrBeanCollectionDataSource);
			JRXlsxExporter exporter = new JRXlsxExporter();
			SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
			configuration.setOnePagePerSheet(false);
			configuration.setWhitePageBackground(false);
			configuration.setIgnorePageMargins(true);
			String[] sheetNames = { "sheet1" };
			configuration.setSheetNames(sheetNames);
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(ouputStream));
			exporter.setConfiguration(configuration);
			exporter.exportReport();
		} catch (IOException | JRException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void exportDownload(Map<String, Object> params, String jasperName, HttpServletResponse response) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.add(new HashMap<String, Object>());
		exportDownload(list, params, jasperName, response);
	}

	// 直接下载dbf文件
	public void dbfWriter(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		   https://blog.csdn.net/sy3345036/article/details/78012323
		DBFField fields[] = new DBFField[3];

		fields[0] = new DBFField();
		fields[0].setName("emp_code");
		fields[0].setType(DBFDataType.CHARACTER);
		fields[0].setLength(10);

		fields[1] = new DBFField();
		fields[1].setName("emp_name");

		fields[1].setType(DBFDataType.CHARACTER);
		fields[1].setLength(20);

		fields[2] = new DBFField();
		fields[2].setName("salary");
		fields[2].setType(DBFDataType.FLOATING_POINT);
		fields[2].setLength(12);
		fields[2].setDecimalCount(2);

		FileOutputStream fos = new FileOutputStream("d:\\test.dbf");
		DBFWriter writer = new DBFWriter(fos, Charset.defaultCharset());
		writer.setFields(fields);

		Object[] rowData = new Object[3];
		rowData[0] = "1000";
		rowData[1] = "John";
		rowData[2] = 5000.00;

		writer.addRecord(rowData);

		rowData = new Object[3];
		rowData[0] = "1001";
		rowData[1] = "Lalit";
		rowData[2] = 3400.00;

		writer.addRecord(rowData);

		rowData = new Object[3];
		rowData[0] = "1002";
		rowData[1] = "Rohit";
		rowData[2] = 7350.00;

		writer.addRecord(rowData);
		writer.close();
		fos.close();
	}
	/**
	* @Title: 报表动态列
	* @Description: 根据传入参数生成动态列
	* @param  detail  列数据
	* @param  params 报表参数
	* @param  head  头信息
	* @param  templateName 模版名称
	* @param  copyCol   从第几列开始动态 从0开始计
	* @param  expType   1:html 2:excel 3:pdf
	* @author 神算子 2020年9月11日15:46:12
	 */
	
	public String dynamicCol(List<Map<String,Object>> detail, Map<String, Object> params,LinkedHashMap<String,String> head,String templateName ,int copyCol,int expType)  {
		JasperDesign design=null;
		File templateFile=getFile(templateName);
		templateName=templateFile.getAbsolutePath();
		try {
			 design= JRXmlLoader.load(templateFile);
		} catch (JRException e) {
			logger.error("加载报表模版报错:" +templateName);
			logger.error(e.getMessage(),e);
		}
		Map<String,Object> firstDetail=null;
		JRDesignBand columnHeaderband =(JRDesignBand) design.getColumnHeader();
		JRDesignStaticText copyHeaderElement=	(JRDesignStaticText)columnHeaderband.getElements()[copyCol];
		JRDesignBand band=(JRDesignBand)design.getDetailSection().getBands()[0];
		JRDesignElement copyElement=	(JRDesignElement)band.getElements()[copyCol];
		
		int detailx = copyElement.getX();
		int detailWidth = copyElement.getWidth();
		
		int heandx = copyHeaderElement.getX();
		int heandWidth = copyHeaderElement.getWidth();
		if(detail!=null&&detail.size()>0) {
			firstDetail=detail.get(0);
		}
		for(Map.Entry<String, String> entry : head.entrySet()){
			JRDesignField shipCountry = new JRDesignField();
			String headKey = entry.getKey();
			shipCountry.setName(headKey);
			if(firstDetail!=null) {
				shipCountry.setValueClass(firstDetail.get(headKey).getClass());
			}else {
				shipCountry.setValueClass(String.class);
			}
			design.getFields();
			try {
				design.addField(shipCountry);
			} catch (JRException e) {
				logger.error("addField error:" +headKey);
				logger.error(e.getMessage(),e);
			}
		    String headValue = entry.getValue();
			JRDesignStaticText headerField=(JRDesignStaticText)copyHeaderElement.clone();
			headerField.setX(heandx);
			headerField.setText(headValue);
			columnHeaderband.addElement(headerField);
			heandx=heandx+heandWidth;
			
			JRDesignExpression	expression = new JRDesignExpression();
			expression.setText("$F{"+headKey+"}");
			JRDesignTextField detailField=(JRDesignTextField)copyElement.clone();
			detailField.setX(detailx);
			detailField.setExpression(expression);
			band.addElement(detailField);
			detailx=detailx+detailWidth;
		}
		columnHeaderband.removeElement(copyHeaderElement);
		band.removeElement(copyElement);
		try {
			JasperCompileManager.writeReportToXmlFile(design,templateName);
			templateName=	templateName.replace(".jrxml", ".jasper");
			JasperCompileManager.compileReportToFile(design, templateName);
//			pdfExportToUrl
//			exportExcel( detail, params, templateName);
			if(expType==1) {//1:html
				return reportHtml(detail, params, templateName);
			}
			if(expType==2) {//2:excel
				return exportExcel( detail, params, templateName);
			}	
			if(expType==3) {//3:pdf
				return pdfExportToUrl( detail, params, templateName);
			}
		} catch (JRException e) {
			logger.error("编译报错:" +templateName);
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	public JRDataSource conVerDataSource(Collection col){
		return new JRMapCollectionDataSource(col);
	}

}
