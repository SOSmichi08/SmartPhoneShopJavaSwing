package dbConnection;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

//database access
public class DatabaseAccess {
    //URI to connect to the database
    private static final String URI = "mongodb://localhost:27017";
    private static final String DB_NAME = "smartphone_shop";
    private static MongoClient client;
    private static MongoDatabase database;

    //get database
    public static MongoDatabase getDatabase() {
        if (database == null) {
            client = MongoClients.create(URI);
            database = client.getDatabase(DB_NAME);
        }
        return database;
    }
}

