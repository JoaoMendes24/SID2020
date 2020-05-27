package inserirNoSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONObject;

public class Alerta {

	private Statement myStatement;
	private EstadoSistema estado;
	private LinkedList<MedicaoTemperatura> buffer = new LinkedList<MedicaoTemperatura>();
	private LinkedList<MedicaoTemperatura> medicoes = new LinkedList<MedicaoTemperatura>();
	private double medicao;
	private int contador;
	private double tmpLimite;
	public static final double DESVIO_MEDIA = 1;
	public static final int MEDICOES_SIZE = 10;
	public static final int BUFFER_SIZE = 5;
	public static final double DELTA_MEDICOES = 2;
	public static final int MEDICOES_ACIMA_LIMITE = 30;

	
	public Alerta(Statement myStatement) {
		this.myStatement = myStatement;
		estado = EstadoSistema.ESTAVEL;
	}

	public void verificarMediçaoTemperatura(JSONObject json) throws SQLException {
		String temperatura = json.optString("tmp");
		String data = json.optString("dat");
		String hora = json.optString("tim");
		try {
			String[] datavector = data.split("/");
			data = datavector[2] + "-" + datavector[1] + "-" + datavector[0];
		} catch (Exception e) {
		}
		String dataHora = "'" + data + " " + hora + "'";
		medicao = Double.parseDouble(temperatura);
		MedicaoTemperatura m = new MedicaoTemperatura(medicao, dataHora);
		if (buffer.size() < BUFFER_SIZE)
			buffer.add(m);
		else {
			double media = calcularMedia(buffer);
			MedicaoTemperatura primeiraMedicao = buffer.remove();
			if (primeiraMedicao.getValor() <= media + DESVIO_MEDIA
					&& primeiraMedicao.getValor() >= media - DESVIO_MEDIA) {
				adicionarMedicoes(primeiraMedicao);
			}
			buffer.add(m);
		}
		ResultSet rs = myStatement.executeQuery("SELECT LimiteTemperatura from sistema");
		rs.absolute(1);
		tmpLimite = rs.getDouble("LimiteTemperatura");
		enviarAlerta();

	}

	public double calcularMedia(LinkedList<MedicaoTemperatura> lista) {
		double soma = 0;
		for (MedicaoTemperatura d : lista)
			soma += d.getValor();
		return soma / lista.size();
	}

	public void adicionarMedicoes(MedicaoTemperatura m) {
		if (medicoes.size() == MEDICOES_SIZE) {
			medicoes.remove();
			medicoes.add(m);
		} else
			medicoes.add(m);
	}

	public void enviarAlertaSubidaTemperatura() throws SQLException {
		if (estado == EstadoSistema.ESTAVEL) {
			double diferenca_temp = 0;
			if (medicoes.size() == MEDICOES_SIZE) {
				diferenca_temp = medicoes.get(0).getValor() - medicoes.peekLast().getValor();
				if (diferenca_temp >= DELTA_MEDICOES) {
					estado = EstadoSistema.SUBIDA;
					myStatement.executeUpdate("insert into alerta" + " values(0" + ","
							+ medicoes.peekLast().getDataHora() + ",'tmp'," + medicoes.peekLast().getValor() + ","
							+ tmpLimite + ",'O sistema detetou que " + "houve uma subida da temperatura',"
							+ estado.getNivel() + "," + estado.getTitulo() + ")");
				}
			}
		}
	}

	public void enviarAlertaPertoLimite() throws SQLException {
		if (estado == EstadoSistema.ESTAVEL || estado == EstadoSistema.SUBIDA) {
			for (MedicaoTemperatura m : medicoes) {
				if (m.getValor() >= tmpLimite - 10) {
					estado = EstadoSistema.PERTO_LIMITE;
					myStatement.executeUpdate(
							"insert into alerta" + " values(0" + "," + m.getDataHora() + ",'tmp'," + m.getValor() + ","
									+ tmpLimite + ",'O sistema detetou que " + "a temperatura está perto do limite',"
									+ estado.getNivel() + "," + estado.getTitulo() + ")");
					break;
				}
			}
		}
	}

	public void enviarAlertaAcimaLimite() throws SQLException {
		if(estado == EstadoSistema.ACIMA_LIMITE)
			contador++;
		if (estado == EstadoSistema.PERTO_LIMITE || contador==MEDICOES_ACIMA_LIMITE) {
			for (MedicaoTemperatura m : medicoes) {
				if (m.getValor() > tmpLimite) {
					estado = EstadoSistema.ACIMA_LIMITE;
					myStatement.executeUpdate(
							"insert into alerta" + " values(0" + "," + m.getDataHora() + ",'tmp'," + m.getValor() + ","
									+ tmpLimite + ",'O sistema detetou que " + "a temperatura está acima do limite',"
									+ estado.getNivel() + "," + estado.getTitulo() + ")");
					break;
				}
			}
		}
	}
	
	public void enviarAlertaDescidaLimite() throws SQLException {
		if(estado == EstadoSistema.ACIMA_LIMITE)
			for (MedicaoTemperatura m : medicoes) {
				if (m.getValor() < tmpLimite) {
					estado = EstadoSistema.PERTO_LIMITE;
					myStatement.executeUpdate(
							"insert into alerta" + " values(0" + "," + m.getDataHora() + ",'tmp'," + m.getValor() + ","
									+ tmpLimite + ",'O sistema detetou que a temperatura está abaixo mas perto do limite',"
									+ estado.getNivel() + "," + estado.getTitulo() + ")");
					contador=0;
					break;
				}
			}
		if (estado == EstadoSistema.PERTO_LIMITE) {
			for (MedicaoTemperatura m : medicoes) {
				if (m.getValor() < tmpLimite-10) {
					estado = EstadoSistema.ESTAVEL;
					myStatement.executeUpdate(
							"insert into alerta" + " values(0" + "," + m.getDataHora() + ",'tmp'," + m.getValor() + ","
									+ tmpLimite + ",'O sistema detetou que a temperatura voltou ao normal',"
									+ estado.getNivel() + "," + estado.getTitulo() + ")");
				}
			}
		}
	}

	public void enviarAlerta() throws SQLException {
		enviarAlertaSubidaTemperatura();
		enviarAlertaPertoLimite();
		enviarAlertaAcimaLimite();
		enviarAlertaDescidaLimite();
	}
}
