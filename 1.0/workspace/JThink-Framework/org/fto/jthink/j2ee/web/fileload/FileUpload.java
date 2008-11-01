
/*
 * FileUpload.java	2005-8-1
 *
 * 版权所有: 自由思考组织(FTO)软件团队 2000-2005, 保留所有权利.
 * 这个软件是自由思考组织(FTO)软件团队开发的，如果要使用这个软件，请首先阅读并接受许可协议。
 *
 * Copyright 2000-2005 FTO Software Team, Inc. All Rights Reserved.
 * This software is the proprietary information of FTO Software Team, Inc.
 * Use is subject to license terms.
 *
 * FTO站点：http://www.free-think.org
 */

package org.fto.jthink.j2ee.web.fileload;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.fto.jthink.exception.JThinkRuntimeException;
import org.fto.jthink.io.SmartAccessFile;
import org.fto.jthink.j2ee.web.HttpRequest;
import org.fto.jthink.log.LogManager;
import org.fto.jthink.log.Logger;
import org.fto.jthink.util.StringHelper;


/**
 * 文件上传, 通过HTTP协议上传文件。
 * 在上传文件时,通常是调用HttpRequest.getFileUpload()方法返回FileUpload类型的实例.
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-08-1  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 */

public class FileUpload {
	private static final Logger logger = LogManager.getLogger(FileUpload.class);
	private ServletContext application;
	private HttpRequest request;
	
	/* 被上传文件以及所有请求数据的总字节数 */
	private int totalBytes = 0; 
	/* 文件名关缀 */
	private String preName = "";
	/* 一个文件在总数据块中的开始位置 */
	private int startData;
	/* 一个文件在总数据块中的结束位置 */
	private int endData;
	/* 请求数据块的分隔串 */
	private String boundary;
	/* 文件名称 */
	private String fileName;
	/* 文件路径名称 */
	private String filePathName;
	/* 文件扩展名称 */
	private String fileExt;
	/* 内容类型 */
	private String contentType;
	/* 内容部署信息 */
	private String contentDisp;
	/* 文件MIME值 */
	private String typeMIME;
	/* 文件子MIME值 */
	private String subTypeMIME;
	/* 请求信息的编码方式 */
	private String encode;
	
	/* 被拒绝上传的文件扩展名列表 */
	private Vector deniedFilesList;
	/* 被允许上传的文件扩展名列表 */
	private Vector allowedFilesList;
	/* 被允许上传的最大单个文件大小 */
	private long maxFileSize=0;
	/* 被允许请求的总内容大小 */
	private long maxContentLength = 0;
	/* 是否拒绝物理路径 */
	private boolean denyPhysicalPath = false;
	/* 是否允许上传文件 */
	
	/* 临时文件 */
	protected java.io.File tempFile = null;
	/* 所有被上传的文件 */
	private List files = new ArrayList();
	
	/** 文件保存路径的类型,自动处理 */
	public static final int SAVE_AUTO = 0;
	/** 文件保存路径的类型,虚拟路径 */
	public static final int SAVE_VIRTUAL = 1;
	/** 文件保存路径的类型,物理路径 */
	public static final int SAVE_PHYSICAL = 2;
	
	/**
	 * 创建FileUpload的实例
	 * 
	 * @param config ServletConfig类型的实例
	 * @param request HttpRequest类型的实例
	 */
	public FileUpload(ServletConfig config, HttpRequest request){
		this(config.getServletContext(), request);
	}
	
	
	/**
	 * 创建FileUpload的实例
	 * 
	 * @param application  ServletContext类型的实例
	 * @param request HttpRequest类型的实例
	 */
	public FileUpload(ServletContext application, HttpRequest request) {
		this.application = application;
		this.request = request;
		this.encode = request.getServletRequest().getCharacterEncoding();
	}

	/**
	 * 设置文件名前缀
	 * 
	 * @param preName 前缀值
	 */
	public void setPrefixionName(String preName) {
		this.preName = preName;
	}
	
	/**
	 * 是否拒绝物理路径
	 * 
	 * @param deny true 拒绝, false 允许
	 */
	public void setDenyPhysicalPath(boolean deny) {
		denyPhysicalPath = deny;
	}

