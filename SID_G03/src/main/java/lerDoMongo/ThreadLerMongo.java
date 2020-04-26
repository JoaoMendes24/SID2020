package lerDoMongo;


import recursosPartilhadosThreads.Medicoes;
import recursosPartilhadosThreads.MongoDataBase;

public class ThreadLerMongo extends Thread {

	private MongoDataBase db;
	private Medicoes medicoes;

	public ThreadLerMongo(MongoDataBase db, Medicoes medicoes) {
		this.db = db;
		this.medicoes = medicoes;
	}

	public void run() {
		new LerMongo().startLerMongo(medicoes,db);
	}
}
