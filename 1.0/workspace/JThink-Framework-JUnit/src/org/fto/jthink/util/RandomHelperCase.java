package org.fto.jthink.util;

import junit.framework.TestCase;

public class RandomHelperCase extends TestCase {

  public void test(){
//    int s = Integer.MIN_VALUE;//-10000;
//    int e = Integer.MAX_VALUE;//10000;
//    
    int s = -1;
    int e = 1;
    
    for(int i=0;i<100000;i++){
      double v = RandomHelper.randomDouble(s, e);
      //if(v>-10&&v<10){
       // System.out.println(NumberHelper.formatNumber(v, "0.00000000000"));
      //}
//      if(v<=s || v>=e){
//        System.out.println(v);
//      }
      if(v<=s+0.0001 || v>=(e-0.0001) || v==((long)e+(long)s)/2){
        System.out.println(v);
      }
    }
  }
}
