/*
 * GUIRequest.java	2005-7-3
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

package org.fto.jthink.gui;

import java.awt.Component;
import java.awt.Container;
import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JToggleButton;
import javax.swing.text.JTextComponent;

import java.awt.Label;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.List;
import java.awt.TextComponent;
import java.awt.Button;

import org.fto.jthink.request.DefaultRequest;

/**
 * 用于桌面应用中的GUI中的请求数据收集， 这个类的实例可以自动收集给定窗口中的大部分(主要是JSwing和AWT)可编辑组件的用户输入的信息，
 * 然后可以用组件的名称来访问组件值。
 * 
 * 
 * <p><pre><b>支持的GUI组件：</b>
 * javax.swing.AbstractButton 
 * javax.swing.JToggleButton
 * javax.swing.JLabel
 * javax.swing.JList
 * javax.swing.JToggleButton
 * javax.swing.text.JTextComponent
 * 
 * java.awt.Label
 * java.awt.Checkbox
 * java.awt.Choice
 * java.awt.List
 * java.awt.TextComponent
 * java.awt.Button
 * </pre>
 * </p>
 * 
 * <p>
 * <pre>
 * <b>历史更新记录:</b>
 * 2005-07-03  创建此类型
 * 2005-09-07  加入对AWT组件的支持
 * </pre>
 * </p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 * @see      org.fto.jthink.request.DefaultRequest
 * @see      org.fto.jthink.request.Request
 * 
 */

public class GUIRequest extends DefaultRequest {

  private static final long serialVersionUID = -5714307716311832323L;

  /**
	 * 实例化一个空的GUIRequest请求对象
	 */
	public GUIRequest() {
	}

	/**
	 * 实例化一个GUIRequest请求对象，它将递归收集Container(指扩展了此容器的对象)或被其包含的子组件中的用户输入的请求信息
	 *
	 * @param container 扩展了此容器的对象
	 */
	public GUIRequest(Container container) {
		init(container);
	}

	private void init(Component cp) {
		initValue(cp);
		if (cp instanceof Container) {
			Component[] cs = ((Container) cp).getComponents();
			for (int i = 0; i < cs.length; i++) {
				/* 递归处理 */
				init(cs[i]);
			}
		}
	}

	private void initValue(Component c) {
		String name = c.getName();

		String value = null;
		if (c instanceof JTextComponent) {
			JTextComponent jtextComponent = (JTextComponent) c;
			value = jtextComponent.getText();
			setValue(name, value);

		} else if (c instanceof AbstractButton) {
			if (c instanceof JToggleButton) {
				JToggleButton jtoggleButton = (JToggleButton) c;
				if (jtoggleButton.isSelected()) {
					value = jtoggleButton.getText();
				}
			} else {
				AbstractButton abstractButton = (AbstractButton) c;
				value = abstractButton.getText();
			}
			setValue(name, value);

		} else if (c instanceof JComboBox) {
			JComboBox jcomboBox = (JComboBox) c;
			Object[] objs = jcomboBox.getSelectedObjects();
			if (objs != null) {
				int count = objs.length;
				for (int ci = 0; ci < count; ci++) {
					setValue(name, objs[ci].toString());
				}
			}

//		} else if (c instanceof JSpinner) {
//			JSpinner jspinner = (JSpinner) c;
//			try {
//				jspinner.commitEdit();
//				setValue(name, jspinner.getValue().toString());
//			} catch (ParseException e) {
//				throw new RuntimeException(e.getMessage(), e);
//			}

		} else if (c instanceof JLabel) {
			JLabel jlabel = (JLabel) c;
			value = jlabel.getText();
			setValue(name, value);

		} else if (c instanceof JList) {
			JList jlist = (JList) c;
			Object[] objs = jlist.getSelectedValues();
			if (objs != null) {
				int count = objs.length;
				for (int ci = 0; ci < count; ci++) {
					setValue(name, objs[ci].toString());
				}
			}
		} else if (c instanceof Label) {
			Label label = (Label) c;
			value = label.getText();
			setValue(name, value);
		} else if (c instanceof Checkbox) {
			Checkbox checkbox = (Checkbox) c;
			Object[] objs = checkbox.getSelectedObjects();
			if (objs != null) {
				int count = objs.length;
				for (int ci = 0; ci < count; ci++) {
					setValue(name, objs[ci].toString());
				}
			}
		} else if (c instanceof Choice) {
			Choice choice = (Choice) c;
			Object[] objs = choice.getSelectedObjects();
			if (objs != null) {
				int count = objs.length;
				for (int ci = 0; ci < count; ci++) {
					setValue(name, objs[ci].toString());
				}
			}
		} else if (c instanceof List) {
			List list = (List) c;
			Object[] objs = list.getSelectedObjects();
			if (objs != null) {
				int count = objs.length;
				for (int ci = 0; ci < count; ci++) {
					setValue(name, objs[ci].toString());
				}
			}
		} else if (c instanceof TextComponent) {
			TextComponent textComponent = (TextComponent) c;
			value = textComponent.getText();
			setValue(name, value);

		} else if (c instanceof Button) {
			Button button = (Button) c;
			value = button.getLabel();
			setValue(name, value);
		}

	}

	private void setValue(String name, String value) {
		if (name != null && value != null) {
			super.putParameter(name, value);
		}
	}
}