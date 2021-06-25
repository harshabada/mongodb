package com.example.demo;

import com.mongodb.client.*;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.InsertOneModel;
import org.bson.Document;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@RestController
public class mongoController {

    @RequestMapping(method = RequestMethod.GET, path = "/connect")
    public String connectmongo() {


        ConnectionString connString = new ConnectionString(
                "mongodb+srv://harsha:harsha21@cluster0.1w5wb.mongodb.net/test?retryWrites=true&w=majority"
        );
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .retryWrites(true)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
         MongoDatabase database = mongoClient.getDatabase("college");
        MongoCollection<Document> collection = database.getCollection("students");


//        Object obj = null;
//        try {
//             obj = parser.parse(new FileReader("/Users/User/Desktop/course.json"));
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }
//        JSONObject jsonObject = (JSONObject)obj;
//        MongoDatabase database = mongoClient.getDatabase("college");
//        Document document = new Document();
//        document.append("name", "Ram");
//        document.append("age", 26);
//        document.append("city", "Hyderabad");
//        database.createCollection("students");
//        database.getCollection("students").insertOne(document);

//        MongoCollection<Document> collection = database.getCollection("listingsAndReviews");


//        FindIterable<Document> iterDoc = collection.find();
//        Iterator it = iterDoc.iterator();
//        while (it.hasNext()) {
//            System.out.println(it.next());
//        }

        int count = 0;
        int batch = 100;

        List<InsertOneModel<Document>> docs = new ArrayList<>();

        try  {
            BufferedReader br = new BufferedReader(new FileReader("/Users/hbada/github/demo/src/main/java/com/example/demo/data1.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                docs.add(new InsertOneModel<>(Document.parse(line)));
                count++;
                if (count == batch) {
                    collection.bulkWrite(docs, new BulkWriteOptions().ordered(false));
                    docs.clear();
                    count = 0;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        if (count > 0) {
            collection.bulkWrite(docs, new BulkWriteOptions().ordered(false));
        }
        return "done";
    }
}
