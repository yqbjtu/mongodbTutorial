package com.yq.controller;

import com.alibaba.fastjson.JSONObject;
import com.yq.dao.UserRepository;
import com.yq.domain.QUser;
import com.yq.domain.User;
import com.yq.service.ICustomerService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import com.querydsl.core.types.Predicate;

/**
 * Simple to Introduction
 * className: MongoReadWriteController
 *
 * @author yqbjtu
 * @version 2018/4/27 8:57
 */
@RestController
@RequestMapping(value = "/mongo")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ICustomerService customerSvc;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @ApiOperation(value = "usersByName userRepository built-in", tags="tag1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", defaultValue = "Tom Hanks", value = "name", required = true, dataType = "string", paramType = "query")
    })
    @GetMapping(value = "/usersByName", produces = "application/json;charset=UTF-8")
    public String usersByName(@RequestParam String name) {
        logger.info("Enter customersByFirstName name={}", name);

        QUser qUser = new QUser("user");
        Predicate predicate = (Predicate) qUser.name.eq(name);

        Iterable<User> users = userRepository.findAll(predicate);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("currentTime", LocalDateTime.now().toString());
        jsonObj.put("totalCount", users.toString());
        jsonObj.put("docList", users);
        return jsonObj.toJSONString();
    }

    @ApiOperation(value = "usersByNameAndAge ", tags="tag1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", defaultValue = "Tom Hanks", value = "name", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "age", defaultValue = "35", value = "age", required = true, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/usersByNameAndAge", produces = "application/json;charset=UTF-8")
    public String usersByNameAndAge(@RequestParam String name, @RequestParam Integer age) {
        logger.info("Enter customersByFirstName name={}", name);

        QUser qUser = new QUser("user");
        Predicate nameAndAge = qUser.name.eq(name).and(qUser.age.eq(age));
        Iterable<User> users = userRepository.findAll(nameAndAge);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("currentTime", LocalDateTime.now().toString());
        jsonObj.put("totalCount", users.toString());
        jsonObj.put("docList", users);
        return jsonObj.toJSONString();
    }

    @ApiOperation(value = "usersByAgeBetween ", tags="tag1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "minAge", defaultValue = "20", value = "minAge", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "maxAge", defaultValue = "40", value = "maxAge", required = true, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/usersByAgeBetween", produces = "application/json;charset=UTF-8")
    public String usersByAgeBetween(@RequestParam Integer minAge, @RequestParam Integer maxAge) {
        logger.info("Enter usersByAgeBetween minAge={}, maxAge={}", minAge, maxAge);

        QUser qUser = new QUser("user");
        Predicate predicate = qUser.age.between(minAge, maxAge);
        Iterable<User> users = userRepository.findAll(predicate);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("currentTime", LocalDateTime.now().toString());
        jsonObj.put("totalCount", users.toString());
        jsonObj.put("docList", users);
        return jsonObj.toJSONString();
    }

    /*
    Pageable pageable = PageRequest.of(0,5);
    Page<SysOrg> orgList = userRepository.findAll(predicate,pageable);

    Predicate predicate = qSysOrg.orgCode.startsWith("tx");
OrderSpecifier orderSpecifier = qSysOrg.orgCode.asc();

List<SysOrg> orgList = (List<SysOrg>)userRepository.findAll(predicate,orderSpecifier);
SysOrg org = (SysOrg) orgList.get(0);
     */
    @ApiOperation(value = "delete all users", tags="tag1")
    @DeleteMapping(value = "/users", produces = "application/json;charset=UTF-8")
    public String deleteAll() {
        logger.info("Enter deleteAll");
        userRepository.deleteAll();

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("currentTime", LocalDateTime.now().toString());
        jsonObj.put("deleteAll", "ok");
        return jsonObj.toJSONString();
    }

    @ApiOperation(value = "find all user", tags="tag1")
    @GetMapping(value = "/users", produces = "application/json;charset=UTF-8")
    public String findAll() {
        logger.info("Enter findAll");
        List<User>  userList = userRepository.findAll();

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("currentTime", LocalDateTime.now().toString());
        jsonObj.put("totalCount", userList.size());
        jsonObj.put("All docList", userList);
        return jsonObj.toJSONString();
    }

    @ApiOperation(value = "usersInitData", tags="tag1")
    @PostMapping(value = "/usersInitData", produces = "application/json;charset=UTF-8")
    public String insertInitData() {
        logger.info("Enter insertInitData");
        User user1 = new User("Keanu Reeves", 20);
        userRepository.save(user1);

        User user2 = new User("Micheal Jackson", 25);
        userRepository.save(user2);

        User user3 = new User("Tom Hanks", 35);
        userRepository.save(user3);

        User user4 = new User("Tom Cruse", 50);
        userRepository.save(user4);

        List<User> userList = userRepository.findAll();
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("currentTime", LocalDateTime.now().toString());
        jsonObj.put("totalCount", userList.size());
        jsonObj.put("All docList", userList);
        return jsonObj.toJSONString();
    }

}
