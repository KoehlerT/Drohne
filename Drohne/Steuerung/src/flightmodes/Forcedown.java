package flightmodes;

import main.Daten;

public class Forcedown implements Flightmode {

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		System.out.println("Modus: Forcedown");
		Daten.setContrArd((byte)0x11);
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		Daten.setContrArd((byte)0x12);
	}

	@Override
	public void onUpdate(float dt) {
		// TODO Auto-generated method stub
		//Daten.setThrottle(1000);
		Daten.setThrottle(Daten.getCont_throttle());
		Daten.setRoll(Daten.getCont_roll());
		Daten.setPitch(Daten.getCont_pitch());
		Daten.setYaw(Daten.getCont_yaw());
	}

}
