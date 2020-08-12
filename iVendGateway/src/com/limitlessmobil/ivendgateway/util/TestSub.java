package com.limitlessmobil.ivendgateway.util;

import java.io.IOException;
import java.sql.Timestamp;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

public class TestSub implements MqttCallback
{
   public static void main(String[] args)
   {
      String url = "tcp://144.217.89.37:1884";
      String clientId = "TestSub_"+System.currentTimeMillis();
      String topicName = "iot/vmc/callbackresponse";
      int qos = 1;
      boolean cleanSession = true;
      String userName = "llmuvmterminal";
      String password = "LLM@br0K3r#19";

      try
      {
         new TestSub(url, clientId, cleanSession, userName, password, topicName, qos);
      }
      catch (MqttException me)
      {
         System.out.println(me.getLocalizedMessage());
         System.out.println(me.getCause());
         me.printStackTrace();
      }
   }

   public TestSub(String url, String clientId, boolean cleanSession, String userName, String password, String topicName, int qos) throws MqttException
   {
      String tmpDir = System.getProperty("java.io.tmpdir");
      MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);
      MqttClient         client;
      MqttConnectOptions conOpt;

      try
      {
         conOpt = new MqttConnectOptions();
         conOpt.setMqttVersion(MqttConnectOptions.MQTT_VERSION_DEFAULT);
         conOpt.setCleanSession(cleanSession);
         if (userName != null)
            conOpt.setUserName(userName);

         if (password != null)
            conOpt.setPassword(password.toCharArray());

         // Construct an MQTT blocking mode client
         client = new MqttClient(url, clientId, dataStore);

         // Set this wrapper as the callback handler
         client.setCallback(this);

         // Connect to the MQTT server
         client.connect(conOpt);
         System.out.println("Connected to " + url + " with client ID " + client.getClientId());

         System.out.println("Subscribing to topic \"" + topicName + "\" qos " + qos);
         client.subscribe(topicName, qos);

         // Continue waiting for messages until the Enter is pressed
         System.out.println("Press <Enter> to exit");
         try
         {
            System.in.read();
         }
         catch (IOException e)
         {
            // If we can't read we'll just exit
         }

         // Disconnect the client from the server
         client.disconnect();
         System.out.println("Disconnected");

      }
      catch (MqttException e)
      {
         e.printStackTrace();
         System.out.println("Unable to set up client: " + e.toString());
         System.exit(1);
      }
   }

   public void connectionLost(Throwable cause)
   {
      System.out.println("Connection lost! " + cause.getLocalizedMessage());
      System.exit(1);
   }

   public void deliveryComplete(IMqttDeliveryToken token)
   {
   }

   public void messageArrived(String topic, MqttMessage message)
         throws MqttException
   {
      String time = new Timestamp(System.currentTimeMillis()).toString();
      System.out.println("Time:\t" + time + "  Topic:\t" + topic + "  Messageeeeeeeeeee :\t" + new String(message.getPayload()));
   }
}