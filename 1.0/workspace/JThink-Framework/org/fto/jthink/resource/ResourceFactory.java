/*
 * 创建日期 2005-6-24
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package org.fto.jthink.resource;


/**
 * 资源工厂, 每个资源工厂的具体实现都必须创建以
 * ResourceManager和Configuration为参数的构造方法
 * 
 * 
 */
public interface ResourceFactory {
	
	/**
	 * 创建资源对象
	 * @return 资源对象
	 */
	public Object create();
	
}
