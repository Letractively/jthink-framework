/**
 * 
 */
package org.fto.jthink.lang;

/**
 * 简单列表, 由于简化了ArrayList的一些功能，理论上要比ArrayList快
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2009-08-09  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 * @see java.util.ArrayList
 * 
 */

class SimpleList {

  private Object[] elementData;
  
  private int size = 0;
  
  /**
   * 构建一个SimpleList，
   * 
   * @param capacity 预计将要被缓冲的对象数量
   */
  public SimpleList(int capacity){
    elementData = new Object[capacity];
  }
  
  /**
   * 构建一个SimpleList
   */
  public SimpleList(){
    this(16);
  }
  
  /**
   * 构建一个SimpleList
   * 
   * <pre>
   *   注意：为了提高效率，将直接引用data数组对象, 所以在此以后不要再修改data对象数组中的数据
   * </pre>
   * 
   * @
   */
  public SimpleList(Object[] data){
    elementData = data;
    size = data.length;
  }
  
  
  void ensureCapacity(int minCapacity) {
    int oldCapacity = elementData.length;
    if (minCapacity > oldCapacity) {
        Object oldData[] = elementData;
        int newCapacity = (oldCapacity * 3)/2 + 1;
        if (newCapacity < minCapacity){
          newCapacity = minCapacity;
        }
        elementData = new Object[newCapacity];
        System.arraycopy(oldData, 0, elementData, 0, size);
        //System.out.println("ensureCapacity");
    }
  }
  
  public void add(Object o){
    ensureCapacity(size + 1);
    elementData[size++] = o;
  }
  
  
  public void addAll(Object[] data){
    addAll(data, 0, data.length);
  }
  
  
  public void addAll(Object[] data, int start, int len){
    ensureCapacity(size + len);
    if(len>10){
      System.arraycopy(data, start, elementData, size, len);
      size+=len;
    }else{
      for(int i=0;i<len;i++){
        elementData[size++] = data[i];
      }
    }
  }  
  
  
  public void addAll(SimpleList l, int start, int len){
    addAll(l.elementData, start, len);
  }  
  
  public void addAll(SimpleList l){
    addAll(l.elementData, 0, l.size);
  }  
  
  public Object get(int index) {
    rangeCheck(index);
    return elementData[index];
  }
  
  
  public Object set(int index, Object element) {
    rangeCheck(index);
    Object oldValue = elementData[index];
    elementData[index] = element;
    return oldValue;
  }
  
  
  public Object[] toArray() {
    Object[] result = new Object[size];
    if(size>0){
      System.arraycopy(elementData, 0, result, 0, size);
    }
    return result;
  }
  
  public Object[] toArray(Object a[]) {
    if (a.length < size)
        a = (Object[])java.lang.reflect.Array.newInstance(
                            a.getClass().getComponentType(), size);
    System.arraycopy(elementData, 0, a, 0, size);
    if (a.length > size)
        a[size] = null;
    return a;
  }
  
  
  public void clear(){
    for (int i = 0; i < size; i++){
        elementData[i] = null;
    }
    size = 0;
  }
  
  public int size(){
    return size;
  }
  
  private void rangeCheck(int index) {
    if (index >= size || index < 0)
        throw new IndexOutOfBoundsException(
      "Index: "+index+", Size: "+size);
  }
  
  
}
