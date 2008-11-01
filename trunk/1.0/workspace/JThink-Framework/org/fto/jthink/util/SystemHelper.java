package org.fto.jthink.util;

import java.util.Properties;

/**
 * 系统环境相关，比如检查java版本，支持class版本等<br>
 * 
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2008-10-27  创建此类型
 * 
 * </pre></p>
 * 
 * 
 * @author   abnerchai, wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */

public class SystemHelper {
  private SystemHelper(){}
  static Object lock = new Object();
  /**
   * 返回系统属性信息
   */
  public static Properties getProperties(){
    if(properties==null){
      synchronized(lock){
        return System.getProperties();
      }
    }
    return properties;
  }
  private static Properties properties=null;
  
  /**
   * 返回Class版本号
   */
  public static double getJavaClassVersion(){
    if(javaClassVersion==0){
      synchronized(lock){
        javaClassVersion = Double.parseDouble(System.getProperty("java.class.version"));
      }
    }
    return javaClassVersion;
  }
  private static double javaClassVersion=0;
  
  /**
   * 返回java主版本号
   */
  public static double getJavaSpecificationVersion(){
    if(javaspecificationVersion==0){
      synchronized(lock){
        javaspecificationVersion = Double.parseDouble(System.getProperty("java.specification.version"));
      }
    }
    return javaspecificationVersion;
  }
  private static double javaspecificationVersion = 0;
  
  /**
   * 返回文件路径中的分隔字符
   */
  public static String getFileSeparator(){
    if(fileSeparator==null){
      synchronized(lock){
        fileSeparator = System.getProperty("file.separator");
      }
    }
    return fileSeparator;
  }
  private static String fileSeparator=null;
}
