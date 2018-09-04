package flightmodes;

import main.Daten;
import main.ProgramState;

public class ControllingThread extends Thread{
	
	public ControllingThread() {
		this.setName("Controlling");
	}
	
	@Override
	public void run() {
		long startTime = System.nanoTime();
		
		while (Daten.running) {
			ProgramState.getInstance().evaluateControlWord();
			
			long diff = System.nanoTime() - startTime;
			FlightModeManager.getInstance().updateFlightmode((float)diff/1000000000.0f);
			startTime = System.nanoTime();
			
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
