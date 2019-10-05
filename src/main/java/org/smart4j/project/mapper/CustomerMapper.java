package org.smart4j.project.mapper;


import org.smart4j.project.model.Customer;

import java.util.List;

/**
 * @author kexinxin
 **/
public interface CustomerMapper {
    Customer getCustomerById(String id);
    List<Customer> getCustomerList();
    boolean createCustomer(Customer customer);
    boolean updateCustomer(Customer customer);
    boolean deleteCustomer(Customer customer);
}
