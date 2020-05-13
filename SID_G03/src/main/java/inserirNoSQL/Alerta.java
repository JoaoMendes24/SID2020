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
	private int nMedicoes;
	private boolean acimaLim=false;

	public Alerta(Statement myStatement) {
		this.myStatement=myStatement;
	}

	public void verificarMedicaoTemperatura(JSONObject json) throws SQLException{
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
		if (tmpAtual >= tmpLimite -10 && tmpAtual<=tmpLimite)
			upCounter++;
		else
			upCounter = 0;
		if (upCounter == 10) {
			upCounter = 0;
			myStatement.executeUpdate("insert into alerta" + " values(0" + "," + dataHora + ",'tmp'," + tmpAtual + ","
					+ tmpLimite + ",'O sistema detetou que "
					+ "houve uma subida da temperatura',true,'Risco de ultrapassar o limite!')");
		}
		if(tmpAtual>tmpLimite && !acimaLim)
			upCounterLim++;
		else upCounterLim=0;
		if (upCounterLim == 10) {
			upCounterLim = 0;
			myStatement.executeUpdate("insert into alerta" + " values(0" + "," + dataHora + ",'tmp'," + tmpAtual + ","
					+ tmpLimite + ",'O sistema detetou que a temperatura está acima do limite',true,'')");
			acimaLim=true;
		}
		if(acimaLim && tmpAtual>tmpLimite)
			nMedicoes++;
		if(nMedicoes==30) {
			myStatement.executeUpdate("insert into alerta" + " values(0" + "," + dataHora + ",'tmp'," + tmpAtual + ","
					+ tmpLimite + ",'O sistema detetou que a temperatura continua acima do limite',true,'')");
			nMedicoes=0;
		}
		if(tmpAtual <tmpLimite && acimaLim) {
			downCounter++;
		}else downCounter=0;
		if(downCounter == 5) {
			downCounter=0;
			myStatement.executeUpdate("insert into alerta" + " values(0" + "," + dataHora + ",'tmp'," + tmpAtual + ","
					+ tmpLimite + ",'O sistema detetou que a temperatura voltou a estar abaixo do limite' "
							+ ",true,'')");
			acimaLim=false;
		}
	}
	

}
