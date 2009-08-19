/**
 * 
 */
package org.fto.jthink.lang;

/**
 * @author Administrator
 *
 */
public class ObjectBuffered {

  SimpleList objData;
  
  /* 对象总长度 */
  private int length = 0;  
  
  /* 
   * 是否被锁定，如果被锁定，就不能再调用append方法，只有当被解锁后才能再使用
   * 
   */
  private boolean locked  = false;
  
  
  /**
   * 构建一个ObjectBuffered，
   * 
   */
  public ObjectBuffered(){
    this(16);
  }
  
  /**
   * 构建一个ObjectBuffered，
   * 
   * @param strCapacity 预计将要被缓冲的对象串数量
   */
  public ObjectBuffered(int strCapacity){
    objData = new SimpleList(strCapacity);
  }
  
  /**
   * 构建一个ObjectBuffered
   * 
   * @param o 对象
   */
  public ObjectBuffered(Object o){
    this();
    append(o);
  }
  
  /**
   * 构建一个ObjectBuffered
   * 
   * @param os 对象数组
   */
  public ObjectBuffered(Object[] os){
    this();
    append(os);
  }

  
  /**
   * 返回对象总长度
   */
  public int length() {
    return length;
  }  
  
  /**
   * 返回被缓冲子对象数量
   * @param minCapacity
   */
  public int size(){
    return objData.size();
  }
  
  
  /**
   * 追加对象
   * @param o 被追加到尾部的对象
   * @return 这个ObjectBuffered
   */
  public ObjectBuffered append(Object o){
    checkLocked();
    length++;
    objData.add(o);
    return this;
  }
  
  /**
   * 追加对象
   * @param o 被追加到尾部的对象
   * @return 这个ObjectBuffered
   */
  public ObjectBuffered append(Object[] o){
    checkLocked();
    length += o.length;
    objData.add(o);
    return this;
  }
  
  /**
   * 追加对象
   * @param o 被追加到尾部的ObjectBuffered对象
   * @return 这个ObjectBuffered
   */
  public ObjectBuffered append(ObjectBuffered ob){
    checkLocked();
    //ob.checkLocked();
    length += ob.length;
    objData.add(ob);
    ob.lock();
    return this;
  }
  
  
  private void getObjects(Object[] buffs, int off, boolean isFirstLayer){
    if(length>0){
      int size = objData.size();
      //int off = 0;
      for(int i=0;i<size;i++){
        Object o = objData.get(i);
        if(o instanceof Object[]){
          Object[] os = (Object[])o;
          int subObjsLen = os.length;
          switch(subObjsLen){
            case 0:
              break;
            case 1:
              buffs[off++] = os[0];
              break;
            case 2:
              buffs[off++] = os[0];
              buffs[off++] = os[1];
              break;
            case 3:
              buffs[off++] = os[0];
              buffs[off++] = os[1];
              buffs[off++] = os[2];
              break;
            case 4:
              buffs[off++] = os[0];
              buffs[off++] = os[1];
              buffs[off++] = os[2];
              buffs[off++] = os[3];
              break;
            default:
              System.arraycopy(os, 0, buffs, off, subObjsLen);
              off+=subObjsLen;
          }
          
        }else if(o instanceof ObjectBuffered){
          ObjectBuffered ob = (ObjectBuffered)o;
          ob.getObjects(buffs, off, false);
          off+= ob.length;
          if(isFirstLayer){
            //ob.unlock(); //解锁
          }
        }else{
          buffs[off++] = o;
        }
      }
    }
  }
  
  
  /**
   * 返回对象数组
   */
  public Object[] toArray() {
    Object[] result = new Object[length];
    if(length>0){
      getObjects(result, 0, true);
      objData.clear();
      objData.add(result);
      
    }
    return result;
  }
  
  
  private void checkLocked(){
    if(locked){
      throw new RuntimeException("This ObjectBuffered is locked.");
    }
  }
  
  private void lock(){
    locked = true;
  }
  
  private void unlock(){
    locked = false;
  }
  
  
}
