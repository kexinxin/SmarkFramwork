package org.smart4j.project.service;

import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.annotation.Transaction;
import org.smart4j.framework.sqlSession.Sqlsession;
import org.smart4j.project.mapper.CustomerMapper;
import org.smart4j.project.model.Customer;

import java.util.List;
import java.util.Map;

/**
 * 提供客户数据服务
 */
@Service
public class CustomerMyBatisService {

    @Inject
    private Sqlsession sqlsession;
    /**
     * 获取客户列表
     */
    public List<Customer> getCustomerList() {
        CustomerMapper customerMapper=sqlsession.getMapper("CustomerMapper.xml",CustomerMapper.class);
        return customerMapper.getCustomerList();
    }

    /**
     * 获取客户
     */
    public Customer getCustomer(String id) {
        CustomerMapper customerMapper=sqlsession.getMapper("CustomerMapper.xml",CustomerMapper.class);
        return customerMapper.getCustomerById(id);
    }

    /**
     * 创建客户
     */
    @Transaction
    public boolean createCustomer(Customer customer) {
        CustomerMapper customerMapper=sqlsession.getMapper("CustomerMapper.xml",CustomerMapper.class);
        return customerMapper.createCustomer(customer);
    }

    /**
     * 更新客户
     */
    @Transaction
    public boolean updateCustomer(Customer customer) {
        CustomerMapper customerMapper=sqlsession.getMapper("CustomerMapper.xml",CustomerMapper.class);
        return customerMapper.updateCustomer(customer);
    }

    /**
     * 删除客户
     */
    @Transaction
    public boolean deleteCustomer(Customer customer) {
        CustomerMapper customerMapper=sqlsession.getMapper("CustomerMapper.xml",CustomerMapper.class);
        return customerMapper.deleteCustomer(customer);
    }
}
