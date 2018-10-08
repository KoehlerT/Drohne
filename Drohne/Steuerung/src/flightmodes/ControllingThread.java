package flightmodes;

import main.Daten;

public class ControllingThread extends Thread{
	
	
	
	public ControllingThread() {
		this.setName("Controlling");
	}
	
	@Override
	public void run() {
		long startTime = System.nanoTime();
		
		while (Daten.running) {
			
			if (System.nanoTime() - Daten.getLastComm() > 600000000) //Wenn 6s keine Kommunikation stattgefunden hat zwischen Basis und RasPI
				FlightModeManager.requestFlightmode(0);
			
			if (FlightModeManager.getInstance().getCurrentFlightmode() != FlightModeManager.getRequestedFlightmode()) {
				FlightModeManager.getInstance().switchFlightmode(FlightModeManager.getRequestedFlightmode());
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
