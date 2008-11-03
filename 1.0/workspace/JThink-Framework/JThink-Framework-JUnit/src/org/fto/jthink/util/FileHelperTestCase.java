package org.fto.jthink.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

public class FileHelperTestCase  extends TestCase{
  
  public static void main(String[] args) {
    junit.textui.TestRunner.run(FileHelperTestCase.class);
  }
  
  
  public void testReadContent(){
    System.out.println("\n[正在测试方法: FileHelper.readContent()...]");
    
    try {
      //InputStream is = new PackageResourceReader(this.getClass()).getResource("/fto-jthink.xml").openStream();
      //is.
      
      //String f = new PackageResourceReader(this.getClass()).getResource("/fto-jthink.xml").getFile();
      String f = "D:\\WJian.Try\\MyWorkSpace\\Free Think Organizing\\JThink-Framework\\1.0\\fto-jthink-framework-1[1].0-M4\\readme.txt";
      //String c = FileHelper.readContent(new File(f), "GBK");
      //System.out.println(c);
      
      byte[] bs = FileHelper.readBytes(new File(f));
      System.out.println(new String(bs, "GBK"));
      
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    };
  }

  public void testWriteContent(){
    System.out.println("\n[正在测试方法: FileHelper.writeContent()...]");
    
    try {
      //InputStream is = new PackageResourceReader(this.getClass()).getResource("/fto-jthink.xml").openStream();
      //is.
      
      //String f = new PackageResourceReader(this.getClass()).getResource("/fto-jthink.xml").getFile();
      String f = "D:\\WJian.Try\\MyWorkSpace\\Free Think Organizing\\JThink-Framework\\1.0\\fto-jthink-framework-1[1].0-M4\\readme.txt";
      String c = FileHelper.readContent(new File(f), "GBK");
      System.out.println(c);
      
      FileHelper.writeContent(new File("testReadme.txt"), c);
      FileHelper.writeContent(new File("testReadmegbk.txt"), c, "GBK");
      FileHelper.writeContent(new File("testReadmeutf8.txt"), c, "UTF-8");
      
      //byte[] bs = FileHelper.readBytes(new File("testReadme.txt"));
      byte[] bs = FileHelper.readBytes(new File("testReadmeutf8.txt"));
      
      FileHelper.writeBytes(new File("testReadmeBytes.txt"), bs);
      
      
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    };
  }
}
