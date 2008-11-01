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
/**
 * 数组相关操作。
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
public class ArrayHelper {
  
  /**
   * 对象的包含检查，检查一个对象是否被包含在指定对象数组中
   * 
   * @param objs 被检查的对象数组
   * @param obj 被检查的对象
   */
  public static boolean contains(Object[] objs, Object obj){
    for(int i=objs.length-1;i>=0;i--){
      Object o = objs[i];
      if(o==null || obj==null){
        if(o==obj){
          return true;
        }
      }else  if(o==obj){
        return true;
      }else  if(o.equals(obj)){
        return true;
      }
    }
    return false;
  }
  
  /**
   * int型数值的包含检查，检查一个int数值是否被包含在指定int数组中
   * 
   * @param nums 被检查的int数组
   * @param num 被检查的int数值
   */
  public static boolean contains(int[] nums, int num){
    for(int i=nums.length-1;i>=0;i--){
      if(nums[i]==num){
        return true;
      }
    }
    return false;
  }

  /**
   * double型数值的包含检查，检查一个double数值是否被包含在指定double数组中
   * 
   * @param nums 被检查的double数组
   * @param num 被检查的double数值
   */
  public static boolean contains(double[] nums, double num){
    for(int i=nums.length-1;i>=0;i--){
      if(nums[i]==num){
        return true;
      }
    }
    return false;
  }

 
  
  private ArrayHelper(){}
}
