/*
 * 创建日期 2005-10-2
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package org.fto.jthink.util;

import java.io.FileNotFoundException;

import org.jdom.Document;
import org.jdom.Element;

import junit.framework.TestCase;

/**
 * @author Administrator
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class XMLHelperTestCase  extends TestCase{

	public static void main(String[] args) {
		junit.textui.TestRunner.run(XMLHelperTestCase.class);
	}

	
	/**
	 * 测试 parseToHashMap
	 */
	public static void testparseToHashMap() throws FileNotFoundException {
		System.out.println("\n[正在测试方法: XMLHelper.parseToHashMap()...]");
		Element el = new Element("Text");
		System.out.println(XMLHelper.parseToHashMap(el));
		el.setAttribute("Attr1","1");
		System.out.println(XMLHelper.parseToHashMap(el));
		el.setAttribute("Attr2","2");
		System.out.println(XMLHelper.parseToHashMap(el));
	}

  
  public static void testtoXMLString(){
    System.out.println("\n[正在测试方法: XMLHelper.toXMLString()...]");
    Element root = new Element("Root");
    Element row = new Element("Row");
    root.addContent(row);
    System.out.println(XMLHelper.toXMLString(root));
    System.out.println(XMLHelper.toXMLString(row));

  }

  public static void testsave(){
    System.out.println("\n[正在测试方法: XMLHelper.save()...]");
    Element root = new Element("Root");
    Element row = new Element("Row");
    root.addContent(row);
    System.out.println(root.getDocument());
    //System.out.println(XMLHelper.toXMLString(row));
    XMLHelper.save(root, "test_root.xml");
    XMLHelper.save(row, "test_row.xml");
    Document doc = new Document();
    doc.setRootElement(root);
    XMLHelper.save(doc, "test_doc_root.xml");

  }

  
  
	//public static void main(String[] args){
	//	System.out.println(111);
	//}
}
