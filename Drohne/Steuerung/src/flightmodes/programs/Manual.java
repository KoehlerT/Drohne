package flightmodes.programs;

import main.Daten;
import utility.ArduinoInstruction;

public class Manual implements flightmodes.programs.Flightmode{
	
	private boolean messageGot = false;
	
	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		System.out.println("Modus: Manuell");
		ArduinoInstruction.getInst().setControl((byte)0x31);
		ArduinoInstruction.getInst().setData(new byte[] {0,0,0,0,0,0,0,0,0}); //Send Altitude Hold, 0 to disable all Automatic modes
		ArduinoInstruction.getInst().enable();
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		messageGot = false;
	}

	@Override
	public void onUpdate(float dt) {
		if (messageGot == true) {
			ArduinoInstruction.getInst().disable();
		
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
