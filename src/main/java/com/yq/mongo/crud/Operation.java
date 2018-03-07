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

    public Operation() {
    }

    public void insert(MongoCollection<Document> coll, Document document) {
        if (coll != null) {
            log.info("Insert a new Document to collection '" + coll.getNamespace().getFullName()+ "'.");
            coll.insertOne(document);
        }
        else {
            log.error("there is no valid collection");
        }
    }

    public List<String> getAllDocs(MongoCollection<Document> coll) {
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

    public Document updateById(MongoCollection<Document> coll, String id, Document newdoc) {
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

    public int deleteById(MongoCollection<Document> coll, String id) {
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

    public Document findById(MongoCollection<Document> coll, String id) {
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
