/*
 * XMLHelper.java	2004-4-5
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
package org.fto.jthink.util;

import org.fto.jthink.exception.JThinkErrorCode;
import org.fto.jthink.exception.JThinkRuntimeException;
import org.jdom.*;

import java.util.*;
import java.io.*;
import java.net.URL;

import org.jdom.output.*;
import org.jdom.input.SAXBuilder;


/**
 * XML操作助手。提供对XML处理的常用方法，比如装入XML树从串或文件，生成XML树到串或文件等。
 * 
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2004-04-05  创建此类型
 * 2004-10-02  修改了一些方法 
 * 
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */

public final class XMLHelper {
	
  
  private XMLHelper() {}

  /**
   * 默认XML的字符编码格式
   */
  static String encoding = "UTF-8";

  /**
   * 将Element中的属性以Map形式返回
   * @param el Element元素
   * @return HashMap
   * 
   * @deprecated
   */
  public static Map parseToHashMap(Element el){
    Map hm = new HashMap();
  	Iterator it = el.getAttributes().iterator();
  	while(it.hasNext()){
  		Attribute attr = (Attribute)it.next();
  		hm.put(attr.getName(),attr.getValue());
  	}
  	return hm;
  }
  /**
   * 将Element中的属性以Map形式返回
   * @param el Element元素
   * @return HashMap
   * 
   */
  public static Map parseToMap(Element el){
    Map hm = new HashMap();
    Iterator it = el.getAttributes().iterator();
    while(it.hasNext()){
      Attribute attr = (Attribute)it.next();
      hm.put(attr.getName(),attr.getValue());
    }
    return hm;
  }


  /**
   * 解析XML串为orj.jdom.Element元素
   * 
   * @param xmlStr XML文档串
   * @return org.jdom.Element元素
   */
	public static Element loadXML(String xmlStr) {
		Reader reader = null;
		try { 
			SAXBuilder builder = new SAXBuilder();
			reader = new StringReader(xmlStr);
			Document xmlJDoc = builder.build(reader);
			return xmlJDoc.getRootElement();
		} catch (Exception e) {
			throw new JThinkRuntimeException(
					JThinkErrorCode.ERRCODE_XML_XML_STRING_LOAD_FAILED,
					"解析XML串为org.jdom.Element时发生异常!", e);
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e1) {
					throw new JThinkRuntimeException(e1);
				}
			}
		}
	}

  /**
   * 解析XML文档为orj.jdom.Element元素从文件
   *
   * @param	 fileName	XML文件名，包括路径
   * @return  org.jdom.Element元素
   */
  public static synchronized Element load(String fileName) {
  	try {
			SAXBuilder builder = new SAXBuilder();
			Document xmlJDoc = builder.build(new java.io.File(fileName));
			return xmlJDoc.getRootElement();
		} catch (Exception e) {
			throw new JThinkRuntimeException(
					JThinkErrorCode.ERRCODE_XML_XML_FILE_LOAD_FAILED,
					"解析XML文件为org.jdom.Element时发生异常!", e);
		}		
	}

  /**
   * 解析XML文档为orj.jdom.Element元素从文件
   *
   * @param	 file	XML文件对象
   * @return  org.jdom.Element元素
   */
  public static synchronized Element load(File file) {
  	try {
			SAXBuilder builder = new SAXBuilder();
			Document xmlJDoc = builder.build(file);
			return xmlJDoc.getRootElement();
		} catch (Exception e) {
			throw new JThinkRuntimeException(
					JThinkErrorCode.ERRCODE_XML_XML_FILE_LOAD_FAILED,
					"解析XML文件为org.jdom.Element时发生异常!", e);
		}		
	}

  
  /**
   * 解析XML文档为orj.jdom.Element元素从输入流
   *
   * @param	 is	InputStream输入流
   * @return  org.jdom.Element元素
   */
  public static synchronized Element load(InputStream is) {
  	try {
			SAXBuilder builder = new SAXBuilder();
			Document xmlJDoc = builder.build(is);
			return xmlJDoc.getRootElement();
		} catch (Exception e) {
			throw new JThinkRuntimeException(
					JThinkErrorCode.ERRCODE_XML_XML_FILE_LOAD_FAILED,
					"解析XML文件为org.jdom.Element时发生异常!", e);
		}		
	}
  
