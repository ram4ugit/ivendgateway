package com.limitlessmobility.iVendGateway.services.mqtt;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MqttClientConnection {
	private static final String CONNECTION_URL = "tcp://144.217.89.37:1884";
    private static final String USERNAME = "llmuvmterminal";
    private static final String PASSWORD = "LLM@br0K3r#19";


	@RequestMapping(value = "/mqtt", method = RequestMethod.POST)
	@ResponseBody
	public String getMQClient(@RequestBody String payloadRequest) throws Exception {
		System.out.println("mqtt payloadRequest is "+payloadRequest);
		JSONObject jsonObj = new JSONObject(payloadRequest);
		/*MqttClient client = new MqttClient( 
			    "tcp://139.59.73.155:1883", //URI 
			    MqttClient.generateClientId(), //ClientId 
			    new MemoryPersistence()); //Persistence
*/		
		System.out.println("CONNECTION_URL "+ CONNECTION_URL);
		MqttClient client = new MqttClient(CONNECTION_URL, MqttClient.generateClientId());
		MqttConnectOptions connOpts = setUpConnectionOptions(USERNAME, PASSWORD);
		client.connect(connOpts);
		
		System.out.println("Client.."+client.getClientId());
		MqttConnectOptions options = new MqttConnectOptions();
		 
		options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
		// options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
		 
		
		if(client.isConnected()) {
			System.out.println("MQ Client is connected!!");
			String terminalId = jsonObj.getString("TerminalId");
			String topic = "iot/vmc/callbackresponse/"+terminalId;
//			String topic = "iot/vmc/callbackresponse";
			String payload = payloadRequest;
			/*client.publish( 
				    topic, // topic 
				   payload.getBytes(StandardCharsets.UTF_8), // payload 
				    2, // QoS 
				    false); // retained? 
*/			
	
			
			byte[] encodedPayload = new byte[0];
			try {
			    encodedPayload = payload.getBytes(StandardCharsets.UTF_8);
			    MqttMessage message = new MqttMessage(encodedPayload);
			    System.out.println("mqtt topic "+topic);
			    System.out.println("mqtt message "+message);
			    client.publish(topic, message);
			    
			    client.disconnect();
			} catch (Exception e) {
			    e.printStackTrace();
			}
	        
	
		}
		
		

		
		

		return "";
	}
	
	/*@RequestMapping(value = "/mqttTest", method = RequestMethod.GET)
	@ResponseBody
	public String getMQClientTest() throws Exception {
		
		MqttClient client = new MqttClient( 
			    "tcp://139.59.73.155:1883", //URI 
			    MqttClient.generateClientId(), //ClientId 
			    new MemoryPersistence()); //Persistence
		
		System.out.println("Client.."+client.getClientId());
		MqttConnectOptions options = new MqttConnectOptions();
		 
		options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
		// options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
		 
		client.connect(options);
		
		if(client.isConnected()) {
			System.out.println("MQ Client is connected!!");
			String topic = "iot/vmc/callbackresponse";
			String payload = "Hi VMC";
			client.publish( 
				    topic, // topic 
				   payload.getBytes(StandardCharsets.UTF_8), // payload 
				    2, // QoS 
				    false); // retained? 
			
	
			
			byte[] encodedPayload = new byte[0];
			try {
			    encodedPayload = payload.getBytes(StandardCharsets.UTF_8);
			    MqttMessage message = new MqttMessage(encodedPayload);
			    client.publish(topic, message);
			} catch (Exception e) {
			    e.printStackTrace();
			}
	        
	
		}
		
		

		
		

		return "";
	}
	*/
	/*public String mqqtCallBack(String s) {
		
		String topic = "foo/bar";
		int qos = 1;
		try {
			MqttClient client = new MqttClient( 
				    "tcp://broker.mqttdashboard.com:1883", //URI 
				    MqttClient.generateClientId(), //ClientId 
				    new MemoryPersistence()); //Persistence
			
			System.out.println("Client.."+client.getServerURI());
			MqttConnectOptions options = new MqttConnectOptions();
			 
			options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
			// options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
			 
			client.connect(options);
			
		    IMqttToken subToken = client.subscribe(topic, qos);
		    subToken.setActionCallback(new IMqttActionListener() {
		        @Override
		        public void onSuccess(IMqttToken asyncActionToken) {
		            // The message was published
		        }
		 
		        @Override
		        public void onFailure(IMqttToken asyncActionToken,
		                              Throwable exception) {
		            // The subscription could not be performed, maybe the user was not
		            // authorized to subscribe on the specified topic e.g. using wildcards
		 
		        }
		    });
		} catch (MqttException e) {
		    e.printStackTrace();
		}
		return s;
	}*/
	
	private static MqttConnectOptions setUpConnectionOptions(String username, String password) {
	       MqttConnectOptions connOpts = new MqttConnectOptions();
	       connOpts.setCleanSession(true);
	       connOpts.setUserName(username);
	       connOpts.setPassword(password.toCharArray());
	       return connOpts;
	   } 
}
