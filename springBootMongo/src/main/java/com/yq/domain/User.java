package com.yq.domain;


import com.mysema.query.annotations.QueryEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

/*
 * because we use typeAlias, so in the collection person, its data is like this. class is 'pers', not 'hello.entity.Customer.
 * > db.person.find()
{ "_id" : ObjectId("5a9677645b82ba3440e280c3"), "_class" : "pers", "name" : "ZhangSan", "age" : 24 }
>
 */
@TypeAlias("pers")
@Document
@QueryEntity
public class User {

    @Id
    private String id;

    private String name;
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", age=" + age + "]";
    }

}