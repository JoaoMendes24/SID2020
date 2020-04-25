package lerMongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.json.JSONObject;
import com.mongodb.client.MongoCursor;
import java.util.ArrayList;

public class LerMongo {

	private MongoClient mongoClient;
	private MongoDatabase db;
	private MongoCollection<Document> mongocol;
	private ArrayList<Medicao> medicoes = new ArrayList<Medicao>();

	public static void main(String[] args) {
		LerMongo lermongo = new LerMongo();

		// conecta a mongo
		lermongo.connectMongo();

		// ler dados do mongo
		lermongo.readData();
		
		// Mostra valores guardados
		for(Medicao m: lermongo.medicoes) {
			System.out.println(m.toString() + "\n");
		}

	}

	private void connectMongo() {
		mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		db = mongoClient.getDatabase("sid2020");
		mongocol = db.getCollection("sensor");
	}

	private void readData() {
		MongoCursor<Document> cursor = mongocol.find().iterator();
		try {
			while (cursor.hasNext()) {
				String json = cursor.next().toJson();
				validarMedicao(json);
			}
		} finally {
			cursor.close();
		}
	}

	private void validarMedicao(String json) {
		try {
			JSONObject js = new JSONObject(json);
			String humidade = js.optString("hum");
			String temperatura = js.optString("tmp");
			String data = js.optString("dat");
			String hora = js.optString("tim");
			String luz = js.optString("cell");
			String movimento = js.optString("mov");
			String tipoSensor = js.optString("sens");
			
			guardarMedicoes(humidade, temperatura, movimento, luz, tipoSensor, data, hora);

		} catch (

		Exception e) {
			// medicoes invalidas
//			 System.out.println("Erro: " + json + "\n");
		}
	}

	private void guardarMedicoes(String humidade, String temperatura, String movimento, String luz, String tipoSensor, String data, String hora) throws Exception {
		Medicao medicaoHumidade;
		Medicao medicaoTemperatura;
		Medicao medicaoLuminosidade;
		Medicao medicaoMovimento;
		
		double hum = Double.parseDouble(humidade);
		double temp = Double.parseDouble(temperatura);
		double luminosidade = Double.parseDouble(luz);
		double mov = Double.parseDouble(movimento);
		
		//criar medicoes
		medicaoHumidade = new Medicao(hum, "hum", data, hora);
		medicaoTemperatura = new Medicao(temp, "tmp", data, hora);
		medicaoLuminosidade = new Medicao(luminosidade, "luz", data, hora);
		medicaoMovimento = new Medicao(mov,"mov", data, hora);
		
		//adicionar medicoes a ArrayList
		medicoes.add(medicaoHumidade);
		medicoes.add(medicaoMovimento);
		medicoes.add(medicaoTemperatura);
		medicoes.add(medicaoLuminosidade);
	}

	public ArrayList<Medicao> getMedicoes() {
		return medicoes;
	}
}
