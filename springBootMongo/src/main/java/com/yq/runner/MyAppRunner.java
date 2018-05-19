

package com.yq.runner;

import com.yq.dao.CustomerRepository;
import com.yq.domain.Customer;
import com.yq.domain.User;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Update.update;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Date;
import java.util.List;

@Component
@Order(value = 1)
public class MyAppRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(MyAppRunner.class);

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void run(ApplicationArguments arg0) throws Exception {
        String[] args = arg0.getSourceArgs();
        String string = String.join(",", args);
        log.info("my second apprunner. order is 2. AppArgs:" + string);
        //repository.deleteAll();
        MongoOperations mongoOps = mongoTemplate;

        Customer tom = new Customer("Tom", "Futami");

        // Insert is used to initially store the object into the database.
        mongoOps.insert(tom);
        log.info("Insert: " + tom);

        // Find
        tom = mongoOps.findById(tom.getId(), Customer.class);
        log.info("Found: " + tom);

        // Update
        mongoOps.updateFirst(query(where("firstName").is("Tom")), update("lastName", "Futami1"), Customer.class);
        tom = mongoOps.findOne(query(where("firstName").is("Tom")), Customer.class);
        log.info("Updated: " + tom);

        // Delete
        mongoOps.remove(tom);

        // Check that deletion worked
        List<Customer> customers = (List) mongoOps.findAll(Customer.class);
        log.info("Number of customers = : " + customers.size());

        mongoOps.dropCollection(Customer.class);

        // save a couple of customers
        Customer alice = repository.save(new Customer("Alice", "Smith"));
        Customer bob = repository.save(new Customer("Bob", "Smith"));

        if (alice != null) {
            System.out.println("Alice:" + alice.toString());
        }
        if (bob != null) {
            System.out.println("Bob:" + bob.toString());
        }

        // fetch all customers
        System.out.println("Customers found with findAll():");
        System.out.println("-------------------------------");
        for (Customer customer : repository.findAll()) {
            System.out.println(customer);
        }
        System.out.println();

        // fetch an individual customer
        System.out.println("Customer found with findByFirstName('Alice'):");
        System.out.println("--------------------------------");
        System.out.println(repository.findByFirstName("Alice"));

        System.out.println("Customers found with findByLastName('Smith'):");
        System.out.println("--------------------------------");
        for (Customer customer : repository.findByLastName("Smith")) {
            System.out.println(customer);
        }

        savePerson();

        //Document msg, String collectionName
        Document document = new Document();
        document.put("userId","u001");
        document.put("devId","dev001");
        document.put("timestamp",new Date());
        document.put("desc","my mongo insert testing");
        String collectionName = "AuditCol";
        mongoTemplate.insert(document, collectionName);
    }

    private void savePerson() {
        MongoOperations mongoOps = mongoTemplate;

        User user = new User("ZhangSan", 24);

        // Insert is used to initially store the object into the database.
        mongoOps.insert(user);
        log.info("Insert: " + user);

        // Find
        user = mongoOps.findById(user.getId(), User.class);
        log.info("FoundZhangSan: " + user);

        user = new User("LiSi", 50);
        mongoOps.insert(user, "person2");
        log.info("Insert: " + user);

        // Find
        User personLiSi1 = mongoOps.findById(user.getId(), User.class);
        log.info("Found LiSi by default collection: " + personLiSi1);

        User personLiSi2 = mongoOps.findById(user.getId(), User.class, "person2");
        log.info("Found LiSi by collection person2: " + personLiSi2);
    }


}
