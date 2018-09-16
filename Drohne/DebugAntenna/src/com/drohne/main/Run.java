package com.drohne.main;

import com.drohne.nrf905.NRF;

public class Run {
	
	static NRF module;
	static byte[] toSend = new byte[32];

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		module = new NRF();
		prepeareSend();
		
		module.setTransmitBuffer(toSend);
		module.send();
		
		//System.out.println("Transmitting Constant Carrier");
		while (true) {
			module.send();
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	static void prepeareSend() {
		for (int i = 0; i < toSend.length; i++) {
			toSend[i] = (byte)i;
		}
	}

}
