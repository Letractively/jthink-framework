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
public class NumberHelperTestCase extends TestCase{

		public static void main(String[] args) {
			junit.textui.TestRunner.run(NumberHelperTestCase.class);
		}

		
		/**
		 * 测试主程序 
		 * @param args	 
		 */
		public void testmain() {
			System.out.println("getReadCNStringOfNumber() : "+NumberHelper.getReadCNStringOfNumber("2022211102"));
			System.out.println("getReadCNStringOfNumber() 2000070009: "+NumberHelper.getReadCNStringOfNumber("2000070009"));
			
			System.out.println("formatNumber() : "+NumberHelper.formatNumber(1111.255,NumberHelper.MONEY_I_2));
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(2100000005));
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(2080004005));
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(100000005));
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(120004005));
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(28004005));
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(3004005));
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(204005));
			
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(54005));		
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(15115.51));
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(1011.346));
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(1000));
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(800));
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(845));
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(805));
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(85));
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(80));
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(10));
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(10.5));
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(5.5));
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(5));
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(0.5));	
			System.out.println("getCNMoneyStringOfNumber() 2000070009.65: "+NumberHelper.getCNMoneyStringOfNumber(2000070009.65));	
			
			System.out.println("getReadMoneyCNStringOfNumber() 2000070009: "+NumberHelper.getReadMoneyCNStringOfNumber(2000070009));
			
			
			
			System.out.println("getReadBigCNStringOfNumber() : "+NumberHelper.getReadBigCNStringOfNumber("5505"));
			System.out.println("getReadBigCNStringOfNumber() : "+NumberHelper.getReadBigCNStringOfNumber(11));
			System.out.println("getReadBigCNStringOfNumber() : "+NumberHelper.getReadBigCNStringOfNumber(2030570609));
			System.out.println("getReadBigCNStringOfNumber() 2000070009 : "+NumberHelper.getReadBigCNStringOfNumber(2000070009));
			
			System.out.println("getCNMoneyStringOfNumber() : "+NumberHelper.getCNMoneyStringOfNumber(114508));
			
			System.out.println("unFormatCommaNumber() : "+NumberHelper.unFormatCommaNumber("3,354.12"));
			
			System.out.println("formatNumber() : "+NumberHelper.formatNumber("1234.22"));
			
				

		}
}
