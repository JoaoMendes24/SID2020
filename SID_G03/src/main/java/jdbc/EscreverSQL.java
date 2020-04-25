package jdbc;

import java.sql.*;
import java.util.ArrayList;

import lerMongo.Medicao;

public class EscreverSQL {
	
	private Connection myConnection;
	private Statement myStatement;
	private ResultSet myResult;
	private ArrayList<Medicao> medicoes;

	public static void main(String[] args) {
		
		EscreverSQL escreverSQL = new EscreverSQL();
		
		// 1. Ligar base de dados MySQLMain
		try {
			escreverSQL.connectToMYSQL();
			System.out.println("Conectado a museumain!\n");
		} catch (Exception e) {
			System.out.println("Erro dos brutos na ligação a museumain");
		}
		
		// 2. Criar um Statement SQL
		try {
			escreverSQL.createStatement();
		} catch (Exception e) {
			System.out.println("Erro dos brutos na criação do statement!");
		}
		
		//3. Executar Query
		try {
			escreverSQL.executeQuery();
		} catch (Exception e) {
			System.out.println("Erro dos brutos na execução do Query!");
		}
		
		//4. Processar resultado do Query
		try {
			escreverSQL.processResult();
		} catch (Exception e) {
			System.out.println("Erro dos brutos no processamento do query");
		}
	}
	
	//Neste exemplo-> Utiliador:root, sem password
	private void connectToMYSQL() throws Exception{
		myConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/museumain", "root", "");
	}
	
	
	private void createStatement() throws SQLException {
		myStatement = this.myConnection.createStatement();	
	}

	//Neste exemplo a tabela selecionada e a tabela utilizador
	private void executeQuery() throws SQLException {
		myResult = myStatement.executeQuery("Select * from utilizador");
		
	}
	
	//Neste exemplo o resultado do query é escrito no ecra
	private void processResult() throws SQLException {
		System.out.println("Resultados da tabela utilizador:");
		while(myResult.next()) {
			System.out.println("Email:" + myResult.getString(1) + ", Nome:" + myResult.getString(2) + ", Tipo:" + myResult.getString(3));
		}
	}
	
}
