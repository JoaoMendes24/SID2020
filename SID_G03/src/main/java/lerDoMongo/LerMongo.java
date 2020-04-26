package lerDoMongo;

import recursosPartilhadosThreads.Medicoes;
import recursosPartilhadosThreads.MongoDataBase;

import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import classeMedicao.Medicao;

import java.util.ArrayList;

public class LerMongo {

	private Medicoes medicoes;
	private MongoDataBase mongodatabase;
	private MongoClient mongoClient;
	private int ultimaMedicaoLida = -1;

	public ArrayList<Medicao> startLerMongo(Medicoes medicoes, MongoDataBase mongodatabase) {
		this.medicoes = medicoes;
		this.mongodatabase = mongodatabase;

		connectMongo();

		while (true) {
			// ler dados do mongo
			readDataWriteMedicoes();
			this.mongodatabase.waitForData();
		}
	}

	private void readDataWriteMedicoes() {
		this.mongodatabase.readDataWriteMedicoes(this);
	}

	public void connectMongo() {
		try {
			String mongo_host = this.mongodatabase.getMongo_host();
			mongoClient = new MongoClient(new MongoClientURI(mongo_host));
			this.mongodatabase.establishConnection(mongoClient);
		} catch (Exception e) {
			System.out.println("erro a conectar");
		}
	}

	public void validarMedicao(String json) {
		try {
			JSONObject js = new JSONObject(json);
			String humidade = js.optString("hum");
			String temperatura = js.optString("tmp");
			String data = js.optString("dat");
			String hora = js.optString("tim");
			String luz = js.optString("cell");
			String movimento = js.optString("mov");
			String tipoSensor = js.optString("sens");

			criarMedicoes(humidade, temperatura, movimento, luz, tipoSensor, data, hora);

		} catch (Exception e) {
			// System.out.println("Erro: " + json + "\n");
		}
	}

	private void criarMedicoes(String humidade, String temperatura, String movimento, String luz, String tipoSensor,
			String data, String hora) throws Exception {
		Medicao medicaoHumidade;
		Medicao medicaoTemperatura;
		Medicao medicaoLuminosidade;
		Medicao medicaoMovimento;

		double hum = Double.parseDouble(humidade);
		double temp = Double.parseDouble(temperatura);
		double luminosidade = Double.parseDouble(luz);
		double mov = Double.parseDouble(movimento);

		// criar medicoes
		medicaoHumidade = new Medicao(hum, "hum", data, hora);
		medicaoTemperatura = new Medicao(temp, "tmp", data, hora);
		medicaoLuminosidade = new Medicao(luminosidade, "luz", data, hora);
		medicaoMovimento = new Medicao(mov, "mov", data, hora);

		// adicionar medicoes
		this.medicoes.addMedicao(medicaoMovimento);
		this.medicoes.addMedicao(medicaoLuminosidade);
		this.medicoes.addMedicao(medicaoTemperatura);
		this.medicoes.addMedicao(medicaoHumidade);
	}

	public int getUltimaMedicaoLida() {
		return ultimaMedicaoLida;
	}

	public void setUltimaMedicaoLida(int ultimaMedicaoLida) {
		this.ultimaMedicaoLida = ultimaMedicaoLida;
	}

}
