package inserirNoSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;

import org.json.JSONObject;

public class Alerta {
	
	private Statement myStatement;
	private double tmpLimite;
	private double tmpAtual;
	private int upCounter;
	private int upCounterLim;
	private int downCounter;
	double medicaoAnterior;
	Timer timer = new Timer();
	boolean acimaLimite = false;

	public Alerta(Statement myStatement) {
		this.myStatement=myStatement;
	}

	public void verificarMediçaoTemperatura(JSONObject json) throws SQLException{
		String temperatura = json.optString("tmp");
		String data = json.optString("dat");
		String hora = json.optString("tim");
		try {
			String[] datavector = data.split("/");
			data = datavector[2] + "-" + datavector[1] + "-" + datavector[0];
		} catch (Exception e) {
		}
		String dataHora = "'" + data + " " + hora + "'";
		tmpAtual = Double.parseDouble(temperatura);
		ResultSet rs = myStatement.executeQuery("SELECT LimiteTemperatura from sistema");
		rs.absolute(1);
		tmpLimite = rs.getDouble("LimiteTemperatura");
		enviarAlerta(dataHora);

	}
	
	public void enviarAlerta(String dataHora) throws SQLException {
		if (tmpAtual >= tmpLimite -10)
			upCounter++;
		else
			upCounter = 0;
		if (upCounter == 10) {
			upCounter = 0;
			myStatement.executeUpdate("insert into alerta" + " values(0" + "," + dataHora + ",'tmp'," + tmpAtual + ","
					+ tmpLimite + ",'O sistema detetou que "
					+ "houve uma subida da temperatura',true,'Risco de ultrapassar o limite!')");
		}
		if(tmpAtual>tmpLimite)
			upCounterLim++;
		else upCounterLim=0;
		if (upCounterLim == 10) {
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
	}
	

}
