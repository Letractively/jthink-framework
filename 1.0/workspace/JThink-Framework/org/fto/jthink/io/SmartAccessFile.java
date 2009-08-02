/*
 * SmartAccessFile.java	2004-11-14
 *
 * 版权所有: 自由思考组织(FTO)软件团队 2000-2005, 保留所有权利.
 * 这个软件是自由思考组织(FTO)软件团队开发的，如果要使用这个软件，请首先阅读并接受许可协议。
 *
 * Copyright 2000-2005 FTO Software Team, Inc. All Rights Reserved.
 * This software is the proprietary information of FTO Software Team, Inc.
 * Use is subject to license terms.
 *
 * FTO站点：http://www.free-think.org
 */

package org.fto.jthink.io;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Date;

/**
 * 这个类主要用于随机存取文件，它具有RandomAccessFile的大部分功能，除支持对文件
 * 的读写等基本操作外，还新加入了插入、删除，搜索等操作。现阶段还只能对字节类型数据进
 * 行处理，在以后的版本中，将会加入对其它数据类型的支持。
 * 
 * <p><pre><b>
 * 当前支持的文件访问模式有：</b>
 * <code>"r"</code>  只读模式，只能对文件进行读数据操作
 * <code>"rw"</code> 读写模式，可以对文件进行读或写数据操作
 * <code>"a"</code>  追加模式，只能在文件末追加数据
 * </pre></p>
 * 
 * <p><pre><b>
 * 历史更新记录：</b>
 * 2004-11-14  创建此类型
 * 2004-12-06  加入了一种新的模式：追加"a"。以前只支持"r"or"rw"两种模式。并且同时新添加了三个新方法:append()
 * 2005-08-01  加入了skipBytes()方法
 * 2005-08-02  优化了一些方法的执行效率，insert(),write(),append(),indexOf()
 * 2005-09-06  对内部类型FileBlock, Status进行了一些优化处理
 * </pre></p>
 * 
 *
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 * @see      java.io.RandomAccessFile
 * 
 */

public class SmartAccessFile {
  /* J2SE提供的标准文件访问对象 */
  private RandomAccessFile raf;
  /* 当前被操作的文件 */
  private File file;
  /* 文件访问模式 */
  private String mode;
  /* 是否"rw"模式 */
  private boolean isrwMode = false;
  /* 是否"a"模式 */
  private boolean isappendMode = false;
  /* 修改标志，如果文件被更新，updatedFlag=true, 
     此标志只有当文件内容被删除，在文件中间或开始位置插入了新内容时，才会被设置为true，
     如果是对文件实体的真正操作，是不会影响此标志的 */
  private boolean updatedFlag = false;
  /* 文件块(FileBlock)列表 */
  private List fileBlocks = new LinkedList();
  /* 初始化时的块数量 */
  //private int initBlocksCount = 0;
  /* 文件当前长度 */
  private long curtFileLength = 0;
  /* 文件当前指针位置 */
  private long curtFilePointer = 0;
  /* 文件当前块 */
  private FileBlock curtFileBlock = null;
  /* 文件当前块中的指针位置 */
  private int curtFBPointer = 0;

  /* 用于读写一个字节数据临时缓冲 */
  private byte[] oneByte = new byte[1];
  
  /**
   * 创建SmartAccessFile的一个实例。
   * 
   * @param name 文件名称，包含文件路径名，可以是相对路径或绝对路径
   * @param mode 文件访问模式,"r","rw","a", write(), insert(), remove()，
   * 							setLength()方法将会被检查是否正确的访问模式
   *
   * @exception  IllegalArgumentException  如果文件访问模式不是<code>"r"</code>或<code>"rw"</code>或<code>"a"</code>.
   * @exception  FileNotFoundException  如果文件没找到或不能建立文件或其它原因引起的打开文件失败
   * @exception  SecurityException   如果存在安全管理并且文件访问被拒绝，通过java.lang.SecurityManager#checkRead或
   * 																	java.lang.SecurityManager#checkWrite检查
   * 
   * @see        java.lang.SecurityException
   * @see        java.lang.SecurityManager#checkRead(java.lang.String)
   * @see        java.lang.SecurityManager#checkWrite(java.lang.String)
   */
  public SmartAccessFile(String name, String mode) throws IOException {
    this(new File(name), mode);
  }
  /**
   * 创建SmartAccessFile的一个实例。
   * 
   * @param file File对象
   * @param mode 文件访问模式,"r","rw","a", write(), insert(), remove()，
   * 							setLength()方法将会被检查是否正确的访问模式
   *
   * @exception  IllegalArgumentException  如果文件访问模式不是<code>"r"</code>或<code>"rw"</code>或<code>"a"</code>.
   * @exception  SecurityException   如果存在安全管理并且文件访问被拒绝，通过java.lang.SecurityManager#checkRead或
   * 																	java.lang.SecurityManager#checkWrite检查
   * 
   * @see        java.lang.SecurityException
   * @see        java.lang.SecurityManager#checkRead(java.lang.String)
   * @see        java.lang.SecurityManager#checkWrite(java.lang.String)
   */
  public SmartAccessFile(File file, String mode) throws IOException {
    this.file = file;
    this.mode = mode;
    if(mode.equals("a")){
        this.isappendMode = true;
        mode = "rw";
    }else if(mode.equals("rw")){
        this.isrwMode = true;
    }else if(!mode.equals("r")){
        throw new IllegalArgumentException("mode must be r or rw or a");
    }
    this.raf = new RandomAccessFile(file, mode);
    this.curtFileLength = raf.length();
    final int intMaxValue = 0x7fffffff;
    long tmpLen = curtFileLength;
    while(tmpLen>=0){
      fileBlocks.add(new FileBlock(raf, 0, tmpLen<=intMaxValue?(int)tmpLen:intMaxValue));
      tmpLen -= intMaxValue;
    }
    //this.initBlocksCount = fileBlocks.size();
    curtFileBlock = (FileBlock)fileBlocks.get(0);//当前文件块
  }


