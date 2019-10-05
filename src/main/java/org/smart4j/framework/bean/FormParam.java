package org.smart4j.framework.bean;

/**
 * 封装表单参数
 *
 * @author huangyong
 * @since 1.0.0
 */
public class FormParam {

    private String fieldName;
    private Object fieldValue;

    public FormParam(String fieldName, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
