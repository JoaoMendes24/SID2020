package jdbc;

import java.sql.*;
import java.util.ArrayList;

import lerMongo.Medicao;

public class EscreverSQL {

	private Connection myConnection;
	private Statement myStatement;
	private ArrayList<Medicao> medicoes;

	public static void start(ArrayList<Medicao> medicoes) {

		EscreverSQL escreverSQL = new EscreverSQL();
		escreverSQL.medicoes = medicoes;

		// 1. Ligar base de dados MySQLMain
		try {
			escreverSQL.connectToMYSQL();
		} catch (Exception e) {
			System.out.println("Erro dos brutos na ligação a museumain");
		}

		// 2. Criar um Statement SQL
		try {
			escreverSQL.createStatement();
		} catch (Exception e) {
			System.out.println("Erro dos brutos na criação do statement!");
		}

		// 3. Percorre medicoes e escreve na base dados museumain

		for (Medicao m : escreverSQL.medicoes) {
			try {
				escreverSQL.sendToMySQL(m);
				System.out.println("Medicao enviada com sucesso para SQL!\n" + m.toString() + "\n\n");
			} catch (Exception e) {
				System.out.println("Erro dos brutos a escrever medicao no SQL!!!!");
			}
		}
	}

	private void sendToMySQL(Medicao medicao) throws Exception {
		double valor = medicao.getValorMedicao();
		String tipo = medicao.getTipoSensor();
		String data = medicao.getDataFormatada();
		String hora = medicao.getHoraFormatada();

		String dataHora = "'" + data + " " + hora + "'";

		executeQuery(valor,"'" + tipo + "'", dataHora);
	}

	// Neste exemplo-> Utiliador:root, sem password
	private void connectToMYSQL() throws Exception {
		myConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/museumain", "root", "");
	}

	private void createStatement() throws SQLException {
		myStatement = this.myConnection.createStatement();
	}

	private void executeQuery(double valor, String tipo, String dataHora) throws SQLException {
		myStatement.executeUpdate(
				"insert into medicoessensores values(" + "0" + "," + valor + "," + tipo + "," + dataHora + ")");
	}

}
