package flightmodes.programs;

import flightmodes.FlightModeManager;
import main.Daten;
import utility.ArduinoInstruction;

public class SetTakeoffThrottle implements Flightmode{

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		ArduinoInstruction.getInst().setControl((byte) 0x20); //Change Adjustable Setting 1
		int throttle = Daten.getCont_throttle();
		ArduinoInstruction.getInst().setData(new byte[] {(byte)throttle,(byte)(throttle >> 8),(byte)(throttle >> 16),(byte)(throttle >> 24)
				,0,0,0,0,0}); //Byte Array current throttle (byte0,1,2,3)
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
		FlightModeManager.requestFlightmode(1); //Set Manual
	}

}
