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
			System.out.println("Erro dos brutos na liga��o a museumain");
		}

		// 2. Criar um Statement SQL
		try {
			createStatement();
		} catch (Exception e) {
			System.out.println("Erro dos brutos na cria��o do statement!");
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
			System.out.println("Erro: Medicao Inv�lida.");
		}
	}

	public void validarInserirSQL(JSONObject json) {
		String humidade = null, temperatura = null, luz = null, movimento = null;
	
		try {
			humidade = json.optString("hum");
		} catch (Exception e) {}

		try {
			temperatura = json.optString("tmp");
		} catch (Exception e) {}

		try {
			luz = json.optString("cell");
		} catch (Exception e) {}

		try {
			movimento = json.optString("mov");
		} catch (Exception e) {}

		String data = json.optString("dat");
		String hora = json.optString("tim");

		String[] datavector = data.split("/");
		data = datavector[2] + "-" + datavector[1] + "-" + datavector[0];
		
		String dataHora = "'" + data + " " + hora + "'";
		
		executarQuery(humidade, "'" + "hum" + "'", dataHora);
		executarQuery(temperatura, "'" + "tmp" + "'", dataHora);
		executarQuery(movimento, "'" + "mov" + "'", dataHora);
		executarQuery(luz, "'" + "luz" + "'", dataHora);

	}
	
	public void executarQuery(String valor, String tipo, String dataHora){
		try {
			myStatement.executeUpdate(
					"insert into medicoessensores values(" + "0" + "," + Double.parseDouble(valor) + "," + tipo + "," + dataHora + ")");
			
			System.out.println("Medicao Inserida no SQL com sucesso!\n" + valor + " " + tipo + " " + dataHora);
		} catch (Exception e) {}
	}
}