	/**
	 * 设置单个最大允许的被上传的文件大小
	 * 
	 * @param maxFileSize 文件大小
	 */
	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	/**
	 * 设置被允许的所有请求内容的长度
	 * 
	 * @param maxContentLength 请求内容长度
	 */
	public void setMaxContentLength(long maxContentLength){
		this.maxContentLength = maxContentLength;
	}
	
	/**
	 * 上传文件. 此类型被实例化后,还必须调用此方法才能正确上传文件
	 */
	public void upload(){
		SmartAccessFile raf = null;
//		long time = System.currentTimeMillis();
		try{
			
			/* 创建临时文件 */
			tempFile = java.io.File.createTempFile("httpUpload", ".tmp");
			raf = new SmartAccessFile(tempFile, "rw");
			/* 返回总字节数量 */
			totalBytes = request.getServletRequest().getContentLength();
//			logger.debug("流失时间: "+(System.currentTimeMillis()-time));
			if (maxContentLength > (long)0 	&& totalBytes > maxContentLength){
				request.getServletRequest().getInputStream().close();
				throw new JThinkRuntimeException("Total request content size exceeded.");	//请求的内容太大，不允许上传文件
			}
			
			/* 读入所有字节数据，并保存到临时文件 */
			InputStream is = request.getServletRequest().getInputStream();
			int readBytes = 1024*256;
			if(totalBytes>1024*256 && totalBytes<=1024*512){
				readBytes = 1024*512;
			}else if(totalBytes>1024*512 && totalBytes<=1048576){
				readBytes = 1048576;
			}else if(totalBytes>1048576 && totalBytes<=1048576*5){
				readBytes = 1048576*2;
			}else if(totalBytes>=1048576*5){
				readBytes = 1048576*5;
			}

			int tmpReadBytes = 0;
			byte[] bs = new byte[readBytes];
			for(int currBytes=0;currBytes<totalBytes;){
				int count = is.read(bs, tmpReadBytes, readBytes-tmpReadBytes);
				currBytes += count;
				tmpReadBytes += count;
				if(tmpReadBytes==readBytes || currBytes>=totalBytes){
					raf.write(bs, 0, tmpReadBytes);
					tmpReadBytes = 0;
				}
			}

			/* 查找请求数据的开始索引位置和请求数据块间的分隔串 */
			raf.seek(0);
			int v = raf.read();
			boundary = "";
			while(v!=13 && raf.getFilePointer() < totalBytes){
				boundary += (char)v; 
				v = raf.read();
			}
			
			/* 如果没有任何请求信息, 直接返回 */
			if(raf.getFilePointer()==1 || raf.getFilePointer()>=raf.length()){
				return;
			}
			while(true){
				if (raf.getFilePointer() >= totalBytes){
					break;
				}
				/* 返回字段头信息 */
				String dataHeader = getDataHeader(raf);
				boolean isFile = dataHeader.indexOf("filename") > 0;
				String fieldName = getDataFieldValue(dataHeader, "name");
				if (isFile) {
					filePathName = getDataFieldValue(dataHeader, "filename");
					fileName = preName + getFileName(filePathName);
					fileExt = getFileExt(fileName);
					contentType = getContentType(dataHeader);
					contentDisp = getContentDisp(dataHeader);
					typeMIME = getTypeMIME(contentType);
					subTypeMIME = getSubTypeMIME(contentType);
				}

				/* 处理数据片段 */
				getDataSection(raf);
				
				/* 判断文件是否被允许上传 */
				if (isFile && fileName.length() > 0) {
					if (deniedFilesList!=null && deniedFilesList.contains(fileExt)){
						throw new JThinkRuntimeException(
								"The extension of the file is denied to be uploaded.");//不允许上传此种类型的文件
					}
					if (allowedFilesList!=null && !allowedFilesList.contains(fileExt)){
						throw new JThinkRuntimeException(
								"The extension of the file is not allowed to be uploaded.");//不允许上传此种类型的文件
					}
					
					if (maxFileSize > (long) 0	&& (long) ((endData - startData) + 1) > maxFileSize){
						throw new JThinkRuntimeException(String.valueOf(
								(new StringBuffer("Size exceeded for this file : ")).append(fileName)));//文件太大，不允许上传
					}
				}
				
				if (isFile) {
					/* 生成被上传的单个临时文件 */
					org.fto.jthink.j2ee.web.fileload.File newFile = new org.fto.jthink.j2ee.web.fileload.File();
					newFile.setParent(this);
					newFile.setFieldName(fieldName);
					newFile.setFileName(fileName);
					newFile.setFileExt(fileExt);
					newFile.setFilePathName(filePathName);
					newFile.setIsMissing(filePathName.length() == 0);
					newFile.setContentType(contentType);
					newFile.setContentDisp(contentDisp);
					newFile.setTypeMIME(typeMIME);
					newFile.setSubTypeMIME(subTypeMIME);
					if (contentType.indexOf("application/x-macbinary") > 0){
						startData = startData + 128;
					}
					newFile.setSize((endData - startData));
					newFile.setStartData(startData);
					newFile.setEndData(endData);
					files.add(newFile);
					
				} else {
					/* 生成非文件的请求信息数据 */
					long tmpPointer = raf.getFilePointer();
					raf.seek(startData);
					byte[] values = new byte[(endData - startData)];
					raf.read(values);
					String value;
					if(encode==null){
						value = new String(values);
					}else{
						value = new String(values, encode);
					}
					request.putParameter(fieldName, value);
					raf.seek(tmpPointer);
				}
				
				raf.skipBytes(1);
				char c = (char)raf.read();
				/* 如果遇到'-'字符,跳出循环 */
				if (c == '-'){
					raf.skipBytes(-1);
					break;
				}
			}
			
			
		}catch(JThinkRuntimeException e){
			logger.error("上传文件时异常!", e);
			throw e;
		}catch(Exception e){
			close();
			logger.error("上传文件时异常!", e);
			throw new JThinkRuntimeException(e);
			
		}finally{
			closeSmartAccessFile(raf);
//			logger.debug("总流失时间: "+(System.currentTimeMillis()-time));
		}
	}	
	
