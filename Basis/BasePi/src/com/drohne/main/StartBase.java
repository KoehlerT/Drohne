package com.drohne.main;
import com.drohne.hardware.*;

public class StartBase {

	private static RFModule antenna;
	private static SerialDevice computer;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Start");
		antenna = new RFModule();
		antenna.pwrUp();
		
		computer = new SerialDevice();
		
		while (true) {
			antenna.send();
			antenna.receive();
			byte[] received = antenna.getReceivedArray();
			computer.write(received);
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static RFModule getAntenna() {return antenna;}
	public static SerialDevice getSerial() {return computer;}

}
