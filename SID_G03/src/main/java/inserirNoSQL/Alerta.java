package inserirNoSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONObject;

public class Alerta {
	
	private Statement myStatement;
	private int upCounter;
	private int downCounter;
	double medicaoAnterior;
	boolean primeiraMedicao = false;

	public Alerta(Statement myStatement) {
		this.myStatement=myStatement;
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
		double tmpAtual = Double.parseDouble(temperatura);
		ResultSet rs = myStatement.executeQuery("SELECT LimiteTemperatura from sistema");
		rs.absolute(1);
		double tmpLimite = rs.getDouble("LimiteTemperatura");
		if (tmpAtual >= medicaoAnterior + 0.5 && primeiraMedicao)
			upCounter++;
		else
			upCounter = 0;
		
		if (upCounter == 10 && tmpAtual <= tmpLimite) {
			upCounter = 0;
			myStatement.executeUpdate("insert into alerta" + " values(0" + "," + dataHora + ",'tmp'," + tmpAtual + ","
					+ tmpLimite + ",'O sistema detetou que "
					+ "houve uma subida da temperatura',true,'Risco de ultrapassar o limite!')");
		}
		if (upCounter == 10 && tmpAtual > tmpLimite) {
			upCounter = 0;
			myStatement.executeUpdate("insert into alerta" + " values(0" + "," + dataHora + ",'tmp'," + tmpAtual + ","
					+ tmpLimite + ",'O sistema detetou que a temperatura está acima do limite',true,'')");
		}
		if(tmpAtual <=medicaoAnterior-0.5 && tmpAtual>tmpLimite) {
			downCounter++;
		}else downCounter=0;
		if(downCounter == 10) {
			downCounter=0;
			myStatement.executeUpdate("insert into alerta" + " values(0" + "," + dataHora + ",'tmp'," + tmpAtual + ","
					+ tmpLimite + ",'O sistema detetou que a temperatura está a diminuir após "
							+ "ter ultrapassado o limite',true,'')");
		}
		medicaoAnterior = tmpAtual;
		primeiraMedicao = true;

	}

}
