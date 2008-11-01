/*
 * File.java	2005-8-31
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;



import org.fto.jthink.exception.JThinkRuntimeException;
import org.fto.jthink.log.LogManager;
import org.fto.jthink.log.Logger;

/**
 * 描述通过HTTP上传的文件。此类型并不代表上传文件实体，但它描述了具体文件的相关信息，
 * 可以方便的保存文件。
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-08-31  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 * 
 */

public class File {
	
	private static final Logger logger = LogManager.getLogger(File.class);
	

	File() {
		isMissing = true;
	}

	/**
	 * 保存文件，以给定的文件名保存文件
	 * 
	 * @param destFilePathName 保存的目标文件名称，包含路径名
	 */
	public void saveAs(String destFilePathName){
		saveAs(destFilePathName, FileUpload.SAVE_AUTO);
	}

	/**
	 * 保存文件，以给定的文件名保存文件
	 * 
	 * @param destFilePathName 保存的目标文件名称，包含路径名
	 * @param optionSaveAs 目标位置类型，可用的值有：
	 *                                  FileUpload.SAVE_AUTO
	 *                                  FileUpload.SAVE_VIRTUAL
	 *                                  FileUpload.SAVE_PHYSICAL
	 * 
	 */
	public void saveAs(String destFilePathName, int optionSaveAs){
		RandomAccessFile raf = null;
		FileOutputStream fileOut = null;
		
		try {
			raf = new RandomAccessFile(parent.tempFile, "r");
			String path = parent.getPhysicalPath(destFilePathName, optionSaveAs);
			if (path == null){
				throw new IllegalArgumentException(
						"There is no specified destination file (1140).");
			}
			java.io.File file = new java.io.File(path);
			fileOut = new FileOutputStream(file);
			raf.seek(startData);
			int readbytes = 1048576*3;
			byte[] bs = getSize()<readbytes?new byte[getSize()]:new byte[readbytes];
			while(raf.getFilePointer()<endData){
				int tcount = endData-(int)raf.getFilePointer();
				int bcount = tcount>readbytes?readbytes:tcount;
				int count = raf.read(bs, 0, bcount);
				fileOut.write(bs, 0, count);
			}
			
		} catch (IOException e) {
			throw new JThinkRuntimeException("File can't be saved (1120).", e);
		}finally{
			if(fileOut!=null){
				try {
					fileOut.close();
				} catch (IOException e1) {
					logger.error("关闭FileOutputStream时异常!", e1);
					throw new JThinkRuntimeException(e1);
				}
			}
			
			if(raf!=null){
				try {
					raf.close();
				} catch (IOException e1) {
					logger.error("关闭RandomAccessFile时异常!", e1);
					throw new JThinkRuntimeException(e1);
				}
			}
		}
	}

	
	/**
	 * 文件是否可用
	 */
	protected boolean isMissing() {
		return isMissing;
	}

	/**
	 * 返回请求字段名称
	 * 
	 * @return 字段名称
	 */
	public String getFieldName() {
		return fieldname;
	}

	/**
	 * 返回文件名称
	 * 
	 * @return 文件名称
	 */
	public String getFileName() {
		return filename;
	}

	/**
	 * 返回文件路径名称
	 * 
	 * @return 文件文件路径名称
	 */
	public String getFilePathName() {
		return filePathName;
	}

	/**
	 * 返回文件扩展名称
	 * 
	 * @return 文件扩展名称
	 */
	public String getFileExt() {
		return fileExt;
	}

	/**
	 * 返回文件内容类型
	 * 
	 * @return 内容类型
	 */
	public String getContentType() {
		return contentType;
	}


	public String getContentDisp() {
		return contentDisp;
	}

//	public String getContentString() {
//		String strTMP = new String(parent.binArray, startData, size);
//		return strTMP;
//	}

	
	/**
	 * 返回文件MIME类型
	 * 
	 * @return MIME类型
	 */
	public String getTypeMIME() throws IOException {
		return typeMime;
	}

	/**
	 * 返回文件MIME子类型
	 * 
	 * @return MIME子类型
	 */
	public String getSubTypeMIME() {
		return subTypeMime;
	}

	/**
	 * 返回文件大小
	 * 
	 * @return 文件大小
	 */
	public int getSize() {
		return size;
	}

	protected int getStartData() {
		return startData;
	}

	protected int getEndData() {
		return endData;
	}

	protected void setParent(FileUpload parent) {
		this.parent = parent;
	}

	protected void setStartData(int startData) {
		this.startData = startData;
	}

	protected void setEndData(int endData) {
		this.endData = endData;
	}

	protected void setSize(int size) {
		this.size = size;
	}

	protected void setIsMissing(boolean isMissing) {
		this.isMissing = isMissing;
	}

	protected void setFieldName(String fieldName) {
		this.fieldname = fieldName;
	}

	protected void setFileName(String fileName) {
		this.filename = fileName;
	}

	protected void setFilePathName(String filePathName) {
		this.filePathName = filePathName;
	}

	protected void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	protected void setContentType(String contentType) {
		this.contentType = contentType;
	}

	protected void setContentDisp(String contentDisp) {
		this.contentDisp = contentDisp;
	}

	protected void setTypeMIME(String TypeMime) {
		this.typeMime = TypeMime;
	}

	protected void setSubTypeMIME(String subTypeMime) {
		this.subTypeMime = subTypeMime;
	}

//	public byte getBinaryData(int index) {
//		if (startData + index > endData)
//			throw new ArrayIndexOutOfBoundsException(
//					"Index Out of range (1115).");
//		if (startData + index <= endData)
//			return parent.binArray[startData + index];
//		else
//			return 0;
//	}

	private FileUpload parent;

	private int startData;

	private int endData;

	private int size;

	private String fieldname;

	private String filename;

	private String fileExt;

	private String filePathName;

	private String contentType;

	private String contentDisp;

	private String typeMime;

	private String subTypeMime;
	
//	private String contentString;

	private boolean isMissing;

//	public static final int SAVEAS_AUTO = 0;
//
//	public static final int SAVEAS_VIRTUAL = 1;
//
//	public static final int SAVEAS_PHYSICAL = 2;
}

