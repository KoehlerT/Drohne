package flightmodes;

import main.Daten;

public class Forcedown implements Flightmode {

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		System.out.println("Modus: Forcedown");
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate(float dt) {
		// TODO Auto-generated method stub
		Daten.setThrottle(1000);
	}

}
