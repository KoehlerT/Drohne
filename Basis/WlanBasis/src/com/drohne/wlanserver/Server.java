package com.drohne.wlanserver;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Server {
	//Danke an: https://stackoverflow.com/questions/2165006/simple-java-client-server-program
	//und https://www.youtube.com/watch?v=vCDrGJWqR8w
	
	private ServerSocket s;
	private Socket client;
	private Scanner inFromClient;
	private PrintStream outToClient;
	private Boolean running = false;
	
	public Server() {
		try {
			s = new ServerSocket(1213);
			System.out.println("Running server at: "+s.getLocalSocketAddress()); 
			running = true;
		} catch (IOException e) {
			running = false;
			System.out.println("Server konnte nicht erstellt werden");
			e.printStackTrace();
		}
	}
	
	public void acceptClients() {
		try {
			client = s.accept();
			inFromClient = new Scanner(client.getInputStream());
			outToClient = new PrintStream(client.getOutputStream());
			System.out.println("Client: "+client.getLocalSocketAddress());
		} catch (IOException e) {
			running = false;
			System.out.println("Verbindungsaufbau Fehlgeschlagen");
			e.printStackTrace();
		}
	}
	
	public int receive() {
		return inFromClient.nextInt();
	}
	
	public void sendControl(int thr, int pitch, int roll, int yaw) {
		long start = System.nanoTime();
		//outToClient.println(-1);
		/*while(inFromClient.nextInt() != -2) {
			outToClient.println(-1);
		}*/
		send(thr); send(pitch); send(roll); send(yaw);
		float dauer = (System.nanoTime()-start)/1000000000f;
		System.out.println("Übertragungsdauer: "+dauer*1000+"ms / "+1/dauer+" Hz");
		Window.instance.setPing((int)(dauer*1000));
	}
	
	public byte[] receivePicture() {
		int höhe = inFromClient.nextInt();
		int breite = inFromClient.nextInt();
		byte[] buffer = new byte[8192];
		int left = höhe * breite;
		int maxRead = buffer.length;
		ByteArrayOutputStream bb = new ByteArrayOutputStream();
		
		while (left > 0) {
			int count;
			try {
				count = client.getInputStream().read(buffer, 0, maxRead);
				left -= count;
				if (left < maxRead) {
					maxRead = left;
				}
				bb.write(buffer, 0, count);
				System.out.println("#"+left);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("Fertig");
		byte[] outp = bb.toByteArray();
		System.out.println(outp[5]);
		return outp;
	}
	
	public void send(int b) {
		outToClient.println(b);
	}
}
