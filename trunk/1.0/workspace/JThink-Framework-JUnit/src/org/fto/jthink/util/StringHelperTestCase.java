/*
 * 创建日期 2005-10-2
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package org.fto.jthink.util;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Arrays;

import org.jdom.Element;

import junit.framework.TestCase;

/**
 * @author Administrator
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class StringHelperTestCase  extends TestCase{

	public static void main(String[] args) {
		junit.textui.TestRunner.run(StringHelperTestCase.class);
	}
	
	
  private static String substring(String src, String start, String end) {
    int len = src.length();
    int endlen = end.length();
    int sindex = src.indexOf(start) + start.length();
    // logger.debug("sindex:"+sindex);
    int eindex = 0;
    int subindex = sindex;
    for (; subindex < len; subindex++) {
      for (int i = subindex; i < len; i++) {
        // logger.debug("i+endlen:"+(i+endlen));
        int subeidx = i + endlen;
        subeidx = subeidx >= len ? len - 1 : subeidx;
        // logger.debug("subeidx:"+subeidx);
        String substr = src.substring(i, subeidx);
        // logger.debug("substr:"+substr);
        if (substr.equals(end)) {
          eindex = i;
          break;
        }
      }
      if (eindex != 0) {
        break;
      }
    }
    // System.out.println("sindex:"+sindex);
    // System.out.println("eindex:"+eindex);
    if (eindex != 0) {
      return src.substring(sindex, eindex);
    }
    return null;
  }


	
	/**
	 * 测试主程序
	 */
	public static void testmain() throws FileNotFoundException {
	  String[] strs = StringHelper.split("34|31|12|11|37|55|41|14|36|13|33|32|44|35|43|45|28|42|65|54|62|15|61|64|21|22|886|23|853|852|52|53|63|86|46","|");
	  System.out.println(StringHelper.contains(strs, "37"));
	 // int index = Character.binarySearch(strs, "28");
	  //System.out.println(index);
//	  System.out.println(StringHelper.join(new String[]{"a","b","c","d","e"}, ","));
//	  System.out.println(StringHelper.join(IteratorHelper.toIterator(new String[]{"a","b","c","d","e"}), ",", "\'"));
	  
	  //System.out.println(StringHelper.substring("abcdefg", 4,4));
//	  System.out.println(StringHelper.substring("1234abcdefg1234abcdef", 3,"a"));
//	  System.out.println(StringHelper.substring("1234abcdefg1234abc1def", "c1dy",4));
//	  System.out.println(StringHelper.substring("1234abcdefg1234abc1def", "12","34a"));
//	  System.out.println(substring("1234abcdefg1234abc1def", "12","34a"));
//		Element esdefs = XMLHelper.load("C:\\Mission\\MissionSM4X\\ef.xml");
//		Element esdef = XMLHelper.getChild(esdefs, "id", "ES_COMMON_OUTPUT");
		
//		System.out.println("convert() : "+StringHelper.convert("ab'cd<e\nf", esdef));
		
//      String[] strs = split("A,", ",", true);
//      String[] strs = StringHelper.split("A,,B", ",");
//      for(int i=0; i<strs.length;i++){
//        System.out.println("'"+strs[i]+"'");
//      }

//      List lt = new ArrayList();
//      lt.add("A");lt.add("B");lt.add("C");lt.add("D");
//      System.out.println(join(lt.iterator(), ","));
//
//      System.out.println(join(new String[]{"A","B","C"}, ","));

//      System.out.println(replaceOnce("abcd", "abc", "ABC"));
//      System.out.println(replace("abcCabcDabc", "abc", "ABC", true));
//      System.out.println(replace("ABCDABCDA", "A", "A1"));
//
//      System.out.println(replace("abcdefg,hijklmn,opq,rst,act,adt,sd", "a*", "AAA*"));
//
//      System.out.println(replaceAll("abcdefg,hijklmn,opq,rst,act,adt,sd", "a*", "AAA*"));

//      String[] x = new String[]{"0","1","2","3"};
//      String[] y = new String[]{"0","1","2","3"};
//      String[] strs = add(x,"+",y);
//      for(int i=0; i<strs.length;i++){
//        System.out.println(strs[i]);
//      }

//      System.out.println(repeat("ABCD",10));


//		System.out.println(getMatchString("Order QT300. Now! I am a sttudent,hou 'arr' you are?", "(r (Q[^u]\\d+\\.))"));
//		System.out.println(getMatchString("Order QT300. Now! I am a sttudent,hou 'arr' you are?", "r (Q[^u]\\d+\\.)"));
//		System.out.println(getMatchString("Order QT300. Now! I am a sttudent,hou 'arr' you are?", "Q[^u]\\d+\\."));
//		System.out.println(getMatchString("Order QT300. Now! I am a sttudent,hou 'arr' you are?", "s.*t"));
//		System.out.println(getMatchString("Order QT300. Now! I am a sttudent,hou 'arr' you are?", "arr'"));
//		System.out.println(getMatchString("Order QT300. Now! I am a sttudent,hou 'arr' you are?", "hou"));
//		System.out.println(getMatchString("smith,john and others are our student", "(.*),(.{1,4})"));
//		System.out.println(getMatchString("QA777. is the next flight.It is on time.", "^Q[^U]\\d+\\."));
//		/**
//		 * 匹配特特字符示例
//		 */
//		System.out.println(getMatchString("Order QT300.", "Q[^U]\\d+\\.$"));
//		System.out.println(getMatchString("Order QT300$.", "Q[^U]\\d+\\$\\.$"));
//		System.out.println(getMatchString("Order QT(300$).", "Q[^U]\\(\\d+\\$\\)\\.$"));
//		System.out.println(getMatchString("Order \\QT300$.", "\\\\Q[^U]\\d+\\$\\.$"));
//		/**
//		 * 匹配中含有换行符的
//		 */
//		System.out.println(getMatchString("I dream of engines\nmore engines, all day long", "engines\nmore engines"));
//
//		/**
//		 * 字符串替换
//		 */
//		System.out.println(replace("I a student,hou about you,hou?", "hou", "how"));
//		/**
//		 * 没有替换，返回原字符串
//		 */
//		System.out.println(replace("I a student,hou about you,hou?", "how", "hw"));
//		System.out.println("test1");
//		System.out.println(replace("I a axxt student,hou about you,hou?", "a[a-z]{1,3}t", "how"));
//
//		/**
//		 * 找出长字符串中的字符
//		 */
//		Reader r = new StringReader("I am tbnert trat,that are you name?\n,hello,how are you?\n\tbabay,this is tjust a test");
//		String[] re = getMatchString(r, "t[a-z]*t");
//		for (int i = 0; i < re.length; i++) {
//			System.out.println(re[i]);
//		}
//		System.out.println("------------------");
//		/**
//		 * 找出文件中的字符
//		 */
//		Reader rf = new FileReader("C:\\test.txt");
//		String[] re2 = getMatchString(rf, "\\?.*\\?");
//		for (int i = 0; i < re2.length; i++) {
//			System.out.println(re2[i]);
//		}
//
//		String[] re3 = getMatchStrings("I dream of engines more engineas, all day long", "e[a-z]{1,8}s");
//		for (int j = 0; j < re3.length; j++) {
//			System.out.println(re3[j]);
//		}
//
//		String[] r4 = split("I am a student, what about you?", " ");
//		for (int j = 0; j < r4.length; j++) {
//			System.out.println(r4[j]);
//		}
//
//		String[] r5 = split("123,234,456,789,", ",");
//		for (int j = 0; j < r5.length; j++) {
//			System.out.println(r5[j]);
//		}
//
//		String[] r6 = split("yourardivattrxyzatxyrjosserabner", "a[a-z]{0,5}r");
//		for (int j = 0; j < r6.length; j++) {
//			System.out.println(r6[j]);
//		}


	}
}
