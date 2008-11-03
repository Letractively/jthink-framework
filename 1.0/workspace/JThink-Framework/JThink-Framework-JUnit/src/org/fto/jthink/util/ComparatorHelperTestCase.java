/*
 * 创建日期 2005-9-30
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package org.fto.jthink.util;



import junit.framework.TestCase;

/**
 * @author Administrator
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class ComparatorHelperTestCase  extends TestCase{

	public static void main(String[] args) {
		junit.textui.TestRunner.run(ComparatorHelperTestCase.class);
	}
	
	/**
	 *方法测试
	 */
	public void testEquals() {
		System.out.println("\n[正在测试方法: ComparatorHelperTestCase.equals()...]");
		
		assertTrue(ComparatorHelper.equals(null, null));
		assertTrue(ComparatorHelper.equals("", ""));
		assertFalse(ComparatorHelper.equals("", null));
		assertTrue(ComparatorHelper.equals("", ""));
		assertTrue(ComparatorHelper.equals("A", "A"));
		assertTrue(ComparatorHelper.equals("B", new String("B")));
		assertFalse(ComparatorHelper.equals(new Object(), new Object()));
	
		String[] xs = new String[]{"A", "B", "C"};
		String[] ys = new String[]{"A", "B", "C"};
		
		Object[] os = new Object[3];
		assertTrue(ComparatorHelper.equals(xs,ys));
		
		assertFalse(ComparatorHelper.equals(xs, os));
//
//		assertFalse(Arrays.equals(xs,null));
//		
//		assertTrue(Arrays.equals(xs, os));		
//		Arrays.equals()
		
	}

}
