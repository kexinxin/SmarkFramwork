package org.smart4j.framework.sqlSession;


import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.smart4j.framework.bean.Function;
import org.smart4j.framework.bean.MapperBean;
import org.smart4j.framework.util.ClassUtil;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author kexinxin
 * 解析xml文件
 **/
public class Configuration {

    //链接数据库的读取
    public Connection build(String resource) {
        try {
            InputStream stream = ClassUtil.getClassLoader().getResourceAsStream(resource);
            SAXReader reader = new SAXReader();
            Document document = reader.read(stream);
            Element root = document.getRootElement();
            return evalDataSource(root);
        } catch (Exception e) {
            throw new RuntimeException("error occured while evaling xml" + resource);
        }
    }

    public Connection evalDataSource(Element node) throws ClassNotFoundException {
        if (!node.getName().equals("database")) {
            System.out.println("数据库解析错误");
            throw new RuntimeException("root should be <database>");
        }

        String driverClassName = null;
        String url = null;
        String username = null;
        String password = null;
        for (Object item : node.elements("property")) {
            Element i = (Element) item;
            //判别 property的多种写法
            String value = getValue(i);
            String name = i.attributeValue("name");
            if (name == null || value == null) {
                throw new RuntimeException("[database]<property> should contain name or value");
            }

            if (name.equals("url")) {
                url = value;
            } else if (name.equals("username")) {
                username = value;
            } else if (name.equals("password")) {
                password = value;
            } else if (name.equals("driverClassName")) {
                driverClassName = value;
            } else {
                System.err.println("unknow property");
                throw new RuntimeException("unknow property");
            }
        }
        Class.forName(driverClassName);
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("database link error");
        }
        return connection;
    }

    //获取 property 属性的值，如果
    private String getValue(Element node) {
        return node.hasContent() ? node.getText() : node.attributeValue("value");
    }

    //    //读取我们的mapper文件
    @SuppressWarnings("rawtypes")
    public MapperBean readMapper(String path) {
        MapperBean mapper = new MapperBean();
        try {
            InputStream is = ClassUtil.getClassLoader().getResourceAsStream(path);
            SAXReader reader = new SAXReader();
            Document document = reader.read(is);
            Element root = document.getRootElement();
            mapper.setInterfaceName(root.attributeValue("nameSpace").trim());
            List<Function> list = new ArrayList<Function>();
            Iterator rootIterator = root.elementIterator();
            while (rootIterator.hasNext()) {
                Function fun = new Function();
                Element e = (Element) rootIterator.next();
                String sqltype = e.getName().trim();
                String funcName = e.attributeValue("id").trim();
                String sql = e.getText().trim();
                String reultType = e.attributeValue("resultType");
                if(reultType!=null) reultType.trim();
                String parameterType=e.attributeValue("parameterType");
                if(parameterType!=null) parameterType.trim();
                fun.setSqltype(sqltype);
                fun.setFuncName(funcName);
                fun.setResultObjectName(reultType);
                fun.setParameterType(parameterType);
                Object newIntance = null;
                fun.setSql(sql);
                list.add(fun);
            }
            mapper.setList(list);
        } catch (Exception e) {
             e.printStackTrace();
        }
        return mapper;
    }
}
