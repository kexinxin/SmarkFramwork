package org.smart4j.framework.sqlSession;

import java.lang.reflect.InvocationTargetException;

/**
 * @author kexinxin
 **/
public interface Excutor {
    <T> T query(String statement, Object parameter, String resultTypeName);
    <T> T queryList(String statement, Object parameter, String resultTypeName);
    public boolean update(String statement, String parameterType, Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
