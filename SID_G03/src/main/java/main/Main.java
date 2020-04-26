package main;

import escreverNoSQL.ThreadEscreverSQL;
import lerDoMongo.ThreadLerMongo;
import lerSensores.ThreadLerSensores;
import recursosPartilhadosThreads.Medicoes;
import recursosPartilhadosThreads.MongoDataBase;

public class Main {

	public static void main(String[] args) {
		
		
		MongoDataBase mongodb = new MongoDataBase();
		Medicoes medicoes = new Medicoes();
		
		//1. Ler Sensores
		new ThreadLerSensores(mongodb).start();
	
		
		//2. Ler Mongo
		new ThreadLerMongo(mongodb,medicoes).start();
		
		//3.Escrever na bd museumain
		new ThreadEscreverSQL(medicoes).start();
	}
}
