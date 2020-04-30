package escutarMongo;

import org.bson.Document;
import com.mongodb.CursorType;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import baseDadosMongo.BaseDadosMongo;
import inserirNoSQL.InserirSQL;

public class EscutarMongo {

	private BaseDadosMongo mongodb;
	private MongoClient mongoClient;
	private InserirSQL inserirSQL;

	public EscutarMongo(BaseDadosMongo mongodb, InserirSQL inserirSQL) {
		this.mongodb = mongodb;
		this.inserirSQL = inserirSQL;
		conectarMongo();
		escutar();
	}

	public void conectarMongo() {
		try {
			String mongo_host = this.mongodb.getMongo_host();
			mongoClient = new MongoClient(new MongoClientURI(mongo_host));
			this.mongodb.estabelecerLigacao(mongoClient);
		} catch (Exception e) {
			System.out.println("Erro: Conectar Base Dados Mongo");
		}
	}

	public void escutar() {
		try {
			MongoCollection<Document> mongocol = this.mongodb.getCollection();
			long numeroDocumentos = mongocol.count();
			boolean firstRun = true; // firstRun verifica se é a primeira vez que o java é corrido
			FindIterable<Document> iterable = mongocol.find().cursorType(CursorType.TailableAwait);
			MongoCursor<Document> cursor = iterable.iterator();
			while (true) {
				if (cursor.hasNext()) {
					if (firstRun) {
						for (long i = 0; i < numeroDocumentos; i++) {
							cursor.next();
						}
						firstRun = false;
					} else {
						this.inserirSQL.escreverNoSQL(cursor.next().toJson());
					}
				}
				Thread.sleep(2000);
			}
		} catch (Exception e) {
			System.out.println("Erro ao escutar Mongo.");
		}
	}
}
