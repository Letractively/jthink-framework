/*
 * EJBHomeFactory.java	2005-7-5
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

package org.fto.jthink.j2ee.ejb;

/**
 * EJBHome工厂，通过实现EJBHomeFactory来返回J2EE/EJBHome接口或JThink/EJBHome类。
 * 是查找J2EE/EJBHome本地主接口、J2EE/EJBHome远程主接口还是JThink/EJBHome类，这要由具体的实现来决定。
 * 我们可以只须在配置文件中修改一下相关的配置项(指定EJBHomeFactory的具体实现)，
 * 就可以方便的将应用发布到运行J2EE/EJB的容器(Jboss,Weblogic,Websphere)或非J2EE/EJB容器(Tomcat)中。
 *
 * <pre>
 * 此工厂的具体实现，必须创建以ResourceManager resManager,String resContainerName, Configuration config, String serverId
 * 为参数的构造方法。比如：
 * 	public XxxxEJBHomeFactory(ResourceManager resManager,String resContainerName, Configuration config, String serverId){
 *	   ... ...
 *  }
 * </pre>
 *
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-5  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */

public interface EJBHomeFactory {

	/**
	 * 创建并返回JThink/EJBHome接口或J2EE/EJBHome接口的实现
	 * 
	 * @param homeClass JThink/EJBHome接口的实现Home类或J2EE/EJBHome接口的子Home接口
	 * @param jndi 用于查找EJBHome接口的JNDI名称
	 * 
	 * @return Home接口的实现
	 */
	public Object create(Class homeClass, String jndi);
	
}
