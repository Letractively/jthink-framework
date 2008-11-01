JTHINK-FRAMEWORK, 发布 1.0 测试1版本 (2005-10)
----------------------------------------------
http://www.free-think.org


1. 介绍

JThink就是为了解决JAVA应用系统在开发过程中的一系列问题所发起的一个框架。她的主要目的是用于解决JAVA/J2EE应用
系统中业务逻辑层面中反复遇到的问题。JThink包括以下几部分内容：资源管理，请求处理，事务处理，连接数据源，数
据访问，EJB组件开发，EJB组件访问，日志处理等。

此版本是JThink框架的第一个版本，在使用过程中发现有何问题或建议，请将问题提交到 jthink_tasks@yahoo.com.cn中。

2. 发布信息

JThink框架需要J2SE 1.3和J2EE 1.3 (Servlet 2.3, JSP 1.2, JTA 1.0, EJB 2.0)的支持或后继版本的支持。

发布的目录内容：
* "src" 包含框架的Java源文件
* "dist" 包含JThink分发的库文件和其它必须文件
* "lib" 包含所有JThink必须的或可选的第三方库文件
* "docs" 包含常规框架文档和框架Java API文档
* "samples" 包含一些例子应用


3. 发布的库文件

在dist目录中的库文件或其它文件，是构建和运行JThink框架必须的。

* "fto-jthink.jar" (~148 KB)
- 说明: 框架核心库文件
- 依靠: jdom.jar, xerces.jar, jakarta-regexp-1.3.jar


* "fto-jthink.xml"
- 说明: 使用JThink框架必须的配置文件