  /**
   * 判断文件位置指针是否已经移动到文件末尾
   *
   * @return 如果返回true，说明当前文件位置指针是在文件末尾，否则返回false
   */
  public boolean isEOF(){
    return getFilePointer()>=length();
  }

  /**
   * 向文件当前指针位置之前插入一个字节，文件指针向前(文件尾的方向)移动一个字节的宽度。
   * 文件当前块、文件当前指针、文件当前长度、文件当前块中的指针位置等会受到影响。
   *
   * @param b 被插入的字节。
   */
  public void insert(int b)throws IOException {
    insert(new byte[]{(byte)b});
  }

  /**
   * 向文件当前指针位置之前插入一组字节，文件指针向前(文件尾的方向)移动一组字节的宽度。
   * 文件当前块、文件当前指针、文件当前长度、文件当前块中的指针位置等会受到影响。
   *
   * @param bs 被插入的字节数组。
   *
   */
  public void insert(byte[] bs)throws IOException {
    insert(bs, 0, bs.length);
  }

  /**
   * 向文件当前指针位置之前插入一组字节，只有在字节数组<code>bs</code>中的<code>off</code>
   * 位置到<code>off+len</code>位置的数据才会被插入到文件，文件指针向前(文件尾的方向)移动参数
   * <code>len</code>给定的宽度。文件当前块、文件当前指针、文件当前长度、文件当前块中的指针位置等会受到影响。
   *
   * @param bs 被插入的字节数组。
   * @param off 字节数组的有效开始位置
   * @param len 有效长度
   * 
   * @exception IOException 如果发生I/O错误
   */
  public void insert(byte[] bs, int off, int len)throws IOException {
    if(!isrwMode){//Check is file access mode is rw.
      throw new SecurityException("Not is rw mode!");
    }
    if(len>bs.length-off){
    	throw new RuntimeException("The len > bs.length - off !");
    } 
    if(len<0){
    	throw new RuntimeException("The len < 0 !");
    }
    
    if(len==0){
      return;
    }
    
    /* 如果还没有对文件进行不对称操作，直接调用RandomAccessFile的write方法 */
    if(updatedFlag==false && curtFilePointer>=length()){
    	raf.seek(curtFilePointer);
    	raf.write(bs, off, len);
    	FileBlock newFB;
      if(curtFilePointer-length()>0){
        newFB = new FileBlock(raf,  null, length());
        newFB.setStatus(Status.NULL);
        fileBlocks.add(newFB);
      }
      
    	newFB = new FileBlock(raf,  null, curtFilePointer, off, len);
      newFB.setStatus(Status.NULL);
      fileBlocks.add(newFB);

      /* 设置文件信息 */
      curtFileBlock = null; //设置文件当前块
      curtFilePointer += len; //设置文件当前指针
      curtFileLength = curtFilePointer;//设置文件当前长度
      curtFBPointer = -1;//设置文件当前块中的指针位置
      
    	return;
    }
    
    updatedFlag = true;//Set file changed flag.
    FileBlock newFB;
    if(curtFilePointer>=length()){//当前指针在文件末尾，将新创建一个文件块
      if(curtFilePointer-length()>0){
         newFB = new FileBlock(raf,  new byte[(int)(curtFilePointer-length())], -1);
         newFB.setStatus(Status.NEW);
         fileBlocks.add(newFB);
      }
      newFB = new FileBlock(raf,  bs, -1, off, len);
      newFB.setStatus(Status.NEW);
      fileBlocks.add(newFB);

      /* 设置文件信息 */
      curtFileBlock = null; //设置文件当前块
      curtFilePointer += len; //设置文件当前指针
      curtFileLength = curtFilePointer;//设置文件当前长度
      curtFBPointer = -1;//设置文件当前块中的指针位置

    }else if(curtFBPointer==0){//当前指针在文件块的第一个字节位置，将新创建一个文件块
        newFB = new FileBlock(raf,  bs, -1, off, len);
        newFB.setStatus(Status.NEW);
        fileBlocks.add(fileBlocks.indexOf(curtFileBlock), newFB);

        /* 设置文件信息 */
        curtFilePointer += len; //设置文件当前指针
        curtFileLength += len;//设置文件当前长度
        curtFBPointer = 0;//设置文件当前块中的指针位置

      }else{//在其它位置，将会分割当前文件块
        FileBlock nextFB = curtFileBlock.split(curtFBPointer);
        newFB = new FileBlock(raf,  bs, -1, off, len);
        newFB.setStatus(Status.NEW);
        int index = fileBlocks.indexOf(curtFileBlock);
        fileBlocks.add(index + 1, newFB);
        fileBlocks.add(index + 2, nextFB);

        curtFileBlock = nextFB; //设置文件当前块
        curtFilePointer += len; //设置文件当前指针
        curtFileLength += len; //设置文件当前长度
        curtFBPointer = 0; //设置文件当前块中的指针位置
      }
  }


