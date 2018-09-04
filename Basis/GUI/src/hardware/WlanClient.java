package hardware;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import main.Data;

public class WlanClient {
	
	Socket server;
	
	PrintStream outToServer;
	Scanner inFromServer;
	
	boolean connected = false;
	
	public WlanClient() {
		
	}
	
	public void connect() {
		try {
			server = new Socket("192.168.1.1", 1213);
			System.out.println("Verbunden");
			inFromServer = new Scanner(server.getInputStream());
			outToServer = new PrintStream(server.getOutputStream());
			connected = true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendControls() {
		try {
			server.getOutputStream().write(Datapackager.getTransmitPackage());
			server.getOutputStream().flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void receive() {
		//while(!connected);
		
		byte[] buffer = new byte[47];
		try {
			int read = server.getInputStream().read(buffer, 1, 46);
			if (read >= 46) {
				packageReady(buffer);
			}
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void packageReady(byte[] p) {
		Datapackager.untangleReceivedPackage(p);
	}
}
