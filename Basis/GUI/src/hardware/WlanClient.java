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
	
	public WlanClient() {
		try {
			server = new Socket("192.168.1.2", 1213);
			System.out.println("Verbunden");
			inFromServer = new Scanner(server.getInputStream());
			outToServer = new PrintStream(server.getOutputStream());
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
		byte[] buffer = new byte[24];
		try {
			int read = server.getInputStream().read(buffer, 1, 23);
			if (read >= 23) {
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