  /**
   * 从当前文件指针位置开始删除指定长度的字节数据
   *
   * @param len 被删除的字节数据长度，从当前指针位置开始
   *
   * @return 实际被删除的字节数，如果当前文件指针位置已经到文件末尾，返回-1，
   * 					如果当前文件指针位置加上<code>len</code>大于文件总长度，
   * 					返回小于<code>len</code>的数值，否则返回<code>len</code>
   * 
   * @exception IOException 如果发生I/O错误
   */
  public int remove(int len) throws IOException {
    if(!isrwMode){//Check is file access mode is rw.
      throw new SecurityException("not is rw mode!");
    }
    if(isEOF()){
      return -1;
    }
    updatedFlag = true;//Set file changed flag.
    int deletedLen = 0;
    int lastLen = len;
    int fbLen = 0;
    int fbPointer = 0;
    for(int i=fileBlocks.indexOf(curtFileBlock); i<fileBlocks.size();){
      FileBlock fb = (FileBlock)fileBlocks.get(i);
      if(fb==curtFileBlock){
        fbLen = (int)fb.length() - (int)curtFBPointer;
        fbPointer = (int)curtFBPointer;
      }else{
        fbLen = (int)fb.length();
        fbPointer = 0;
      }
      if(lastLen>fbLen){//不是最后一个块
        if(fbPointer==0){//块指针是在块的第0索引
          fileBlocks.remove(fb);
          deletedLen += fb.length();
        }else{//块指针不是在块的第0索引
          FileBlock deletedFB = fb.split(fbPointer);
          deletedLen += deletedFB.length();
        }
        i++;
        lastLen -= fbLen;
      }else{//是最后一个块
        if(lastLen==fb.length()){//最后的块长度刚好与最后被删除的数据长度相等， "000000"
          fileBlocks.remove(fb);
          deletedLen += fb.length();
        }else if(fbPointer==0){//不相等，但被删除的数据是从0索引开始，"000011"
          FileBlock nextFB = fb.split(lastLen);
          fileBlocks.add(fileBlocks.indexOf(fb), nextFB);
          fileBlocks.remove(fb);
          deletedLen += fb.length();
        }else if(fb.length()-fbPointer==lastLen){//不相等，但被删除的数据刚好到块数据的最后，"110000"
          FileBlock deletedFB = fb.split(fbPointer);
          deletedLen += deletedFB.length();
        }else{//不相等，被删除的数据在块数据的中间,"110011"
          FileBlock deletedFB = fb.split(fbPointer);
          FileBlock nextFB = deletedFB.split(lastLen);
          fileBlocks.add(fileBlocks.indexOf(fb)+1, nextFB);
          deletedLen += deletedFB.length();
        }
        break;
      }
    }
    curtFileLength -=deletedLen ;//设置文件当前长度
    seek(getFilePointer());//设置文件当前指针
    return deletedLen;
  }


