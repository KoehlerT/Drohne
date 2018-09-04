package flightmodes;

import main.Daten;

public class Automatic implements Flightmode{
	
	float altitudeStart;
	float altitudeEnd;
	
	float velocity = 100;
	
	boolean goalReached = false;
	
	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		altitudeStart = Daten.getPrsAltitude();
		altitudeEnd = altitudeStart +2f;
		
		String kons = String.format("Autom. Flug von %f nach %f \n", altitudeStart, altitudeEnd);
		Daten.addConsole(kons);
		
		goalReached = false;
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate(float deltaTime) {
		int throttle = Daten.getThrottle();
		float altitude = Daten.getPrsAltitude();
		
		Daten.setRoll(1500);
		Daten.setYaw(1500);
		Daten.setPitch(1500);
		
		if (altitude >= altitudeEnd) {
			Daten.addConsole("Goal Reached, returning to ground\n");
			goalReached = true;
		}
		
		float diffThr = deltaTime * velocity;
		if (goalReached)
			diffThr *= -1;
		
		throttle += (int)Math.ceil(diffThr);
		throttle = clamp(throttle);
		
		//System.out.println("Thr: "+throttle + " GR: "+goalReached+ " diffThr "+diffThr);
		
		Daten.setThrottle(throttle);
		
	}
	
	
	private int clamp(int val) {
		if (val >= 2000)
			return 2000;
		if (val <= 1000)
			return 1000;
		return val;
	}
}
