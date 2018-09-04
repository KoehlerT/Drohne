package flightmodes;

import main.Daten;

public class Forcestop implements Flightmode{

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		System.out.println("Modus: Forcestop");
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate(float dt) {
		// TODO Auto-generated method stub
		Daten.setYaw(1000);
		Daten.setThrottle(1000);
	}

}
