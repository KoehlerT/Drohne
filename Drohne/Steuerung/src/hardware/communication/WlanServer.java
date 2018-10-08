package hardware.communication;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import flightmodes.FlightModeManager;
import hardware.Beeper;
import hardware.dataHandling.Datapackager;

public class WlanServer {
	private ServerSocket s;
	private Socket client;
	private Scanner inFromClient;
	private PrintStream outToClient;
	private Boolean running = false;
	
	private byte[] receiveBuffer = new byte[9];
	private int offset = 1; //Wg Emulation Antenne, da 1. Byte status!
	
	public WlanServer() {
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
	
	byte[] buffer = new byte[9];
	byte lastByte = 0;
	
	public void receive() {
		if (client == null)
			return;
		try {
			buffer = new byte[32];
			//int read = client.getInputStream().read(buffer, 0, 8);
			if (client.isClosed()) {
				Beeper.getInstance().addBeep(20);
			}
			int read = client.getInputStream().read(buffer);
			//System.out.println("Read "+read);
			if (read == -1)
				return;
			for (int i = 0; i<read; i++) {
				if (offset < receiveBuffer.length) {
					receiveBuffer[offset] = buffer[i];
					offset ++;
				}
				
				if (lastByte == (byte)0b01001010 && buffer[i] == (byte)0b10010110) {
					packageReady();
					offset = 1;
				}else {
					lastByte = buffer[i];
				}
			}
			
		} catch (IOException e) {
			System.out.println("CONNECTION ERROR! DRONE FORCESTOP!");
			Beeper.getInstance().addBeep(10);
			FlightModeManager.requestFlightmode(0);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void packageReady() {
		Datapackager.untangleReceived(receiveBuffer);
	}
	
	public void sendPackage() {
		byte[] toTransmit = Datapackager.packageTransmit();
		try {
			client.getOutputStream().write(toTransmit);
		} catch (IOException e) {
			
			System.out.println("CONNECTION ERROR! DRONE FORCESTOP!");
			Beeper.getInstance().addBeep(10);
			FlightModeManager.requestFlightmode(0);
			//FlightModeManager.getInstance().switchFlightmode();
			e.printStackTrace();
		}
	}
	
	public void send(int b) {
		outToClient.println(b);
	}
	
	public void closeConnection() {
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
