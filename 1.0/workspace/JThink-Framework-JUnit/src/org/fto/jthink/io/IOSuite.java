/*
 * 创建日期 2005-7-10
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package org.fto.jthink.io;

import org.fto.jthink.io.SmartAccessFileTestCase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Administrator
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class IOSuite extends TestSuite {

	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(IOSuite.class);
	}

  public static Test suite(){
  	System.out.println("批量测试包[org.fto.jfree.io.*]");
    TestSuite suite = new IOSuite("Running all tests");
    suite.addTestSuite( SmartAccessFileTestCase.class);
    
    return suite;
  }

	public IOSuite(String name) {
		super(name);
	}

}