	/**
	 * 关闭SmartAccessFile
	 * @param saf
	 */
	private void closeSmartAccessFile(SmartAccessFile saf){
		/* 关闭SmartAccessFile */
		if(saf!=null){
			try {
				saf.close();
			} catch (IOException e1) {
				logger.error("关闭RandomAccessFile时异常!", e1);
				throw new JThinkRuntimeException(e1);
			}
		}
	}
	
	/**
	 * 关闭FileUpload, 在上传文件处理完成成后,必须调用此方法执行清理工作
	 */
	public void close(){
		if(tempFile!=null){
			tempFile.delete();
		}
	}

	/**
	 * 保存被上传的所有文件
	 * 
	 * @param destPathName 目标路径名称
	 * 
	 * @return 返回被成功保存的文件数量
	 */
	public int save(String destPathName){
		return save(destPathName, 0);
	}

	/**
	 * 保存被上传的所有文件
	 * 
	 * @param destPathName 目标路径名称
	 * @param option 目标路径类型,值有:SAVE_AUTO,SAVE_VIRTUAL,SAVE_PHYSICAL
	 * 
	 * @return 返回被成功保存的文件数量
	 */
	public int save(String destPathName, int option) {
		int count = 0;
		if (destPathName == null){
			destPathName = application.getRealPath("/");
		}
		if (destPathName.indexOf("/") != -1) {
			if (destPathName.charAt(destPathName.length() - 1) != '/'){
				destPathName = String.valueOf(destPathName).concat("/");
			}
		} else if (destPathName.charAt(destPathName.length() - 1) != '\\'){
			destPathName = String.valueOf(destPathName).concat("\\");
		}
		for (int i = 0; i < files.size(); i++){
			File file = (File)files.get(i);
			if (!file.isMissing()) {
				file.saveAs(
								destPathName + file.getFileName(),option);
				count++;
			}
		}
		return count;
	}

	/**
	 * 返回被请求的数据总长度
	 * 
	 * @return 数据总长度
	 */
	public int getSize() {
		return totalBytes;
	}

	/**
	 * 返回所有上传的文件
	 * 
	 * @return 所有上传的文件
	 */
	public List getFiles() {
		return files;
	}	
	
