
package com.yq.dao;

import com.yq.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Predicate;

/**
 * Simple to Introduction
 * className: UserRepository
 *
 * @author EricYang
 * @version 2018/5/19 22:57
 */
@Repository
public interface UserRepository extends MongoRepository<User, String>, QueryDslPredicateExecutor<User> {

    //public Iterable<User> findAll(Predicate predicate);
}
