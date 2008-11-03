/*
 * 创建日期 2005-7-13
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package org.fto.jthink.util;

import java.net.URL;
import java.util.Date;

import org.fto.jthink.util.PackageResourceReader;


import junit.framework.TestCase;

/**
 * @author Administrator
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class PackageResourceReaderTestCase extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(PackageResourceReaderTestCase.class);
	}


	/**
	 *方法测试
	 */
	public void testGetResource() {
		System.out.println("\n[正在测试方法: PackageResourceReaderTestCase.getResource()...]");

		URL url;
		/* 测试资源, 绝对路径 */
		url = new PackageResourceReader(this.getClass()).getResource("/org/fto/jthink/util/PackageResourceReader.class");
		System.out.println(url);
		assertNull(url);

		url = new PackageResourceReader(this.getClass()).getResource("/org/fto/jthink/util/UtilSuite.class");
		System.out.println(url);
		assertNotNull(url);
		
		url = new PackageResourceReader(PackageResourceReader.class).getResource("/org/fto/jthink/util/PackageResourceReader.class");
		System.out.println(url);
		assertNotNull(url);
		
		
		url = new PackageResourceReader(Long.class).getResource("/java/lang/Long.class");
		System.out.println(url);
		assertNotNull(url);

		url = new PackageResourceReader(this.getClass()).getResource("/META-INF/DbClrBrk.gif");
		System.out.println(url);
		//assertNotNull(url);
		
		
		url = new PackageResourceReader(Long.class).getResource("/META-INF/MANIFEST.MF");
		System.out.println(url);
		//assertNotNull(url);

		url = new PackageResourceReader(this.getClass()).getResource("/META-INF/MANIFEST.MF");
		System.out.println(url);
		//assertNotNull(url);
	
		/* 测试资源, 相对路径 */
		url = new PackageResourceReader(this.getClass()).getResource("DbCllStk.gif");
		System.out.println(url);
		//assertNotNull(url);
		
		url = new PackageResourceReader(this.getClass()).getResource("../DbBrkAt.gif");
		System.out.println(url);
		//assertNotNull(url);
		
		System.out.println("getContextClassLoader:"+Thread.currentThread().getContextClassLoader().getResource("META-INF/MANIFEST.MF"));
		System.out.println("getClassLoader:"+this.getClass().getClassLoader().getResource("META-INF/MANIFEST.MF"));
		
//		try {
//			Enumeration enu = this.getClass().getClassLoader().getResources("META-INF/MANIFEST.MF");
//			while(enu.hasMoreElements()){
//				System.out.println("getClassLoader enu="+enu.nextElement());
//			}
//			
//		} catch (IOException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}
//		
//		try {
//			Enumeration enu = Thread.currentThread().getContextClassLoader().getResources("META-INF/MANIFEST.MF");
//			while(enu.hasMoreElements()){
//				System.out.println("getContextClassLoader enu="+enu.nextElement());
//			}
//			
//		} catch (IOException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}

		
	}

	
	/**
	 *方法测试
	 */
	public void testGetClassURL() {
		System.out.println("\n[正在测试方法: PackageResourceReaderTestCase.getClassURL()...]");
		
		URL url = PackageResourceReader.getClassURL(this.getClass());
		System.out.println(url);
		assertNotNull(url);
		
		url = PackageResourceReader.getClassURL(Date.class);
		System.out.println(url);
		assertNotNull(url);

	}

	
	
}