//  /**
//   * 解析XML文档为orj.jdom.Element元素从输入源
//   *
//   * @param	 is	InputSource输入源
//   * @return  org.jdom.Element元素
//   */
//  public static synchronized Element load(InputSource is) {
//  	try {
//			SAXBuilder builder = new SAXBuilder();
//			Document xmlJDoc = builder.build(is);
//			return xmlJDoc.getRootElement();
//		} catch (Exception e) {
//			throw new JThinkRuntimeException(
//					JThinkErrorCode.ERRCODE_XML_XML_FILE_LOAD_FAILED,
//					"解析XML文件为org.jdom.Element时发生异常!", e);
//		}		
//	}
  
  /**
   * 解析XML文档为orj.jdom.Element元素从流数据读入器
   *
   * @param	 reader	Reader
   * @return  org.jdom.Element元素
   */
  public static synchronized Element load(Reader reader) {
  	try {
			SAXBuilder builder = new SAXBuilder();
			Document xmlJDoc = builder.build(reader);
			return xmlJDoc.getRootElement();
		} catch (Exception e) {
			throw new JThinkRuntimeException(
					JThinkErrorCode.ERRCODE_XML_XML_FILE_LOAD_FAILED,
					"解析XML文件为org.jdom.Element时发生异常!", e);
		}		
	}
  
 
  /**
   * 解析XML文档为orj.jdom.Element元素从URL统一资源定义器
   *
   * @param	 url	URL
   * @return  org.jdom.Element元素
   */
  public static synchronized Element load(URL url) {
  	try {
			SAXBuilder builder = new SAXBuilder();
			Document xmlJDoc = builder.build(url);
			return xmlJDoc.getRootElement();
		} catch (Exception e) {
			throw new JThinkRuntimeException(
					JThinkErrorCode.ERRCODE_XML_XML_FILE_LOAD_FAILED,
					"解析XML文件为org.jdom.Element时发生异常!", e);
		}		
	}

 
  
  /**
   * 将org.jdom.Element元素保存为一个文件，字符编码为默认方式
   *
   * @param el         org.jdom.Element元素
   * @param fileName   文件名称，包括路径
   *
   */
  public static void save(Element el, String fileName) {
    save(el, fileName, encoding);
  }

  /**
   * 将org.jdom.Element元素保存为一个文件
   *
   * @param el         org.jdom.Element元素
   * @param fileName   文件名称，包括路径
   * @param encoding   字符编码方式
   *
   */
  public static  synchronized void save(Element el, String fileName, String encoding){
    try {
      toXmlFile(buildDocument(el), encoding, fileName);
    }
    catch (Exception e) {
      throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_XML_XML_FILE_SAVE_FAILED,  "Save xml error!", e);
    }
  }

  /**
   * 将Xorg.jdom.Element元素输出为XML串，采用默认编码方式
   *
   * @param el       org.jdom.Element元素
   *
   * @return         XML串
   */
  public static String toXMLString(Element el) {
    if(el==null){
      throw new NullPointerException();
    }
    try {
      return toXmlString(el, encoding);
    }
    catch (IOException e) {
      throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_XML_XML_STRING_SAVE_FAILED, "to xml string error!", e);
    }
  }

  /**
   * 将org.jdom.Element元素输出为XML串
   *
   * @param el         XML树对象
   * @param encoding   XML编码方式
   *
   * @return           XML串
   *
   */
  public static String toXMLString(Element el, String encoding) {
    if(el==null){
      throw new NullPointerException();
    }
    try {
        return toXmlString(el, encoding);

    }catch (IOException e) {
      throw new JThinkRuntimeException(JThinkErrorCode.ERRCODE_XML_XML_STRING_SAVE_FAILED, "to xml string error!", e);
    }
  }


  /**
   * 向org.jdom.Element元素设置一个属性。如果属性值(value)为空对象(null)，将不会向element设置任
   * 何值。
   * @param element      org.jdom.Element元素
   * @param name         属性名称
   * @param value        属性值
   */
  public static void setAttribute(Element element, String name,
                                       String value) {
    if (value != null) {
      element.setAttribute(new Attribute(name, value));
    }
  }

  /**
   * 加入多个属性到org.jdom.Element元素, 如果属性已经存在，将会覆盖对应属性值。
   * @param element     org.jdom.Element元素
   * @param attrList    属性列表
   */
  public static void setAttributes(Element element, List attrList) {
    int len = attrList.size();
    for (int i = 0; i < len; i++) {
      Attribute attr = (Attribute) attrList.get(i);
      setAttribute(element, attr.getName(), attr.getValue());
    }
  }

  /**
   * 向org.jdom.Element元素中加入一个属性。如果属性值(value)为空对象(null)，将不会向element设置任
   * 何值；如果属性名称为(name)的属性已经存在，将抛出运行时异常。
   * @param element      org.jdom.Element元素
   * @param name         属性名称
   * @param value        属性值
   *
   */
  public static void addAttribute(Element element, String name,
                                       String value) {
    if (value != null) {
      if(element.getAttributeValue(name)==null){
        element.setAttribute(new Attribute(name, value));
      }else{
        throw new JThinkRuntimeException("Attribute already exist! attribute name is " + name);
      }
    }
  }


  /**
   * 返回属性值。如果属性不存在，返回空串：“”。
   * @param element  org.jdom.Element元素
   * @param name     属性名称
   * @return         属性值
   */
  public static String getAttributeString(Element element, String name) {
    String attributeValue = element.getAttributeValue(name);
    if (attributeValue == null) {
      attributeValue = "";
    }
    return attributeValue;
  }


  /**
   * 遍历XML树, 从根开始
   * @param el          org.jdom.Element元素
   * @param xmlIter     实现了XMLIterator接口的对象
   */
  public static void rootIterator(Element el, XMLIterator xmlIter){
    int[] deepness = {
        0};
    rootIterator(el, xmlIter, deepness, 0);
  }

  private static void rootIterator(Element el, XMLIterator xmlIter,
                                   int[] deepness, int index) {
    xmlIter.iterator(el, deepness[0], index);
    deepness[0]++;
    List elList = el.getChildren();
    for (int i = 0; i < elList.size(); i++) {
      Element subEl = (Element) elList.get(i);
      rootIterator(subEl, xmlIter, deepness, i);
      deepness[0]--;
    }
  }

  /**
   * 遍历 XML树, 从叶开始
   * @param el          org.jdom.Element元素
   * @param xmlIter     实现了XMLIterator接口的对象
   * 
   */
  public static void leafIterator(Element el, XMLIterator xmlIter){
    int[] deepness = {
        -1};
    leafIterator(el, xmlIter, deepness, 0);
  }

  private static void leafIterator(Element el, XMLIterator xmlIter,
                                   int[] deepness, int index) {
  	deepness[0]++;
  	List elList = el.getChildren();
    for (int i = 0; i < elList.size(); i++) {
      Element subEl = (Element) elList.get(i);
      leafIterator(subEl, xmlIter, deepness, i);
      deepness[0]--;
    }
    xmlIter.iterator(el, deepness[0], index);
  }

  /**
   * 返回指定属性的XML节点在节点列表中的索引位置，如果没有找到指定属性的XML节点，返回-1。
   * @param els　　      org.jdom.Element元素列表
   * @param name　　　　 属性名称
   * @param value       属性值
   * @return            索引位置，如果没找到，返回-1
   */
  public static int getIndex(List els, String name, String value) {
    int index = -1;
    if (value != null) {
      int listLength = els.size();
      for (int i = 0; i < listLength; i++) {
        String attrValue = ( (Element) els.get(i)).
            getAttributeValue(name);
        if (attrValue != null) {
          if (attrValue.equals(value)) {
            index = i;
            break;
          }
        }
      }
    }
    return index;
  }

  /**
   * 返回满足属性条件的子节点
   * 
   * @param el     org.jdom.Element元素
   * @param name   子节点的属性名
   * @param value  子节点的属性值
   * @return 子节点,如果没找到,返回null
   */
  public static Element getChild(Element el, String name, String value){
    if (value != null) {
    	List elementLT = el.getChildren();
      int listLength = elementLT.size();
      for (int i = 0; i < listLength; i++) {
      	Element subEL = (Element)elementLT.get(i);
        String attrValue = subEL.getAttributeValue(name);
        if (attrValue != null) {
          if (attrValue.equals(value)) {
          	return subEL;
          }
        }
      }
    }
    return null;
  }
  
  /**
   * 返回所有子节点
   * @param el org.jdom.Element元素
   * @return 以列表方式返回所以子节点
   */
  public static List getAllChildren(Element el){
      List childrens = new ArrayList(100);
      List lt = el.getChildren();
      childrens.addAll(lt);
      for(int i=0; i<lt.size(); i++){
          childrens.addAll(getAllChildren((Element)lt.get(i)));
      }
      return childrens;
  }


  /**
   * 返回XML树的深度。
   * @param el   org.jdom.Element元素XML树
   * @return     深度
   */
  public static int getDeepness(Element el) {
    int[] deepness = {
        0, 0};
    getDeepness(el, deepness);
    return deepness[1];
  }

  private static void getDeepness(Element el, int[] deepness) {
    deepness[0]++;
    if (deepness[0] > deepness[1]) {
      deepness[1] = deepness[0];
    }
    List elList = el.getChildren();
    int ellength = elList.size();
    for (int i = 0; i < ellength; i++) {
      Element subEl = (Element) elList.get(i);
      getDeepness(subEl, deepness);
      deepness[0]--;
    }
  }

  
  /*
   * copy attrs to element
   *
   * copy_type:  0 = 覆盖，1 = 忽略
   *
   */
  
  /**
   * 复制类型，覆盖，用于copyAttributesToElement()方法。
   */
  public final static int COPY_TYPE_OVER = 0;

  /**
   * 复制类型，忽略，用于copyAttributesToElement()方法。
   */
  public final static int COPY_TYPE_IGNORE = 1;
  /**
   * 将节点srcEL的所有属性复制到节点tgtEL。
   * @param srcEL     源org.jdom.Element元素
   * @param tgtEL     目标org.jdom.Element元素
   * @param copyType　类型，COPY_TYPE_OVER： 覆盖，COPY_TYPE_IGNORE： 忽略
   * @return 目标org.jdom.Element元素
   */
  public static Element copyAttributes(Element srcEL, Element tgtEL,
                                           int copyType) {
    List attrs = srcEL.getAttributes();
    int len = attrs.size();
    for (int i = 0; i < len; i++) {
      Attribute attr = (Attribute) attrs.get(i);
      Attribute attr2 = tgtEL.getAttribute(attr.getName());
      if (attr2 == null) {
        tgtEL.setAttribute( (Attribute) attr.clone());
      }
      else {
        if (copyType == COPY_TYPE_OVER) {
          attr2.setValue(attr.getValue());
        }
      }
    }
    return tgtEL;
  }

  /*
   *  合并两个节点列表
   *  join_type: AND, OR, LEFT，RIGHT，
   */
  /**
   * 连接类型，与，用于join()方法。
   */
  public final static String JOIN_TYPE_AND = "AND";
  /**
   * 连接类型，或，用于join()方法。
   */
  public final static String JOIN_TYPE_OR = "OR";
  /**
   * 连接类型，左，用于join()方法。
   */
  public final static String JOIN_TYPE_LEFT = "LEFT";
  /**
   * 连接类型，右，用于join()方法。
   */
  public final static String JOIN_TYPE_RIGHT = "RIGHT";
  /**
   * 将两个XML节点列表属性进行join连接操作，生成一个新的XML列表。
   * @param elList1     第一个XML节点列表
   * @param elList2     第二个XML节点列表
   * @param attrName    关联属性名称，将根据此属性值进行连接，将此属性名指定的属性值相等的节点连接为一个新XML节点
   * @param joinType    连接类型。
   *                         <BR>JOIN_TYPE_AND：与连接，将两个XML节点列表中关联属性存在并且相等的节点连接为一个新XML节点
   *                         <BR>JOIN_TYPE_OR：或连接， 将两个XML节点列表中关联属性存在并且相等或不存在或不相等的节点连接为一个新XML节点
   *                         <BR>JOIN_TYPE_LEFT：左连接，将两个XML节点列表中关联属性存在并且相等或在第一个XML节点列表中不存在或不相等的节点连接为一个新XML节点
   *                         <BR>JOIN_TYPE_RIGHT：右连接，将两个XML节点列表中关联属性存在并且相等或在第二个XML节点列表中不存在或不相等的节点连接为一个新XML节点
   *
   * @return            XML节点列表，由elList1与elList2连接生成
   */
  public static List join(List elList1, List elList2, String attrName,
                          String joinType) {
    return join(elList1, attrName, elList2, attrName, joinType);
  }
  /**
   * 将两个XML节点列表属性进行join连接操作，根据attrName1与attrName2的值相等的节点连接生成一个新的XML列表。
   * @param elList1     第一个XML节点列表
   * @param attrName1    关联属性名称，将根据此属性值进行连接
   * @param elList2     第二个XML节点列表
   * @param attrName2    关联属性名称，将根据此属性值进行连接
   * @param joinType    连接类型。
   *                         <BR>JOIN_TYPE_AND：与连接，将两个XML节点列表中关联属性存在并且相等的节点连接为一个新XML节点
   *                         <BR>JOIN_TYPE_OR：或连接， 将两个XML节点列表中关联属性存在并且相等或不存在或不相等的节点连接为一个新XML节点
   *                         <BR>JOIN_TYPE_LEFT：左连接，将两个XML节点列表中关联属性存在并且相等或在第一个XML节点列表中不存在或不相等的节点连接为一个新XML节点
   *                         <BR>JOIN_TYPE_RIGHT：右连接，将两个XML节点列表中关联属性存在并且相等或在第二个XML节点列表中不存在或不相等的节点连接为一个新XML节点
   *
   * @return            XML节点列表，由elList1与elList2连接生成
   */
  public static List join(List elList1, String attrName1, List elList2, String attrName2,
                          String joinType) {
    List elList = null;
    if (joinType.equals(JOIN_TYPE_AND)) {
      elList = join_and(elList1, elList2, attrName1, attrName2);
    }else if (joinType.equals(JOIN_TYPE_OR)) {
      elList = join_or(elList1, elList2, attrName1, attrName2);
    }else if (joinType.equals(JOIN_TYPE_LEFT)) {
      elList = join_left(elList1, elList2, attrName1, attrName2);
    }else if (joinType.equals(JOIN_TYPE_RIGHT)) {
      elList = join_left(elList2, elList1, attrName2, attrName1);
    }
    return elList;
  }

  /**
   * 对指定XML节点的所有子节点进行排序。
   *
   * @param element        被排序的XML节点
   * @param orderByItem    被排序的属性字段名称
   * @param dataType       XML属性字段的数据类型，当前有三种数据类型：'S','N','D'
   *                       <BR>   S :  String
   *                       <BR>   N :  Number
   *                       <BR>   D :  Date (2002-2-3 00:00:00)
   * @param isDesc         排序方式(true, false)
   *                       <BR>    true :  descend，降序
   *                       <BR>    false:  ascend， 升序
   */

  public static void orderBy(Element element, String orderByItem,
                             String dataType, boolean isDesc) {

    List childList = element.getChildren();

    int listSize = childList.size();

    for (int i = 0; i < listSize; i++) {
      org.jdom.Element currentElement = (org.jdom.Element) childList.get(i);
      String currentStr = currentElement.getAttributeValue(orderByItem);

      if (i == 0) {
        continue;
      }
      for (int ii = i; ii > 0; ii--) {
        org.jdom.Element previousElement = (org.jdom.Element) childList.get(ii -
            1);
        String previousStr = previousElement.getAttributeValue(orderByItem);
        boolean compareResult = compareResult_OrderBy(dataType,
            currentStr, previousStr);
        compareResult = isDesc ? !compareResult : compareResult;
        if (compareResult) {
          break;
        }
        else {
          childList.remove(ii);
          childList.add(ii - 1, currentElement);
        }
      }
    }

  }
  /**
   * 根据conditionHT中指定的条件，从element选择满足条件的子节点。
   * @param element      XML节点
   * @param conditionHT  包含了XML属性条件的Hashtable
   * @return             满足条件的XML节点
   */
  public static Element select(Element element, Hashtable conditionHT) {

    Element result = (Element) element.clone();
    filter(result, conditionHT);
    return result;
  }
  /**
   * 将所有不满足条件的XML子节点过滤掉。
   * @param element      XML节点
   * @param conditionHT  包含了XML属性条件的Hashtable
   */
  public static void filter(Element element, Hashtable conditionHT) {

    List childList = element.getChildren();

    Enumeration e = conditionHT.keys();
    while (e.hasMoreElements()) {
      String attributeName = (String) e.nextElement();

      String comparedValue = (String) conditionHT.get(attributeName);

      for (int i = 0; i < childList.size(); ) {

        org.jdom.Element childElement = (org.jdom.Element) childList.get(i);

        String currentValue = childElement.getAttributeValue(attributeName);

        if (!compareResult_Condition(comparedValue, currentValue)) {
          childList.remove(i);
        }
        else {
          i++;
          continue;
        }
      }
    }

  }

  
  /**
   * 将XML文档保存到文件
   * @param xmlJDoc
   * @param encoding
   * @param fileName
   * @throws FileNotFoundException
   * @throws IOException
   */
   static void toXmlFile(org.jdom.Document xmlJDoc, String encoding,
                                    String fileName) throws FileNotFoundException,
        IOException {

//      boolean writefileStatus = true;

      XMLOutputter xmlOutput = new XMLOutputter();
      Format format = Format.getPrettyFormat();
      format.setEncoding(encoding);
      format.setIndent("   ");
      xmlOutput.setFormat(format);
      OutputStream os = new java.io.FileOutputStream(fileName, false);
      xmlOutput.output(xmlJDoc, os);
      os.close();

    }

    /**
     * Out xml document string
     *
     * @param xmlEL org.jdom.Document
     * @param encoding 编码方式
     *
     * @return xml string
     *
     */
     static String toXmlString(Element xmlEL,
                                        String encoding) throws IOException {
      return toXmlString(buildDocument(xmlEL), encoding);
     }


     static String toXmlString(org.jdom.Document xmlJDoc,
                                        String encoding) throws IOException {
      XMLOutputter xmlOutput = new XMLOutputter();
      Format format = Format.getPrettyFormat();
      format.setEncoding(encoding);
      format.setIndent("   ");
      xmlOutput.setFormat(format);
      return xmlOutput.outputString(xmlJDoc);

    }


    /**
     * get InputStream data
     *
     * @param xmlStr, xml string
     *
     * @return InputStream
     *
     */
     static InputStream getInputStream(String xmlStr) {
      return new ByteArrayInputStream(xmlStr.getBytes());
    }


     static Document buildDocument(Element el) {
      Document doc = null;
      if (el.getParentElement() != null) {
        el = (Element) el.clone();
      }
      doc = el.getDocument();
      if (doc == null) {
        doc = new Document(el);
      }
      return doc;
    }

    //mode
    //join element list, join mode: and, unite element's all attribute.
    static List join_and(List elList1, List elList2, String attrName) {
      List elList = new ArrayList();
      int len = elList1.size();
      for (int i = 0; i < len; i++) {
        Element el1 = (Element) elList1.get(i);
        String value = el1.getAttributeValue(attrName);
        Element el2 = getElment(elList2, attrName, value);
        if (el2 != null) {
          Element el = (Element) el1.clone();
          elList.add(el);
          XMLHelper.copyAttributes(el2, el,
                                     XMLHelper.COPY_TYPE_IGNORE);
        }
      }
      return elList;
    }
    
    static List join_and(List elList1, List elList2, String attrName1, String attrName2) {
      List elList = new ArrayList();
      int len = elList1.size();
      for (int i = 0; i < len; i++) {
        Element el1 = (Element) elList1.get(i);
        String value = el1.getAttributeValue(attrName1);
        Element el2 = getElment(elList2, attrName2, value);
        if (el2 != null) {
          Element el = (Element) el1.clone();
          elList.add(el);
          XMLHelper.copyAttributes(el2, el,
                                     XMLHelper.COPY_TYPE_IGNORE);
        }
      }
      return elList;
    }

    static List join_or(List elList1, List elList2, String attrName1, String attrName2) {
      List elList = new ArrayList();
      List tmpElList2 = new ArrayList(elList2.subList(0, elList2.size()));
      int len = elList1.size();
      for (int i = 0; i < len; i++) {
        Element el1 = (Element) elList1.get(i);
        String value = el1.getAttributeValue(attrName1);
        Element el2 = getElment(tmpElList2, attrName2, value);
        Element el = (Element) el1.clone();
        elList.add(el);
        if (el2 != null) {
          tmpElList2.remove(el2);
          XMLHelper.copyAttributes(el2, el,
                                     XMLHelper.COPY_TYPE_IGNORE);
        }
      }
      elList.addAll(tmpElList2);
      return elList;
    }
    
    
    static List join_left(List elList1, List elList2, String attrName1, String attrName2) {
      List elList = new ArrayList();
      int len = elList1.size();
      for (int i = 0; i < len; i++) {
        Element el1 = (Element) elList1.get(i);
        String value = el1.getAttributeValue(attrName1);
        Element el2 = getElment(elList2, attrName2, value);
        Element el = (Element) el1.clone();
        elList.add(el);
        if (el2 != null) {
          XMLHelper.copyAttributes(el2, el,
                                     XMLHelper.COPY_TYPE_IGNORE);
        }
      }
      return elList;
    }


    static Element getElment(List elementList, String name, String vlaue) {
      Element el = null;
      int index = XMLHelper.getIndex(elementList, name, vlaue);
      if (index != -1) {
        el = (Element) elementList.get(index);
      }
      return el;
    }

    /**
     * compare result with dondition
     */
    static boolean compareResult_Condition(
        String comparison,
        String currentStr) {
      String comparedStr = null;

      //first parse comparison operator and type
      String operator = "";
      String type = "";

      //if has no ',' , use the default operator (=) and type (s)
      //that means check the two string is equal or not
      if (comparison.indexOf(",") >= 0) {
        StringTokenizer st = new StringTokenizer(comparison, ",");
        type = (String) st.nextToken();
        operator = (String) st.nextToken();
        if (st.hasMoreTokens()) {
          comparedStr = (String) st.nextToken();
        }
        else {
          comparedStr = "";
        }

      }
      else {
        operator = "=";
        type = "S";
        comparedStr = comparison;
      }

      if (currentStr == null) {
        if (operator.equalsIgnoreCase("!=")) {
          return true;
        }
        else {
          return false;
        }
      }

      operator = operator.trim();
      type = type.trim();

      if (type.equalsIgnoreCase("S")) {
        if (operator.equals("=")) {
          return comparedStr.equals(currentStr);
        }
        else if (operator.equals("!=")) {
          return!comparedStr.equals(currentStr);
        }
        else if (operator.equalsIgnoreCase("LIKE")) {
          if (currentStr.indexOf(comparedStr) >= 0) {
            return true;
          }
        }
      }

      double comparedNum = 0;
      double currentNum = 0;

      if (type.equalsIgnoreCase("N")) {
        comparedNum = Double.parseDouble(comparedStr);
        currentNum = Double.parseDouble(currentStr);
      }
      if (type.equalsIgnoreCase("D")) {
        comparedNum = parseStrToMSecond(comparedStr);
        currentNum = parseStrToMSecond(currentStr);
      }

      if (operator.equalsIgnoreCase("=")) {
        return currentNum == comparedNum;
      }
      else if (operator.equalsIgnoreCase(">=")) {
        return currentNum >= comparedNum;
      }
      else if (operator.equalsIgnoreCase("<=")) {
        return currentNum <= comparedNum;
      }
      else if (operator.equalsIgnoreCase(">")) {
        return currentNum > comparedNum;
      }
      else if (operator.equalsIgnoreCase("<")) {
        return currentNum < comparedNum;
      }
      else if (operator.equalsIgnoreCase("!=")) {
        return currentNum != comparedNum;
      }
      return false;
    }

    
    /**
     * compare result with order by
     *
     * dataType: ('N', 'D', 'S')
     *
     */
    static boolean compareResult_OrderBy(String dataType,
                                         String currentStr,
                                         String previousStr) {

      if (currentStr == null) {
        return false;
      }
      if (previousStr == null) {
        return true;
      }

      currentStr = currentStr.trim();
      previousStr = previousStr.trim();

      //if the data type is number or date, just compare their value
      double previousNum = 0;
      double currentNum = 0;
      if (dataType.equalsIgnoreCase("N")) {
        previousNum = Double.parseDouble(previousStr);
        currentNum = Double.parseDouble(currentStr);
      }
      if (dataType.equalsIgnoreCase("D")) {
        previousNum = parseStrToMSecond(previousStr);
        currentNum = parseStrToMSecond(currentStr);
      }

      if (currentNum > previousNum) {
        return true;
      }

      //if the data type is String, compare the total char value
      if (dataType.equalsIgnoreCase("S")) {
        char[] previousC = previousStr.toLowerCase().toCharArray();
        char[] currentC = currentStr.toLowerCase().toCharArray();

        for (int i = 0; i < currentC.length; i++) {
          if (i > previousC.length - 1) {
            return true;
          }

          if ( (int) currentC[i] > (int) previousC[i]) {
            return true;
          }
          else if ( (int) currentC[i] < (int) previousC[i]) {
            return false;
          }
        }
      }
      return false;
    }
 
    /**
     * parse date str to second
     */
    private static long parseStrToMSecond(String str) {
      return DateTimeHelper.parseToDate(str).getTime();
    }    
  


}

