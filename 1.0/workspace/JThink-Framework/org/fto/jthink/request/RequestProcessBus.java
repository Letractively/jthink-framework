/*
 * RequestProcessBus.java	2005-4-16
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
package org.fto.jthink.request;

import java.util.*;

/**
 * 请求处理总线。
 * 
 *
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-04-16  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 * 
 */
public class RequestProcessBus {
	
	/* 请求对象 */
	private Request req;
	/* 所有请求处理机 */
	private List processors = new ArrayList();
	/* 所有结果,由请求处理机处理后返回的结果 */
	private Map resultHM = new HashMap();

	/**
	 * 创建RequestProcessBus的实例
	 */
	public RequestProcessBus(){}
	
	/**
	 * 创建RequestProcessBus的实例
	 * 
	 * @param req 请求对象
	 */
	public RequestProcessBus(Request req){
		this.req=req;
	}
	
	/* 是否终止处理请求, 如果值为 true, 终止请求处理, 否则继续 */
	private boolean stop = false;
	
	/**
	 * 终止处理请求
	 */
	public void stop(){
		stop = true;
	}
	
	/**
	 * 设置请求对象
	 * 
	 * @param req 请求对象
	 */
	public void setRequest(Request req){
		this.req=req;
	}
	
	/**
	 * 返回请求对象
	 * 
	 * @return 请求对象
	 */
	public Request getRequest(){
		return req;
	}
	
	/**
	 * 加入一个请求处理机
	 * 
	 * @param reqPrcsr 请求处理机
	 */
	public void add(RequestProcessor reqPrcsr){
		reqPrcsr.setBus(this);
		processors.add(reqPrcsr);
	}
	
	/**
	 * 移除请求处理机
	 * 
	 * @param reqPrcsr 请求处理机
	 */
	public void remove(RequestProcessor reqPrcsr){
		processors.remove(reqPrcsr);
		resultHM.remove(reqPrcsr);
	}
	
	/**
	 * 清除所有请求处理机
	 */
	public void clear(){
		processors.clear();
		resultHM.clear();
	}

	/**
	 * 返回所有请求处理机
	 * @return 所有请求处理机
	 */
	public List getProcessors(){
		return processors;
	}
	
	/**
	 * 返回所有结果
	 * 
	 * @return 所有结果
	 */
	public Map getResults(){
		return resultHM;
	}

	/**
	 * 返回一个结果,如果没有结果,返回null
	 * 
	 * @return 结果
	 */
	public Object getResult(){
		if(resultHM.size()>0){
			return resultHM.values().iterator().next();
		}
		return null;
	}

	/**
	 * 返回与请求处理机相关系的结果,即由指定请求处理机返回的结果
	 * 
	 * @param reqPrcsr 请求处理机
	 * @return 结果
	 */
	public Object getResult(RequestProcessor reqPrcsr){
		return resultHM.get(reqPrcsr);
	}
	
	/**
	 * 开始执行请求处理机。调用getResults()方法返回处理的结果
	 *
	 */
	public void start(){
		for(int i=0;i<processors.size();i++){
			RequestProcessor reqPrcsr = (RequestProcessor)processors.get(i);
			Object result = reqPrcsr.run(req);
			if(result!=null){
				resultHM.put(reqPrcsr, result);
			}
			if(stop){
				break;
			}
		}
		return;
	}
	
	
}
