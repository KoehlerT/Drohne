package flightmodes.programs;

import com.koehlert.flowerflyer.main.Vector3;

import main.Daten;
import utility.ArduinoInstruction;

public class Follow implements Flightmode{
	
	private final int operationRange = 200;
	
	@Override
	public void onEnable() {
		System.out.println("Modus: Follow");
		ArduinoInstruction.getInst().setControl((byte)0x31);
		ArduinoInstruction.getInst().setData(new byte[] {0,0,0,0,0,0,0,0,0}); //Send Altitude Hold, 0 to disable all Automatic modes
		ArduinoInstruction.getInst().enable();
		
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate(float deltaTime) {
		int cont_thr = Daten.getCont_throttle();
		int cont_pth = Daten.getCont_pitch();
		int cont_rll = Daten.getCont_roll();
		int cont_yaw = Daten.getCont_yaw();
		
		Vector3 target = Daten.getTargetVel();
		
		//Verrechne die Controller inputs mit dem Ziel
		//target value -1 - 1
		
		int roll = cont_rll + (int)(target.x * operationRange);
		roll = capValue(roll);
		
		int pitch = cont_pth + (int)(target.y * operationRange);
		pitch = capValue(pitch);
		
		Daten.setRoll(roll);
		Daten.setThrottle(cont_thr);
		Daten.setPitch(pitch);
		Daten.setYaw(cont_yaw);
		
	}
	
	public int capValue(int v) {
		if (v > 2000)
			return 2000;
		if (v < 1000)
			return 1000;
		return v;
	}

	@Override
	public void onCallback() {
		ArduinoInstruction.getInst().disable();
	}

}
