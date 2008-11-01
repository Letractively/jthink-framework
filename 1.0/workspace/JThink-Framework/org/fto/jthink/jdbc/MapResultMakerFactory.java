package org.fto.jthink.jdbc;

/**
 * MapResultMaker工厂类，创建MapResultMaker实例
 * 
 * @author try.jiwen
 *
 */

public class MapResultMakerFactory implements ResultMakerFactory{
  public ResultMaker create() {
    return new MapResultMaker();
  }
  
}
