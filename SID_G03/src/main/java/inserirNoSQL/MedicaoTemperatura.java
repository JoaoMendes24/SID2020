package inserirNoSQL;

public class MedicaoTemperatura {
	
	private double valor;
	private String dataHora;
	public MedicaoTemperatura(double valor, String dataHora) {
		super();
		this.valor = valor;
		this.dataHora = dataHora;
	}
	public double getValor() {
		return valor;
	}
	public String getDataHora() {
		return dataHora;
	}
	
	public String toString() {
		return ""+valor+"";
		
	}
	
	
}
