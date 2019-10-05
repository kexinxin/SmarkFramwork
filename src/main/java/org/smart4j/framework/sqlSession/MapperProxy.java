package org.smart4j.framework.sqlSession;



import org.smart4j.framework.bean.Function;
import org.smart4j.framework.bean.MapperBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author kexinxin
 **/
public class MapperProxy implements InvocationHandler {
    private Sqlsession sqlsession;
    private Configuration configuration;
    private String mapperXMLName;

    public MapperProxy(String mapperXMLName, Configuration configuration, Sqlsession sqlsession) {
        this.configuration = configuration;
        this.sqlsession = sqlsession;
        this.mapperXMLName = mapperXMLName;
    }


    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MapperBean readMapper = configuration.readMapper(mapperXMLName);
        if (!method.getDeclaringClass().getName().equals(readMapper.getInterfaceName())) {
            return null;
        }

        List<Function> list = readMapper.getList();
        if (null != list || 0 != list.size()) {
            for (Function function : list) {
                if (method.getName().equals(function.getFuncName())) {
                    if(function.getSqltype().equals("select")){
                        if(method.getReturnType().equals(List.class)){
                            if(args==null){
                                return sqlsession.selectList(function.getSql(), null, function.getReusltObjectName());
                            }else{
                                return sqlsession.selectList(function.getSql(), String.valueOf(args[0]), function.getReusltObjectName());
                            }
                        }else{
                            return sqlsession.selectOne(function.getSql(), String.valueOf(args[0]), function.getReusltObjectName());
                        }
                    }else if(function.getSqltype().equals("update")){
                        return sqlsession.update(function.getSql(),function.getParameterType(),args[0]);
                    }else if(function.getSqltype().equals("delete")){
                        return sqlsession.delete(function.getSql(),function.getParameterType(),args[0]);
                    }else if(function.getSqltype().equals("insert")){
                        return sqlsession.insert(function.getSql(),function.getParameterType(),args[0]);
                    }
                }
            }
        }
        return null;
    }
}