	/**
	 * 返回数据头信息
	 */
	private String getDataHeader(SmartAccessFile raf) throws IOException {
		long start = raf.getFilePointer();
		long end = 0;
		while (true){
			int flag1 = raf.read();
			raf.skipBytes(1);
			int flag2 = raf.read();
			raf.skipBytes(-2);
			if (flag1 == 13 && flag2 == 13) {
				end = raf.getFilePointer() - 1;
				raf.skipBytes(2);
				break;
			}
		}
		/* 生成头信息串 */
		long tmpFilePointer = raf.getFilePointer();
		raf.seek(start);
		byte[] bs = new byte[(int)(end - start) + 1];
		raf.read(bs);
		String dataHeader;
		if(encode==null){
			dataHeader = new String(bs);
		}else{
			dataHeader = new String(bs, encode);
		}
		raf.seek(tmpFilePointer);
		return dataHeader;
	}

	/**
	 *  返回数据字段信息 
	 */
	private String getDataFieldValue(String dataHeader, String fieldName) {
		String token = new String();
		String value = new String();
		int pos = 0;
		int i = 0;
		int start = 0;
		int end = 0;
		token = String.valueOf((new StringBuffer(String.valueOf(fieldName)))
				.append("=").append('"'));
		pos = dataHeader.indexOf(token);
		if (pos > 0) {
			i = pos + token.length();
			start = i;
			token = "\"";
			end = dataHeader.indexOf(token, i);
			if (start > 0 && end > 0)
				value = dataHeader.substring(start, end);
		}
		return value;
	}

	/**
	 *  返回数据片段 
	 */
	private void getDataSection(SmartAccessFile raf) throws IOException {
//		long time = System.currentTimeMillis();
		raf.skipBytes(1);
		startData = (int)raf.getFilePointer();
		byte[] bs = boundary.getBytes();
		endData = (int)raf.indexOf(bs)-2;
		raf.seek(endData+bs.length+1);
//		logger.debug("流失时间: "+(System.currentTimeMillis()-time));
	}

	/**
	 * 返回文件名称
	 */
	private String getFileName(String filePathName) {
		int pos = 0;
		pos = filePathName.lastIndexOf('/');
		if (pos != -1)
			return filePathName.substring(pos + 1, filePathName.length());
		pos = filePathName.lastIndexOf('\\');
		if (pos != -1)
			return filePathName.substring(pos + 1, filePathName.length());
		else
			return filePathName;
	}	

	/**
	 * 返回文件扩展名称
	 */
	private String getFileExt(String fileName) {
		String value = new String();
		int start = 0;
		int end = 0;
		if (fileName == null)
			return null;
		start = fileName.lastIndexOf('.') + 1;
		end = fileName.length();
		value = fileName.substring(start, end);
		logger.debug("getFileExt().ext="+value);
		if (fileName.lastIndexOf('.') > 0)
			return value;
		else
			return "";
	}

	/**
	 * 返回文件类型
	 */
	private String getContentType(String dataHeader) {
		String token = new String();
		String value = new String();
		int start = 0;
		int end = 0;
		token = "Content-Type:";
		start = dataHeader.indexOf(token) + token.length();
		if (start != -1) {
			end = dataHeader.length();
			value = dataHeader.substring(start, end);
		}
		return value;
	}

	/**
	 * 返回MIME类型
	 */
	private String getTypeMIME(String ContentType) {
		int pos = 0;
		pos = ContentType.indexOf("/");
		if (pos != -1)
			return ContentType.substring(1, pos);
		else
			return ContentType;
	}

	/**
	 * 返回子MIME类型
	 */
	private String getSubTypeMIME(String ContentType) {
		int start = 0;
		int end = 0;
		start = ContentType.indexOf("/") + 1;
		if (start != -1) {
			end = ContentType.length();
			return ContentType.substring(start, end);
		} else {
			return ContentType;
		}
	}
	
	/**
	 * 返回部署信息
	 */
	private String getContentDisp(String dataHeader) {
		String value = new String();
		int start = 0;
		int end = 0;
		start = dataHeader.indexOf(":") + 1;
		end = dataHeader.indexOf(";");
		value = dataHeader.substring(start, end);
		return value;
	}
	
