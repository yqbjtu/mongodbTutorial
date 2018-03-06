package com.yq.mongo.conn;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
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

            //连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址  
            //ServerAddress()两个参数分别为 服务器地址 和 端口  
            ServerAddress serverAddress = new ServerAddress(host,Integer.valueOf(port));
            List<ServerAddress> addrs = new ArrayList<ServerAddress>();
            addrs.add(serverAddress);

            //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码 
            String dbName = dbCfg.getConfiguration(DBConstants.DB_NAME);
            String dbUserName = dbCfg.getConfiguration(DBConstants.DB_USERNAME);
            String dbPassword = dbCfg.getConfiguration(DBConstants.DB_PASSWORD);
            MongoCredential credential =
                MongoCredential.createScramSha1Credential(dbUserName, dbName, dbPassword.toCharArray());  
            List<MongoCredential> credentials = new ArrayList<MongoCredential>();  
            credentials.add(credential);  

            //通过连接认证获取MongoDB连接  
            mongoClient = new MongoClient(addrs,credentials);  

            // 连接到数据库
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

    public MongoCollection<Document> getCollection(String collectionName) {
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
