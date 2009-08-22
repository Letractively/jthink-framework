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

import org.fto.jthink.util.StringHelper;

/**
 * 用于缓冲字符串，理论上比java.lang.StringBuffer和java.lang.StringBuilder性能要优，经过测试比java.lang.StringBuffer性能好，
 * 在在jdk 5.0过后的java.lang.StringBuilder经过测试在通常情况下性能比StringBuffered高一点，但在某此特殊应用中
 * StringBuffered的性能远高于java.lang.StringBuffer和java.lang.StringBuilder，比如在串处理的中间环节当不需要toString的情况
 * 下。JThink中的SQL语句构建将采用这种方式，这样只有在最终整个SQL语句构建完成后才会只创建一次String对象。
 * 
 * <pre>
 *  StringBuffered有那些特点：
 *  1. 在append操作时不会对串进行System.arraycopy操作，只在最后toString才进行
 *  2. 空StringBuffered在toString时直接返回StringHelper.EMPTY, 减少了new String()操作
 *  3. 如果只有一个子串，就直接返回此子串，减少了new String()操作
 *  4. 对boolean型数据进行了优化，对int和long型数据进行了优化
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
 * @see java.lang.StringBuffer
 * @see java.lang.StringBuilder
 * 
 */
public class StringBuffered  implements java.io.Serializable {
  private static final long serialVersionUID = -8820146754877460901L;

  private static final char[] CHARS_TRUE = new char[]{'t','r','u','e'};
  private static final char[] CHARS_FALSE = new char[]{'f','a','l','s','e'};
  
  private static final int intTableSize = 255;
  private static char[][] intTable = new char[intTableSize][];
  static{
    for(int i=0;i<intTable.length;i++){
      intTable[i] = Integer.toString(i).toCharArray();
    }
  }
  
  /* 串总长度 */
  private int length = 0;
  /* 串列表 */
  private Object[] strData;
  /* 串数量 */
  private int strCount = 0; 
  
  /* 
   * 是否被锁定，如果被锁定，就不能再调用append方法，只有当被解锁后才能再使用
   */
  private boolean locked  = false;  
  
  /**
   * 构建一个StringBuffer，串列表大小为16
   */
  public StringBuffered(){
    this(16);
  }
  
  /**
   * 构建一个StringBuffer，指定将要被缓冲的字符串数量
   * 
   * @param strCapacity 预计将要被缓冲的字符串数量
   */
  public StringBuffered(int strCapacity){
    strData = new Object[strCapacity];
  }
  
  /**
   * 构建一个StringBuffer
   * 
   * @param s 串
   */
  public StringBuffered(String s){
    this();
    append(s);
  }
  
  /**
   * 返回串总长度
   */
  public int length() {
    return length;
  }  
  
  /**
   * 返回被缓冲子串数量
   */
  public int size(){
    return strCount;
  }
  

  void ensureCapacity(int minCapacity) {
    int oldCapacity = strData.length;
    if (minCapacity > oldCapacity) {
        //System.out.println(this.hashCode()+".ensureCapacity.minCapacity:"+minCapacity+", oldCapacity:"+oldCapacity);
        Object oldData[] = strData;
        int newCapacity = (oldCapacity * 3)/2 + 1;
        if (newCapacity < minCapacity){
          newCapacity = minCapacity;
        }
        //int newCapacity = minCapacity*2;
        strData = new Object[newCapacity];
        System.arraycopy(oldData, 0, strData, 0, strCount);
    }
  }
  
  void add(Object o){
    ensureCapacity(strCount + 1);
    strData[strCount++] = o;
  }
  
  void add(Object[] data, int start, int len){
    ensureCapacity(strCount + len);
    if(len>10){
      System.arraycopy(data, start, strData, strCount, len);
      strCount+=len;
    }else{
      for(int i=0;i<len;i++){
        strData[strCount++] = data[i];
      }
    }
  }  
  
  void clear(){
    for (int i = 0; i < strCount; i++){
        strData[i] = null;
    }
    strCount = 0;
  }
  
