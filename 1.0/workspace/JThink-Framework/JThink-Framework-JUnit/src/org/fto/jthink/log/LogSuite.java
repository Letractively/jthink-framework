/*
 * 创建日期 2005-7-25
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package org.fto.jthink.log;

import org.fto.jthink.util.UtilSuite;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Administrator
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class LogSuite extends TestSuite {
	public static void main(String[] args) {
		junit.textui.TestRunner.run(UtilSuite.class);
	}

  public static Test suite(){
  	System.out.println("批量测试包[org.fto.jfree.log.*]");
    TestSuite suite = new UtilSuite("Running all tests");
    
    suite.addTestSuite(LogManagerTestCase.class);
    
    return suite;
  }

	public LogSuite(String name) {
		super(name);
	}

}
