/*
 * 创建日期 2005-7-15
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package org.fto.jthink.jdbc;




import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletInputStream;

import org.fto.jthink.jdbc.Column;
import org.fto.jthink.jdbc.Condition;
import org.fto.jthink.jdbc.SQL;
import org.fto.jthink.jdbc.SQLBuilder;
import org.fto.jthink.util.NumberHelper;

import junit.framework.TestCase;

/**
 * @author Administrator
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class SQLBuilderTestCase extends TestCase {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(SQLBuilderTestCase.class);
  }

  private SQLBuilder sqlBuilder = new DefaultSQLBuilder(); 
  
  /**
   *方法测试
   */
  public void testConstructSQLForInsert() {
    System.out.println("\n[正在测试方法: SQLBuilderTestCase.ConstructSQLForInsert()...]");
    
    HashMap columns = new HashMap();
    columns.put("DeptId", "1");
    columns.put("DeptName", "3");
    columns.put("DeptDesc", null);
    columns.put("F1", "1");
    columns.put("F2", "2");
    columns.put("F3", "3");
    
    
    /* constructSQLForInsert测试开始 */
    {
      double totalUseTime = 0;
      int count = 0;
      for(int i=0;i<100;i++){//在此设置测试次数
          long stime = System.nanoTime();        
          
          /* 测试代码 开始 */
          sqlBuilder.constructSQLForInsert("departments", columns);
          /* 测试代码 结束 */
          
          double usetime = (System.nanoTime()-stime)/1000000f;
          if(usetime<0.1 && usetime>0){//大于50可认为是随机峰值，不参加统计，可根据情况调整
              totalUseTime += usetime;
              count++;
          }
          //System.out.println("time:"+(NumberHelper.formatNumber(usetime, NumberHelper.NUMBER_I_6_0)));
      }
      System.out.println("constructSQLForInsert测试 次数："+count+", 总用时："+NumberHelper.formatNumber(totalUseTime, NumberHelper.NUMBER_I_6_0)+", 平均用时（毫秒）:"+NumberHelper.formatNumber((totalUseTime/count), NumberHelper.NUMBER_I_6_0));
    }

    SQL sqlStatement = sqlBuilder.constructSQLForInsert("departments", columns);
    System.out.println(sqlStatement.getSQLString());
    if(!"INSERT INTO departments (F1,DeptName,DeptId,F3,F2) VALUES (?,?,?,?,?) ".equals(sqlStatement.getSQLString())){
      super.fail();
    }
    printObjects(sqlStatement.getValues());
    
    DataObject data = new DefaultDataObject();
    data.setTableName("departments");
    data.set("DeptId", "1");
    sqlStatement = sqlBuilder.constructSQLForInsert(data);
    System.out.println(sqlStatement.getSQLString());
    printObjects(sqlStatement.getValues());
    
    //List a;
    //a.addAll(c)
  }

  /**
   *方法测试
   */
  public void testconstructSQLForUpdate() {
    System.out.println("\n[正在测试方法: SQLBuilderTestCase.constructSQLForUpdate()...]");
    
    HashMap columns = new HashMap();
    columns.put("DeptName", "test1");
    columns.put("DeptDesc", null);//"desc test1");
    
    
    Condition condn = new Condition();
    Condition condn1 = new Condition();
    condn1.add(new ConditionItem("DeptId", "!=", "3"));
    
    Condition condn2 = new Condition();
    condn2.add(Condition.OR, new ConditionItem("DeptId", "Between", new Object[]{"6", "9"}));
    condn1.add("OR", condn2);
    condn.add(Condition.AND_NOT, new ConditionItem("DeptName", "like", "%部%"));
    condn.add(Condition.AND, condn1);
    condn.add(Condition.AND_NOT, new ConditionItem("DeptDesc", "like", "%技术%"));

    
    /* constructSQLForUpdate测试开始 */
    {
      double totalUseTime = 0;
      int count = 0;
      for(int i=0;i<100;i++){//在此设置测试次数
          long stime = System.nanoTime();        
          
          /* 测试代码 开始 */
          sqlBuilder.constructSQLForUpdate("departments", columns, condn);
          /* 测试代码 结束 */
          
          double usetime = (System.nanoTime()-stime)/1000000f;
          if(usetime<0.1 && usetime>0){//大于50可认为是随机峰值，不参加统计，可根据情况调整
              totalUseTime += usetime;
              count++;
          }
      }
      System.out.println("constructSQLForUpdate测试 次数："+count+", 总用时："+NumberHelper.formatNumber(totalUseTime, NumberHelper.NUMBER_I_6_0)+", 平均用时（毫秒）:"+NumberHelper.formatNumber((totalUseTime/count), NumberHelper.NUMBER_I_6_0));
    }   
    
    SQL sqlStatement = sqlBuilder.constructSQLForUpdate("departments", columns, condn);
    System.out.println(sqlStatement.getSQLString());
    if(!"UPDATE departments SET DeptName=?,DeptDesc=NULL  WHERE NOT DeptName like ? AND ( DeptId != ? OR ( DeptId Between ? AND ? ) ) AND NOT DeptDesc like ? ".equals(sqlStatement.getSQLString())){
      super.fail();
    }
    printObjects(sqlStatement.getValues());
    
    
    sqlStatement = sqlBuilder.constructSQLForUpdate("departments", columns, null);
    System.out.println(sqlStatement.getSQLString());
    //UPDATE departments SET DeptName=?,DeptDesc=NULL
    if(!"UPDATE departments SET DeptName=?,DeptDesc=NULL".equals(sqlStatement.getSQLString())){
      super.fail();
    }
    printObjects(sqlStatement.getValues());
  }


  /**
   *方法测试
   */
  public void testconstructSQLForDelete() {
    System.out.println("\n[正在测试方法: SQLBuilderTestCase.constructSQLForDelete()...]");
    
    Condition condn = new Condition();
    
    Condition condn1 = new Condition();
    condn1.add(new ConditionItem("DeptId", "!=", "3"));
    Condition condn2 = new Condition();
    condn2.add(Condition.OR, new ConditionItem("DeptId", "Between", new Object[]{"6", "9"}));
    condn1.add("OR", condn2);
    condn.add(Condition.AND_NOT, new ConditionItem("DeptName", "like", "%部%"));
    condn.add(Condition.AND, condn1);

    /* constructSQLForDelete测试开始 */
    {
      double totalUseTime = 0;
      int count = 0;
      for(int i=0;i<200;i++){//在此设置测试次数
          long stime = System.nanoTime();        
          
          /* 测试代码 开始 */
          sqlBuilder.constructSQLForDelete("departments", condn);
          /* 测试代码 结束 */
          
          double usetime = (System.nanoTime()-stime)/1000000f;
          if(usetime<0.03 && usetime>0){//大于50可认为是随机峰值，不参加统计，可根据情况调整
              totalUseTime += usetime;
              count++;
          }
      }
      System.out.println("constructSQLForDelete测试 次数："+count+", 总用时："+NumberHelper.formatNumber(totalUseTime, NumberHelper.NUMBER_I_6_0)+", 平均用时（毫秒）:"+NumberHelper.formatNumber((totalUseTime/count), NumberHelper.NUMBER_I_6_0));
    }   
    
    SQL sqlStatement = sqlBuilder.constructSQLForDelete("departments", condn);
    System.out.println(sqlStatement.getSQLString());
        //DELETE FROM departments WHERE  DeptName like ? AND ( DeptId != ? OR ( DeptId Between ? AND ? ) ) 
    if(!"DELETE FROM departments WHERE NOT DeptName like ? AND ( DeptId != ? OR ( DeptId Between ? AND ? ) ) ".equals(sqlStatement.getSQLString())){
      super.fail();
    }
    
    
    
    printObjects(sqlStatement.getValues());
  }
  /**
   *方法测试
   */
  public void testconstructSQL_ForSelect$Condition() {
    System.out.println("\n[正在测试方法: SQLBuilderTestCase.testconstructSQL_ForSelect$Condition()...]");
    
    Condition condn = new Condition();
    condn.add(new ConditionItem("DEPT_NAME", "IN", new SQL(SQL.SELECT, "SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID", null)));
    condn.add(new ConditionItem("DEPT_NO", "IS", "NOT NULL"));
    
    
    Column[] columns = new Column[]{
        new Column("DEPT_ID"),
        new Column("DEPT_NAME", new SQL(SQL.SELECT, "SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID", null)),
    };
    
    SQL sqlStatement = sqlBuilder.
    constructSQLForSelect("departments",columns, condn);

      System.out.println(sqlStatement.getSQLString());
      printObjects(sqlStatement.getValues());   
  }
  
  
  /**
   *方法测试
   */
  public void testconstructSQL_ForSelect() {
    System.out.println("\n[正在测试方法: SQLBuilderTestCase.constructSQL_ForSelect()...]");
    
    Condition condn = new Condition();
    
    Condition condn1 = new Condition();
    condn1.add(new ConditionItem("DeptId", "!=", "3"));
    
    Condition condn2 = new Condition();
    condn2.add(Condition.OR, new ConditionItem("DeptId", "!=", new Integer(2)));
    condn2.add(Condition.OR, new ConditionItem("DeptId", "Between", new Object[]{"6", "9"}));
    
    condn1.add("OR", condn2);
    
    condn.add(Condition.AND_NOT, new ConditionItem("DeptName", "like", "%部%"));
    condn.add(Condition.AND, condn1);
//    condn.add(Condition.AND_NOT, "DeptDesc", "like", "%技术%");
    
    SQL sqlStatement = sqlBuilder.
          constructSQLForSelect("departments", 
                true, null, condn, null, null);
    
    System.out.println(sqlStatement.getSQLString());
    printObjects(sqlStatement.getValues());
    
    Condition userNameCDN = new Condition();
    userNameCDN.add(new ConditionItem("Users.UserId", "=", "Departments.UserId", true));
    SQL userNameSQL = sqlBuilder.constructSQLForSelect("Users", new Column[]{new Column("UserName")}, userNameCDN);
    
    Column[] columns = new Column[]{
      new Column("DeptId"),
      new Column("DeptName", "DeptName"),
      new Column("DeptXXX1", "DeptName"),
      new Column("DeptXXX2", "DeptId*100/6"),
      new Column("DeptXXX3", (String)null),
      new Column("UserName", userNameSQL),
    };
//    new String[]{"DeptId, DeptName, DeptDesc"}
    sqlStatement = sqlBuilder.
    constructSQLForSelect("departments", 
        false, columns, condn, "DeptName", "DeptId");

    System.out.println(sqlStatement.getSQLString());
    printObjects(sqlStatement.getValues());

    sqlStatement = sqlBuilder.
          constructSQLForSelect("departments",null, null);

    System.out.println(sqlStatement.getSQLString());
    printObjects(sqlStatement.getValues());   
  }

  
  /**
   *方法测试
   */
  public void testconstructSQLForCount() {
    System.out.println("\n[正在测试方法: SQLBuilderTestCase.constructSQLForCount()...]");
    
    Condition condn = new Condition();
    
    Condition condn1 = new Condition();
    condn1.add(new ConditionItem("DeptId", "!=", "3"));
    
    Condition condn2 = new Condition();
    condn2.add(Condition.OR, new ConditionItem("DeptId", "!=", new Integer(2)));
    condn2.add(Condition.OR, new ConditionItem("DeptId", "Between", new Object[]{"6", "9"}));
    
    condn1.add("OR", condn2);
    
    condn.add(Condition.AND_NOT, new ConditionItem("DeptName", "like", "%部%"));
    condn.add(Condition.AND, condn1);
//    condn.add(Condition.AND_NOT, "DeptDesc", "like", "%技术%");

    SQL sqlStatement = sqlBuilder.
            constructSQLForCount("departments","*", "DeptCount", condn);
    System.out.println(sqlStatement.getSQLString());
    printObjects(sqlStatement.getValues());   
    
    
    sqlStatement = sqlBuilder.
      constructSQLForCount("departments","DeptId", "DeptCount", null);
    System.out.println(sqlStatement.getSQLString());
    printObjects(sqlStatement.getValues());   
  }

  
  
  private void printObjects(Object[] objs){
    for(int i=0;i<objs.length;i++){
      System.out.print(objs[i]+",");
    }
    System.out.println();
  }
}
