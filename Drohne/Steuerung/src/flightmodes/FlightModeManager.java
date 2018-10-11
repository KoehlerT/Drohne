package flightmodes;

import flightmodes.programs.AltitudeHold;
import flightmodes.programs.Automatic;
import flightmodes.programs.CalibrateLevel;
import flightmodes.programs.Flightmode;
import flightmodes.programs.Forcedown;
import flightmodes.programs.Forcestop;
import flightmodes.programs.Manual;
import hardware.Beeper;
import main.Daten;

public class FlightModeManager {
	
	private static FlightModeManager inst = new FlightModeManager();
	public static FlightModeManager getInstance() {return inst;}
	
	int currentIndex;
	Flightmode current;
	
	Flightmode[] modes = new Flightmode[6];
	
	private FlightModeManager(){
		
		//Lade alle Flugprogramme
		modes[0] = new Forcestop();
		modes[1] = new Manual();
		modes[2] = new Forcedown();
		modes[3] = new Automatic();
		modes[4] = new AltitudeHold();
		modes[5] = new CalibrateLevel();
		
		current = modes[0];
	}
	
	
	
	void switchFlightmode(int index) {
		if (index == currentIndex)
			return; //Gleicher Flightmode, nichts ändert sich return
		
		current.onDisable();
		
		current = modes[index];
		currentIndex = index;
		
		Beeper.getInstance().addBeep(500);
		current.onEnable();
	}
	
	public void updateFlightmode(float deltaTime) {
		current.onUpdate(deltaTime);
	}
	
	public void sentCallback() {
		current.onCallback();
	}
	
	public int getCurrentFlightmode() {
		return currentIndex;
	}
	public static int getRequestedFlightmode() {return requestedFlightMode;}
	
	private static int requestedFlightMode;
	public static synchronized void requestFlightmode(int m) {
		requestedFlightMode = m;
	}
}
