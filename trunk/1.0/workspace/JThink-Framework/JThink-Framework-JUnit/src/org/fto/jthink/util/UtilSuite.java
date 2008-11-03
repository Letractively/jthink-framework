/*
 * 创建日期 2005-7-10
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package org.fto.jthink.util;


import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Administrator
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class UtilSuite extends TestSuite {

	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(UtilSuite.class);
	}

  public static Test suite(){
  	System.out.println("批量测试包[org.fto.jfree.util.*]");
    TestSuite suite = new UtilSuite("Running all tests");
    
    suite.addTestSuite(PackageResourceReaderTestCase.class);
    suite.addTestSuite(XMLHelperTestCase.class);
    suite.addTestSuite(FileHelperTestCase.class);
    
    return suite;
  }

	public UtilSuite(String name) {
		super(name);
	}

}
