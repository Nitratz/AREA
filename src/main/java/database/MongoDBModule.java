package database;

import area.Application;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import modules.IModules;
import org.apache.catalina.core.AprLifecycleListener;
import org.bson.Document;
import tools.ModulesTools;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by quent on 06/01/2017.
 */
public class MongoDBModule {

    public static List<Document> getModules() {
        MongoDatabase database = Application.mongoClient.getDatabase("Area");
        MongoCollection<Document> collection = database.getCollection("Modules");
        com.mongodb.client.FindIterable<Document> cursor = collection.find();
        List<Document> lol = new ArrayList<>();
        for (Document item : cursor) {
            lol.add(item);
        }
        return lol;
    }

    public static void LoadAllModules() {
        MongoDatabase database = Application.mongoClient.getDatabase("Area");
        MongoCollection<Document> collection = database.getCollection("Modules");
        com.mongodb.client.FindIterable<Document> cursor = collection.find();
        List<Document> lol = new ArrayList<>();
        for (Document item : cursor) {
            URL link = ModulesTools.getURL(item.getString("path"));
            IModules ret = ModulesTools.LoadModule(link, item.getString("name"));
            if (ret != null) {
                Application.lModules.add(ret);
            }
        }
    }

    public static void setModules(Document doc) {
        MongoDatabase database = Application.mongoClient.getDatabase("Area");
        MongoCollection<Document> collection = database.getCollection("Modules");
        collection.insertOne(doc);
    }

    public static void setModules(String name, String path) {
        Document doc = new Document();
        doc.put("path", path);
        doc.put("name", name);
        MongoDatabase database = Application.mongoClient.getDatabase("Area");
        MongoCollection<Document> collection = database.getCollection("Modules");
        collection.insertOne(doc);
    }

}
