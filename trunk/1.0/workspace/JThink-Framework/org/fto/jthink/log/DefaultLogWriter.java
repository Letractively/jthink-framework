/*
 * DefaultLogWriter.java 2005-9-22
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
package org.fto.jthink.log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.fto.jthink.config.Configuration;
import org.jdom.Element;

/**
 * 此类型的主要职责是向文件和控制台输出日志信息。
 * 
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-9-22  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */
class DefaultLogWriter extends Thread{
	

	/* 是否在控制台输出日志信息, 在配置文件中设置 */
	private boolean consoleOutput = true;
	/* 日志输出文件名称, 在配置文件中设置 */
	private String absoluteLogfileName;
	/* 向文件中输出的日志的编码方式, 在配置文件中设置 */
	private String logfileencoding;
	/* 一个日志文件的最大大小, 在配置文件中设置 */
	private long maxFileSize;
	/* 总共可备份多少个日志文件, 在配置文件中设置 */
	private int maxBackupIndex;	
	/* 是否为异步方式输出日志信息, 在配置文件中设置 */
	private boolean asynchronism = false;
	/* 异步处理的时间间隔, 在配置文件中设置 */
	private int timeinterval = 1000;
	/* 异步处理,一批日志最大数量 */
	private int batchMaxCount=100;
  /* 用于缓存日志信息的池 */
  //private List logPool = new LinkedList(batchMaxCount);
	private List logPool = new ArrayList(batchMaxCount);
	
	/**
	 * 创建DefaultLogWriter的实例
	 * 
	 * @param config 描述配置文件信息的对象
	 */
	public DefaultLogWriter(Configuration config){
		this.setDaemon(true);
		
		/* 如果不指定配置文件,只能在控制台输出日志信息 */
		if(config==null){
			return;
		}
		
		/* 返回日志配置信息 */
		Element logConfig = config.getLogManagerConfig();
		
		consoleOutput = logConfig.getChildText("console-output").equalsIgnoreCase("yes");
		
		asynchronism = logConfig.getChildText("asynchronism").equalsIgnoreCase("yes");
		if(asynchronism){
			timeinterval = Integer.parseInt(logConfig.getChildText("time-interval"))*1000;
		}
		logfileencoding = logConfig.getChildText("log-file-encoding");
		logfileencoding = logfileencoding.trim().length()==0?"utf-8":logfileencoding;
		
		/* 返回输出的日志文件 */
		absoluteLogfileName = logConfig.getChildText("log-file");
		if(absoluteLogfileName!=null && absoluteLogfileName.trim().length()>0){
			char separator = File.separatorChar;
			absoluteLogfileName = absoluteLogfileName.replace('\\',separator);
			absoluteLogfileName = absoluteLogfileName.replace('/',separator);
			String maxFileSizeStr = logConfig.getChildText("max-file-size");
			maxFileSize = maxFileSizeStr!=null&&maxFileSizeStr.trim().length()>0 ? Integer.parseInt(maxFileSizeStr)*1024:512*1024;
			String maxBackupIndexStr = logConfig.getChildText("max-backup-index");
			maxBackupIndex = maxBackupIndexStr!=null&&maxBackupIndexStr.trim().length()>0 ? Integer.parseInt(maxBackupIndexStr):2;
		}else{
			absoluteLogfileName = null;
		}
		
    /* 系统退出进事件 */
    Runtime.getRuntime().addShutdownHook(new ExitEvent(this));
	}

	/**
	 * 判断是否为异步日志输出
	 */
	public boolean isAsynchronism(){
		return asynchronism;
	}	
	
	/**
	 * 写日志
	 * 
	 * @param priority 日志输出优先级别
	 * @param message 日志信息
	 * @param t Throwable的实例或其子类型的实例
	 */
	public void write(Priority priority, String message, Throwable t){
		if(consoleOutput){
			writeLogToConsole(priority, message, t);
		}
		if(asynchronism && logPool.size()<batchMaxCount){
			logPool.add(0, new Object[]{priority, message, t});
		}else{
			writeLogToFile(priority, message, t);
		}
	}