  /**
   * 从当前文件指针位置读出一个字节，文件指针向前移动一个字节宽度
   *
   * @return  读出的一个字节，如果文件指针指向文件末尾，返回-1
   *
   * @exception  IOException  如果发生I/O错误
   *
   * @see  java.io.RandomAccessFile#read()
   */
  public int read() throws IOException {
    if(isEOF()){
      return -1;
    }
    curtFileBlock.seek((int)curtFBPointer);
    //byte[] b = new byte[1];
    curtFileBlock.read(oneByte, 0 ,1);
    seek(getFilePointer()+1);
    return oneByte[0];
  }

  /**
   * 从当前文件指针位置读出一组字节数据到数组<code>bs</code>,文件指针向前移动实际读出数据量的宽度
   *
   * @param      bs 用于存储读出的数据的字节数组
   * 
   * @return     实际读出数据长度，如果文件指针指向文件末尾，返回-1
   * 
   * @exception  IOException  如果发生I/O错误
   * 
   * @see  java.io.RandomAccessFile#read(byte[])
   */
  public int read(byte bs[]) throws IOException {
      return read(bs, 0, bs.length);
  }

  /**
   * 从当前文件指针位置读出一组字节数据到数组<code>bs</code>,文件指针向前移动实际读出数据量的宽度
   *
   * @param      bs 		用于存储读出的数据的字节数组
   * @param      off   在字节数组存储数据的开始位置
   * @param      len   读出字节数据的最大长度
   * 
   * @return     实际读出数据长度，如果文件指针指向文件末尾，返回-1
   * 
   * @exception  IOException  如果发生I/O错误
   * 
   * @see  java.io.RandomAccessFile#read(byte[], int, int)
   */
  public int read(byte bs[], int off, int len) throws IOException {
    if(isEOF()){
      return -1;
    }
    int readedLen = 0;
    int lastOff = off;
    int lastLen = len;
    int fbLen = 0;
    int fbPointer = 0;
    for(int i=fileBlocks.indexOf(curtFileBlock); i<fileBlocks.size();i++){
      FileBlock fb = (FileBlock)fileBlocks.get(i);
      if(fb==curtFileBlock){
        fbLen = (int)fb.length() - (int)curtFBPointer;
        fbPointer = (int)curtFBPointer;
      }else{
        fbLen = (int)fb.length();
        fbPointer = 0;
      }
      if(lastLen>fbLen){
        fb.seek(fbPointer);
        readedLen += fb.read(bs, lastOff, fbLen);
        lastOff += fbLen;
        lastLen -= fbLen;
      }else{
        fb.seek(fbPointer);
        readedLen += fb.read(bs, lastOff, lastLen);
        break;
      }
    }
    seek(getFilePointer()+readedLen);
    return readedLen;
  }

  /**
   * 向当前文件指针位置写入一个字节。
   *
   * @param      b   被写入的字节数据
   * 
   * @exception  IOException  如果发生I/O错误
   * @exception  SecurityException 不是<code>rw</code>模式
   * 
   * @see  java.io.RandomAccessFile#write(int)
   */
  public void write(int b) throws IOException{
    oneByte[0] = (byte)b;
    write(oneByte, 0,1);
  }
  /**
   * 向当前文件指针位置写入一组字节。
   *
   * @param      bs  被写入的字节数组
   * 
   * @exception  IOException  如果发生I/O错误
   * @exception  SecurityException 不是<code>rw</code>模式
   * 
   * @see  java.io.RandomAccessFile#write(byte[])
   */
  public void write(byte[] bs) throws IOException {
    write(bs, 0, bs.length);
  }
  /**
   * 向当前文件指针位置写入一组字节。
   *
   * @param      bs  被写入的字节数组
   * @param      off 被写入的数据在字节数组中的开始位置
   * @param      len 被写入的数据在字节数组中的长度
   * 
   * @exception  IOException  如果发生I/O错误
   * @exception  SecurityException 不是<code>rw</code>模式
   * 
   * @see  java.io.RandomAccessFile#write(byte[], int, int)
   */
   public void write(byte[] bs, int off, int len) throws IOException {
     if(!isrwMode){//Check is file access mode is rw.
       throw new SecurityException("not is rw mode!");
     }
     if(bs.length==0 && off==0 && len==0){
       return;
     }
     
     /* 如果还没有对文件进行不对称操作，直接调用RandomAccessFile的write方法 */
     if(updatedFlag==false){
     	if(curtFilePointer>=length()){
     		insert(bs, off,len);
     		return;
     	}else{
     		if(length()>=(curtFilePointer+len)){
     			raf.write(bs, off, len);
     			seek(getFilePointer()+len);
     			return;
     		}
     	}
     }
     
     updatedFlag = true;//Set file changed flag.
     long cfp = getFilePointer();
     if(cfp+len>curtFileLength){//写入的数据太多，增加新块
       seek(cfp+len);
       int newFBLen = (int)((cfp+len)-curtFileLength);
       FileBlock newFB = new FileBlock(raf,  new byte[newFBLen], -1);
       newFB.setStatus(Status.NEW);
       fileBlocks.add(newFB);
       curtFileLength += newFBLen;
       seek(cfp);
     }
     int lastOff = off;
     int lastLen = len;
     int fbLen = 0;
     int fbPointer = 0;
     for(int i=fileBlocks.indexOf(curtFileBlock); i<fileBlocks.size();i++){
       FileBlock fb = (FileBlock)fileBlocks.get(i);
       if(fb==curtFileBlock){
         fbLen = (int)fb.length() - (int)curtFBPointer;
         fbPointer = (int)curtFBPointer;
       }else{
         fbLen = (int)fb.length();
         fbPointer = 0;
       }
       if(lastLen>fbLen){//不是最后一个块
         fb.seek(fbPointer);
         fb.update(bs, lastOff, fbLen);
         lastOff += fbLen;
         lastLen -= fbLen;
       }else{//是最后一个块
         fb.seek(fbPointer);
         fb.update(bs, lastOff, lastLen);
         break;
       }
     }
     seek(getFilePointer()+len);
   }

