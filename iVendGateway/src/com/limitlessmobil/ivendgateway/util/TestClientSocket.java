package com.limitlessmobil.ivendgateway.util;

import java.io.*;
import java.net.*;

public class TestClientSocket {
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        //get the localhost IP address, if server is running on some other IP, you need to use that
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        for(int i=0; i<5;i++){
            //establish socket connection to server
        	
//            socket = new Socket("172.20.10.2", 9000);
        	socket = new Socket("localhost", 9000);
        	
            //write to socket using ObjectOutputStream
            oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Sending request to Socket Server");
            if(i==4)oos.writeObject("exit");
            else oos.writeObject(""+i);
            //read the server response message
            ois = new ObjectInputStream(socket.getInputStream());
            String message = "Test Message";
            System.out.println("Message: " + message);
            //close resources
            ois.close();
            oos.close();
            Thread.sleep(100);
        }
    }
}