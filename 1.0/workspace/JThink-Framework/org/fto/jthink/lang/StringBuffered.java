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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.fto.jthink.util.StringHelper;

/**
 * 用于缓冲字符串，理论上比java.lang.StringBuffer和java.lang.StringBuilder性能要优，经过测试比java.lang.StringBuffer性能好，
 * 在在jdk 5.0过后的java.lang.StringBuilder经过测试在通常情况下性能比StringBuffered高一点，但在某此特殊应用中
 * StringBuffered的性能远高于java.lang.StringBuffer和java.lang.StringBuilder，比如在串处理的中间环节当不需要toString的情况
 * 下。JThink中的SQL语句构建将采用这种方式，这样只有在最终整个SQL语句构建完成后才会只创建一次String对象。
 * 
 * <pre>
 *  StringBuffered有那些特点：
 *  
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
 * 
 */
public class StringBuffered  implements java.io.Serializable {
  private static final long serialVersionUID = -8820146754877460901L;

  private static final char[] CHARS_TRUE = new char[]{'t','r','u','e'};
  private static final char[] CHARS_FALSE = new char[]{'f','a','l','s','e'};
  
  /* 串总长度 */
  private int length = 0;
  /* 串列表 */
  private List subStrs;
  //java.lang.StringBuffer s;
  /**
   * 构建一个StringBuffer，串列表大小为48
   */
  public StringBuffered(){
    subStrs = new ArrayList(48);
    
  }
  
  /**
   * 构建一个StringBuffer，指定将要被缓冲的字符串数量
   * 
   * @param count 预计将要被缓冲的字符串数量
   */
  public StringBuffered(int count){
    subStrs = new ArrayList(count);
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
   * 追加字符串
   * @param s 被追加的字符串
   * @return 这个StringBuffer
   */
  public StringBuffered append(String s){
    length+=s.length();
    subStrs.add(s);
    return this;
  }
  
  /**
   * 追加字符数组
   * @param s 被追加的字符数组
   * @return 这个StringBuffered
   */
  public StringBuffered append(char[] chars){
    length+=chars.length;
    subStrs.add(chars);
    return this;
  }
  
  /**
   * 追加字符
   * @param s 被追加的字符
   * @return 这个StringBuffered
   */
  public StringBuffered append(char c){
    length++;;
    subStrs.add(new Character(c));
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
    return append(String.valueOf(i));
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
  
  
  /**
   * 到String对象
   */
  public String toString(){
    int subStrCount = subStrs.size();
    
    /* 是空 */
    if(subStrCount==0){
      return StringHelper.EMPTY;
    }
    
    /* 只有加入了一个对象 */
    if(subStrCount==1){
      Object o = subStrs.get(0);
//      Object o = subStrs.iterator().next();
      if(o instanceof String){
        return (String)o;
      }else if(o instanceof char[]){
        String newStr = new String((char[])o);
        subStrs.clear();
        subStrs.add(newStr);
        return newStr;
      }else if(o instanceof Character){
        String newStr = ((Character)o).toString();
        subStrs.clear();
        subStrs.add(newStr);
        return newStr;
      }
    }
    
    /* 加入了多个对象 */
    char[] newChars = new char[length];
    int pos=0;
//    Iterator it = subStrs.iterator();
//    while(it.hasNext()){
//      Object o = it.next();
    for(int i=0;i<subStrCount;i++){
      Object o = subStrs.get(i);
      if(o instanceof String){
        String s = (String)o;
        int clen = s.length(); 
        s.getChars(0, clen, newChars, pos);
        pos+=clen;
      }else if(o instanceof char[]){
        char[] s = (char[])o;
        int clen = s.length; 
        System.arraycopy(s, 0, newChars, pos, clen);
        pos+=clen;
      }else if(o instanceof Character){
        newChars[pos]=((Character)o).charValue();
        pos++;
      }
      
    }
    subStrs.clear();
    String newStr = new String(newChars);
    subStrs.add(newStr);
    return newStr;
  }

  
  final static char[] digits = {
    '0' , '1' , '2' , '3' , '4' , '5' ,
    '6' , '7' , '8' , '9' , 'a' , 'b' ,
    'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
    'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
    'o' , 'p' , 'q' , 'r' , 's' , 't' ,
    'u' , 'v' , 'w' , 'x' , 'y' , 'z'
      };
  
}
