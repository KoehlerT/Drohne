package flightmodes.programs;

import flightmodes.FlightModeManager;
import utility.ArduinoInstruction;

public class CalibrateCompass implements Flightmode{

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		ArduinoInstruction.getInst().setControl((byte)0x11); //Control Word Calibrate Level
		ArduinoInstruction.getInst().enable();
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate(float deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCallback() {
		// TODO Auto-generated method stub
		ArduinoInstruction.getInst().disable();
		FlightModeManager.requestFlightmode(1); //Set to Manual
	}

}
