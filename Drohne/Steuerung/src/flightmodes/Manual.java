package flightmodes;

import main.Daten;

public class Manual implements flightmodes.Flightmode{

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		System.out.println("Modus: Manuell");
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate(float dt) {
		Daten.setThrottle(Daten.getCont_throttle());
		Daten.setRoll(Daten.getCont_roll());
		Daten.setPitch(Daten.getCont_pitch());
		Daten.setYaw(Daten.getCont_yaw());
		
	}

}
