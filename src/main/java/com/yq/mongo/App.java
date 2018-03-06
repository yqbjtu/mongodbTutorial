package com.yq.mongo;

import java.util.List;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.yq.mongo.config.DBConfig;
import com.yq.mongo.conn.MongoDBConn;
import com.yq.mongo.crud.Operation;

/**
 * Hello world!
 *
 */
public class App {
    //**************************************************************************
    // CLASS
    //**************************************************************************
    private static final Logger log = Logger.getLogger(App.class);

    public static void main( String[] args ) {
        MongoDBConn conn = new MongoDBConn(DBConfig.getInstance());
        String collectionName = "col1";
        MongoCollection<Document> coll = conn.getAllCollections(collectionName);
        if (coll != null) {
            Operation operation = new Operation(coll);
            operation.insert();
            List<String> idList = operation.getAllDocs();
            //we insert a doc, so idList has 1 element at least.
            String id = idList.get(0);
            Document doc = operation.findById(id);
            System.out.println("Find by id '" + id + ". Result:" + doc);

            Document newDoc = new Document("title", "JavaSE Core 9 ").
            //原来有title， 我们相当于修改了title， 原来没有的sales volume，相当于添加老的新的field。
            append("sales volume", "3000").
            append("price", 100).
            append("Author", "ericYang");
            operation.updateById(id, newDoc);
            doc = operation.findById(id);
            System.out.println("Find by id '" + id + " after updating. Result:" + doc);

            int count = operation.deleteById(id);
            System.out.println("delete by id '" + id + "'. AffectedRowCount:" + count);

            System.out.println("Show all docs");
            operation.getAllDocs();

            conn.closeMongoClient();
        }
        else {
            log.info("Can't find collection '" + collectionName + "'.");
        }
    }
}