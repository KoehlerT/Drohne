package flightmodes;

import hardware.Beeper;
import main.Daten;
import utility.FlyingMode;

public class FlightModeManager {
	
	private static FlightModeManager inst = new FlightModeManager();
	public static FlightModeManager getInstance() {return inst;}
	
	Flightmode current;
	
	Flightmode[] modes = new Flightmode[4];
	FlyingMode mode;
	
	private FlightModeManager(){
		
		modes[0] = new Manual();
		modes[1] = new Automatic();
		modes[2] = new Forcedown();
		modes[3] = new Forcestop();
		
		current = modes[0];
	}
	
	
	
	void switchFlightmode() {
		FlyingMode flm = Daten.getFlyingMode();
		mode = flm;
		current.onDisable();
		switch (flm) {
		case MANUAL: current = modes[0]; break;
		case AUTOMATIC: current = modes[1]; break;
		case FORCEDOWN: current = modes[2]; break;
		case FORCESTOP: current = modes[3]; break;
		default:
			break;
		}
		
		Beeper.getInstance().addBeep(500);
		current.onEnable();
	}
	
	public void updateFlightmode(float deltaTime) {
		current.onUpdate(deltaTime);
	}
	
	public FlyingMode getCurrentFM(){
		return mode;
	}
}
