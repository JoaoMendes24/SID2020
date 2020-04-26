package lerMongo;

public class Medicao {
	
	private double valorMedicao;
	private String tipoSensor;
	private String data;
	private String hora;
	
	
	public Medicao(double valorMedicao, String tipoSensor, String data, String hora) throws Exception {
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
	
	
	// constroi a data formato SQL
	private String formatarData() {
		String data = this.data;
		String result = "";
		String bonus;
		
		String[] vector = data.split("/");
		
		result = result + vector[2];
		result = result + "-";
		
		
		bonus = "";
		if(vector[1].length() == 1) {
			bonus = "0";
		}
		
		result = result + bonus + vector[1];
		result = result + "-";
		
		
		bonus = "";
		if(vector[0].length() == 1) {
			bonus = "0";
		}
		result = result + bonus + vector[0];
		
		return result;
	}
	
	public String getDataFormatada() {
		String data = formatarData();
		return data;
	}
	
	
	// constroi a hora formato SQL
	private String formatarHora() {
		String hora = this.hora;
		String result = "";
		String bonus;
		
		String[] vector = hora.split(":");
		
		bonus = "";
		if(vector[0].length() == 1) {
			bonus = "0";
		}
		result = result + bonus + vector[0] + ":";
		
		bonus = "";
		if(vector[1].length() == 1) {
			bonus = "0";
		}
		result = result + bonus + vector[1] + ":";
		
		bonus = "";
		if(vector[2].length() == 1) {
			bonus = "0";
		}
		result = result + bonus + vector[2];
		
		return result;
	}
	
	
	
	public String getHoraFormatada() {
		String hora = formatarHora();
		return hora;
	}
	
	
	
	
	
}
