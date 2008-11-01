/*
 * FileHelper.java  2008-8-16
 *
 * 版权所有: 自由思考组织(FTO)软件团队 2000-2005, 保留所有权利.
 * 这个软件是自由思考组织(FTO)软件团队开发的，如果要使用这个软件，请首先阅读并接受许可协议。
 *
 * Copyright 2000-2008 FTO Software Team, Inc. All Rights Reserved.
 * This software is the proprietary information of FTO Software Team, Inc.
 * Use is subject to license terms.
 *
 * FTO站点：http://www.free-think.org
 */
package org.fto.jthink.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.fto.jthink.exception.JThinkRuntimeException;

/**
 * 方便读写文件，处理编码格式<br>
 * 
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2008-08-21  创建此类型
 * </pre></p>
 * 
 * 
 * @author   try.jiwen
 * @version  1.0
 * @since    JThink 1.0
 * 
 */
public class FileHelper {
  private FileHelper(){}
  
  
  /**
   * 以字节数组方式读出文件所有内容
   * 
   * @param file 文件
   * @return 返回文件字节数组内容
   */
  public static byte[] readBytes(File file){
    BufferedInputStream bis = null;
    try {
      bis = new BufferedInputStream(new FileInputStream(file));
      byte[] content = new byte[(int)file.length()];
      bis.read(content);
      return content;
    } catch (Exception ex) {
      throw new JThinkRuntimeException(ex.getMessage(), ex);
    } finally {
      try {
        bis.close();
      } catch (IOException ex) {
        throw new JThinkRuntimeException(ex.getMessage(), ex);
      }
    }
  }
  
  /**
   * 以串的方法读出文件所有内容
   * 
   * @param file 文件
   * @param enc 文件编码方式
   * @return 返回文件内容
   */
  public static String readContent(File file, String enc){
    BufferedInputStream bis = null;
    try {
      bis = new BufferedInputStream(new FileInputStream(file));
      byte[] content = new byte[(int)file.length()];
      bis.read(content);
      if(enc!=null){
        return new String(content, enc);
      }else{
        return new String(content);
      }
    } catch (Exception ex) {
      throw new JThinkRuntimeException(ex.getMessage(), ex);
    } finally {
      try {
        bis.close();
      } catch (IOException ex) {
        throw new JThinkRuntimeException(ex.getMessage(), ex);
      }
    }
  }
  /**
   * 以串的方法读出文件所有内容
   * 
   * @param file 文件
   * @return 返回文件内容
   */
  public static String readContent(File file){
    return readContent(file, null);
  }
  
  /**
   * 写串内容到文件
   * 
   * @param file 文件
   * @param content 内容
   */
  public static void writeContent(File file, String content){
    writeContent(file, content, null);
  }
  
  /**
   * 写串内容到文件
   * 
   * @param file 文件
   * @param content 内容
   * @param encode 文件编码方式
   */
  public static void writeContent(File file, String content, String encode){
    OutputStreamWriter writer = null;
    try{
      OutputStream os = new FileOutputStream(file);
      if(encode!=null){
        writer = new OutputStreamWriter(os, encode);
      }else{
        writer = new OutputStreamWriter(os);
      }
      writer.write(content);
      writer.flush();
    }catch(Exception e){
      if(writer!=null){
        try {
          writer.close();
        } catch (IOException ex) {
          throw new JThinkRuntimeException(ex.getMessage(), ex);
        }
      }
    }
  }
  
  /**
   * 写字节数据内容到文件
   * 
   * @param file 写入的文件
   * @param bytes 写入的字节内容
   */
  public static void writeBytes(File file, byte[] bytes){
    OutputStream os = null;
    try{
      os = new FileOutputStream(file);
      os.write(bytes);
      os.flush();
    }catch(Exception e){
      if(os!=null){
        try {
          os.close();
        } catch (IOException ex) {
          throw new JThinkRuntimeException(ex.getMessage(), ex);
        }
      }
    }
  }
}
