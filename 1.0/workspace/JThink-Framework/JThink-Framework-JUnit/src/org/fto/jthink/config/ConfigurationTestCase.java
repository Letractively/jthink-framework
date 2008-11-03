/**
 * 
 */
package org.fto.jthink.config;

import junit.framework.TestCase;

/**
 * @author try.jiwen
 *
 */
public class ConfigurationTestCase extends TestCase {

  /**
   * @param name
   */
  public ConfigurationTestCase(String name) {
    super(name);
  }

  public void test(){
    System.out.println("\n[正在测试方法: ConfigurationTestCase.test()...]");
    //Configuration.configure(Class.class.getResource("/fto-jthink.xml"));
    Configuration config = Configuration.getConfiguration();
    assertTrue(config != null);
    
  }
  
  
  
  /* (non-Javadoc)
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
  }

  /* (non-Javadoc)
   * @see junit.framework.TestCase#tearDown()
   */
  protected void tearDown() throws Exception {
    super.tearDown();
  }

}