	/**
	 * 同步所有日志信息, 将池中的日志信息全部写入日志文件
	 */
	public void synchronize() {
		if(asynchronism){
			if(logPool.size()>0){
				try {
					batchWriteLogs(logPool.size());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}	
	


	
	/**
	 * 批量写日志到文件
	 * 
	 * @param logsCount 一次批量处理的文件条数
	 */
	private void batchWriteLogs(int logsCount) throws IOException{
		
		ByteArrayOutputStream baostream = batchConnectLogs(logsCount);
	
  	//SmartAccessFile logfile=null;
  	//BufferedWriter writer;
  	BufferedOutputStream out=null;
    try {
      //writer = new BufferedWriter(new FileWriter(absoluteLogfileName,true));
    	//logfile = new SmartAccessFile(absoluteLogfileName, "a");
      out = new BufferedOutputStream(new FileOutputStream(absoluteLogfileName, true));
      
    	//logfile.append(baostream.toByteArray());
      //writer.append(baostream.toByteArray());
      out.write(baostream.toByteArray());
    	
      out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null){
				try {
				  out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		/* 处理日志文件名称 */
		processLogFileName();

	}
	
	/**
	 * 批量生成日志信息
	 * 
	 * @param logsCount 一次批量处理的文件条数
	 */
	private ByteArrayOutputStream batchConnectLogs(int logsCount) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for(int i=0;i<logsCount && logPool.size()>0;i++){
			Object[] logs = (Object[])logPool.remove(logPool.size()-1);
//			Priority priority = (Priority)logs[0];
			String message = (String)logs[1];
			Throwable t = (Throwable)logs[2];
			baos.write(message.getBytes(logfileencoding));
			baos.write(new byte[]{13,10});
	    if(t!=null){
//	    	PrintStream ps = new PrintStream(baos, true, logfileencoding);
	    	ByteArrayOutputStream os = new ByteArrayOutputStream();
	    	PrintStream ps = new PrintStream(os, true);
	    	t.printStackTrace(ps);
	    	baos.write(os.toString().getBytes(logfileencoding));
	    }
		}
		return baos;
	}
	
	/**
	 * 在控制台输出日志信息
	 */
	private void writeLogToConsole(Priority priority, String message, Throwable t){
    /* 在控制台输出日志信息 */
		if(priority==null || priority.getLevel()<Priority.ERROR_INT){
			System.out.println(message);
		}else{
    	System.err.println(message);
    }
    if(t!=null){
      t.printStackTrace();
    }
	}
	
	/**
	 * 在日志输出文件中打印日志信息
	 */
	private void writeLogToFile(Priority priority, String message, Throwable t){
    /* 在日志输出文件中打印日志信息 */
    if(absoluteLogfileName!=null){
    	ByteArrayOutputStream os = null;
    	PrintStream ps = null;
    	//SmartAccessFile logfile=null;
    	BufferedOutputStream out=null;
	    try {
	      out = new BufferedOutputStream(new FileOutputStream(absoluteLogfileName, true));
	    	//logfile = new SmartAccessFile(absoluteLogfileName, "a");
	    	
	    	out.write(message.getBytes(logfileencoding));
	    	out.write(new byte[]{13,10});
	    	
	    	//logfile.append(message.getBytes(logfileencoding));
	    	//logfile.append(new byte[]{13,10});
	    	
	    	if(t!=null){
		    	os = new ByteArrayOutputStream();
//		    	ps = new PrintStream(os, true, logfileencoding);
		    	ps = new PrintStream(os, true);
		    	t.printStackTrace(ps);
		    	out.write(os.toString().getBytes(logfileencoding));
	    	}
	    	out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(ps!=null){
					ps.close();
				}
				if(os!=null){
					try {
						os.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				if(out!=null){
					try {
					  out.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			
			/* 处理日志文件名称 */
			processLogFileName();
		}
	}

	/**
	 * 检查日志文件大小,如果当前日志文件大于配置的文件大小，重新生成新的日志文件
	 *
	 */
	private void processLogFileName(){

		File currLogfile = new File(absoluteLogfileName);
		if(currLogfile.length()>maxFileSize){
			try {
				File oldLogFile = new File(absoluteLogfileName+"."+(maxBackupIndex-1));
				if(oldLogFile.exists()){
					/* 删除一个最旧的日志文件 */
					oldLogFile.delete();
				}
				/* 修改日志文件名称 */
				for(int i=maxBackupIndex-2;i>0;i--){
					File file = new File(absoluteLogfileName+"."+i);
					if(file.exists()){
						file.renameTo(new File(absoluteLogfileName+"."+(i+1)));
					}
				}
				/* 修改不当前日志文件名称 */
				currLogfile.renameTo(new File(absoluteLogfileName+".1"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 运行日志异步处理线程
	 */
	public void run(){
			while(true){
				if(asynchronism && logPool.size()>0){
						try {
							batchWriteLogs(logPool.size());
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
				try{
					sleep(timeinterval);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}

  /**
   *  程序退出时事件代码
   */
  class ExitEvent
      extends Thread{
    DefaultLogWriter writer;
    public ExitEvent(DefaultLogWriter writer){
      this.writer=writer; 
      this.setDaemon(true);
    }
    public void run(){
      try{
        writer.synchronize();
      }
      catch(Exception ex){
        ex.printStackTrace();
        writer.write(Priority.WARN, ex.getMessage(), ex);
      }
    }
  } 
	}
	
