package escreverNoSQL;

import recursosPartilhadosThreads.Medicoes;

public class ThreadEscreverSQL extends Thread {

	private Medicoes medicoes;

	public ThreadEscreverSQL(Medicoes medicoes) {
		this.medicoes = medicoes;
	}

	public void run() {
		new EscreverSQL().startEscreverSQL(medicoes);
	}
}
