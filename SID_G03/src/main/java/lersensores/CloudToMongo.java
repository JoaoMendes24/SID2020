package lersensores;

import org.bson.Document;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.*;
import java.io.*;
import javax.swing.*;

public class CloudToMongo implements MqttCallback {
	MqttClient mqttclient;
	static MongoClient mongoClient;
	static MongoDatabase db;
	static MongoCollection<Document> mongocol;
	static String cloud_server = new String();
	static String cloud_topic = new String();
	static String mongo_host = new String();
	static String mongo_database = new String();
	static String mongo_collection = new String();

	public static void main(String[] args) {

		try {
			Properties p = new Properties();
			p.load(new FileInputStream("src/main/java/lersensores/CloudToMongo.ini"));
			cloud_server = p.getProperty("cloud_server");
			cloud_topic = p.getProperty("cloud_topic");
			mongo_host = p.getProperty("mongo_host");
			mongo_database = p.getProperty("mongo_database");
			mongo_collection = p.getProperty("mongo_collection");
		} catch (Exception e) {

			System.out.println("Error reading CloudToMongo.ini file " + e);
			JOptionPane.showMessageDialog(null, "The CloudToMongo.inifile wasn't found.", "CloudToMongo",
					JOptionPane.ERROR_MESSAGE);
		}
		new CloudToMongo().connecCloud();
		new CloudToMongo().connectMongo();
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
		mongoClient = new MongoClient(new MongoClientURI(mongo_host));
		db = mongoClient.getDatabase(mongo_database);
		mongocol = db.getCollection(mongo_collection);
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {

		String payload = new String(message.getPayload());

		// limpar os erros dos sensores do professor
		payload = firstCleanProf(payload);
		payload = secondCleanProf(payload);

		try {
			JSONObject jsonmsg = new JSONObject(payload);
			Document doc = Document.parse(jsonmsg.toString());
			mongocol.insertOne(doc);
			System.out.println("Mensagem guardada com sucesso! Conteúdo:\n" + payload);
		} catch (Exception e) {
			System.out.println("Formato Inválido");
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