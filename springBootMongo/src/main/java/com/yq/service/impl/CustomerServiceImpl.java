
package com.yq.service.impl;

import com.yq.dao.CustomerRepository;
import com.yq.domain.Customer;
import com.yq.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Simple to Introduction
 * className: CustomerServiceImpl
 *
 * @author EricYang
 * @version 2018/5/19 15:15
 */

@Service
public class CustomerServiceImpl implements ICustomerService {
    @Autowired
    private CustomerRepository repository;

    @Override
    public List<Customer> findByFirstName(String firstName) {

        return repository.findByFirstName(firstName);
    }

    @Override
    public List<Customer> findByLastName(String lastName) {
        return repository.findByLastName(lastName);
    }

    @Override
    public List<Customer> findByAgeBetween(int ageGT, int ageLT) {
        return repository.findByAgeBetween(ageGT, ageLT);
    }
}
