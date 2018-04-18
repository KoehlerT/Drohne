package com.drohne.main;
import com.drohne.rf.*;

public class StartBase {

	private static RFModule antenna;
	private static byte[] toSend = new byte[32];
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Start");
		antenna = new RFModule();
		antenna.pwrUp();
		prepareSending();
		antenna.setTxRegister(toSend);
		
		while (true) {
			antenna.receive();
			//break;
			//antenna.send();
		}
	}
	
	private static void prepareSending() {
		for (int i = 0; i < toSend.length; i++) {
			toSend[i] = (byte)i;
		}
	}

}
