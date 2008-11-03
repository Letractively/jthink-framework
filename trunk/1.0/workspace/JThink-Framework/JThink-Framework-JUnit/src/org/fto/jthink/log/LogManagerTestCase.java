/*
 * 创建日期 2005-7-25
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package org.fto.jthink.log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.fto.jthink.config.Configuration;
import org.fto.jthink.log.LogManager;
import org.fto.jthink.log.Logger;
import org.fto.jthink.util.PackageResourceReader;
import org.fto.jthink.util.XMLHelper;

import junit.framework.TestCase;

/**
 * @author Administrator
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class LogManagerTestCase extends TestCase {
	
		Logger logger = LogManager.getLogger(LogManagerTestCase.class);
	
		public static void main(String[] args) {
			junit.textui.TestRunner.run(LogManagerTestCase.class);
		}

		static{
			URL url = new PackageResourceReader(LogManagerTestCase.class).getResource("/META-INF/fto-jthink.xml");
			System.out.println(url);
			Configuration config = new Configuration(XMLHelper.load(url));
			LogManager.configure(config);
		}
		
		/**
		 *方法测试
		 */
		public void testGetLogger() {
			System.out.println("\n[正在测试方法: LogManager.getLogger()...]");
			
//			Logger logger = LogManager.getLogger(LogManagerTestCase.class);
//			System.out.println(logger);
			
			for(int i=0;i<3;i++){
				logger.log("测试log!!!");
				logger.debug("测试debug!!!");
				logger.info("测试Info!!!");
				logger.warn("测试warn!!!");
				logger.error("测试error!!!");
				logger.fatal("测试fatal!!!");
	
				logger = LogManager.getLogger("LogManagerTestCase");
	//			System.out.println(logger);
				logger.error("测试 Exception error!!!");
				try{
					throw new Exception("测试 测试 Test exception error!");
				}catch(Exception e){
					logger.error("测试 Exception error!!!2", e);
				}
			}
			
			try {
				String str = new BufferedReader(new InputStreamReader(System.in)).readLine();
				
				
			} catch (Exception e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
}
