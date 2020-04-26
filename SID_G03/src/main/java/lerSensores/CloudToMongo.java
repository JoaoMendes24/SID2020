package lerSensores;

import org.bson.Document;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import com.mongodb.*;

import recursosPartilhadosThreads.MongoDataBase;

import java.util.*;
import java.io.*;


public class CloudToMongo implements MqttCallback {
	
	private MongoDataBase mongoDataBase;
	private MongoClient mongoClient;
	private MqttClient mqttclient;
	private String cloud_server = new String();
	private String cloud_topic = new String();

	
	public CloudToMongo(MongoDataBase db) {
		this.mongoDataBase = db;
		
		Properties p = new Properties();
		try {
			p.load(new FileInputStream("src/main/java/lerSensores/CloudToMongo.ini"));
			cloud_server = p.getProperty("cloud_server");
			cloud_topic = p.getProperty("cloud_topic");
		} catch (Exception e) {
			System.out.println("Erro na leitura do ficheiro .ini");
		}
		connecCloud();
		connectMongo();
	}

	public void connecCloud() {
		int i;
		try {
			i = new Random().nextInt(100000);
			mqttclient = new MqttClient(cloud_server, "CloudToMongo_" + String.valueOf(i) + "_" + cloud_topic);
			mqttclient.connect();
			mqttclient.setCallback(this);
			mqttclient.subscribe(cloud_topic);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public void connectMongo() {
		String mongo_host = this.mongoDataBase.getMongo_host();
		mongoClient = new MongoClient(new MongoClientURI(mongo_host));
		this.mongoDataBase.establishConnection(mongoClient);
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {

		String payload = new String(message.getPayload());

		// limpar os erros dos sensores do professor
		payload = firstCleanProf(payload);
		payload = secondCleanProf(payload);

		try {
			JSONObject jsonmsg = new JSONObject(payload);
			Document doc = Document.parse(jsonmsg.toString());
			this.mongoDataBase.addToMongo(doc);
			System.out.println("Mensagem guardada com sucesso! Conteúdo:\n" + payload);
		} catch (Exception e) {
			System.out.println("Formato enviado pelos sensores Inválido!");
		}
	}

	public void connectionLost(Throwable cause) {
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
	}

	public String firstCleanProf(String message) {
		String old = '"' + "mov" + '"' + ':' + '"' + "0" + '"';
		message = message.replaceFirst(old, ",");
		return message;

	}

	public String secondCleanProf(String message) {
		String old = '"' + "" + '"';
		String replace = '"' + "," + '"';
		message = message.replaceFirst(old, replace);
		return message;
	}
}