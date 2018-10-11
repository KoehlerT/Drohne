package flightmodes.programs;

import main.Daten;
import utility.ArduinoInstruction;

public class AltitudeHold implements Flightmode{
	
	private boolean messageGot = false;
	
	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		ArduinoInstruction.getInst().setControl((byte)0x31);
		ArduinoInstruction.getInst().setData(new byte[] {1,0,0,0,0,0,0,0,0});
		ArduinoInstruction.getInst().enable();
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate(float deltaTime) {
		// TODO Auto-generated method stub
		if (messageGot == true) {
			ArduinoInstruction.getInst().disable();
		
		//Controler Inputs werden standardmäßig übertragen
		Daten.setThrottle(Daten.getCont_throttle());
		Daten.setRoll(Daten.getCont_roll());
		Daten.setPitch(Daten.getCont_pitch());
		Daten.setYaw(Daten.getCont_yaw());
		}
	}

	@Override
	public void onCallback() {
		// TODO Auto-generated method stub
		messageGot = true;
	}

}
