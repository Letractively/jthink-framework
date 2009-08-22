/*
 * ConditionItem.java	2005-7-16
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
package org.fto.jthink.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.fto.jthink.lang.ObjectBuffered;
import org.fto.jthink.lang.StringBuffered;


/**
 * 条件单项, 用于描述SQL语句查询条件中的一项条件表达式。
 * 
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-16  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 */
public class ConditionItem implements java.io.Serializable{

  private static final long serialVersionUID = 1L;
  
  private String name;
	private String operator;
	private Object[] values;
	//private Object value;
	//private ObjectBuffered valueBuff;
	private boolean isQuote = false;
	private SQL sql;
	
	/**
	 * 构造方法, 创建条件单项
	 * 
	 * @param name 列名称
	 * @param operator 条件运算符,合法的运算符有: "=", ">=", "<=", ">", "<", "<>", "!=", "LIKE", "NOT LIKE","IS"
	 * @param value 条件值,可以是串类型,或其它能正确转换为数据库数据类型的JAVA类类型
	 */
	public ConditionItem(String name, String operator, Object value){
		this(name, operator, value, false);
	}
	
	
	/**
	 * 构造方法, 创建条件单项
	 * 
	 * @param name 列名称
	 * @param operator 条件运算符,合法的运算符有: "=", ">=", "<=", ">", "<", "<>", "!=", "LIKE", "NOT LIKE","IS"
	 * @param value 条件值,可以是串类型,或其它能正确转换为数据库数据类型的JAVA类类型
	 * @param isQuote 是否是直接引用条件值, 如果是，将会把整个条件项构建为SQL串，而不会将值用？号替换
	 */
	public ConditionItem(String name, String operator, Object value, boolean isQuote){
		if(!isValidOperator(OPERATOR_SIGNS_SINGLE, operator)){
			throw new IllegalArgumentException(
				"不合法的条件运算符, 正确的条件运算符是: =, >=, <=, >, <, <>, !=, LIKE, NOT LIKE, IS");
		}
		
		this.name = name;
		this.operator = operator;
		//this.valueBuff = new ObjectBuffered(1);
		//this.valueBuff.append(value);
		this.values = new Object[]{value};
		if(operator.equalsIgnoreCase("IS")){
			this.isQuote = true;
			String v = ((String)value).trim();
			if(!(v.equalsIgnoreCase("NULL") || v.equalsIgnoreCase("NOT NULL"))){
				throw new IllegalArgumentException("如果运算符是IS, 条件值只能是: NULL或NOT NULL");
			}
		}else{
			this.isQuote = isQuote;
		}
	}

	/**
	 * 构造方法, 创建条件单项
	 * 
	 * @param name 列名称
	 * @param operator 条件运算符,合法的运算符有: "=", ">=", "<=", ">", "<", "<>", "!=", "LIKE", "NOT LIKE"
	 * @param sql 条件值, 此条件值本身就是一个SQL语句,这样可以在条件中构建子查询
	 */
	public ConditionItem(String name, String operator, SQL sql){
		if(!isValidOperator(OPERATOR_SIGNS_SINGLE, operator) && !isValidOperator(OPERATOR_SIGNS_IN, operator)){
			throw new IllegalArgumentException(
				"不合法的条件运算符, 正确的条件运算符是: =, >=, <=, >, <, <>, !=, LIKE, NOT LIKE, IN, NOT IN");
		}
		this.name = name;
		this.operator = operator;
		this.sql = sql;
		//this.valueBuff = sql.getValueList();
		this.values = sql.getValues();
	}

	
	/**
	 * 构造方法, 创建条件单项
	 * 
	 * @param name 列名称
	 * @param operator 条件运算符,合法的运算符有: Between,NOT Between, IN, NOT IN
	 * @param values 条件值数组,数组值可以是串类型,或其它能正确转换为数据库数据类型的JAVA类类型,
	 *               注意:数据组的长度与条件运算符operator有一定关系,如果运算符是Between,
	 *               values数组的值必须是2个,如果运算符是IN,values数组的值至少有1个
	 */
	public ConditionItem(String name, String operator, Object[] values){
		if(values.length==0){
			throw new IllegalArgumentException(
			"不能没有条件值！");
		}
		operator = operator.trim();
		if(!isValidOperator(OPERATOR_SIGNS_MORE, operator)){
			throw new IllegalArgumentException(
				"不合法的条件运算符, 正确的条件运算符是: Between,NOT Between, IN, NOT IN");
		}
		
		if(isValidOperator(OPERATOR_SIGNS_BETWEEN, operator)){
			if(values.length!=2){
				throw new IllegalArgumentException(
				"不合法的条件值数量，Between,Not Between操作的条件值数量必须为2！");
			}
		}
		
		this.name = name;
		this.operator = operator;
    //this.valueBuff = new ObjectBuffered(1);
    //this.valueBuff.append(values);
		this.values = values;
	}
	
