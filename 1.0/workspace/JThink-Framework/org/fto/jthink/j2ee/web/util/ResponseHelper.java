/*
 * ResponseHelper.java  2008-8-16
 *
 * 版权所有: 自由思考组织(FTO)软件团队 2000-2008, 保留所有权利.
 * 这个软件是自由思考组织(FTO)软件团队开发的，如果要使用这个软件，请首先阅读并接受许可协议。
 *
 * Copyright 2000-2005 FTO Software Team, Inc. All Rights Reserved.
 * This software is the proprietary information of FTO Software Team, Inc.
 * Use is subject to license terms.
 *
 * FTO站点：http://www.free-think.org
 */
package org.fto.jthink.j2ee.web.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;

/**
 * Response相关工具方法, 向HTTP Response发送文件，发送图像文件，发送流数据等。
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
public class ResponseHelper {
  private ResponseHelper(){}
  

  /**
   * 发送一个图像文件内容到HTTP Response.
   *
   * @param response  HTTP response对象.
   * @param file  图像文件
   *
   * @throws IOException if there is an I/O problem.
   */
  public static void sendImage(HttpServletResponse response, File file)
          throws IOException {

      String mimeType = null;
      String filename = file.getName();
      
      if (filename.length() > 5) {
        String expand=filename.substring(filename.length() - 4, 
            filename.length());
        
          if (("."+expand).equalsIgnoreCase(".jpeg")) {
              mimeType = "image/jpeg";
          } 
          else if (expand.equalsIgnoreCase(".png")) {
              mimeType = "image/png";
          }
          else if (expand.equalsIgnoreCase(".jpg")) {
              mimeType = "image/jpeg";
          }
          else if (expand.equalsIgnoreCase(".gif")) {
              mimeType = "image/gif";
          }
          else if (expand.equalsIgnoreCase(".bmp")) {
              mimeType = "image/bmp";
          }

      }
      sendFile(response, file, mimeType);
  }

  /**
   * 发送一个文件内容到HTTP Response.
   *
   * @param response  HTTP response对象.
   * @param file  文件
   * @param mimeType  文件mime type, 允许为null.
   *
   * @throws IOException if there is an I/O problem.
   */
  public static void sendFile(HttpServletResponse response, File file, String mimeType) throws IOException {
      if (file.exists()) {
        BufferedInputStream bis=null;
        BufferedOutputStream bos = null;
        try{
          bis = new BufferedInputStream(new FileInputStream(file));
          //  Set HTTP headers
          if (mimeType != null) {
              response.setHeader("Content-Type", mimeType);
          }
          response.setHeader("Content-Length", String.valueOf(file.length()));
          SimpleDateFormat sdf = new SimpleDateFormat(
              "EEE, dd MMM yyyy HH:mm:ss z"
          );
          sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
          response.setHeader(
              "Last-Modified", sdf.format(new Date(file.lastModified()))
          );

          bos = new BufferedOutputStream(
              response.getOutputStream()
          );
          byte[] input = new byte[1024];
          boolean eof = false;
          while (!eof) {
              int length = bis.read(input);
              if (length == -1) {
                  eof = true;
              } 
              else {
                  bos.write(input, 0, length);
              }
          }
          bos.flush();
        }finally{
          if(bis!=null){
            bis.close();
          }
          if(bos!=null){
            bos.close();
          }
        }
      }else {
          throw new FileNotFoundException(file.getAbsolutePath());
      }
  }
  
  
  /**
   * 发送流数据到HTTP Response.
   *
   * @param response  HTTP response对象.
   * @param is  流数据
   * @param mimeType  文件mime type, 允许为null.
   *
   * @throws IOException if there is an I/O problem.
   */
  public static void send(HttpServletResponse response, InputStream is, String mimeType) throws IOException {
        BufferedInputStream bis=null;
        BufferedOutputStream bos = null;
        try{
          bis = new BufferedInputStream(is);
          //  Set HTTP headers
          if (mimeType != null) {
              response.setHeader("Content-Type", mimeType);
          }

          bos = new BufferedOutputStream(
              response.getOutputStream()
          );
          byte[] input = new byte[1024];
          boolean eof = false;
          while (!eof) {
              int length = bis.read(input);
              if (length == -1) {
                  eof = true;
              } 
              else {
                  bos.write(input, 0, length);
              }
          }
          bos.flush();
        }finally{
          if(bis!=null){
            bis.close();
          }
          if(bos!=null){
            bos.close();
          }
        }
  }
}






