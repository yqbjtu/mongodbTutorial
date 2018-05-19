

package com.yq.service;

import com.yq.domain.Customer;

import java.util.List;

/**
 * Simple to Introduction
 * className: ICustomerService
 *
 * @author EricYang
 * @version 2018/5/19 15:14
 */
public interface ICustomerService {
    public List<Customer> findByFirstName(String firstName);
    public List<Customer> findByLastName(String lastName);
    public List<Customer> findByAgeBetween(int ageGT, int ageLT);
}