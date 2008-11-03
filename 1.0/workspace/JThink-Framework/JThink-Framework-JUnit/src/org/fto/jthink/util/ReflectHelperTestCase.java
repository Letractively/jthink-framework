/*
 * 创建日期 2005-10-2
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
public class ReflectHelperTestCase extends TestCase{

	public String str = "";
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(NumberHelperTestCase.class);
	}
	
	
	/**
	 * 测试
	 */
	public void testmain() {
		try {
			System.out.println("isPublic() : "+ReflectHelper.isPublic(ReflectHelperTestCase.class, ReflectHelperTestCase.class.getMethod("testmain", null)));
			System.out.println("isPublic() : "+ReflectHelper.isPublic(ReflectHelper.class, ReflectHelper.class.getMethod("isAbstract", new Class[]{Class.class})));
			System.out.println("isPublic() : "+ReflectHelper.isPublic(ReflectHelperTestCase.class, ReflectHelperTestCase.class.getDeclaredMethod("t", null)));
			System.out.println("isPublic() : "+ReflectHelper.isPublic(ReflectHelperTestCase.class, ReflectHelperTestCase.class.getDeclaredField("str")));

			String str = "测试主ReflectHelper.invoke";
			//StringBuffer sb = new StringBuffer();
			
			System.out.println(ReflectHelper.invoke(str, "toString"));
			System.out.println(ReflectHelper.invoke(this, "t"));
			System.out.println(ReflectHelper.invokeDeclared(this, "a"));
			System.out.println(ReflectHelper.invoke(new A1(), "m", new Class[]{String.class}, new Object[]{"test!!!"}));
			
		} catch (SecurityException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}
	
	public void t(){
		System.out.println("执行方法: t()");
	}
	private String a(){
	  System.out.println("执行方法: a()");
	  return "a";
	}
	
	class A{
	  public String m(String s){
	    return s;
	  }
	}
	
	class A1 extends A{
	  
	}
}
