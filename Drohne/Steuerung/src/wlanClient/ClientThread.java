package wlanClient;


import com.drohne.wlandrohne.Client;

import main.Daten;


public class ClientThread extends Thread{

	private Client c;
	
	private Boolean running = true;
	
	ClientThread() {
		c = new Client();
		
	}
	
	@Override
	public void run() {
		while (running) {
			int[] conts = c.receiveControls();
			//Reihenfolge: Throttle, pitch, roll, yaw
			Daten.setCont_throttle(conts[0]);
			Daten.setCont_pitch(conts[1]);
			Daten.setCont_roll(conts[2]);
			Daten.setCont_yaw(conts[3]);
		}
	}
	
}
