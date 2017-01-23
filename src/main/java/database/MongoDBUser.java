package database;

import area.Application;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class MongoDBUser implements IUser {

    private Document userdoc;
    private String Token;

    private MongoDBUser() {

    }

    public static List<Document> getUsers() {
        MongoDatabase database = Application.mongoClient.getDatabase("Area");
        MongoCollection<Document> collection = database.getCollection("Users");
        com.mongodb.client.FindIterable<Document> cursor = collection.find();
        List<Document> lol = new ArrayList<>();
        for (Document item : cursor) {
            lol.add(item);
        }
        return lol;
    }

    public static MongoDBUser getUser(String email, String password) throws Exception {
        MongoDBUser ret = new MongoDBUser();
        MongoDatabase database = Application.mongoClient.getDatabase("Area");
        MongoCollection<Document> collection = database.getCollection("Users");
        Document doc = collection.find(
                Filters.and(Filters.eq("email", email),
                        Filters.eq("password", password))).first();
        MessageDigest digest = MessageDigest.getInstance("SHA-1");

        if (doc == null) {
            throw new Exception("Authentication Failed");
        }

        ret.userdoc = doc;
        digest.update((email + "_" + ObjectId.get().toString()).getBytes("utf8"));
        byte[] digestBytes = digest.digest();
        ret.Token = javax.xml.bind.DatatypeConverter.printHexBinary(digestBytes);
        return ret;
    }

    public static MongoDBUser createUser(String email, String password, String FullName) throws Exception {
        MongoDBUser ret = new MongoDBUser();
        MongoDatabase database = Application.mongoClient.getDatabase("Area");
        MongoCollection<Document> collection = database.getCollection("Users");
        ObjectId id = new ObjectId();
        JSONObject userobj = new JSONObject();

        FindIterable<Document> result = collection.find(Filters.eq("email", email));
        if (result.first() != null) {
            throw new Exception("User Already exist");
        }

        userobj.put("email", email)
                .put("password", password)
                .put("fullname", FullName)
                .put("Services", new JSONObject())
                .put("Areas", new JSONArray())
                .put("UserID", id.toString());
        ret.userdoc = Document.parse(userobj.toString());
        collection.insertOne(ret.userdoc);
        return ret;
    }

    @Override
    public String getName() {
        return userdoc.getString("fullname");
    }

    @Override
    public String getServices() {
        return userdoc.get("Services", org.bson.Document.class).toJson();
    }

    @Override
    public String getToken() {
        return Token;
    }

    @Override
    public String getEmail() {
        return userdoc.getString("email");
    }

    @Override
    public boolean setService(String service, String Service_Name) {
        MongoDatabase database = Application.mongoClient.getDatabase("Area");
        MongoCollection<Document> collection = database.getCollection("Users");
        org.bson.Document JService = org.bson.Document.parse(service);
        org.bson.Document JServices = userdoc.get("Services", org.bson.Document.class);
        JServices.put(Service_Name, JService);
        userdoc.put("Services", JServices);
        collection.replaceOne(Filters.eq("UserID", userdoc.getString("UserID")), userdoc);
        return true;
    }

    @Override
    public boolean addArea(String Area) {
        MongoDatabase database = Application.mongoClient.getDatabase("Area");
        MongoCollection<Document> collection = database.getCollection("Users");
        org.bson.Document JArea = org.bson.Document.parse(Area);
        List<Document> JAreas = userdoc.get("Areas", List.class);
        JAreas.add(JArea);
        userdoc.put("Areas", JAreas);
        userdoc.remove("_id");
        collection.replaceOne(Filters.eq("UserID", userdoc.getString("UserID")), userdoc);
        return true;
    }

    @Override
    public String getAreas() {
        JSONArray array = new JSONArray();
        List<Document> l = userdoc.get("Areas", List.class);
        for (Document d : l) {
            array.put(new JSONObject(d.toJson()));
        }
        return (array.toString());
    }

    @Override
    public void DeleteArea(String Area) {
        MongoDatabase database = Application.mongoClient.getDatabase("Area");
        MongoCollection<Document> collection = database.getCollection("Users");
        List<Document> JAreas = userdoc.get("Areas", List.class);
        for (int i = 0; i < JAreas.size(); i++) {
            if (JAreas.get(i).getString("ID").equals(Area)) {
                JAreas.remove(i);
                i--;
            }
        }
        userdoc.put("Areas", JAreas);
        collection.replaceOne(Filters.eq("UserID", userdoc.getString("UserID")), userdoc);
    }

}
