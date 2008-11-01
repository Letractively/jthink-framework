/*
 * ComparatorHelper.java  2008-9-19
 *
 * 版权所有: 自由思考组织(FTO)软件团队 2000-2005, 保留所有权利.
 * 这个软件是自由思考组织(FTO)软件团队开发的，如果要使用这个软件，请首先阅读并接受许可协议。
 *
 * Copyright 2000-2005 FTO Software Team, Inc. All Rights Reserved.
 * This software is the proprietary information of FTO Software Team, Inc.
 * Use is subject to license terms.
 *
 */
package org.fto.jthink.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 生成指定范围之间的随机数，批量生成随机数。
 * 
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2008-09-19  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */
public class RandomHelper {

  /**
   * 返回随机数
   */
  public static int randomInt(){
    return rnGenerator.nextInt();
  }
  /**
   * 返回从0到scope-1之间的随机数
   */
  public static int randomInt(int scope){
    return rnGenerator.nextInt(scope);
  }

  /**
   * 返回从0到scope-1之间的随机数, 排除excluded中的值
   * 
   * @param scope 生成的随机数范围
   * @param excluded List中的对象必须是Integer类型
   * @return 0到scope-1之间的随机数
   */
  public static int randomInt(int scope, List excluded){
    if(scope<=excluded.size()){
      throw new RuntimeException("随机数范围(scope)不能小于等于被排除数值(excluded.size())列表长度!");
    }
    while(true){
      int r = rnGenerator.nextInt(scope);
      if(excluded.contains(new Integer(r))==false){
        return r;
      }
    }
  }
  /**
   * 返回从0到scope-1之间的随机数, 排除excluded中的值
   * 
   * @param scope  生成的随机数范围
   * @param excluded 被排除开的数值，存在于excluded中的数值将不会被生成
   * @return 0到scope-1之间的随机数
   */
  public static int randomInt(int scope, int[] excluded){
    if(scope<=excluded.length){
      throw new RuntimeException("随机数范围(scope)不能小于等于被排除数值(excluded.size())列表长度!");
    }
    while(true){
      int r = rnGenerator.nextInt(scope);
      if(ArrayHelper.contains(excluded, r)==false){
        return r;
      }
    }
  }

  /**
   * 返回从0到scope-1之间的count个随机数,不能重复
   * 
   * @param scope 生成的随机数范围
   * @param count 一次生成的随机数数量
   * @return 0到scope-1之间的count个随机数
   */
  public static int[] randomInts(int scope, int count){
    if(scope<count){
      throw new RuntimeException("随机数范围(scope)不能小于返回的随机数数量(count)!");
    }
    List excluded = new ArrayList(count);
    int[] rands = new int[count];
    int i=0;
    while(i<count){
      Integer r = new Integer(rnGenerator.nextInt(scope));
      if(excluded.contains(r)==false){
        rands[i]=r.intValue();
        excluded.add(r);
        i++;
      }
    }
    return rands;
  }
  
  /**
   * 返回从0到scope-1之间的count个随机数
   * 
   * @param scope 生成的随机数范围
   * @param count 一次生成的随机数数量
   * @param repeat true为可重复，false为不可重复
   * @return 0到scope-1之间的count个随机数
   */
  public static int[] randomInts(int scope, int count, boolean repeat){
    if(repeat==false){
      return randomInts(scope, count);
    }
    int[] rands = new int[count];
    for(int i=0;i<count;i++){
      rands[i] = rnGenerator.nextInt(scope);
    }
    return rands;
  }
  
  private RandomHelper(){}
  private static Random rnGenerator;
  static {
    init();
  }
  private static synchronized void init() {
    rnGenerator = new Random();
  }
}
