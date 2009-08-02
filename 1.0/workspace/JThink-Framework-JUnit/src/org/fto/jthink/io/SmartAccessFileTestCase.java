/*
 * 创建日期 2005-7-10
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package org.fto.jthink.io;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.fto.jthink.io.SmartAccessFile;

import junit.framework.TestCase;

/**
 * @author wenjian
 * 
 */
public class SmartAccessFileTestCase extends TestCase {
	String  testedfileRW = "SmartAccessFile_rw.txt";
	String  testedfileR = "SmartAccessFile_r.txt";
	String  testedfileA = "SmartAccessFile_a.txt";
	public static void main(String[] args) {
		junit.textui.TestRunner.run(SmartAccessFileTestCase.class);
     
	}
	
	
	/**
	 *方法测试
	 */
	public void testindexOf() {
		System.out.println("\n[正在测试方法: SmartAccessFile.indexOf()...]");
		SmartAccessFile saf = null;
		try {
			saf = new SmartAccessFile("SmartAccessFile_testIndexOf.txt", "rw");
		
			byte[] bs = "pqrstuvwxyz".getBytes();
			long index = saf.indexOf(bs, 0, bs.length);
			System.out.println("saf.indexOf(bs):"+index);
			if(index!=-1){
			  super.fail();
      }
      
      bs = "0123456789".getBytes();
      index = saf.indexOf(bs, 0, bs.length);
      System.out.println("saf.indexOf(bs):"+index);
      if(index!=3){
        super.fail();
      }
      
      bs = "试索引".getBytes();
      index = saf.indexOf(bs, 0, bs.length);
      System.out.println("saf.indexOf(bs):"+index);
      if(index!=17){
        super.fail();
      }

			printFileAllData(saf);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
			
		}finally{
			closeFile(saf);
		}
		System.out.println("方法indexOf()通过测试.");
	}
	
	
	/**
	 *方法测试
	 */
	public void testIsEOF() {
		System.out.println("\n[正在测试方法: SmartAccessFile.isEOF()...]");
		SmartAccessFile saf = null;
		try {
			saf = new SmartAccessFile(testedfileRW, "rw");
			saf.insert("Test inEOF()".getBytes());
			saf.seek(saf.length());
			assertTrue("是文件尾:", saf.isEOF());
			saf.seek(1);
			assertTrue("不是文件尾:", !saf.isEOF());
			
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
			
		}finally{
			closeFile(saf);
		}
		System.out.println("方法isEOF()通过测试.");
	}

	
	/**
	 *方法测试
	 */
	public void testInsert() {
		System.out.println("\n[正在测试方法: SmartAccessFile.insert()...]");
		SmartAccessFile safrw = null;
		SmartAccessFile safr = null;
		SmartAccessFile safa = null;
		
		/* 测试"rw"模式 */
		try {
			safrw = new SmartAccessFile(testedfileRW, "rw");
			byte[] bs = null;
			safrw.seek(safrw.length());
			long beginLen = safrw.length();
			safrw.insert("测试直接调用RandomAccessFile的write方法!".getBytes());
			bs = new byte[(int)safrw.length()-(int)beginLen];
      safrw.seek(beginLen);
      safrw.read(bs);
			System.out.println("测试直接调用write方法:"+new String(bs));
			if(!"测试直接调用RandomAccessFile的write方法!".equals(new String(bs))){
			  super.fail();
      }
      long p = safrw.getFilePointer();
			safrw.insert("Test insert!".getBytes("UTF-8"));
			safrw.insert(0x30);
			safrw.insert("123456789".getBytes("UTF-8"), 1,2);
			safrw.insert("测试!".getBytes("UTF-8"));

			bs = new byte[(int)safrw.length()-(int)p];
			safrw.seek(p);
			int readedcount = safrw.read(bs);
			System.out.println("测试\"rw\"模式,实际读出字节数量:"+readedcount);
			printBytes("测试\"rw\"模式,", bs);
			String str = new String(bs,"UTF-8");
			System.out.println("测试\"rw\"模式,打印文件内容:"+str);
      if(!"Test insert!023测试!".equals(str)){
        super.fail();
      }

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
			
		}finally{
			closeFile(safrw);
		}
		
		/* 测试"r"模式 */
		try{
			safr = new SmartAccessFile(testedfileR, "r");
			safr.insert("Test insert!".getBytes());
			fail("测试\"r\"模式: 不应该到达此点!");
		}catch(Exception e){
			System.out.println("测试\"r\"模式,应该到达此点!  "+e.getMessage());
		}finally{
			closeFile(safr);
		}

		/* 测试"a"模式 */
		try{
			safa = new SmartAccessFile(testedfileA, "a");
			safa.append("Test insert!".getBytes());
			System.out.println("测试\"a\"模式: 应该到达此点!");
		}catch(Exception e){
			fail("测试\"a\"模式,不应该到达此点!  "+e.getMessage());
		}finally{
			closeFile(safa);
		}
		
		System.out.println("方法insert()通过测试.");
	}

	
  
  
  /**
   *方法测试
   */
  public void testWrite() {
    System.out.println("\n[正在测试方法: SmartAccessFile.write()...]");
    SmartAccessFile safrw = null;
    SmartAccessFile safr = null;
    SmartAccessFile safa = null;
    
    /* 测试"rw"模式 */
    try {
      safrw = new SmartAccessFile(testedfileRW, "rw");
      
      byte[] bs = "测试在文件头部写入数据".getBytes();
      safrw.write(bs);
      safrw.seek(0);
      Arrays.fill(bs, (byte)0);
      safrw.read(bs);
      System.out.println("测试在文件头部写入数据:"+new String(bs));
      if(!"测试在文件头部写入数据".equals(new String(bs))){
        super.fail();
      }
      long p = safrw.getFilePointer();
      safrw.write("Test insert!".getBytes("UTF-8"));
      safrw.write(0x30);
      safrw.write("123456789".getBytes("UTF-8"), 1,2);
      safrw.write("测试!".getBytes("UTF-8"));
      bs = new byte[(int)safrw.getFilePointer()-(int)p];
      safrw.seek(p);
      int readedcount = safrw.read(bs);
      System.out.println("测试\"rw\"模式,实际读出字节数量:"+readedcount);
      printBytes("测试\"rw\"模式,", bs);
      String str = new String(bs,"UTF-8");
      System.out.println("测试\"rw\"模式,打印文件内容:"+str);
      if(!"Test insert!023测试!".equals(str)){
        super.fail();
      }

    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
      
    }finally{
      closeFile(safrw);
    }
    
    /* 测试"r"模式 */
    try{
      safr = new SmartAccessFile(testedfileR, "r");
      safr.insert("Test insert!".getBytes());
      fail("测试\"r\"模式: 不应该到达此点!");
    }catch(Exception e){
      System.out.println("测试\"r\"模式,应该到达此点!  "+e.getMessage());
    }finally{
      closeFile(safr);
    }

    /* 测试"a"模式 */
    try{
      safa = new SmartAccessFile(testedfileA, "a");
      safa.append("Test insert!".getBytes());
      System.out.println("测试\"a\"模式: 应该到达此点!");
    }catch(Exception e){
      fail("测试\"a\"模式,不应该到达此点!  "+e.getMessage());
    }finally{
      closeFile(safa);
    }
    
    System.out.println("方法insert()通过测试.");
  }
  

//	/**
//	 *方法测试
//	 */
//	public void testInsert() {
//		System.out.println("...正在测试方法: SmartAccessFile.insert()...");
//		SmartAccessFile saf = null;
//		try {
//			saf = new SmartAccessFile(testedfile, "rw");
//			saf.setLength(0);
//			saf.insert("Test insert!".getBytes());
//			saf.insert(0x30);
//			saf.insert("123456789".getBytes(), 1,2);
//			System.out.println("字节长度:"+"测试!".getBytes("UTF-8").length);
//			saf.insert("测试!".getBytes("UTF-8"), 3, 4);
//			printFileAllData(saf);
////			throw new Exception("Text error!");
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail(e.getMessage());
//			
//		}finally{
//			if(saf!=null){
//				try {
//					saf.close();
//				} catch (IOException ie) {
//					ie.printStackTrace();
//					fail(ie.getMessage());
//				}
//			}
//		}
//	}	
//	

//  /**
//   * 打印块信息，用于调试
//   * @throws IOException
//   */
//  void print() throws IOException {
//    int tmppointer = pointer;
//
//    System.out.println("--FileBlock--"+ this +"------------------------------------");
//    System.out.println("在文件中的位置(文件指针):  "+ pos);
//    System.out.println("在值数组中的开始位置:  "+off);
//    System.out.println("在块中的位置(块指针)： "+pointer);
//    System.out.println("块长度:  "+length);
//    System.out.println("文件块状态:  "+status);
//    System.out.println("文件块数据:"+bs);
////    int size;
////    byte[] b = new byte[1];
////    for(int i=0; i<this.length(); i++){
////      this.seek(i);
////      this.read(b, 0 ,1);
////      System.out.print((char)b[0]);
////    }
//    System.out.println();
//    seek(tmppointer);
//  }	
	
