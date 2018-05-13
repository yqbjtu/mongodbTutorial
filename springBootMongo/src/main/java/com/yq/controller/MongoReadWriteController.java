package com.yq.controller;

import com.alibaba.fastjson.JSONObject;
import com.yq.dao.CustomerRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Simple to Introduction
 * className: MongoReadWriteController
 *
 * @author yqbjtu
 * @version 2018/4/27 8:57
 */
@RestController
@RequestMapping(value = "/mongo")
public class MongoReadWriteController {
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final Logger logger = LoggerFactory.getLogger(MongoReadWriteController.class);

    @ApiOperation(value = "write")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "collectionName", value = "col01", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "content", required = true, dataType = "string", paramType = "query")
    })
    @PostMapping(value = "/write", produces = "application/json;charset=UTF-8")
    public String writeDocument(@RequestParam String collectionName, @RequestParam String content) {
        logger.info("enter writeDocument, collectionName={}, conent={}", collectionName, content);

        Document document = new Document();
        document.put("userId","u001");
        document.put("devId","dev001");
        document.put("timestamp",new Date());
        document.put("desc","my mongo insert testing");
        document.put("content", content);

        mongoTemplate.insert(document, collectionName);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("curentTime", LocalDateTime.now().toString());
        return jsonObj.toJSONString();
    }

    @ApiOperation(value = "query")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "collectionName",  value = "collectionName", defaultValue = "col01", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "count", value = "发送多少遍", defaultValue = "1", required = true, dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "jsonStr", value = "jsonStr", defaultValue = "jsonStr", required = true, dataType = "string", paramType = "body")
    })
    @GetMapping(value = "/devJsonMsg", produces = "application/json;charset=UTF-8")
    public String queryMongo(@RequestParam String collectionName, @RequestParam int count, @RequestBody String jsonStr) {
        //mongoTemplate.getCollection().
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("curentTime", LocalDateTime.now().toString());
        return jsonObj.toJSONString();
    }


}