   /**
    * 在文件末尾追加数据
    * 
    * @param bs   被写入的字节数组
    * @param off  被写入的数据在字节数组中的开始位置
    * @param len  被写入的数据在字节数组中的长度
    * 
    * @exception  IOException  如果发生I/O错误
    * @exception  SecurityException 不是<code>rw</code>或<Code>a</Code>文件访问模式
    */
   public void append(byte[] bs, int off, int len) throws IOException{
     if(!isrwMode && !isappendMode){//Check is file access mode is rw.
       throw new SecurityException("not is rw or a mode!");
     }
     seek(length());
     boolean isrwModeTmp = isrwMode;
     isrwMode = true;
     write(bs, off, len);
     isrwMode = isrwModeTmp;
   }
   /**
    * 在文件末尾追加数据
    *
    * @param bs   被写入的字节数组
    * 
    * @exception  IOException  如果发生I/O错误
    * @exception  SecurityException 不是<code>rw</code>或<Code>a</Code>文件访问模式
    */
   public void append(byte[] bs) throws IOException{
       append(bs, 0, bs.length);
   }

   /**
    * 在文件末尾追加数据
    *
    * @param b   被写入的字节
    * 
    * @exception  IOException  如果发生I/O错误
    * @exception  SecurityException 不是<code>rw</code>或<Code>a</Code>文件访问模式
    */
    public void append(int b) throws IOException{
       append(new byte[]{(byte)b}, 0,1);
   }
   
   /**
    * 返回给定字节数组数据在文件中的索引位置。从当前文件指针位置开始查找，可以用<code>seek()</code>改变
    * 查找的开始位置。用于查找的数据不一定就与给定的字节数组数据宽度相等。
    *
    * @param   bs    用于查找的字节数组数据.
    * @param   off   字节数组的开始索引，指定用于查找的字节数组的开始位置。
    * @param   len   字节数组的长度，指定用于查找的字节数组的长度。
    *
    * @return  返回<code>-1</code>，如果没有查找到；<br>
    * 					返回当前文件指针位置，如果参数<code>len</code>等于<code>0</code>；<br>
    * 					返回第一个索引位置，如果找到
    *
    * @exception  IndexOutOfBoundsException  如果<code>off</code>与<code>len</code>超出字节数组边界。
    * @exception  IOException  如果发生I/O错误
    */
    public long indexOf(byte[] bs, int off, int len) throws IOException {
      if(off<0 || len<0 || off>bs.length || len>bs.length || len+off>bs.length){
        throw new StringIndexOutOfBoundsException();
      }
      if(isEOF()){
        if(len==0){
          return getFilePointer();
        }
        return -1;
      }
      if(len==0){
        return getFilePointer();
      }
      long tmpFilePointer = getFilePointer();
      int readlen =len<1024?1024:len;//一次读出的数据量
      int tmpOff = 0;
      byte[] tmpbs = new byte[len+readlen];//用于存储当前被查找的数据,一次从文件中读出1M的数据
      while(true){
      	long tmpCurrFilePointer = getFilePointer()-tmpOff;
      	int count = read(tmpbs, tmpOff, readlen);
      	int frs = find(tmpbs, count+tmpOff, bs, off, len);//查找，返回查询结果
      	if(frs!=-1){
      		seek(tmpFilePointer);
      		return tmpCurrFilePointer+frs;
      	}else if(isEOF()){
      		seek(tmpFilePointer);
      		return -1;
      	}else{
      		copyBytes(tmpbs, count+tmpOff-len, tmpbs, 0, len);
      		tmpOff = len;
      	}
      }
    }
    /**
     * 将xbs中的数据复制到ybs
     * 
     * @param xbs
     * @param xoff
     * @param ybs
     * @param yoff
     * @param len
     */
    private void copyBytes(byte[] xbs, int xoff, byte[] ybs, int yoff, int len){
    	for(int i=0;i<len;i++){
    		ybs[i+yoff] = xbs[i+xoff];
    	}
    }
    
