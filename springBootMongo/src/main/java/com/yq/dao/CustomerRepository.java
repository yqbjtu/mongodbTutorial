
package com.yq.dao;

import com.yq.domain.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CustomerRepository extends MongoRepository<Customer, String> {

    public List<Customer> findByFirstName(String firstName);
    @Query("{ 'firstName' : ?0 }")
    List<Customer> myfindCustomerByFirstName(String firstName);

    public List<Customer> findByLastName(String lastName);
    @Query("{ 'lastName' : ?0 }")
    List<Customer> myfindCustomerByLastName(String firstName);

    //db.getCollection('customer').find({ $or:[ { 'firstName' : { $regex: 'o' } }, { 'lastName' : { $regex: 's' } }] })
    @Query("{ $or:[ { 'firstName' : { $regex: ?0 } }, { 'lastName' : { $regex: ?0 } }] }")
    List<Customer> myfindCustomerByRegexName(String regex);

    List<Customer> findByFirstNameLikeOrderByAgeAsc(String regex);

    public List<Customer> findByAgeBetween(int ageGT, int ageLT);
    @Query("{ 'age' : { $gt: ?0, $lt: ?1 } }")
    List<Customer> myfindCustomerByAgeBetween(int ageGT, int ageLT);

}
