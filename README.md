# SmarkFramwork
Smart Framwork框架为黄勇在开源中国发布的Jave Web框架实现 IOC、AOP、ORM轻量级解决方案。针对Smart Framwork中ORM框架设计比较简单，采用类似Mybatis重新改写Smart Framwork中的ORM部分

黄勇写的关于该框架的系列博文可以在这里查看   https://my.oschina.net/huangyong?tab=newest&catalogId=386891
之前的写法如下

这里重点介绍我自己改写的ORM内容

SqlSession为主要的类，包含有Configuration和Executor对象。SqlSession通过getMapper方法来对mapper的接口类进行代理，并且与SQL语句实现的XML文件进行对应。实现的代理类为MapperProxy，MapperProxy将sqlsession以及configuration对象传进来。configuration主要负责对xml文件的解析（包括SQL语句），sqlsession主要提供数据的操作方法，而sqlsession的查询又调用了Executor去做实际的查询，Executor会使用JDBC查询结果集，然后将结果集映射为POJO，返回给SqlSession。在服务层调用Mapper的方法，通过动态代理实际上是sqlsession在做事情。

类的关系图参见我的博文  https://www.cnblogs.com/kexinxin/p/11628474.html
