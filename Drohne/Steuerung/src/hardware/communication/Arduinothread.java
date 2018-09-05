package hardware.communication;

import main.Daten;
import main.Info;

public class Arduinothread extends Thread{
	
	private Arduino arduinoMng;
	
	public Arduinothread() {
		if (Info.sensorAttached) {
			arduinoMng = new Arduino();
		}
		
		this.start();
	}
	
	@Override
	public void run(){
		while (Daten.running) {
			if (Info.sensorAttached) {
				arduinoMng.sendControllerInputs();
			}
			
			
			try {
				Thread.sleep(4);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
