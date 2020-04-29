package inserirNoSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.JSONObject;

public class InserirSQL {

	private Connection myConnection;
	private Statement myStatement;

	public InserirSQL() {
		// 1. Ligar base de dados MySQLMain
		try {
			connectToMYSQL();
		} catch (Exception e) {
			System.out.println("Erro dos brutos na ligação a museumain");
		}

		// 2. Criar um Statement SQL
		try {
			createStatement();
		} catch (Exception e) {
			System.out.println("Erro dos brutos na criação do statement!");
		}
	}

	private void connectToMYSQL() throws Exception {
		myConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/museumain", "root", "");
	}

	private void createStatement() throws SQLException {
		myStatement = this.myConnection.createStatement();
	}

	public void escreverNoSQL(String json) {
		try {
			validarInserirSQL(new JSONObject(json));
		} catch (Exception e) {
			System.out.println("Erro: Medicao Inválida.");
		}
	}

	public void validarInserirSQL(JSONObject json) {

		String humidade = json.optString("hum");
		String temperatura = json.optString("tmp");
		String luz = json.optString("cell");
		String movimento = json.optString("mov");
		String data = json.optString("dat");
		String hora = json.optString("tim");
		
		try {
			String[] datavector = data.split("/");
			data = datavector[2] + "-" + datavector[1] + "-" + datavector[0];
		} catch (Exception e) {}

		String dataHora = "'" + data + " " + hora + "'";

		executarQuery(humidade, "'" + "hum" + "'", dataHora);
		executarQuery(temperatura, "'" + "tmp" + "'", dataHora);
		executarQuery(movimento, "'" + "mov" + "'", dataHora);
		executarQuery(luz, "'" + "luz" + "'", dataHora);
	}

	public void executarQuery(String valor, String tipo, String dataHora) {
		try {
			myStatement.executeUpdate("insert into medicoessensores values(" + "0" + "," + Double.parseDouble(valor)
					+ "," + tipo + "," + dataHora + ")");

			System.out.println("Medicao Inserida no SQL com sucesso!\n" + valor + " " + tipo + " " + dataHora);
		} catch (Exception e) {
		}
	}
}
