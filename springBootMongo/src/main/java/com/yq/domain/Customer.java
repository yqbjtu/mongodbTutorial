
package com.yq.domain;

/**
 * Simple to Introduction
 * className: Customer
 *
 * @author EricYang
 * @version 2018/5/13 19:14
 */
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Customer {

    @Id
    private String id;

    private String firstName;
    private String lastName;
    private int age;
    private long memberPoints;

    public Customer() {}

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%s, firstName='%s', lastName='%s',age='%d',memberPoints='%d']",
                id, firstName, lastName, age, memberPoints);
    }

    public String getId() {
        return id;
    }

}