	/**
	 * 返回条件单项列名称
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * 返回条件单项运算符号
	 */
	public String getOperator(){
		return operator;
	}
	
	/**
	 * 返回所有条件值数组, 如果没有，返回null
	 */
	public Object[] getValues(){
		if(isQuote){
			return null;//new Object[0];
		}
//		if(values!=null){
//		  return valueBuff.toArray();
//		}
		return values;
//		if(value!=null){
//			return new Object[]{value};
//		}else if(values!=null){
//			return values;
//		}
//		return sql.getValues();
	}

  
//  /**
//   * 返回所有条件值列表, 如果没有，返回null
//   */
//  public ObjectBuffered getValueList(){
//    if(isQuote){
//      return null;
//    }
//    if(valueBuff!=null){
//      return valueBuff;
//    }
//    return null;    
//    if(value!=null){
//      ObjectBuffered vs = new ObjectBuffered(1);
//      vs.append(value);
//      return vs;
//      //return new Object[]{value};
//    }else if(values!=null){
//      ObjectBuffered vs = new ObjectBuffered(1);
//      vs.append(values);
//      return vs;
//    }
//    return sql.getValueList();
//  }
  
  
  /**
   * 返回SQL串形式的条件单项
   * 
   * @deprecated
   */
  public String getConditionItemString(){
    if(sql!=null){
      return new StringBuffered(name).append(" ").append(operator).append(" (").append(sql.getSQLString()).append(") ").toString();
    }
    if(isValidOperator(OPERATOR_SIGNS_SINGLE, operator)){
      if(isQuote){
        return new StringBuffered(name).append(" ").append(operator).append(" ").append(values[0]).append(" ").toString();
      }else{
        return new StringBuffered(name).append(" ").append(operator).append(" ? ").toString();
      }
    }else{
      if(isValidOperator(OPERATOR_SIGNS_BETWEEN, operator)){
          return new StringBuffered(name).append(" ").append(operator).append(" ? AND ? ").toString();
      }else{
        StringBuffered valuesStr = new StringBuffered(name).append(" ").append(operator).append(" (");
        int valuesCount = values.length;
        for(int i=0; i<valuesCount; i++){
          valuesStr.append(i==0?"? ":",? ");
        }
        return valuesStr.append(") ").toString();
      }
    }
  }
	
  
  /**
   * 返回SQL串形式的条件单项
   */
  public StringBuffered getConditionItemStatement(){
    if(sql!=null){
      return new StringBuffered(name).append(" ").append(operator).append(" (").append(sql.getSQLString()).append(") ");
    }
    if(isValidOperator(OPERATOR_SIGNS_SINGLE, operator)){
      if(isQuote){
        return new StringBuffered(name).append(" ").append(operator).append(" ").append(values[0]).append(" ");
      }else{
        return new StringBuffered(name).append(" ").append(operator).append(" ? ");
      }
    }else{
      if(isValidOperator(OPERATOR_SIGNS_BETWEEN, operator)){
          return new StringBuffered(name).append(" ").append(operator).append(" ? AND ? ");
      }else{
        StringBuffered valuesStr = new StringBuffered(name).append(" ").append(operator).append(" ("); 
        int valuesCount = values.length;
        for(int i=0; i<valuesCount; i++){
          valuesStr.append(i==0?"? ":",? ");
        }
        return valuesStr.append(") ");
      }
    }
  }
  
  

	/* 在构建SQL条件单项时用到的运算符号: "=", ">=", "<=", ">", "<", "<>", "!=", "LIKE", "NOT LIKE" ,"IS"*/
	private final static String[] OPERATOR_SIGNS_SINGLE = {
      "=", ">=", "<=", ">", "<", "<>", "!=", "LIKE", "NOT LIKE", "IS"};
	
	/* 在构建SQL条件单项时用到的运算符号: "Between","NOT Between", "IN", "NOT IN" */
	private final static String[] OPERATOR_SIGNS_MORE = {
			"Between","NOT Between", "IN", "NOT IN"};
	/* 在构建SQL条件单项时用到的运算符号: "Between","NOT Between" */
	private final static String[] OPERATOR_SIGNS_BETWEEN = {
			"Between","NOT Between"};
	/* 在构建SQL条件单项时用到的运算符号: "IN", "NOT IN" */
	private final static String[] OPERATOR_SIGNS_IN = {
			"IN", "NOT IN"};
	
  /**
   * 检索是否有效的条件运算符
   * 
   * @param oprtor 条件运算符号
   * 
   * @return true,是, false,否
   */
	private boolean isValidOperator(String[] definedSigns, String oprtor){
    int len = definedSigns.length;
    for(int i=0; i<len;i++){
        if(definedSigns[i].equalsIgnoreCase(oprtor.trim())){
          return true;
        }
    }
    return false;
  }	
	
}
