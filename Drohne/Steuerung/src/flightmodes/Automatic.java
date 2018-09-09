package flightmodes;

import com.koehlert.flowerflyer.main.Vector3;

import main.Daten;

public class Automatic implements Flightmode{
	
	@Override
	public void onEnable() {
		Daten.addConsole("Activating Autonomous Flight\n");
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate(float deltaTime) {
		Vector3 vel = Daten.getTargetVel();
		
		//Rechts Links: vel.x
		float rollAdjust = vel.x;
		
		int throttle = Daten.getCont_throttle();
		
		int roll = 1500 + (int)(rollAdjust * 150);
		
		Daten.setThrottle(throttle);
		Daten.setRoll(roll);
		Daten.setPitch(Daten.getCont_pitch());
		Daten.setYaw(1500);
	}
	
	
	/*private int clamp(int val) {
		if (val >= 2000)
			return 2000;
		if (val <= 1000)
			return 1000;
		return val;
	}*/
}
