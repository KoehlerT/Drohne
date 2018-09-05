package flightmodes;

import main.Daten;
import main.ProgramState;
import utility.FlyingMode;

public class ControllingThread extends Thread{
	
	public ControllingThread() {
		this.setName("Controlling");
	}
	
	@Override
	public void run() {
		long startTime = System.nanoTime();
		
		while (Daten.running) {
			ProgramState.getInstance().evaluateControlWord();
			
			if (System.nanoTime() - Daten.getLastComm() > 600000000) //Wenn 6s keine Kommunikation stattgefunden hat zwischen Basis und RasPI
				Daten.setFlyingMode(FlyingMode.FORCESTOP);
			
			if (FlightModeManager.getInstance().getCurrentFM() != Daten.getFlyingMode()) {
				FlightModeManager.getInstance().switchFlightmode();
			}
			
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
