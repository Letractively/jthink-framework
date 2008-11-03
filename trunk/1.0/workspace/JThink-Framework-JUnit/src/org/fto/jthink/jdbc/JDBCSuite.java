/*
 * 创建日期 2005-7-10
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package org.fto.jthink.jdbc;


import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Administrator
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class JDBCSuite extends TestSuite {

	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(JDBCSuite.class);
	}

  public static Test suite(){
  	System.out.println("批量测试包[org.fto.jfree.util.*]");
    TestSuite suite = new JDBCSuite("Running all tests");
    
    suite.addTestSuite(ConditionTestCase.class);
    suite.addTestSuite(SQLBuilderTestCase.class);
    suite.addTestSuite(ConnectionPoolTestCase.class);
    suite.addTestSuite(SQLTestCase.class);
   
    return suite;
  }

	public JDBCSuite(String name) {
		super(name);
	}

}
