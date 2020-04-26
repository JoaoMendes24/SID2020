package lerSensores;

import recursosPartilhadosThreads.MongoDataBase;

public class ThreadLerSensores extends Thread {
	
	private MongoDataBase db;
	

	public ThreadLerSensores(MongoDataBase db) {
		this.db = db;
	}
	
	public void run() {
		new CloudToMongo(db);
	}

}
