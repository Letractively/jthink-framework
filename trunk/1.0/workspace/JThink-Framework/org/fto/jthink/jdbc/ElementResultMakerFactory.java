package org.fto.jthink.jdbc;

/**
 * ElementResultMaker工厂类，创建ElementResultMaker实例
 * 
 * @author try.jiwen
 *
 */

public class ElementResultMakerFactory implements ResultMakerFactory{
  public ResultMaker create() {
    return new ElementResultMaker();
  }
  
}
