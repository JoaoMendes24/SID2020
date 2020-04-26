package escreverNoSQL;

import java.sql.*;

import classeMedicao.Medicao;
import recursosPartilhadosThreads.Medicoes;

public class EscreverSQL {

	private Connection myConnection;
	private Statement myStatement;
	private Medicoes medicoes;
	private int ultimaMedicaoEscritaNoSQL = 0;

	public void startEscreverSQL(Medicoes medicoes) {

		this.medicoes = medicoes;

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

		while (true) {
			// 3. le medicoes e escreve na base dados museumain
			writeToSQL();
			this.medicoes.waitForNewData();

		}
	}

	private void writeToSQL() {
		this.medicoes.readDataWriteSQL(this);
	}
	
	public void sendToMySQL(Medicao medicao) throws Exception {
		double valor = medicao.getValorMedicao();
		String tipo = medicao.getTipoSensor();
		String data = medicao.getDataFormatada();
		String hora = medicao.getHoraFormatada();

		String dataHora = "'" + data + " " + hora + "'";

		executeQuery(valor, "'" + tipo + "'", dataHora);
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

	public int getUltimaMedicaoEscritaNoSQL() {
		return ultimaMedicaoEscritaNoSQL;
	}

	public void setUltimaMedicaoEscritaNoSQL(int ultimaMedicaoEscritaNoSQL) {
		this.ultimaMedicaoEscritaNoSQL = ultimaMedicaoEscritaNoSQL;
	}
	
	

}
