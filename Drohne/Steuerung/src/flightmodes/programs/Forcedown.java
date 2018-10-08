package flightmodes.programs;

import main.Daten;
import utility.ArduinoInstruction;

public class Forcedown implements Flightmode {

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		System.out.println("Modus: Forcedown");
		ArduinoInstruction.getInst().enable();
		ArduinoInstruction.getInst().setControl((byte)0x40);
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		ArduinoInstruction.getInst().disable();
	}

	@Override
	public void onUpdate(float dt) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCallback() {
		// TODO Auto-generated method stub
		
	}

}
