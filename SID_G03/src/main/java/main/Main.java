package main;

import baseDadosMongo.BaseDadosMongo;
import escutarMongo.EscutarMongo;
import inserirNoSQL.InserirSQL;


public class Main {

	public static void main(String[] args) {
		try {
			new EscutarMongo(new BaseDadosMongo(), new InserirSQL());
		} catch (Exception e) {
			System.out.println("Erro: Main");
		}	
	}
}
