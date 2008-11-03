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
		assertEquals(condn.getConditionString(), " NOT DeptName like ? AND ( DeptId != ? OR ( DeptId = ? OR DeptId Between ? AND ? ) ) AND NOT DeptDesc like ? ");
		printObjects(condn.getValues());
		
		Condition deptCondn = new Condition();
		deptCondn.add(new ConditionItem("Depts.DeptId", "=", "Users.DeptId", true));
		SQL sql = sqlBuilder.constructSQLForSelect("Depts", new Column[]{new Column("DeptName")},deptCondn);
		
		condn = new Condition();
		condn.add(new ConditionItem("DeptName", "like", "%部%", true));
		condn.add(new ConditionItem("Depts.DeptId", "=", "Users.DeptId", true));
		condn.add(new ConditionItem("DeptName", "=", sql));
		System.out.println("getConditionString=("+condn.getConditionString()+")");
		printObjects(condn.getValues());
		
		
	}

	
	private void printObjects(Object[] objs){
		for(int i=0;i<objs.length;i++){
			System.out.print(objs[i]+",");
		}
		System.out.println();
	}
}