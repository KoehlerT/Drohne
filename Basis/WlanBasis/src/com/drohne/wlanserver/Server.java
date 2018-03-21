package com.drohne.wlanserver;

import java.io.*;
import java.net.*;

public class Server {
	//Danke an: https://stackoverflow.com/questions/2165006/simple-java-client-server-program
	private ServerSocket server;
	private BufferedReader inFromClient;
	private DataOutputStream outToClient;
	private Boolean connection = false;
	
	public Server() {
		try {
			server = new ServerSocket(1213);
			while (true) {
				Socket connected = server.accept();
				connection = true;
				inFromClient = new BufferedReader(new InputStreamReader(connected.getInputStream()));
				outToClient = new DataOutputStream(connected.getOutputStream());
				while (connected.isConnected()) {
					System.out.println(inFromClient.readLine());
					outToClient.writeUTF("Hi\n");
				}
				connection = false;
				System.out.println("Connected: "+connection);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Es konnte kein Server erstellt werden");
		}
	}
}
