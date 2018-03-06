package com.yq.mongo.crud;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

public class Operation {
    //**************************************************************************
    // CLASS
    //**************************************************************************
    private static final Logger log = Logger.getLogger(Operation.class);
    private MongoCollection<Document> coll = null;

    public Operation(MongoCollection<Document> collection) {
        this.coll = collection;
    }

    public void insert() {
        if (coll != null) {
            log.info("Insert a new Document to collection '" + coll.getNamespace().getFullName()+ "'.");
            Document document = new Document("title", "Java Core v9").
            append("description", "guide for java9").
            append("price", 100).
            append("Author", "ericYang");
            List<Document> documents = new ArrayList<Document>();
            documents.add(document);
            coll.insertMany(documents);
        }
        else {
            log.error("there is no valid collection");
        }
    }

    public List<String> getAllDocs() {
        /*检索所有文档  
        /** 
        * 1. 获取迭代器FindIterable<Document> 
        * 2. 获取游标MongoCursor<Document> 
        * 3. 通过游标遍历检索出的文档集合 
        * */
        List<String> idList = new ArrayList<String>();
        if (coll != null) {
            FindIterable<Document> findIterable = coll.find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();

            while(mongoCursor.hasNext()){
                Document doc = mongoCursor.next();
                System.out.println(doc);
                ObjectId id =doc.getObjectId("_id");  
                idList.add(id.toString());
            }
            log.info("There are " + coll.count() + " in collection '" + coll.getNamespace().getFullName()+ "'.");
        }

        return idList;
    }

    public Document updateById(String id, Document newdoc) {
        ObjectId _idobj = null;
        try {
            _idobj = new ObjectId(id);
        } catch (Exception e) {
            return null;
        }

        Bson filter = Filters.eq("_id", _idobj);
        coll.updateOne(filter, new Document("$set", newdoc));
        return newdoc;
    }

    public int deleteById(String id) {
        int count = 0;
        ObjectId _id = null;
        try {
            _id = new ObjectId(id);
        }
        catch (Exception e) {
            return 0;
        }
        Bson filter = Filters.eq("_id", _id);
        DeleteResult deleteResult = coll.deleteOne(filter);
        count = (int)deleteResult.getDeletedCount();
        return count;
    }

    public Document findById(String id) {
        ObjectId _idobj = null;
        try {
            _idobj = new ObjectId(id);
        }
        catch (Exception e) {
            return null;
        }
        Document myDoc = coll.find(Filters.eq("_id", _idobj)).first();
        return myDoc;
    }
}