    /**
     * 在字节数组中查询一子字节数组，如果找到，返回位置，如果没找到返回-1
     * 
     * @param tmpbs
     * @param bs
     * @param off
     * @param len
     * @return
     */
    private int find(byte[] tmpbs, int count, byte[] bs, int off, int len){
  		for(int i=0;i<count;i++){
  			if(equalsBytes(tmpbs, i, bs, off, len)){
  				return i;
  			}else{
  				if(count-i<len){
  					return -1;
  				}
  			}
  		}
  		return -1;
    }
    
    /**
     * 比较两个字节数组的值是否相等
     * 
     * @param xbs
     * @param xoff
     * @param ybs
     * @param yoff
     * @param len
     * @return
     */
    private boolean equalsBytes( byte[] xbs, int xoff, byte[] ybs, int yoff, int len){
    	if(xbs.length-xoff<len || ybs.length-yoff<len){
    		return false;
    	}
    	for(int i=0;i<len;i++){
    		if(xbs[xoff+i]!=ybs[yoff+i]){
    			return false;
    		}
    	}
    	return true;
    }

    /**
     * 返回给定字节数组数据在文件中的索引位置。从当前文件指针位置开始查找，可以用<code>seek()</code>改变
     * 查找的开始位置。
     *
     * @param   bs    用于查找的字节数组数据.
     *
     * @return  返回<code>-1</code>，如果没有查找到；<br>
     * 					返回当前文件指针位置，如果参数<code>len</code>等于<code>0</code>；<br>
     * 					返回第一个索引位置，如果找到
     *
     * @exception  IOException  如果发生I/O错误
     */   
    
   public long indexOf(byte[] bs) throws IOException {
     return indexOf(bs, 0 , bs.length);
   }

   /**
    * 返回给定字节数据在文件中的索引位置。从当前文件指针位置开始查找，可以用<code>seek()</code>改变
    * 查找的开始位置。
    *
    * @param   b    用于查找的字节数据.
    *
    * @return  返回<code>-1</code>，如果没有查找到；<br>
    * 					返回当前文件指针位置，如果参数<code>len</code>等于<code>0</code>；<br>
    * 					返回第一个索引位置，如果找到
    *
    * @exception IOException 如果发生I/O错误
    */
   public long indexOf(int b) throws IOException {
     return indexOf(new byte[]{(byte)b}, 0 , 1);
   }


   /**
    * 返回文件指针当前位置
    *
    * @return     从文件的开始位置到下次读或写操作的位置
    *
    * @see        java.io.RandomAccessFile#getFilePointer()
    */
   public long getFilePointer(){
      return curtFilePointer;
   }

   /**
    * 返回文件长度。
    *
    * @return     文件的标准长度
    *
    * @see        java.io.RandomAccessFile#length()
    */
   public long length(){
     return curtFileLength;
   }

  /**
   * 设置文件指针当前位置。
   *
   * @param      pos   新的当前文件指针位置
   *
   * @exception  IOException  如果发生I/O错误
   * 
   * @see        java.io.RandomAccessFile#seek(long pos)
   */
  public void seek(long pos) throws IOException{
    if(pos<0){
      throw new IOException("the position is less than 0");
    }
    curtFilePointer = pos; //设置文件当前指针
    if(pos>=length()){
      curtFileBlock = null; //设置文件当前块, 如果文件指针移动到了文件的最后，文件当前块设置为null
      curtFBPointer = -1; //设置文件当前块中的指针位置, 无意义
    }else{
      Iterator it = fileBlocks.iterator();
      long tmpLen = 0;
      while (it.hasNext()) {
        FileBlock fb = (FileBlock) it.next();
        long tmpCurtLen = tmpLen + fb.length();
        if (curtFilePointer >= tmpLen && curtFilePointer < tmpCurtLen) {
          curtFileBlock = fb; //设置文件当前块
          curtFBPointer = (int)(curtFilePointer - tmpLen); //设置文件当前块中的指针位置
          break;
        }
        tmpLen = tmpCurtLen;
      }
    }
  }

