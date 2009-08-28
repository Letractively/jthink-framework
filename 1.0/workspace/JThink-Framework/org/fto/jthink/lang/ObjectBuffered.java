/*
 * StringBuffered.java 2009-8-3
 *
 * 版权所有: 自由思考组织(FTO)软件团队 2000-2005, 保留所有权利.
 * 这个软件是自由思考组织(FTO)软件团队开发的，如果要使用这个软件，请首先阅读并接受许可协议。
 *
 * Copyright 2000-2009 FTO Software Team, Inc. All Rights Reserved.
 * This software is the proprietary information of FTO Software Team, Inc.
 * Use is subject to license terms.
 *
 * FTO站点：http://www.free-think.org
 */
package org.fto.jthink.lang;

/**
 * 用于缓冲连接对象
 * 
 * <pre>
 *  
 *  
 * </pre>
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2009-08-03  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */
public class ObjectBuffered {

  /* 对象数组 */
  private Object[] objData;
  /* 对象数量 */
  private int objCount = 0; 
  
  /* 对象总长度 */
  private int length = 0;  
  
  /* 
   * 是否被锁定，如果被锁定，就不能再调用append方法，只有当被解锁后才能再使用
   */
  private boolean locked  = false;
  
  
  /**
   * 构建一个ObjectBuffered，
   * 
   */
  public ObjectBuffered(){
    this(16);
  }
  
  /**
   * 构建一个ObjectBuffered，
   * 
   * @param strCapacity 预计将要被缓冲的对象串数量
   */
  public ObjectBuffered(int strCapacity){
    objData = new Object[strCapacity];
  }
  
  /**
   * 构建一个ObjectBuffered
   * 
   * @param o 对象
   */
  public ObjectBuffered(Object o){
    this();
    append(o);
  }
  
  /**
   * 构建一个ObjectBuffered
   * 
   * @param os 对象数组
   */
  public ObjectBuffered(Object[] os){
    this();
    append(os);
  }

  
  /**
   * 返回对象总长度
   */
  public int length() {
    return length;
  }  
  
  /**
   * 返回被缓冲子对象数量
   * @param minCapacity
   */
  public int size(){
    return objCount;
  }
  
  
  /**
   * 追加对象
   * @param o 被追加到尾部的对象
   * @return 这个ObjectBuffered
   */
  public ObjectBuffered append(Object o){
    checkLocked();
    length++;
    add(o);
    return this;
  }
  
  /**
   * 追加对象
   * @param o 被追加到尾部的对象
   * @return 这个ObjectBuffered
   */
  public ObjectBuffered append(Object[] o){
    checkLocked();
    length += o.length;
    add(o);
    return this;
  }
  
  /**
   * 追加对象
   * @param o 被追加到尾部的ObjectBuffered对象
   * @return 这个ObjectBuffered
   */
  public ObjectBuffered append(ObjectBuffered ob){
    checkLocked();
    //ob.checkLocked();
    length += ob.length;
    add(ob);
    ob.lock();
    return this;
  }
  
  
  private void getObjects(Object[] buffs, int off){
    if(length>0){
      for(int i=0;i<objCount;i++){
        Object o = objData[i];
        if(o instanceof Object[]){
          Object[] os = (Object[])o;
          int subObjsLen = os.length;
          switch(subObjsLen){
            case 0:
              break;
            case 1:
              buffs[off++] = os[0];
              break;
            case 2:
              buffs[off++] = os[0];
              buffs[off++] = os[1];
              break;
            case 3:
              buffs[off++] = os[0];
              buffs[off++] = os[1];
              buffs[off++] = os[2];
              break;
            case 4:
              buffs[off++] = os[0];
              buffs[off++] = os[1];
              buffs[off++] = os[2];
              buffs[off++] = os[3];
              break;
            case 5:
              buffs[off++] = os[0];
              buffs[off++] = os[1];
              buffs[off++] = os[2];
              buffs[off++] = os[3];
              buffs[off++] = os[4];
              break;
            default:
              System.arraycopy(os, 0, buffs, off, subObjsLen);
              off+=subObjsLen;
          }
          
        }else if(o instanceof ObjectBuffered){
          ObjectBuffered ob = (ObjectBuffered)o;
          ob.getObjects(buffs, off);
          off+= ob.length;
          //if(isFirstLayer){
            //ob.unlock(); //解锁
          //}
        }else{
          buffs[off++] = o;
        }
      }
    }
  }
  
  
  /**
   * 返回对象数组
   */
  public Object[] toArray() {
    Object[] result = new Object[length];
    if(length>0){
      getObjects(result, 0);
      clear();
      add(result);
    }
    return result;
  }
  
  
  private void checkLocked(){
    if(locked){
      throw new RuntimeException("This ObjectBuffered is locked.");
    }
  }
  
  private void lock(){
    locked = true;
  }
  
//  private void unlock(){
//    locked = false;
//  }
  
  
  void ensureCapacity(int minCapacity) {
    int oldCapacity = objData.length;
    if (minCapacity > oldCapacity) {
        Object oldData[] = objData;
        int newCapacity = (oldCapacity * 3)/2 + 1;
        if (newCapacity < minCapacity){
          newCapacity = minCapacity;
        }
        objData = new Object[newCapacity];
        System.arraycopy(oldData, 0, objData, 0, objCount);
    }
  }
  
  void add(Object o){
    ensureCapacity(objCount + 1);
    objData[objCount++] = o;
  }
  
  void clear(){
    for (int i = 0; i < objCount; i++){
        objData[i] = null;
    }
    objCount = 0;
  }
}
