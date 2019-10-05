package org.smart4j.framework.sqlSession;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kexinxin
 **/
public class Myexcutor implements Excutor {
    private Configuration xmlConfiguration = new Configuration();

    public <T> T query(String sql, Object parameter, String reulstTypeName) {
        Connection connection = getConnection();
        ResultSet resultset = null;
        PreparedStatement pre = null;
        try {
            if(sql.contains("?")){
                sql=sql.replace("?","'"+parameter.toString()+"'");
            }
            pre = connection.prepareStatement(sql);
            resultset = pre.executeQuery();
            Class pojo =  Class.forName(reulstTypeName);
            Object pojoObj=pojo.newInstance();
            //遍历pojo中每一个属性域去设置参数
            if(resultset.next()){
                for (Field field : pojoObj.getClass().getDeclaredFields()) {
                    setValue(pojoObj, field, resultset);
                }
            }
            return (T) pojoObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T queryList(String sql, Object parameter, String resultTypeName) {
        Connection connection = getConnection();
        ResultSet resultset = null;
        PreparedStatement pre = null;
        try {
            if(sql.contains("?")){
                sql.replace("?","'"+parameter.toString()+"'");
            }
            pre = connection.prepareStatement(sql);
            resultset = pre.executeQuery();
            Class pojo =  Class.forName(resultTypeName);
            List<Object> list=new ArrayList<Object>();
            //遍历pojo中每一个属性域去设置参数
            while(resultset.next()){
                Object pojoObj=pojo.newInstance();
                for (Field field : pojoObj.getClass().getDeclaredFields()) {
                    setValue(pojoObj, field, resultset);
                }
                list.add(pojoObj);
            }
            return (T) list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean update(String statement,String parameterType, Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Field field : object.getClass().getDeclaredFields()) {
            Method setMethod = object.getClass().getMethod("get" + firstWordCapital(field.getName()));
            String replaceHold="#{"+field.getName()+"}";
            if(setMethod.invoke(object)!=null){
                statement=statement.replace(replaceHold,"'"+setMethod.invoke(object).toString()+"'");
            }
        }
        Connection connection = getConnection();
        PreparedStatement pre = null;
        try {
            System.out.println(statement);
            pre = connection.prepareStatement(statement);
            int res = pre.executeUpdate();
            if(res>0){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    /**
     * 利用反射给每一个属性设置参数
     */
    private void setValue(Object pojo, Field field, ResultSet rs) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, SQLException {
        Method setMethod = pojo.getClass().getMethod("set" + firstWordCapital(field.getName()), field.getType());
        setMethod.invoke(pojo, getResult(rs, field));
    }
    /**
     * 根据反射判断类型，从ResultSet中取对应类型参数
     */
    private Object getResult(ResultSet rs, Field field) throws SQLException {
        //TODO 这里需要用TypeHandle处理，偷懒简化了，后续升级点
        Class type = field.getType();
        if (Integer.class == type) {
            return rs.getInt(field.getName());
        }
        if (String.class == type) {
            return rs.getString(field.getName());
        }
        return rs.getString(field.getName());
    }

    private String firstWordCapital(String word){
        String first = word.substring(0, 1);
        String tail = word.substring(1);
        return first.toUpperCase() + tail;
    }

    private Connection getConnection() {

        try {
            Connection connection = xmlConfiguration.build("config.xml");
            return connection;
        } catch (Exception e) {
            throw new RuntimeException("parse config.xml error");
        }
    }
}
