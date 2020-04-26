package recursosPartilhadosThreads;

import java.util.ArrayList;

import classeMedicao.Medicao;
import escreverNoSQL.EscreverSQL;

public class Medicoes {

	private ArrayList<Medicao> medicoes = new ArrayList<Medicao>();

	public synchronized ArrayList<Medicao> getMedicoes() {
		return medicoes;
	}

	public synchronized void setMedicoes(ArrayList<Medicao> medicoes) {
		this.medicoes = medicoes;
	}

	public synchronized void addMedicao(Medicao m) {
		this.medicoes.add(m);
		notifyAll();
	}

	public synchronized void waitForNewData() {
		try {
			wait();
		} catch (InterruptedException e) {
			System.out.println("Erro no waitForNewData em medicoes");
		}

	}

	public synchronized void readDataWriteSQL(EscreverSQL esSQL) {
		int ultimo = esSQL.getUltimaMedicaoEscritaNoSQL();
		
		for(int i=ultimo;i<this.medicoes.size();i++) {
			try {
				esSQL.sendToMySQL(medicoes.get(i));
				System.out.println("Medicao enviada com sucesso para SQL!\n" + medicoes.get(i).toString() + "\n\n");
			} catch (Exception e) {
//				System.out.println(medicoes.get(i).toString());
			}
		}
		if(this.medicoes.size() != 0) {
			esSQL.setUltimaMedicaoEscritaNoSQL(this.medicoes.size());
		}
	}
}
