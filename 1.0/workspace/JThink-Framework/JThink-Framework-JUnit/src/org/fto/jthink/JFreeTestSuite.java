/*
 * 创建日期 2005-7-10
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package org.fto.jthink;

import org.fto.jthink.io.IOSuite;
import org.fto.jthink.jdbc.JDBCSuite;
import org.fto.jthink.log.LogSuite;
import org.fto.jthink.util.UtilSuite;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Administrator
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class JFreeTestSuite extends TestSuite {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(JFreeTestSuite.class);
	}

  public static Test suite(){
  	System.out.println("批量测试包[org.fto.jfree.*]");
    TestSuite suite = new JFreeTestSuite("Running all tests");
    
    junit.textui.TestRunner.run(IOSuite.suite());
    junit.textui.TestRunner.run(UtilSuite.suite());
    junit.textui.TestRunner.run(JDBCSuite.suite());
    junit.textui.TestRunner.run(LogSuite.suite());
    
    return suite;
  }

	public JFreeTestSuite(String name) {
		super(name);
	}

}