  /**
   * 文件指针向前或向后移动
   * 
   * @param n 移动的长度。如果是负数，向后移动；如果是正数，向前移动
   * 
   * @exception  IOException  如果发生I/O错误
   * 
   */
  public void skipBytes(int n) throws IOException{
  	if(n!=0){
  		seek(getFilePointer()+n);
  	}
  }

  /**
   * 设置文件长度
   *
   * @param      newLen    文件新的长度。如果newLen大于当前文件长度，文件大小将增加，
   * 												新的增加的部分文件内容由0来填充；如果newLen小于当前文件长度，文件将被剪裁。
   *
   * @exception  IOException  如果发生I/O错误
   * @exception  SecurityException  不是<code>rw</code>访问模式
   *
   * @see        java.io.RandomAccessFile#setLength(long newLen)
   */
  public void setLength(long newLen) throws IOException {
    if(!isrwMode){//Check is file access mode is rw.
      throw new SecurityException("Not is rw mode!");
    }
    if(newLen<0){
      throw new IOException("The new file length is less than 0");
    }
    if(newLen==curtFileLength){
      return;
    }

    /* 如果还没有对文件进行不对称操作，直接调用RandomAccessFile的write方法 */
    if(updatedFlag==false){
    	raf.setLength(newLen);
    }
   final int intMaxValue = 0x7fffffff;
    if(newLen<curtFileLength){
      seek(newLen);
      long cutedLen = curtFileLength-newLen;
      while(cutedLen>0){
        remove(cutedLen<=intMaxValue?(int)cutedLen:intMaxValue);
        cutedLen -= intMaxValue;
      }
    }else{
      seek(curtFileLength);
      long addedLen = newLen-curtFileLength;
      while(addedLen>0){
        insert(new byte[addedLen<=intMaxValue?(int)addedLen:intMaxValue]);
        addedLen -= intMaxValue;
      }
    }
  }

  /**
   * 关闭文件，对文件操作完后，必须调用此方法关闭文件
   *
   * @exception  IOException  如果发生I/O错误
   *
   * @see        java.io.RandomAccessFile#close()
   */
  public void close() throws IOException{
    /* 如果是读(r)访问模式或没有对文件进行块更新操作 */
    if(mode.equals("r") || updatedFlag==false){
      raf.close();
      return;
    }
    
    /* 如果是追加(a)模式 */
    if(isappendMode){
        raf.seek(raf.length());
        this.seek(raf.length());
        int i = fileBlocks.indexOf(curtFileBlock);
        for(;i<fileBlocks.size();i++){
            FileBlock fb = (FileBlock)fileBlocks.get(i);
            raf.write(fb.getData(), fb.offset(), fb.length());
        }
        raf.close();
        return;
    }
    
    /* 如果是读写(rw)模式 */
    String tmpFileName = file.getPath().substring(0, file.getPath().lastIndexOf(file.getName()))+new Date().getTime()+"$"+file.getName();
    File tmpFile = new File(tmpFileName);
    FileOutputStream fos = new FileOutputStream(tmpFile);
    int len = 1048576;
    if(length()<len){
      len = (int)length();
    }
    byte[] bs = new byte[len];
    seek(0);
    while(!isEOF()){
      len = read(bs);
      fos.write(bs,0, len);
    }
    fos.close();
    raf.close();
    String fileName = file.getPath();
    file.delete();
    tmpFile.renameTo(new File(fileName));
    tmpFile.delete();
  }

  
  /* 内部类型 ***********************************************************/
  /**
   * 文件块, 对文件进行分块管理，方便实现文件内容的添加，修改，删除等操作。
   */
  class FileBlock{
    private RandomAccessFile raf = null;
    private byte[] bs; //值数组，有可能为null, 如果为null其真实数据在文件中
    private long pos; //在文件中的位置，如果块状态是NEW，pos等于-1，无意义
    private int off ; //在值数组中的开始位置
    private int length; //块长度，与值数组有可能不等
    private int pointer = 0; //当前块指针

    private Status status; //文件块状态

    /**
     * 构造方法, 建立一个文件块
     *
     * @param raf      <code>RandomAccessFile</code>实例对象
     * @param pos      这个文件块在文件中的指针位置
     * @param len      文件块的长度
     */
    public FileBlock(RandomAccessFile raf, long pos, int len){
      this.raf = raf;
      this.bs = null;
      this.pos = pos;
      this.off = 0;
      this.length = len;
      this.status = Status.NULL;
    }