  /**
   * 追加字符串
   * @param s 被追加的字符串
   * @return 这个StringBuffer
   */
  public StringBuffered append(String s){
    checkLocked();
    length+=s.length();
    add(s);
    return this;
  }
  
  /**
   * 追加字符数组
   * 
   * <pre>
   *   注意：此方法将直接引用chars数组，所以在此之后最好不要修改chars里的数据
   * </pre>
   * 
   * @param s 被追加的字符数组
   * @return 这个StringBuffered
   */
  public StringBuffered append(char[] chars){
    checkLocked();
    length+=chars.length;
    add(chars);
    return this;
  }
  
  /**
   * 追加字符
   * @param s 被追加的字符
   * @return 这个StringBuffered
   */
  public StringBuffered append(char c){
    checkLocked();
    length++;;
    add(new Character(c));
    return this;
  }
  
  public StringBuffered append(StringBuffered sb) {
//    Object[] data = sb.strData;
//    add(data, 0, sb.strCount);
//    length+=sb.length();
//    return this;
    checkLocked();
    length += sb.length;
    add(sb);
    sb.lock();
    return this;
  }
  
  public StringBuffered append(boolean b) {
    if(b){
      return append(CHARS_TRUE);
    }else{
      return append(CHARS_FALSE);
    }
  }
  
  public StringBuffered append(Object obj) {
    return append(String.valueOf(obj));
  }

  public StringBuffered append(int i) {
    if(i>=0 && i<=intTableSize){
      return append(intTable[i]);
    }else if(i<=-0 && i>=-intTableSize){
      append("-");
      append(intTable[-i]);
      return this;
    }
    return append(Integer.toString(i));
  }
  
  public StringBuffered append(long l) {
    return append(String.valueOf(l));
  }
  
  public StringBuffered append(float f) {
    return append(String.valueOf(f));
  }
  
  public StringBuffered append(double d) {
    return append(String.valueOf(d));
  }
  
  
  private void getChars(char[] newChars, int off){
    /* 只有加入了一个对象 */
//    if(strCount==1){
//      Object o = strData[0];
//      if(o instanceof String){
//        newChars[off++] = (String)o;
//      }else if(o instanceof char[]){
//        String newStr = new String((char[])o);
//        clear();
//        add(newStr);
//        return newStr;
//      }else if(o instanceof Character){
//        String newStr = ((Character)o).toString();
//        clear();
//        add(newStr);
//        return newStr;
//      }
//    }
    
    /* 加入了多个对象 */
    //char[] newChars = new char[length];
    //int pos=0;
    for(int i=0;i<strCount;i++){
      Object o = strData[i];
      if(o instanceof String){
        String s = (String)o;
        int clen = s.length(); 
        s.getChars(0, clen, newChars, off);
        off+=clen;
      }else if(o instanceof char[]){
        char[] s = (char[])o;
        int clen = s.length; 
        System.arraycopy(s, 0, newChars, off, clen);
        off+=clen;
      }else if(o instanceof Character){
        newChars[off++]=((Character)o).charValue();
        //pos++;
      }else if(o instanceof StringBuffered){
        StringBuffered sb = (StringBuffered)o;
        sb.getChars(newChars, off);
        off+= sb.length;
      }
      
    }
  }
  
  /**
   * 到String对象
   */
  public String toString(){
    /* 是空 */
    if(strCount==0){
      return StringHelper.EMPTY;
    }
    
    /* 只有加入了一个对象 */
    if(strCount==1){
      Object o = strData[0];
      if(o instanceof String){
        return (String)o;
      }else if(o instanceof char[]){
        String newStr = new String((char[])o);
        clear();
        add(newStr);
        return newStr;
      }else if(o instanceof Character){
        String newStr = ((Character)o).toString();
        clear();
        add(newStr);
        return newStr;
      }
    }
    /* 加入了多个对象或StringBuffered */
    char[] newChars = new char[length];
    getChars(newChars, 0);
    
    
    clear();
    String newStr = new String(newChars);
    add(newStr);
    return newStr;
  }
  
  private void checkLocked(){
    if(locked){
      throw new RuntimeException("This StringBuffered is locked.");
    }
  }
  
  private void lock(){
    locked = true;
  }  
  
}
