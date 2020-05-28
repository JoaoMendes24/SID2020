package lerSensores;

import org.bson.Document;
import org.json.JSONObject;

import com.mongodb.client.MongoCollection;

public class Filtrar {
	
	private double tmp;
	private double hum;
	private int mov;
	private int cell;
	private String dat;
	private String tim;
	private boolean isValidTmp;
	private boolean isValidHum;
	private boolean isValidMov;
	private boolean isValidCell;
	private boolean isValidDat;
	private boolean isValidTim;
	private MongoCollection<Document> mongocol;
	private MongoCollection<Document> dumpcol;
	private JSONObject jsonRecebido;
	private JSONObject goodInfo = new JSONObject();
	private JSONObject badInfo = new JSONObject();
	
	public Filtrar(MongoCollection<Document> mongocol, MongoCollection<Document> dumpcol, JSONObject jsonRecebido) {
		this.jsonRecebido = jsonRecebido;
		this.mongocol = mongocol;
		this.dumpcol = dumpcol;
	}
	
	
	
	public void executar() {
		preencherDados();
		validar();
		escreverDB();
	}
	
	public void preencherDados() {
		tmp = Double.parseDouble(jsonRecebido.optString("tmp"));
		hum = Double.parseDouble(jsonRecebido.optString("hum"));
		mov = Integer.parseInt(jsonRecebido.optString("mov"));
		cell = Integer.parseInt(jsonRecebido.optString("cell"));
		dat = jsonRecebido.optString("dat");
		tim = jsonRecebido.optString("tim");
	}
	
	public void escreverDB() {
		if(!isValidDat || !isValidTim ) {
			Document doc = Document.parse(jsonRecebido.toString());
			dumpcol.insertOne(doc);
		}
		else {
			escreverJsons();
			if(!goodInfo.isEmpty()) {
				Document doc = Document.parse(goodInfo.toString());
				mongocol.insertOne(doc);
			}
			if(!badInfo.isEmpty()) {
				Document doc = Document.parse(badInfo.toString());
				dumpcol.insertOne(doc);
			}
		}
	}
	
	public void validar() {
		if(tmp < 100 && tmp > (-10)) {
			isValidTmp = true;
		}
		if(hum <= 100 && hum >= 0 ) {
			isValidHum = true;
		}
		if( mov == 0 || mov == 1 ) {
			isValidMov = true;
		}
		
		if(cell > 0 && cell < 4095) {
			isValidCell = true;
		}
		isValidDat = dat.matches("(^(((0[1-9]|1[0-9]|2[0-8])/(0[1-9]|1[012]))|((29|30|31)/(0[13578]|1[02]))|((29|30)/(0[4,6,9]|11)))/(19|[2-9][0-9])[0-9][0-9]$)|(^29/02/(19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)$)");
		isValidTim = tim.matches("^([01][0-9]|2[0123]):([012345][0-9]):([012345][0-9])");
	}
	
	public void escreverJsons() {
		if(isValidTmp) { goodInfo.put("tmp", String.valueOf(tmp)) ;}
		else { badInfo.put("tmp", String.valueOf(tmp));}
		if(isValidHum) { goodInfo.put("hum", String.valueOf(hum)) ;}
		else { badInfo.put("hum", String.valueOf(hum));}
		if(isValidMov) { goodInfo.put("mov", String.valueOf(mov)) ;}
		else { badInfo.put("mov", String.valueOf(mov));}
		if(isValidCell) { goodInfo.put("cell", String.valueOf(cell)) ;}
		else { badInfo.put("cell", String.valueOf(cell));}
		goodInfo.put("dat", dat);
		goodInfo.put("tim", tim);
	}
	

}
