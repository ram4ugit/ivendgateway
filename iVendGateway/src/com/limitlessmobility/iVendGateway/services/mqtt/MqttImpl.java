package com.limitlessmobility.iVendGateway.services.mqtt;

import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MqttImpl {
	
	 @RequestMapping(value = "/mqttcb", method = RequestMethod.POST)
	    @ResponseBody
	    
	    public String mqqtCallBack(String s) {
	        
	        String topic = "iot/vmc";;
	        int qos = 1;
	        try{
	            MqttClient client1 = new MqttClient(
	                    "tcp://broker.hivemq.com:1883",
	                    MqttClient.generateClientId(),
	                    new MemoryPersistence()
	            );

	           
	              client1.connect();
	              client1.subscribe(topic,qos);
	              client1.setCallback(new MqttCallback() {
	                 
	                @Override
	                public void connectionLost(Throwable cause) { //Called when the client lost the connection to the broker 
	                }
	     
	                @Override
	                public void messageArrived(String topic, MqttMessage message) throws Exception {
	                    System.out.println(topic + ": " + Arrays.toString(message.getPayload()));
	                }
	     
	                @Override
	                public void deliveryComplete(IMqttDeliveryToken token) {//Called when a outgoing publish is complete 
	                }
	            });
	            
	             
	        }
	            
	        catch(MqttException e){

	        }
	        
	        return s;
	    }

/*	@RequestMapping(value = "/getmqtt", method = RequestMethod.POST)
	@ResponseBody
public String mqqtCallBack(String s) {
		
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
				public void onFailure(IMqttToken arg0, Throwable arg1) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(IMqttToken arg0) {
					// TODO Auto-generated method stub
					
				}
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
}