	private void printFileAllData(SmartAccessFile saf) throws IOException{
		try{
			System.out.println("文件长度："+saf.length());
	    System.out.print("文件所有数据：");
      saf.seek(0);
      byte[] data = new byte[(int)saf.length()];
      saf.read(data);
      System.out.println(new String(data));
		}catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
  }

	private void printBytes(String title, byte[] bs){
		System.out.print(title+"打印字节数组值:");
    for (int i = 0; i < bs.length; i++) {
      System.out.print(","+bs[i]);
    }
    System.out.println();
	}

//  /**
//   * 用于调试，打印所有块信息
//   */
//  public void printFileBlockInfo() throws IOException {
//    for(int i=0; i<fileBlocks.size();i++){
//      FileBlock fb = (FileBlock)fileBlocks.get(i);
//      fb.print();
//    }
//  }
//  /**
//   * 用于调试，打印当前文件信息
//   */
//  public void printFileInfo(boolean printData) throws IOException {
//    long tmpcurtFilePointer = getFilePointer();
//    System.out.println("=====Start=====================================================");
//    System.out.println("是否文件末尾：isEOF = " + isEOF());
//    System.out.println("文件当前长度：curtFileLength = " + curtFileLength);
//    System.out.println("文件当前指针位置：curtFilePointer = " + curtFilePointer);
//    System.out.println("文件当前块中的指针位置：curtFBPointer = " + curtFBPointer);
//    System.out.println("文件块数量：fileBlocks.size() = " + fileBlocks.size());
//    if(printData){
//      System.out.println("文件当前块数据:" + curtFileBlock);
//
//      int size;
//      if (curtFileBlock != null) {
//        int empFBPointer = curtFileBlock.getPointer();
//        byte[] b = new byte[1];
//        for (int i = 0; i < curtFileBlock.length(); i++) {
//          curtFileBlock.seek(i);
//          curtFileBlock.read(b, 0, 1);
//          System.out.print( (char) b[0]);
//        }
//        curtFileBlock.seek(empFBPointer);
//      }
//      System.out.println();
//      System.out.println("文件所有数据：");
//      System.out.print("(");
//      for (int i = 0; i < (int) length(); i++) {
//        seek(i);
//        System.out.print( (char) read());
//      }
//      System.out.println(")");
//    }
//    System.out.println("=====End=====================================================");
//
//    seek(tmpcurtFilePointer);
//  }	
//	
	private void closeFile(SmartAccessFile saf){
		if(saf!=null){
			try {
				saf.close();
			} catch (IOException ie) {
				ie.printStackTrace();
				fail(ie.getMessage());
			}
		}

	}
	
	/**
	 * 初始化
	 */
	protected void setUp() {
		try{
//			System.out.println();
//			System.out.println("开始测试:SmartAccessFile.java");
			
		  File frw = new File(testedfileRW);
		  if(!frw.exists()){
		    frw.createNewFile();
		  }

		  File fr = new File(testedfileR);
		  if(!fr.exists()){
		    fr.createNewFile();
		  }
		  
		  File fa = new File(testedfileA);
		  if(!fa.exists()){
		    fa.createNewFile();
		  }		  
		  
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 销毁
	 */
	protected void tearDown() {
//		System.out.println();
//		System.out.println("结束测试:SmartAccessFile.java");
	}

}