package inserirNoSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.JSONObject;

public class InserirSQL {

	private Connection myConnection;
	private Statement myStatement;
	private Alerta alerta;
	

	public InserirSQL() {
		try {
			myConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/museu2", "root", "");
			myStatement = this.myConnection.createStatement();
			alerta=new Alerta(myStatement);
		} catch (Exception e) {
			System.out.println("Erro InserirSQL");
		}
	}

	public void escreverNoSQL(String json) {
		try {
			enviarMedicoesParaSQL(new JSONObject(json));
			alerta.verificarMediçaoTemperatura(new JSONObject(json));
		} catch (Exception e) {
		}
	}

	public void enviarMedicoesParaSQL(JSONObject json) {
		String humidade = json.optString("hum");
		String temperatura = json.optString("tmp");
		String luz = json.optString("cell");
		String movimento = json.optString("mov");
		String data = json.optString("dat");
		String hora = json.optString("tim");
		try {
			String[] datavector = data.split("/");
			data = datavector[2] + "-" + datavector[1] + "-" + datavector[0];
		} catch (Exception e) {
		}

		String dataHora = "'" + data + " " + hora + "'";
		inserirMedicaoNoSQL(humidade, "'" + "hum" + "'", dataHora);
		inserirMedicaoNoSQL(temperatura, "'" + "tmp" + "'", dataHora);
		inserirMedicaoNoSQL(movimento, "'" + "mov" + "'", dataHora);
		inserirMedicaoNoSQL(luz, "'" + "luz" + "'", dataHora);
	}

	public void inserirMedicaoNoSQL(String valor, String tipo, String dataHora) {
		try {
			myStatement.executeUpdate("insert into medicoessensores values(" + "0" + "," + Double.parseDouble(valor)
					+ "," + tipo + "," + dataHora + ")");
		} catch (Exception e) {
		}
	}

}