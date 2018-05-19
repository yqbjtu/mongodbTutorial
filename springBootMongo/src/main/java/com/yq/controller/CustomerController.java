package com.yq.controller;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.yq.dao.CustomerRepository;
import com.yq.domain.Customer;
import com.yq.service.ICustomerService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Simple to Introduction
 * className: MongoReadWriteController
 *
 * @author yqbjtu
 * @version 2018/4/27 8:57
 */
@RestController
@RequestMapping(value = "/mongo")
public class CustomerController {

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private ICustomerService customerSvc;

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @ApiOperation(value = "save", tags="tag1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "firstName", defaultValue = "firstName", value = "firstName", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "lastName", defaultValue = "lastName", value = "lastName", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "age", defaultValue = "18", value = "age", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "memberPoints", defaultValue = "1000", value = "memberPoints", required = true, dataType = "long", paramType = "query")
    })
    @PostMapping(value = "/customers", produces = "application/json;charset=UTF-8")
    public String saveCustomer(@RequestParam String firstName, @RequestParam String lastName, @RequestParam Integer age, @RequestParam Long memberPoints) {
        logger.info("enter saveCustomer, firstName={}, lastName={}", firstName, lastName);

        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setAge(age);
        customer.setMemberPoints(memberPoints);

        customer = repository.save(customer);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("currentTime", LocalDateTime.now().toString());
        jsonObj.put("customer", customer);
        return jsonObj.toJSONString();
    }

    @ApiOperation(value = "query customers", tags="tag1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startAge", defaultValue = "18", value = "age", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "endAge", defaultValue = "28", value = "age", required = true, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/customers", produces = "application/json;charset=UTF-8")
    public String queryCustomers(
                             @RequestParam(required=false, defaultValue = "20") Integer startAge,
                             @RequestParam(required=false, defaultValue = "25") Integer endAge) {
        logger.info("Enter queryCustomers , startTime={},endTime={}", startAge, endAge);
        List<Customer>  customerList = repository.myfindCustomerByAgeBetween(startAge, endAge);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("currentTime", LocalDateTime.now().toString());
        jsonObj.put("totalCount", customerList.size());
        jsonObj.put("docList", customerList);
        return jsonObj.toJSONString();
    }

    @ApiOperation(value = "customersByFirstName repository built-in", tags="tag1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "firstName", defaultValue = "Tom", value = "firstName", required = true, dataType = "string", paramType = "query"),
    })
    @GetMapping(value = "/customersFindByFirstName", produces = "application/json;charset=UTF-8")
    public String customersByFirstName(@RequestParam String firstName) {
        logger.info("Enter customersByFirstName firstName={}", firstName);

        List<Customer>  customerList = repository.findByFirstName(firstName);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("currentTime", LocalDateTime.now().toString());
        jsonObj.put("totalCount", customerList.size());
        jsonObj.put("docList", customerList);
        return jsonObj.toJSONString();
    }

    @ApiOperation(value = "customersMyFindByFirstName repository with annotation query", tags="tag1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "firstName", defaultValue = "Tom", value = "firstName", required = true, dataType = "string", paramType = "query"),
    })
    @GetMapping(value = "/customersMyFindByFirstName", produces = "application/json;charset=UTF-8")
    public String customersMyFindByFirstName(@RequestParam String firstName) {
        logger.info("Enter customersByFirstName firstName={}", firstName);

        List<Customer>  customerList = repository.myfindCustomerByFirstName(firstName);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("currentTime", LocalDateTime.now().toString());
        jsonObj.put("totalCount", customerList.size());
        jsonObj.put("docList", customerList);
        return jsonObj.toJSONString();
    }

    @ApiOperation(value = "customersMyFindByRegexName repository with annotation query", tags="tag1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", defaultValue = "a", value = "firstName or lastName regex", required = true, dataType = "string", paramType = "query"),
    })
    @GetMapping(value = "/customersMyFindByRegexName", produces = "application/json;charset=UTF-8")
    public String customersMyFindByName(@RequestParam String name) {
        logger.info("Enter customersByFirstName name={}", name);
        List<Customer>  customerList = repository.myfindCustomerByRegexName(name);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("currentTime", LocalDateTime.now().toString());
        jsonObj.put("totalCount", customerList.size());
        jsonObj.put("docList", customerList);
        return jsonObj.toJSONString();
    }

    @ApiOperation(value = "findByFirstNameLikeOrderByAgeAsc repository with annotation query", tags="tag1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", defaultValue = "a", value = "firstName or lastName regex", required = true, dataType = "string", paramType = "query"),
    })
    @GetMapping(value = "/customersFindByFirstNameLikeOrderByAgeAsc", produces = "application/json;charset=UTF-8")
    public String findByFirstNameLikeOrderByAgeAsc(@RequestParam String name) {
        logger.info("Enter customersByFirstName name={}", name);
        List<Customer>  customerList = repository.findByFirstNameLikeOrderByAgeAsc(name);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("currentTime", LocalDateTime.now().toString());
        jsonObj.put("totalCount", customerList.size());
        jsonObj.put("docList", customerList);
        return jsonObj.toJSONString();
    }

    @ApiOperation(value = "delete all", tags="tag1")
    @DeleteMapping(value = "/customers", produces = "application/json;charset=UTF-8")
    public String deleteAll(@RequestParam String name) {
        logger.info("Enter customersByFirstName name={}", name);
        repository.deleteAll();

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("currentTime", LocalDateTime.now().toString());
        jsonObj.put("deleteAll", "ok");
        return jsonObj.toJSONString();
    }

    @ApiOperation(value = "find all", tags="tag1")
    @GetMapping(value = "/customersFindAll", produces = "application/json;charset=UTF-8")
    public String findAll() {
        logger.info("Enter findAll");
        List<Customer>  customerList = repository.findAll();

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("currentTime", LocalDateTime.now().toString());
        jsonObj.put("totalCount", customerList.size());
        jsonObj.put("All docList", customerList);
        return jsonObj.toJSONString();
    }

    @ApiOperation(value = "customersInitData", tags="tag1")
    @PostMapping(value = "/customersInitData", produces = "application/json;charset=UTF-8")
    public String insertInitData() {
        logger.info("Enter insertInitData");
        Customer customer1 = new Customer("Keanu", "Reeves");
        customer1.setAge(18);
        customer1.setMemberPoints(100);
        repository.save(customer1);

        Customer customer2 = new Customer("Micheal", "Jackson");
        customer2.setAge(25);
        customer2.setMemberPoints(1250);
        repository.save(customer2);

        Customer customer3 = new Customer("Tom", "Hanks");
        customer3.setAge(34);
        customer3.setMemberPoints(3400);
        repository.save(customer3);

        Customer customer4 = new Customer("Tom", "Cruse");
        customer4.setAge(50);
        customer4.setMemberPoints(5000);
        repository.save(customer4);

        List<Customer>  customerList = repository.findAll();

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("currentTime", LocalDateTime.now().toString());
        jsonObj.put("totalCount", customerList.size());
        jsonObj.put("All docList", customerList);
        return jsonObj.toJSONString();
    }

}
