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
import org.fto.jthink.jdbc.hsql.HsqlSQLBuilder;
import org.fto.jthink.jdbc.mssql.MssqlSQLBuilder;
import org.fto.jthink.jdbc.mysql.MysqlSQLBuilder;
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
  private SQLBuilder hsqlBuilder = new HsqlSQLBuilder(); 
  private SQLBuilder mssqlSQLBuilder = new MssqlSQLBuilder(); 
  private SQLBuilder mysqlSQLBuilder = new MysqlSQLBuilder(); 
  
  /**
   *方法测试
   */
  public void testConstructSQLForInsert() {
    System.out.println("\n[正在测试方法: SQLBuilder.ConstructSQLForInsert()...]");
    
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
      for(int i=0;i<1000;i++){//在此设置测试次数
          long stime = System.nanoTime();        
          
          /* 测试代码 开始 */
          sqlBuilder.constructSQLForInsert("departments", columns).getSQLString();
          /* 测试代码 结束 */
          
          double usetime = (System.nanoTime()-stime)/1000000f;
          if(usetime<0.05 && usetime>0){//大于50可认为是随机峰值，不参加统计，可根据情况调整
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
    System.out.println("\n[正在测试方法: SQLBuilder.constructSQLForUpdate()...]");
    
    HashMap columns = new HashMap();
    columns.put("DeptName", "test1");
    columns.put("DeptDesc", null);//"desc test1");
    columns.put("F1", "1");
    columns.put("F2", "2");
    columns.put("F3", "3");
    columns.put("F4", "4");
    columns.put("F5", "5");
    
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
      for(int i=0;i<2000;i++){//在此设置测试次数
          long stime = System.nanoTime();        
          
          /* 测试代码 开始 */
          sqlBuilder.constructSQLForUpdate("departments", columns, condn).getSQLString();
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
    System.out.println("columns.size()+condn.size():"+(columns.size()*3+4));
    System.out.println("sqlStatement.getSQLStatement().size():"+sqlStatement.getSQLStatement().size());
    System.out.println(sqlStatement.getSQLString());
    if(!"UPDATE departments SET F1=?,DeptName=?,F5=?,F4=?,F3=?,F2=?,DeptDesc=NULL WHERE NOT DeptName like ? AND ( DeptId != ? OR ( DeptId Between ? AND ? ) ) AND NOT DeptDesc like ? ".equals(sqlStatement.getSQLString())){
      super.fail();
    }
    printObjects(sqlStatement.getValues());
    
    
    sqlStatement = sqlBuilder.constructSQLForUpdate("departments", columns, null);
    System.out.println(sqlStatement.getSQLString());
    //UPDATE departments SET DeptName=?,DeptDesc=NULL
    if(!"UPDATE departments SET F1=?,DeptName=?,F5=?,F4=?,F3=?,F2=?,DeptDesc=NULL".equals(sqlStatement.getSQLString())){
      super.fail();
    }
    printObjects(sqlStatement.getValues());
  }


  /**
   *方法测试
   */
  public void testconstructSQLForDelete() {
    System.out.println("\n[正在测试方法: SQLBuilder.constructSQLForDelete()...]");
    
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
      for(int i=0;i<2000;i++){//在此设置测试次数
          long stime = System.nanoTime();        
          
          /* 测试代码 开始 */
          sqlBuilder.constructSQLForDelete("departments", condn).getSQLString();
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
    System.out.println("\n[正在测试方法: SQLBuilder.testconstructSQL_ForSelect$Condition()...]");
    
    Condition condn = new Condition();
    condn.add(new ConditionItem("DEPT_NAME", "IN", new SQL(SQL.SELECT, "SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID", null)));
    condn.add(new ConditionItem("DEPT_NO", "IS", "NOT NULL"));
    condn.add(new ConditionItem("F1", "=", "1"));
    condn.add(new ConditionItem("F2", "=", "2"));
    condn.add(new ConditionItem("F3", "=", "3"));
    condn.add(new ConditionItem("F4", "=", "4"));
    condn.add(new ConditionItem("F5", "=", "5"));
    condn.add(new ConditionItem("F6", "in", new Object[]{"1", "2", "3", "4"}));
    
    
    Column[] columns = new Column[]{
        new Column("DEPT_ID"),
        new Column("DEPT_NAME", new SQL(SQL.SELECT, "SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID", null)),
        new Column("F1"),
        new Column("F2"),
        new Column("F3"),
        new Column("F4"),
        new Column("F5"),
        new Column("F6"),
    };
    
    /* constructSQLForSelect测试开始 */
    {
      double totalUseTime = 0;
      int count = 0;
      for(int i=0;i<2000;i++){//在此设置测试次数
          long stime = System.nanoTime();        
          
          /* 测试代码 开始 */
          sqlBuilder.constructSQLForSelect("departments",false, columns, condn, (String)null, (String)null).getSQLString();
          /* 测试代码 结束 */
          
          double usetime = (System.nanoTime()-stime)/1000000f;
          if(usetime<0.09 && usetime>0){//大于50可认为是随机峰值，不参加统计，可根据情况调整
              totalUseTime += usetime;
              count++;
          }
      }
      System.out.println("constructSQLForSelect 测试 次数："+count+", 总用时："+NumberHelper.formatNumber(totalUseTime, NumberHelper.NUMBER_I_6_0)+", 平均用时（毫秒）:"+NumberHelper.formatNumber((totalUseTime/count), NumberHelper.NUMBER_I_6_0));
    }  
    
    SQL sqlStatement = sqlBuilder.constructSQLForSelect("departments", columns, condn);
    System.out.println("sqlStatement.getValues().length:"+sqlStatement.getValues().length);
    System.out.println("sqlStatement.getValueBuffered().size():"+sqlStatement.getValueBuffered().size());
    //System.out.println("sqlStatement.getValueBuffered().length():"+sqlStatement.getValueBuffered().length());
    //System.out.println("sqlStatement.getSQLStatement().size():"+sqlStatement.getSQLStatement().size());
    System.out.println(sqlStatement.getSQLString());
    if(!"SELECT DEPT_ID,(SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID) AS DEPT_NAME,F1,F2,F3,F4,F5,F6 FROM departments WHERE  DEPT_NAME IN (SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID) AND DEPT_NO IS NOT NULL AND F1 = ? AND F2 = ? AND F3 = ? AND F4 = ? AND F5 = ? AND F6 in (? ,? ,? ,? ) ".equals(sqlStatement.getSQLString())){
      super.fail();
    }
    printObjects(sqlStatement.getValues());   
  }
  
  
  /**
   *方法测试
   */
  public void testconstructSQL_ForSelect() {
    System.out.println("\n[正在测试方法: SQLBuilder.constructSQL_ForSelect()...]");
    
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
    if(!"SELECT  DISTINCT * FROM departments WHERE NOT DeptName like ? AND ( DeptId != ? OR ( DeptId != ? OR DeptId Between ? AND ? ) ) ".equals(sqlStatement.getSQLString())){
      super.fail();
    }
    
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

    //System.out.println("str comp:"+("DeptName"=="DeptName"));
    
    /* constructSQLForSelect测试开始 */
    {
      double totalUseTime = 0;
      int count = 0;
      for(int i=0;i<2000;i++){//在此设置测试次数
          long stime = System.nanoTime();        
          
          /* 测试代码 开始 */
          SQL sql = sqlBuilder.constructSQLForSelect("departments", 
              false, columns, condn, "DeptName", "DeptId");
          sql.getSQLString();
          //sql.getValueList().toArray();
          /* 测试代码 结束 */
          
          double usetime = (System.nanoTime()-stime)/1000000f;
          if(usetime<0.09 && usetime>0){//大于50可认为是随机峰值，不参加统计，可根据情况调整
              totalUseTime += usetime;
              count++;
          }
      }
      System.out.println("constructSQL_ForSelect 测试 次数："+count+", 总用时："+NumberHelper.formatNumber(totalUseTime, NumberHelper.NUMBER_I_6_0)+", 平均用时（毫秒）:"+NumberHelper.formatNumber((totalUseTime/count), NumberHelper.NUMBER_I_6_0));
    } 
    
    sqlStatement = sqlBuilder.
    constructSQLForSelect("departments", 
        false, columns, condn, "DeptName", "DeptId");
    
    System.out.println(sqlStatement.getSQLString());
    if(!"SELECT DeptId,DeptName,(DeptName) AS DeptXXX1,(DeptId*100/6) AS DeptXXX2,DeptXXX3,(SELECT UserName FROM Users WHERE  Users.UserId = Departments.UserId ) AS UserName FROM departments WHERE NOT DeptName like ? AND ( DeptId != ? OR ( DeptId != ? OR DeptId Between ? AND ? ) )  GROUP BY DeptName ORDER BY DeptId".equals(sqlStatement.getSQLString())){
      super.fail();
    }
    printObjects(sqlStatement.getValues());

    sqlStatement = sqlBuilder.
          constructSQLForSelect("departments",null, null);

    System.out.println(sqlStatement.getSQLString());
    if(!"SELECT * FROM departments".equals(sqlStatement.getSQLString())){
      super.fail();
    }
    
    printObjects(sqlStatement.getValues());   
  }

  
  /**
   *方法测试
   */
  public void testconstructSQLForCount() {
    System.out.println("\n[正在测试方法: SQLBuilder.constructSQLForCount()...]");
    
    Condition condn = new Condition();
    
    Condition condn1 = new Condition();
    condn1.add(new ConditionItem("DeptId", "!=", "3"));
    
    Condition condn2 = new Condition();
    condn2.add(Condition.OR, new ConditionItem("DeptId", "!=", new Integer(2)));
    condn2.add(Condition.OR, new ConditionItem("DeptId", "Between", new Object[]{"6", "9"}));
    
    condn1.add("OR", condn2);
    
    condn.add(Condition.AND_NOT, new ConditionItem("DeptName", "like", "%部%"));
    condn.add(Condition.AND, condn1);

    /* constructSQLForCount测试开始 */
    {
      double totalUseTime = 0;
      int count = 0;
      for(int i=0;i<2000;i++){//在此设置测试次数
          long stime = System.nanoTime();        
          
          /* 测试代码 开始 */
          sqlBuilder.constructSQLForCount("departments","*", "DeptCount", condn).getSQLString();
          /* 测试代码 结束 */
          
          double usetime = (System.nanoTime()-stime)/1000000f;
          if(usetime<0.05 && usetime>0){//大于50可认为是随机峰值，不参加统计，可根据情况调整
              totalUseTime += usetime;
              count++;
          }
      }
      System.out.println("constructSQLForCount 测试 次数："+count+", 总用时："+NumberHelper.formatNumber(totalUseTime, NumberHelper.NUMBER_I_6_0)+", 平均用时（毫秒）:"+NumberHelper.formatNumber((totalUseTime/count), NumberHelper.NUMBER_I_6_0));
    }  

    SQL sqlStatement = sqlBuilder.
            constructSQLForCount("departments","*", "DeptCount", condn);
    System.out.println("sqlStatement.getSQLStatement().size():"+sqlStatement.getSQLStatement().size());
    System.out.println(sqlStatement.getSQLString());
    if(!"SELECT COUNT(*) AS DeptCount FROM departments WHERE NOT DeptName like ? AND ( DeptId != ? OR ( DeptId != ? OR DeptId Between ? AND ? ) ) ".equals(sqlStatement.getSQLString())){
      super.fail();
    }
    
    printObjects(sqlStatement.getValues());   
    
    
    sqlStatement = sqlBuilder.
      constructSQLForCount("departments","DeptId", "DeptCount", null);
    System.out.println(sqlStatement.getSQLString());
    if(!"SELECT COUNT(DeptId) AS DeptCount FROM departments".equals(sqlStatement.getSQLString())){
      super.fail();
    }
    printObjects(sqlStatement.getValues());   
  }

  
  public void testHsqlSQLBuilder(){
    System.out.println("\n[正在测试方法: HsqlSQLBuilder.constructSQLForSelect()...]");
    
    
    Condition condn = new Condition();
    condn.add(new ConditionItem("DEPT_NAME", "IN", new SQL(SQL.SELECT, "SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID", null)));
    condn.add(new ConditionItem("DEPT_NO", "IS", "NOT NULL"));
    condn.add(new ConditionItem("F1", "=", "1"));
    condn.add(new ConditionItem("F2", "=", "2"));
    condn.add(new ConditionItem("F3", "=", "3"));
    condn.add(new ConditionItem("F4", "=", "4"));
    condn.add(new ConditionItem("F5", "=", "5"));
    condn.add(new ConditionItem("F6", "in", new Object[]{"1", "2", "3", "4"}));
    
    
    Column[] columns = new Column[]{
        new Column("DEPT_ID"),
        new Column("DEPT_NAME", new SQL(SQL.SELECT, "SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID", null)),
        new Column("F1"),
        new Column("F2"),
        new Column("F3"),
        new Column("F4"),
        new Column("F5"),
        new Column("F6"),
    };
    
    /* constructSQLForSelect测试开始 */
    {
      double totalUseTime = 0;
      int count = 0;
      for(int i=0;i<2000;i++){//在此设置测试次数
          long stime = System.nanoTime();        
          
          condn = new Condition();
          condn.add(new ConditionItem("DEPT_NAME", "IN", new SQL(SQL.SELECT, "SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID", null)));
          condn.add(new ConditionItem("DEPT_NO", "IS", "NOT NULL"));
          condn.add(new ConditionItem("F1", "=", "1"));
          condn.add(new ConditionItem("F2", "=", "2"));
          condn.add(new ConditionItem("F3", "=", "3"));
          condn.add(new ConditionItem("F4", "=", "4"));
          condn.add(new ConditionItem("F5", "=", "5"));
          condn.add(new ConditionItem("F6", "in", new Object[]{"1", "2", "3", "4"}));
          
          
          columns = new Column[]{
              new Column("DEPT_ID"),
              new Column("DEPT_NAME", new SQL(SQL.SELECT, "SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID", null)),
              new Column("F1"),
              new Column("F2"),
              new Column("F3"),
              new Column("F4"),
              new Column("F5"),
              new Column("F6"),
          };          
          
          /* 测试代码 开始 */
          hsqlBuilder.constructSQLForSelect("departments",false, columns, condn, "DeptId", "DeptName", 5, 20).getSQLString();
          /* 测试代码 结束 */
          
          double usetime = (System.nanoTime()-stime)/1000000f;
          if(usetime<0.09 && usetime>0){//大于50可认为是随机峰值，不参加统计，可根据情况调整
              totalUseTime += usetime;
              count++;
          }
      }
      System.out.println("constructSQLForSelect 测试 次数："+count+", 总用时："+NumberHelper.formatNumber(totalUseTime, NumberHelper.NUMBER_I_6_0)+", 平均用时（毫秒）:"+NumberHelper.formatNumber((totalUseTime/count), NumberHelper.NUMBER_I_6_0));
    }  
    
    SQL sqlStatement = hsqlBuilder.constructSQLForSelect("departments",false, columns, condn, "DeptId", "DeptName", 5, 20);
    System.out.println(sqlStatement.getSQLString());
    if(!"SELECT  LIMIT 5 20 DEPT_ID,(SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID) AS DEPT_NAME,F1,F2,F3,F4,F5,F6 FROM departments WHERE  DEPT_NAME IN (SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID) AND DEPT_NO IS NOT NULL AND F1 = ? AND F2 = ? AND F3 = ? AND F4 = ? AND F5 = ? AND F6 in (? ,? ,? ,? )  GROUP BY DeptId ORDER BY DeptName".equals(sqlStatement.getSQLString())){
      super.fail();
    }
    printObjects(sqlStatement.getValues());   
    
    sqlStatement = hsqlBuilder.constructSQLForSelect("departments",false, null, null, "DeptId", "DeptName", 5, 20);
    System.out.println(sqlStatement.getSQLString());
    if(!"SELECT  LIMIT 5 20 * FROM departments GROUP BY DeptId ORDER BY DeptName".equals(sqlStatement.getSQLString())){
      super.fail();
    }
  }
  
  public void testMssqlSQLBuilder(){
    System.out.println("\n[正在测试方法: MssqlSQLBuilder.constructSQLForSelect()...]");
    
    
    Condition condn = new Condition();
    condn.add(new ConditionItem("DEPT_NAME", "IN", new SQL(SQL.SELECT, "SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID", null)));
    condn.add(new ConditionItem("DEPT_NO", "IS", "NOT NULL"));
    condn.add(new ConditionItem("F1", "=", "1"));
    condn.add(new ConditionItem("F2", "=", "2"));
    condn.add(new ConditionItem("F3", "=", "3"));
    condn.add(new ConditionItem("F4", "=", "4"));
    condn.add(new ConditionItem("F5", "=", "5"));
    condn.add(new ConditionItem("F6", "in", new Object[]{"1", "2", "3", "4"}));
    
    
    Column[] columns = new Column[]{
        new Column("DEPT_ID"),
        new Column("DEPT_NAME", new SQL(SQL.SELECT, "SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID", null)),
        new Column("F1"),
        new Column("F2"),
        new Column("F3"),
        new Column("F4"),
        new Column("F5"),
        new Column("F6"),
    };
    
    /* constructSQLForSelect测试开始 */
    {
      double totalUseTime = 0;
      int count = 0;
      for(int i=0;i<2000;i++){//在此设置测试次数
          long stime = System.nanoTime();        
          
          Condition condnA = new Condition();
          condnA.add(new ConditionItem("DEPT_NAME", "IN", new SQL(SQL.SELECT, "SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=?", new Object[]{1})));
          condnA.add(new ConditionItem("DEPT_NAME", "IN", new SQL(SQL.SELECT, "SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID in (?)", new Object[]{1, 2, 3, 4})));
          condnA.add(new ConditionItem("DEPT_NO", "IS", "NOT NULL"));
          condnA.add(new ConditionItem("F1", "=", "1"));
          condnA.add(new ConditionItem("F2", "=", "2"));
          condnA.add(new ConditionItem("F3", "=", "3"));
          condnA.add(new ConditionItem("F4", "=", "4"));
          condnA.add(new ConditionItem("F5", "=", "5"));
          condnA.add(new ConditionItem("F6", "in", new Object[]{"1", "2", "3", "4"}));
          
          
          columns = new Column[]{
              new Column("DEPT_ID"),
              new Column("DEPT_NAME", new SQL(SQL.SELECT, "SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID", null)),
              new Column("F1"),
              new Column("F2"),
              new Column("F3"),
              new Column("F4"),
              new Column("F5"),
              new Column("F6"),
          };
          
          /* 测试代码 开始 */
          mssqlSQLBuilder.constructSQLForSelect("departments",false, columns, condnA, "DeptId", "DeptName", 5, 20).getSQLString();
          /* 测试代码 结束 */
          
          double usetime = (System.nanoTime()-stime)/1000000f;
          if(usetime<0.09 && usetime>0){//大于50可认为是随机峰值，不参加统计，可根据情况调整
              totalUseTime += usetime;
              count++;
          }
      }
      System.out.println("constructSQLForSelect 测试 次数："+count+", 总用时："+NumberHelper.formatNumber(totalUseTime, NumberHelper.NUMBER_I_6_0)+", 平均用时（毫秒）:"+NumberHelper.formatNumber((totalUseTime/count), NumberHelper.NUMBER_I_6_0));
    }  
    
    SQL sqlStatement = mssqlSQLBuilder.constructSQLForSelect("departments",false, columns, condn, "DeptId", "DeptName", 5, 20);
    System.out.println(sqlStatement.getSQLString());
    if(!"SELECT  TOP 25 DEPT_ID,(SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID) AS DEPT_NAME,F1,F2,F3,F4,F5,F6 FROM departments WHERE  DEPT_NAME IN (SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID) AND DEPT_NO IS NOT NULL AND F1 = ? AND F2 = ? AND F3 = ? AND F4 = ? AND F5 = ? AND F6 in (? ,? ,? ,? )  GROUP BY DeptId ORDER BY DeptName".equals(sqlStatement.getSQLString())){
      super.fail();
    }
    printObjects(sqlStatement.getValues());   
    if(!getObjects(sqlStatement.getValues()).equals("1,2,3,4,5,1,2,3,4,")){
      super.fail();
    }
    
    sqlStatement = mssqlSQLBuilder.constructSQLForSelect("departments",false, null, null, "DeptId", "DeptName", 5, 20);
    System.out.println(sqlStatement.getSQLString());
    //if(!"SELECT  TOP 25 DEPT_ID,(SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID) AS DEPT_NAME,F1,F2,F3,F4,F5,F6 FROM departments WHERE  DEPT_NAME IN (SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID) AND DEPT_NO IS NOT NULL AND F1 = ? AND F2 = ? AND F3 = ? AND F4 = ? AND F5 = ? AND F6 in (? ,? ,? ,? )  GROUP BY DeptId ORDER BY DeptName".equals(sqlStatement.getSQLString())){
    //  super.fail();
    //}
    
    
  }
 
  public void testMysqlSQLBuilder(){
    System.out.println("\n[正在测试方法: MysqlSQLBuilder.constructSQLForSelect()...]");
    
    
    Condition condn = new Condition();
    condn.add(new ConditionItem("DEPT_NAME", "IN", new SQL(SQL.SELECT, "SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID", null)));
    condn.add(new ConditionItem("DEPT_NO", "IS", "NOT NULL"));
    condn.add(new ConditionItem("F1", "=", "1"));
    condn.add(new ConditionItem("F2", "=", "2"));
    condn.add(new ConditionItem("F3", "=", "3"));
    condn.add(new ConditionItem("F4", "=", "4"));
    condn.add(new ConditionItem("F5", "=", "5"));
    condn.add(new ConditionItem("F6", "in", new Object[]{"1", "2", "3", "4"}));
    
    
    Column[] columns = new Column[]{
        new Column("DEPT_ID"),
        new Column("DEPT_NAME", new SQL(SQL.SELECT, "SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID", null)),
        new Column("F1"),
        new Column("F2"),
        new Column("F3"),
        new Column("F4"),
        new Column("F5"),
        new Column("F6"),
    };
    
    /* constructSQLForSelect测试开始 */
    {
      double totalUseTime = 0;
      int count = 0;
      for(int i=0;i<2000;i++){//在此设置测试次数
          long stime = System.nanoTime();        
          
          
          condn = new Condition();
          condn.add(new ConditionItem("DEPT_NAME", "IN", new SQL(SQL.SELECT, "SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID", null)));
          condn.add(new ConditionItem("DEPT_NO", "IS", "NOT NULL"));
          condn.add(new ConditionItem("F1", "=", "1"));
          condn.add(new ConditionItem("F2", "=", "2"));
          condn.add(new ConditionItem("F3", "=", "3"));
          condn.add(new ConditionItem("F4", "=", "4"));
          condn.add(new ConditionItem("F5", "=", "5"));
          condn.add(new ConditionItem("F6", "in", new Object[]{"1", "2", "3", "4"}));
          
          
          columns = new Column[]{
              new Column("DEPT_ID"),
              new Column("DEPT_NAME", new SQL(SQL.SELECT, "SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID", null)),
              new Column("F1"),
              new Column("F2"),
              new Column("F3"),
              new Column("F4"),
              new Column("F5"),
              new Column("F6"),
          };
          
          /* 测试代码 开始 */
          mysqlSQLBuilder.constructSQLForSelect("departments",false, columns, condn, "DeptId", "DeptName", 5, 20).getSQLString();
          /* 测试代码 结束 */
          
          double usetime = (System.nanoTime()-stime)/1000000f;
          if(usetime<0.05 && usetime>0){//大于50可认为是随机峰值，不参加统计，可根据情况调整
              totalUseTime += usetime;
              count++;
          }
      }
      System.out.println("constructSQLForSelect 测试 次数："+count+", 总用时："+NumberHelper.formatNumber(totalUseTime, NumberHelper.NUMBER_I_6_0)+", 平均用时（毫秒）:"+NumberHelper.formatNumber((totalUseTime/count), NumberHelper.NUMBER_I_6_0));
    }  
    
    SQL sqlStatement = mysqlSQLBuilder.constructSQLForSelect("departments",false, columns, condn, "DeptId", "DeptName", 5, 20);
    System.out.println(sqlStatement.getSQLString());
    if(!"SELECT DEPT_ID,(SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID) AS DEPT_NAME,F1,F2,F3,F4,F5,F6 FROM departments WHERE  DEPT_NAME IN (SELECT DEPT_NAME FROM ALL_DEPTS WHERE D.DEPT_ID=C.DEPT_ID) AND DEPT_NO IS NOT NULL AND F1 = ? AND F2 = ? AND F3 = ? AND F4 = ? AND F5 = ? AND F6 in (? ,? ,? ,? )  GROUP BY DeptId ORDER BY DeptName LIMIT 5,20".equals(sqlStatement.getSQLString())){
      super.fail();
    }
    printObjects(sqlStatement.getValues());   
    if(!getObjects(sqlStatement.getValues()).equals("1,2,3,4,5,1,2,3,4,")){
      super.fail();
    }
    
    sqlStatement = mysqlSQLBuilder.constructSQLForSelect("departments",false, null, null, "DeptId", "DeptName", 5, 20);
    System.out.println(sqlStatement.getSQLString());
  }
  
  private String getObjects(Object[] objs){
    String str = "";
    for(int i=0;i<objs.length;i++){
      str += (objs[i]+",");
    }
    return str;
  }
  
  private void printObjects(Object[] objs){
    for(int i=0;i<objs.length;i++){
      System.out.print(objs[i]+",");
    }
    System.out.println();
  }
}
