package org.fto.jthink.lang;

import junit.framework.TestCase;

public class StringBufferedTest  extends TestCase {
  public static void main(String[] args) {
    junit.textui.TestRunner.run(StringBufferedTest.class);
  }

  public void test() {
    System.out.println("\n[正在测试方法: StringBufferedTest ...]");  
    
    StringBuffered sb1 = new StringBuffered().append(1).append("2").append(3);
    StringBuffered sb2 = new StringBuffered().append(4).append("5").append(6);  
    
    System.out.println("sb1:"+sb1.toString());
    System.out.println("sb2:"+sb2.toString());
    
    StringBuffered sb = new StringBuffered().append(sb1.append(sb2)).append(7).append(8);
    
    //sb1.append("a");
    
    System.out.println("sb:"+sb.toString());
    System.out.println("sb:"+sb.toString());
    
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
  
  
}
