/*
 * 创建日期 2005-7-15
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package org.fto.jthink.jdbc;






import org.fto.jthink.jdbc.Column;
import org.fto.jthink.jdbc.Condition;
import org.fto.jthink.jdbc.ConditionItem;
import org.fto.jthink.jdbc.SQL;
import org.fto.jthink.jdbc.SQLBuilder;
//import org.fto.jthink.lang.ObjectBuffered;
import org.fto.jthink.util.NumberHelper;

import junit.framework.TestCase;

/**
 * @author Administrator
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class ConditionTestCase extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(ConditionTestCase.class);
	}

	private SQLBuilder sqlBuilder = new DefaultSQLBuilder(); 
	
	/**
	 *方法测试
	 */
	public void testGetConditionString() {
		System.out.println("\n[正在测试方法: ConditionTestCase.getConditionString()...]");
		Condition condn;
		condn = new Condition();
		System.out.println("getConditionString=("+condn.getConditionString()+")");
		assertTrue(condn.getConditionString().length()==0);
		printObjects(condn.getValues());

		condn.add(" anD ", new ConditionItem("DeptId", "=", "1"));
		condn.add(Condition.AND, new ConditionItem("DeptId", "=", "2857"));
		condn.add(Condition.AND, new ConditionItem("DeptName", "Like", "%部%"));
		condn.add(new ConditionItem("DeptId", "Between", new Object[]{"1","3"}));
		condn.add(new ConditionItem("DeptId", "IN", new Object[]{"1","2", "3", "4"}));
		System.out.println("getConditionString=("+condn.getConditionString()+")");
		printObjects(condn.getValues());

		Condition condn1 = new Condition();
		condn1.add(new ConditionItem("DeptId", "!=", "3"));

		
		Condition condn2 = new Condition();
		condn2.add(Condition.OR, new ConditionItem("DeptId", "=", "1"));
//		condn2.add(Condition.OR, "DeptId", "=", "2");
//		condn2.add(Condition.OR, "DeptId", "IN", new Object[]{"3", "4", "5"});
		condn2.add(Condition.OR, new ConditionItem("DeptId", "Between", new Object[]{"6", "9"}));
		System.out.println("getConditionString=("+condn2.getConditionString()+")");
		printObjects(condn2.getValues());
		
		condn1.add("OR", condn2);
		
		condn = new Condition();
		condn.add(Condition.AND_NOT, new ConditionItem("DeptName", "like", "%部%"));
		condn.add(Condition.AND, condn1);
		condn.add(Condition.AND_NOT, new ConditionItem("DeptDesc", "like", "%技术%"));
		System.out.println("getConditionString=("+condn.getConditionString()+")");
		assertEquals(condn.getConditionString().trim(), "NOT DeptName like ? AND ( DeptId != ? OR ( DeptId = ? OR DeptId Between ? AND ? ) ) AND NOT DeptDesc like ?");
		printObjects(condn.getValues());
		
		Condition deptCondn = new Condition();
		deptCondn.add(new ConditionItem("Depts.DeptId", "=", "Users.DeptId", true));
		SQL sql = sqlBuilder.constructSQLForSelect("Depts", new Column[]{new Column("DeptName")},deptCondn);
		
		condn = new Condition();
		condn.add(new ConditionItem("DeptName", "like", "%部%", true));
		condn.add(new ConditionItem("Depts.DeptId", "=", "Users.DeptId", true));
		condn.add(new ConditionItem("DeptName", "=", sql));
//		System.out.println("getConditionString=("+condn.getConditionStatement()+")");
		printObjects(condn.getValues());
		
    
    /* Condition测试开始 */
    {
      double totalUseTime = 0;
      int count = 0;
      for(int i=0;i<2000;i++){//在此设置测试次数
          long stime = System.nanoTime();        
          /* 测试代码 开始 */
          
          condn = new Condition();
          condn.add(new ConditionItem("DeptName", "like", "%部%", true));
          condn.add(new ConditionItem("Depts.DeptId", "=", "Users.DeptId", true));
          condn.add(new ConditionItem("DeptName", "=", sql));
          
//          //condn.getConditionString();
//          //condn.getConditionStatement().toString();
//          //condn.getValues();
//          //ObjectBuffered list = new ObjectBuffered();
//          
//          //list.addAll(condn.getValueList());
//          //ObjectBuffered l = condn.getValueList();
////          int len = l.size();
////          for(int c=0;c<len;c++){
////            list.append(l.toArray());
////          }
//          
////          list.toArray();
//          
////          condn.getValueList().toArray();
          
          /* 测试代码 结束 */
          
          double usetime = (System.nanoTime()-stime)/1000000f;
          if(usetime<0.05 && usetime>0){//大于50可认为是随机峰值，不参加统计，可根据情况调整
              totalUseTime += usetime;
              count++;
          }
      }
      System.out.println("Condition测试 次数："+count+", 总用时："+NumberHelper.formatNumber(totalUseTime, NumberHelper.NUMBER_I_6_0)+", 平均用时（毫秒）:"+NumberHelper.formatNumber((totalUseTime/count), NumberHelper.NUMBER_I_6_0));
    } 
		
	}

	
	private void printObjects(Object[] objs){
		for(int i=0;i<objs.length;i++){
			System.out.print(objs[i]+",");
		}
		System.out.println();
	}
}
