package recursosPartilhadosThreads;

import java.io.FileInputStream;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import lerDoMongo.LerMongo;

import com.mongodb.*;

public class MongoDataBase {

	private MongoCollection<Document> mongocol;
	private String mongo_host = new String();
	private String mongo_database = new String();
	private String mongo_collection = new String();
	private MongoDatabase db;

	public MongoDataBase() {

		try {
			Properties p = new Properties();
			p.load(new FileInputStream("src/main/java/lersensores/CloudToMongo.ini"));
			mongo_host = p.getProperty("mongo_host");
			mongo_database = p.getProperty("mongo_database");
			mongo_collection = p.getProperty("mongo_collection");
		} catch (Exception e) {

			System.out.println("Error reading CloudToMongo.ini file " + e);
			JOptionPane.showMessageDialog(null, "The CloudToMongo.inifile wasn't found.", "CloudToMongo",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public synchronized MongoCollection<Document> getMongocol() {
		return mongocol;
	}

	public synchronized void setMongocol(MongoCollection<Document> mongocol) {
		this.mongocol = mongocol;
	}

	public synchronized void addToMongo(Document doc) {
		mongocol.insertOne(doc);
		notifyAll();
	}

	public synchronized String getMongo_host() {
		return mongo_host;
	}

	public synchronized void setMongo_host(String mongo_host) {
		this.mongo_host = mongo_host;
	}

	public synchronized String getMongo_database() {
		return mongo_database;
	}

	public synchronized void setMongo_database(String mongo_database) {
		this.mongo_database = mongo_database;
	}

	public synchronized String getMongo_collection() {
		return mongo_collection;
	}

	public synchronized void setMongo_collection(String mongo_collection) {
		this.mongo_collection = mongo_collection;
	}

	public synchronized void establishConnection(MongoClient client) {
		db = client.getDatabase(mongo_database);
		mongocol = db.getCollection(mongo_collection);
	}

	// solucao mesmo para despachar, nao sei resolver de outra maneira
	public synchronized void readDataWriteMedicoes(LerMongo lermongo) {
		int ultimo = lermongo.getUltimaMedicaoLida();
		int indice = -1;
		Document next;
		MongoCursor<Document> cursor = mongocol.find().iterator();
		try {
			while (cursor.hasNext()) {
				indice++;
				next = cursor.next();
				if (indice > ultimo && indice < mongocol.count()) {
					String json = next.toJson();
					lermongo.validarMedicao(json);
				}
			}
			lermongo.setUltimaMedicaoLida(indice);
		} finally {
			cursor.close();
		}
	}

	public synchronized void waitForData() {
		try {
			wait();
		} catch (InterruptedException e) {
			System.out.println("Erro no waitForData em MongoDataBase");
		}

	}
}
