package com.yq.mongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
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
        MongoCollection<Document> coll = conn.getCollection(collectionName);
        if (coll != null) {
            Operation operation = new Operation();

            Document document = new Document("title", "Java Core v9")
                .append("description", "guide for java9")
                .append("price", 100.65)
                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
                .append("isPublished", true)
                .append("Author", "ericYang");

            List<Document> docList = new ArrayList<Document>();
            for (int i=0; i < 2; i++) {
                Document doc = new Document("id", i)
                    .append("description", "doc array demo")
                    .append("text", "test only");
                docList.add(doc);
            }
            document.append("docArray", docList);
            operation.insert(coll, document);

            List<String> idList = operation.getAllDocs(coll);
            //we insert a doc, so idList has 1 element at least.
            String id = idList.get(0);
            Document doc = operation.findById(coll, id);
            System.out.println("Find by id '" + id + ". Result:" + doc);

            Document newDoc = new Document("title", "JavaSE Core 9 ").
            //原来有title， 我们相当于修改了title， 原来没有的sales volume，相当于添加新的field。
                append("sales volume", 3000).
                append("price", 100).
                append("Author", "ericYang");
            operation.updateById(coll, id, newDoc);
            doc = operation.findById(coll, id);
            System.out.println("Find by id '" + id + " after updating. Result:" + doc);

            int count = operation.deleteById(coll, id);
            System.out.println("delete by id '" + id + "'. AffectedRowCount:" + count);

            System.out.println("Show all docs");
            operation.getAllDocs(coll);

            log.info("query specific fields");
            Document fields = new Document("title", 1)
                                  .append("price", 2);
            operation.querySpecifiedFields(coll, fields);

            App app = new App();
            app.insertMany(coll);
            app.queryAndSort(coll);

            System.out.println("Show all docs");
            operation.getAllDocs(coll);
            app.deleteMany(coll);
            conn.closeMongoClient();
        }
        else {
            log.info("Can't find collection '" + collectionName + "'.");
        }
    }

    public void insertMany(MongoCollection<Document> coll) {
        List<Document> docList = new ArrayList<Document>();
        for (int i=0; i < 10; i++) {
            Document doc = new Document("id", i)
                .append("description", "doc demo")
                .append("index", 100 -i)
                .append("price", i + 10);
            docList.add(doc);
        }
        coll.insertMany(docList);
    }

    public void deleteMany(MongoCollection<Document> coll) {
        Bson filter = Filters.gte("id", 0);
        DeleteResult deleteResult = coll.deleteMany(filter);
        System.out.println("delete " + deleteResult.getDeletedCount() + " document(s).");
    }

    public void queryAndSort(MongoCollection<Document> coll) {
       Document findQuery = new Document("id", new Document("$gte",6));
       Document orderBy = new Document("price", 1);

       System.out.println("order by price");
       MongoCursor<Document> cursor = coll.find(findQuery).sort(orderBy).iterator();
       try {
           while (cursor.hasNext()) {
               Document doc = cursor.next();
               System.out.println(
                   "Id " + doc.get("id") + ", price:" + doc.get("price") + " ."
               );
           }
       } finally {
           cursor.close();
       }
    }
}
