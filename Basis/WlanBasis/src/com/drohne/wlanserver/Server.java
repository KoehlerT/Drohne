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
				System.out.println("Suche Klienten");
				
				Socket connected = server.accept();
				connection = true;
				inFromClient = new BufferedReader(new InputStreamReader(connected.getInputStream()));
				outToClient = new DataOutputStream(connected.getOutputStream());
				
				System.out.println("Client angeschlossen ");
				while (/*connected.isConnected() && !connected.isClosed()*/ true) {
					//Write
					System.out.println("Sende...");
					outToClient.writeUTF("Hi Client/ Drohne\n");
					outToClient.flush();
					System.out.println("?!");
					//Read
					int i;
					while ((i = inFromClient.read()) >=0) {
						System.out.print((char)i);
					}
					System.out.println("Status: Conn.: "+connected.isConnected()+" Cl.: "+connected.isClosed());
				}
				//connection = false;
				//System.out.println("Connected: "+connection);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Es konnte kein Server erstellt werden");
		}
	}
}
