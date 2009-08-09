package org.fto.jthink.lang;

import java.util.ArrayList;

import org.fto.jthink.util.NumberHelper;

import junit.framework.TestCase;


public class SimpleListTest  extends TestCase {
  public static void main(String[] args) {
    junit.textui.TestRunner.run(SimpleListTest.class);
  }
  
  public void test() {
    System.out.println("\n[正在测试方法: SimpleList ...]");
    
    SimpleList l  = new SimpleList(new String[]{"a","b", "c"});
    
//    l.add("a");
//    l.add("b");
//    l.add("c");
    
    l.addAll(new String[]{"d", "e", "f"});
    l.addAll(l);
    l.add(1);
    
    String str = "";
    for(int i=0;i<l.size();i++){
      str += l.get(i);
    }
    System.out.print(str);
    if(!"abcdefabcdef1".equals(str)){
      super.fail();
    }
    
//    Object[] data = new Object[]{"1","2","3","4","5"};
//    SimpleList sl = new SimpleList(data);
//    
//    System.out.println("\ndata[0]:"+data[1]);
//    System.out.println("sl.get(0):"+sl.get(1));
    
    
    //System.out.println("\nl.get(l.size()):"+l.get(l.size()));
    
    /* SimpleList测试开始 */
    {
      double totalUseTime = 0;
      int count = 0;
      for(int i=0;i<2000;i++){//在此设置测试次数
          long stime = System.nanoTime();        
          
          /* 测试代码 开始 */
          SimpleList list = new SimpleList(new Object[]{"1","2","3","4","5"});
          //ArrayList list = new ArrayList();
//          list.addAll(new Object[]{"1","2","3","4","5"});
//          list.add("a");
//          list.add("b");
//          list.add("c");
//          
//          //list.addAll(new String[]{"d", "e", "f"});
//          list.addAll(list);
//          list.add(1);
          
          /* 测试代码 结束 */
          
          double usetime = (System.nanoTime()-stime)/1000000f;
          if(usetime<0.05 && usetime>0){//大于50可认为是随机峰值，不参加统计，可根据情况调整
              totalUseTime += usetime;
              count++;
          }
      }
      System.out.println("\nSimpleList 测试 次数："+count+", 总用时："+NumberHelper.formatNumber(totalUseTime, NumberHelper.NUMBER_I_6_0)+", 平均用时（毫秒）:"+NumberHelper.formatNumber((totalUseTime/count), NumberHelper.NUMBER_I_6_0));
    }  
    
  }
  
}
