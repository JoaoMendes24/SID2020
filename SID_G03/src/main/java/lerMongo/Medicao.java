package lerMongo;

public class Medicao {
	
	private double valorMedicao;
	private String tipoSensor;
	private String data;
	private String hora;
	
	
	public Medicao(double valorMedicao, String tipoSensor, String data, String hora) {
		this.tipoSensor = tipoSensor;
		this.data = data;
		this.valorMedicao = valorMedicao;
		this.hora = hora;
	}


	public double getValorMedicao() {
		return valorMedicao;
	}


	public void setValorMedicao(double valorMedicao) {
		this.valorMedicao = valorMedicao;
	}


	public String getTipoSensor() {
		return tipoSensor;
	}


	public void setTipoSensor(String tipoSensor) {
		this.tipoSensor = tipoSensor;
	}


	public String getData() {
		return data;
	}


	public void setData(String data) {
		this.data = data;
	}


	public String getHora() {
		return hora;
	}


	public void setHora(String hora) {
		this.hora = hora;
	}


	@Override
	public String toString() {
		return "Medicao [valorMedicao=" + valorMedicao + ", tipoSensor=" + tipoSensor + ", data=" + data + ", hora="
				+ hora + "]";
	}
}
