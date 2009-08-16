package org.fto.jthink.lang;

import org.fto.jthink.util.NumberHelper;

import junit.framework.TestCase;

public class ObjectBufferedTest  extends TestCase {
  public static void main(String[] args) {
    junit.textui.TestRunner.run(ObjectBufferedTest.class);
    
    //start(1,2);
    
  }
  public void test() {
    System.out.println("\n[正在测试方法: ObjectBufferedTest ...]");  
    
    ObjectBuffered objs = new ObjectBuffered();
    objs.append("0");
    objs.append("1");
    objs.append("2");
    objs.append(new String[]{"3", "4", "5", "6", "7", "8"});
    objs.append("6");
    objs.append("7");
    objs.append("8");
    objs.append(new ObjectBuffered().append("a").append("b").append("c").append("d").append(new ObjectBuffered().append("e").append(new String[]{"f", "g"})));
    objs.append("h");
    ObjectBuffered subObjs = new ObjectBuffered().append("i").append(new ObjectBuffered("j"));
    objs.append(subObjs);
    
    //subObjs.append("k");
    System.out.println("objs.size:"+objs.size());
    System.out.println("objs.length:"+objs.length());
    printObjects(objs.toArray());
    System.out.println("objs.size:"+objs.size());
    System.out.println("objs.length:"+objs.length());
    subObjs.append("k");
    objs.append(subObjs);
    //objs.append(subObjs);
    
    System.out.println("objs.size:"+objs.size());
    System.out.println("objs.length:"+objs.length());
    printObjects(objs.toArray());
    
//    String[] s78 = new String[]{"7", "8"};
//    /* ObjectBufferedTest测试开始 */
//    {
//      double totalUseTime = 0;
//      int count = 0;
//      for(int i=0;i<20000;i++){//在此设置测试次数
//          long stime = System.nanoTime();        
//          
//          /* 测试代码 开始 */
//            objs = new ObjectBuffered();
//            objs.append("0");
//            objs.append("1");
//            objs.append("2");
//            objs.append(new String[]{"3", "4"});//, "7", "8"});
//            //objs.append(new String[]{"3", "4", "5", "6", "7", "8"});
//            objs.append("6");
//            objs.append("7");
//            objs.append("8");
//            objs.toArray();
//            
//          /* 测试代码 结束 */
//          
//          double usetime = (System.nanoTime()-stime)/1000000f;
//          if(usetime<0.009 && usetime>0){//大于50可认为是随机峰值，不参加统计，可根据情况调整
//              totalUseTime += usetime;
//              count++;
//          }
//      }
//      System.out.println("\nObjectBufferedTest 测试 次数："+count+", 总用时："+NumberHelper.formatNumber(totalUseTime, NumberHelper.NUMBER_I_6_0)+", 平均用时（毫秒）:"+NumberHelper.formatNumber((totalUseTime/count), NumberHelper.NUMBER_I_6_0));
//    } 
//    
//    
    
  }
  
  
  
  
  private void printObjects(Object[] objs){
    for(int i=0;i<objs.length;i++){
      System.out.print(objs[i]+",");
    }
    System.out.println();
  }
  
  /**  
   * 启动线程  
//   */  
//  public static void start(final Object param1, final Object param2)   
//  {   
//    Thread thread = new Thread(new Runnable()   
//      {   
//          public void run()   
//          {   
//            
//            System.out.println(param1);
//            System.out.println(param2);
//            
//              //runInternal();   
//          }   
//      }, "");   
//    
//    thread.setDaemon(true);
//    thread.start();   
//  } 
  
}
