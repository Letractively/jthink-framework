/*
 * XMLIterator.java	2004-4-5
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
package org.fto.jthink.util;

import org.jdom.Element;


/**
 * 用于遍历XML文档树，实现了此接口的对象可以在iterator方法中得到XML树的每一个节点，必须与
 * XMLHelper中的rootIterator或leafIterator方法结合使用。
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2004-04-05  创建此类型
 * 
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */

public interface XMLIterator {
  /**
   * 实现此接口方法的对象将得到XML文档的每一个节点元素以及当前节点元素所在的层次和在这层次上的索引位置
   * 
   * @param el     当前被遍历到的org.jdom.Element元素
   * @param layer  当前节点在XML树中的层次，从0开始
   * @param index  在当前层上的索引位置,从0开始
   */
	public void iterator(Element el, int layer, int index);

}
