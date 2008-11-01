package org.fto.jthink.jdbc;

public class DataObjectResultMakerFactory implements ResultMakerFactory {

  public ResultMaker create() {
    return new DataObjectResultMaker();
  }

}
