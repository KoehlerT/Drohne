package wlanClient;


import com.drohne.wlandrohne.Client;

import main.Daten;


public class ClientThread extends Thread{

	private Client c;
	
	private Boolean running = true;
	
	ClientThread(String host) {
		c = new Client(host);
		
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
			//System.out.println("Thr: "+conts[0]+" pit: "+conts[1]+" rll: "+conts[2]+" yaw: "+conts[3]);
		}
	}
	
}
