package wlanClient;
import java.io.*;
import java.net.*;


public class ClientThread extends Thread{

	//Danke an: https://stackoverflow.com/questions/2165006/simple-java-client-server-program
	private Socket client;
	private DataOutputStream outToServer;
	private BufferedReader inFromServer;
	
	private Boolean running = true;
	
	ClientThread() {
		try {
			client = new Socket("localhost",1213);
			inFromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
			outToServer = new DataOutputStream(client.getOutputStream());
		}catch(IOException e) {
			System.out.println("Keine Verbindung möglich");
			running = false;
		}
		
	}
	
	@Override
	public void run() {
		System.out.println("Kommunikation gestartet");
		while (running) {
			try {
				//Read
				int i;
				while ((i = inFromServer.read()) >=0) {
					System.out.print((char)i);
				}
				//Write 
				//outToServer.writeUTF("Hi Server/ Basis\n");
				//outToServer.flush();
				outToServer.writeInt(42);
				outToServer.flush();
				
			}catch (IOException e) {
				System.out.println("Serverproblem");
				e.printStackTrace();
				running = false;
			}
			
		}
	}
	
}
