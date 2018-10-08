package flightmodes.programs;

import utility.ArduinoInstruction;

public class CalibrateLevel implements Flightmode{
	
	
	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		ArduinoInstruction.getInst().setControl((byte)0x11);
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
	}

}