	/**
	 * 设置不允许上传的文件类型
	 * 
	 * @param deniedFilesListStr 用逗号(,)分隔的文件扩展名列表 
	 */
	public void setDeniedFilesList(String deniedFilesListStr)
			throws SQLException, IOException, ServletException {
		if (deniedFilesList == null) {
			deniedFilesList = new Vector();
		}
		String[] deniedFileExtNames = StringHelper.split(deniedFilesListStr, ",");
		for(int i=0;i<deniedFileExtNames.length;i++){
			deniedFilesList.add(deniedFileExtNames[i]);
		}
	}

	/**
	 * 设置允许上传的文件类型
	 * 
	 * @param allowedFilesListStr 用逗号(,)分隔的文件扩展名列表 
	 */
	public void setAllowedFilesList(String allowedFilesListStr) {
		if (allowedFilesList == null) {
			allowedFilesList = new Vector();
		}
		String[] allowedFileExtNames = StringHelper.split(allowedFilesListStr, ",");
		for(int i=0;i<allowedFileExtNames.length;i++){
			allowedFilesList.add(allowedFileExtNames[i]);
		}
	}	
	
	/**
	 * 返回物理路径
	 */
	protected String getPhysicalPath(String filePathName, int option)
			throws IOException {
		String path = new String();
		String fileName = new String();
		String fileSeparator = new String();
		boolean isPhysical = false;
		fileSeparator = System.getProperty("file.separator");
		if (filePathName == null)
			throw new IllegalArgumentException(
					"There is no specified destination file.");
		if (filePathName.equals(""))
			throw new IllegalArgumentException(
					"There is no specified destination file.");
		if (filePathName.lastIndexOf("\\") >= 0) {
			path = filePathName.substring(0, filePathName.lastIndexOf("\\"));
			fileName = filePathName
					.substring(filePathName.lastIndexOf("\\") + 1);
		}
		if (filePathName.lastIndexOf("/") >= 0) {
			path = filePathName.substring(0, filePathName.lastIndexOf("/"));
			fileName = filePathName
					.substring(filePathName.lastIndexOf("/") + 1);
		}
		path = path.length() != 0 ? path : "/";
		java.io.File physicalPath = new java.io.File(path);
		if (physicalPath.exists())
			isPhysical = true;
		if (option == 0) {
			if (isVirtual(path)) {
				path = application.getRealPath(path);
				if (path.endsWith(fileSeparator))
					path = path + fileName;
				else
					path = String.valueOf((new StringBuffer(String
							.valueOf(path))).append(fileSeparator).append(
							fileName));
				return path;
			}
			if (isPhysical) {
				if (denyPhysicalPath)
					throw new IllegalArgumentException(
							"Physical path is denied.");
				else
					return filePathName;
			} else {
				throw new IllegalArgumentException(
						"This path does not exist.");
			}
		}
		if (option == 1) {
			if (isVirtual(path)) {
				path = application.getRealPath(path);
				if (path.endsWith(fileSeparator))
					path = path + fileName;
				else
					path = String.valueOf((new StringBuffer(String
							.valueOf(path))).append(fileSeparator).append(
							fileName));
				return path;
			}
			if (isPhysical)
				throw new IllegalArgumentException(
						"The path is not a virtual path.");
			else
				throw new IllegalArgumentException(
						"This path does not exist.");
		}
		if (option == 2) {
			if (isPhysical)
				if (denyPhysicalPath)
					throw new IllegalArgumentException(
							"Physical path is denied.");
				else
					return filePathName;
			if (isVirtual(path))
				throw new IllegalArgumentException(
						"The path is not a physical path.");
			else
				throw new IllegalArgumentException(
						"This path does not exist.");
		} else {
			return null;
		}
	}
	
	/**
	 * 判断是否虚拟目录
	 */
	private boolean isVirtual(String pathName) {
		if (application.getRealPath(pathName) != null) {
			java.io.File virtualFile = new java.io.File(application.getRealPath(pathName));
			return virtualFile.exists();
		} else {
			return false;
		}
	}
	
}
