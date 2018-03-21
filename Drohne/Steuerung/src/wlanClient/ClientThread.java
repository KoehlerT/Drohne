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
			running = false;
		}
		
	}
	
	@Override
	public void run() {
		while (running) {
			try {
				String recv = inFromServer.readLine();
				outToServer.writeUTF("Empfangen\n");
				System.out.println("Empfangen: "+recv);
			}catch (IOException e) {
				System.out.println("Serverproblem");
				running = false;
			}
			
		}
	}
	
}
