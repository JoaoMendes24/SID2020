package escutarMongo;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import baseDadosMongo.BaseDadosMongo;
import inserirNoSQL.InserirSQL;

public class EscutarMongo {

	private BaseDadosMongo mongodb;
	private MongoClient mongoClient;
	private InserirSQL inserirSQL;
	private long ultimaMedicao;

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
			ultimaMedicao = this.mongodb.getCollection().count() - 1;
		} catch (Exception e) {
			System.out.println("Erro: Conectar Base Dados Mongo");
		}
	}

	public void escutar() {
		try {
			while (true) {
				int indice = -1;
				MongoCollection<Document> mongocol = this.mongodb.getCollection();
				Document next;
				MongoCursor<Document> cursor = mongocol.find().iterator();

				while (cursor.hasNext()) {
					indice++;
					next = cursor.next();
					if (indice > ultimaMedicao && indice < mongocol.count()) {
						this.inserirSQL.escreverNoSQL(next.toJson());
					}
				}
				this.ultimaMedicao = indice;
				cursor.close();
				Thread.sleep(2000);
			}
		} catch (Exception e) {
			System.out.println("Erro: Leitura do Mongo");
		}
	}
}