    /**
     * 构造方法, 建立一个文件块
     *
     * @param raf      <code>RandomAccessFile</code>实例对象
     * @param bs       字节数组数据
     * @param pos      这个文件块在文件中的指针位置
     */
    public FileBlock(RandomAccessFile raf, byte[] bs, long pos){
      this.raf = raf;
      this.bs = bs;
      this.pos = pos;
      this.off = 0;
      this.length = bs.length;
      this.status = Status.SAME;
    }

    /**
     * 构造方法, 建立一个文件块
     *
     * @param raf      <code>RandomAccessFile</code>实例对象
     * @param bs       字节数组数据
     * @param pos      这个文件块在文件中的指针位置
     * @param off      这个文件块数据在<code>bs</code>中的开始位置
     * @param len      这个文件块数据在<code>bs</code>中的长度，也是文件块的长度
     */
    public FileBlock(RandomAccessFile raf, byte[] bs, long pos, int off, int len){
      this.raf = raf;
      this.bs = bs;
      this.pos = pos;
      this.off = off;
      this.length = len;
      this.status = Status.SAME;
    }

    /**
     * 填充块数据
     * @param bs
     * @param off
     */
    public void fill(byte[] bs, int off){
      this.bs = bs;
      this.off = off;
      this.status = Status.SAME;
    }

    /**
     * 设置块状态
     * @param s 状态
     */
    public void setStatus(Status s){
      status = s;
    }
    /**
     * 返回块状态
     * @return 状态
     */
    public Status getStatus(){
      return status;
    }
    /**
     * 分割文件块
     * @param offset 分割点
     */
    public FileBlock split(int offset){
      if(offset<=0 || offset>length){
        throw new RuntimeException();
      }
      FileBlock fb = null;
      if(status==Status.NULL){
        fb = new FileBlock(raf, pos+(long)offset, length-offset);
        fb.setStatus(status);
      }else{
        fb = new FileBlock(raf, bs, pos+(long)offset, (int)offset+off, length-offset);
        fb.setStatus(status);
      }
      this.length = offset;
      this.pointer = 0;
      return fb;
    }

    /**
     * 读文件块数据
     * @param bOff 在数组b[]中的开始索引
     * @param bLen 长度
     */
    public int read(byte b[], int bOff, int bLen) throws IOException {
      if(pointer>=length){
        throw new RuntimeException("pointer already moved to file block foot!");
      }
      int size;
      if(bs==null){
        raf.seek(pos+(long)pointer);
        size = raf.read(b, bOff, bLen);
      }else{
        size=0;
        for(int i=pointer+off; i<bs.length && size<bLen; i++,size++, bOff++){
          b[bOff] = bs[i];
        }
      }
      pointer += size;
      return size;
    }
    /**
     * 修改块数据
     *
     * @param bOff 在数组b[]中的开始索引
     * @param bLen 长度
     *
     * @return 实际更新的字节数量
     */
    public int update(byte b[], int bOff, int bLen) throws IOException {
      if(status==Status.NULL){
        int tmpPointer = pointer;
        byte[] tbs = new byte[(int)length()];
        seek(0);
        read(tbs, 0, (int)length());
        fill(tbs, 0);
        setStatus(Status.CHANGED);
        seek(tmpPointer);
      }
      int size = 0;
      for(int i=pointer+off; i<length && size<bLen; i++,size++, bOff++){
        bs[i] = b[bOff];
      }
      pointer += size;
      return size;
    }

    /**
     * 设置块指针当前位置。
     * @param pointer
     */
    public void seek(int pointer){
      this.pointer = pointer;
    }
    /**
     * 返回块指针位置
     * @return
     */
    public int getPointer(){
      return pointer;
    }
    
    /**
     * 返回块字节数组数据
     */
    public byte[] getData(){
        return bs;
    }
    
    /**
     * 返回这个文件块数据在<code>bs</code>中的开始位置
     */
    public int offset(){
        return off;
    }
    
    /**
     * 返回这个文件块字节长度
     */
    public int length(){
      return length;
    }

  }

  /* 内部类型 ***********************************************************/
  /**
   * 文件块状态定义。枚举出文件块所有状态，这个类只有且只可能存在以下几个实例，NULL，
   * SAME，CHANGED，NEW。
   */
  static class Status{
    public static final Status NULL = new Status("NULL"); 			//没有数据(0)
    public static final Status SAME = new Status("SAME"); 			//数据与文件相同(1)
    public static final Status CHANGED = new Status("CHANGED"); 	//数据被修改(2)
    public static final Status NEW = new Status("NEW"); 			//新插入的数据(4)
    private String s;
    private Status(){}
    private Status(String s){
      this.s = s;
    }
    public String toString(){
      return s;
    }
  }

}






