package baseDadosMongo;

import java.io.FileInputStream;
import java.util.Properties;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class BaseDadosMongo {

	private MongoCollection<Document> collection;
	private String mongo_host = new String();
	private String mongo_database = new String();
	private String mongo_collection = new String();
	private MongoDatabase db;

	public BaseDadosMongo() {
		try {
			Properties p = new Properties();
			p.load(new FileInputStream("mongodb.ini/mongodb.ini"));
			mongo_host = p.getProperty("mongo_host");
			mongo_database = p.getProperty("mongo_database");
			mongo_collection = p.getProperty("mongo_collection");
		} catch (Exception e) {
			System.out.println("Erro: ficheiro.ini\n");
		}
	}

	public String getMongo_host() {
		return mongo_host;
	}

	public void estabelecerLigacao(MongoClient client) {
		db = client.getDatabase(mongo_database);
		collection = db.getCollection(mongo_collection);
	}

	public MongoCollection<Document> getCollection() {
		return collection;
	}

}