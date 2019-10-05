package org.smart4j.framework.sqlSession;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

/**
 * @author kexinxin
 * 封装来自程序的请求
 **/
public class Sqlsession {
    private Excutor excutor = new Myexcutor();

    private Configuration configuration = new Configuration();

    public <T> T selectOne(String statement, Object parameter, String reusltTypeName) {
        return excutor.query(statement, parameter, reusltTypeName);
    }

    public <T> T selectList(String statement, Object parameter, String reusltTypeName){
        return excutor.queryList(statement, parameter, reusltTypeName);
    }

    public boolean update(String statement,String parametertype,Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return excutor.update(statement,parametertype,object);
    }

    public boolean delete(String statement,String parametertype,Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return excutor.update(statement,parametertype,object);
    }

    public boolean insert(String statement,String parametertype,Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return excutor.update(statement,parametertype,object);
    }

    @SuppressWarnings("unchecked")
    public <T> T getMapper(String mapperXML, Class<T> c) {
        //调用方法的入口,通过初始化 MyMapper连接操作数据库和配置文件 -->
        return (T) Proxy.newProxyInstance(c.getClassLoader(), new Class[]{c},
                new MapperProxy(mapperXML, configuration, this));
    }

}
