package com.drohne.main;
import com.drohne.hardware.*;

public class StartBase {

	private static RFModule antenna;
	private static SerialDevice computer;
	private static byte[] toSend = new byte[32];
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Start");
		antenna = new RFModule();
		antenna.pwrUp();
		prepareSending();
		antenna.setTxRegister(toSend);
		
		computer = new SerialDevice();
		
		while (true) {
			//antenna.receive();
			//break;
			//antenna.send();
			
			computer.write(toSend);
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void prepareSending() {
		for (int i = 0; i < toSend.length; i++) {
			toSend[i] = (byte)i;
		}
	}

}
