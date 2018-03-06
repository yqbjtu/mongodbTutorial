package com.yq.mongo.conn;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.yq.mongo.common.DBConstants;
import com.yq.mongo.config.DBConfig;

public class MongoDBConn {
    //**************************************************************************
    // CLASS
    //**************************************************************************
    private static final Logger log = Logger.getLogger(MongoDBConn.class);
    private MongoDatabase mongoDatabase = null;
    private MongoClient mongoClient = null;
    //private MongoCollection<Document>

    public MongoDBConn(DBConfig dbCfg) {
        try {
            // 连接到 mongodb 服务
            String host = dbCfg.getConfiguration(DBConstants.DB_HOST);
            String port = dbCfg.getConfiguration(DBConstants.DB_PORT);
            log.info("Will connect to mongo db by host" + host + " port" + port);
            mongoClient = new MongoClient(host, Integer.valueOf(port));

            // 连接到数据库
            String dbName = dbCfg.getConfiguration(DBConstants.DB_NAME);
            if (mongoClient != null) {
                mongoDatabase = mongoClient.getDatabase(dbName);
                log.info("Connect to database '" + dbName + "' successfully.");
            }
            else {
                log.error("Can't connect to mongo db by host" + host + " port" + port);
            }

        }
        catch (Exception e) {
            System.err.println("Connecting to database Failed. " + e.getMessage());
        }
    }

    public MongoDatabase getDB() {
        return mongoDatabase;
    }

    public MongoCollection<Document> getAllCollections(String collectionName) {
        MongoCollection<Document> coll = null;
        if (mongoDatabase != null) {
            coll = mongoDatabase.getCollection(collectionName);
        }

        return coll;
    }

    /**
    * 查询DB下的所有表名
    */
    public List<String> getAllCollectionNames() {
         MongoIterable<String> colls = mongoDatabase.listCollectionNames();
           List<String> _list = new ArrayList<String>();
          for (String s : colls) {
             _list.add(s);
          }
          return _list;
    }

    public MongoCollection<Document> createCollection(String collectionName) {
        MongoCollection<Document> coll = null;
        if (mongoDatabase != null) {
            coll = mongoDatabase.getCollection(collectionName);
            if (coll == null) {
                mongoDatabase.createCollection(collectionName);
                log.info("集合创建成功");
            }
        }

        return coll;
    }

    public void closeMongoClient() {
        if (mongoDatabase != null) {
            mongoDatabase = null;
        }
        if (mongoClient != null) {
            mongoClient.close();
        }
        log.info("CloseMongoClient successfully");  
    }

}
