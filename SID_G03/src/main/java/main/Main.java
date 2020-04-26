package main;

import java.util.ArrayList;

import jdbc.EscreverSQL;
import lerMongo.LerMongo;
import lerMongo.Medicao;

public class Main {

	public static void main(String[] args) {
		ArrayList<Medicao> medicoes = LerMongo.start();
		EscreverSQL.start(medicoes);
	}

}
