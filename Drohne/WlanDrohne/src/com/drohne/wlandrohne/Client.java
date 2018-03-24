package com.drohne.wlandrohne;
import java.io.*;
import java.net.*;
import java.util.Scanner;

import sun.security.util.Length;

public class Client {
	
	private Socket s;
	private Boolean running = true;
	private Scanner inFromServer;
	private PrintStream outToServer;
	
	public Client(){
		try {
			s = new Socket("192.168.178.50",1213);
			inFromServer = new Scanner(s.getInputStream());
			outToServer = new PrintStream(s.getOutputStream());
			
		} catch (UnknownHostException e) {
			running = false;
			System.out.println("Server nicht Gefunden");
			e.printStackTrace();
		} catch (IOException e) {
			running = false;
			System.out.println("Verbindungsfehler");
			e.printStackTrace();
		}
	}
	
	public int receive(){
		return inFromServer.nextInt();
	}
	
	public int[] receiveControls(){
		int[] res = new int[4];
		//while(inFromServer.nextInt() != -1);
		//outToServer.println(-2);
		for (int i = 0; i < res.length; i++){
			res[i] = receive();
			//System.out.println(i+ " "+res[i]);
		}
		return res;
		
	}
	
	
	public void send(int b){
		outToServer.println(b);
	}
	
	public void sendPicture(byte[][] pic){
		outToServer.println(pic[0].length);
		outToServer.println(pic.length);
		byte[] buffer = new byte[pic[0].length*pic.length];
		try{Thread.sleep(100);}catch (InterruptedException e){}
		long start = System.nanoTime();
		for (int x = 0; x < pic.length; x ++){
			ByteArrayInputStream inp = new ByteArrayInputStream(pic[x]);
			int len = 0;
			try{
				while ((len = inp.read(buffer))>0){
					s.getOutputStream().write(buffer, 0, len);
					//s.getOutputStream().flush();
				}
			}catch (IOException e){e.printStackTrace();}
		}
		long delta = System.nanoTime() - start;
		System.out.println("Übertragung des bildes in "+(delta/1000000)+"ms");
		
		
	}
}
