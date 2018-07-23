package steuerung;

import main.Data;

public class ControllerInfo {
	//Dank an: https://www.codeproject.com/Articles/26949/Xbox-Controller-Input-in-C-with-XInput
	
	
	static {
		if (System.getProperty("sun.arch.data.model").equals("64")) {
			System.out.println("Load 64 bit Library");
			System.loadLibrary("XBoxInput64");
		}else {
			System.out.println("Load 32 bit Library");
			System.loadLibrary("XBoxInput86");
		}
		
	}
	
	private native int initController();
	private native int deleteController();
	private native boolean isConnected();
	
	private native boolean getGamepad_A();
	private native boolean getGamepad_B();
	private native boolean getGamepad_X();
	private native boolean getGamepad_Y();
	private native boolean getGamepad_D_UP();
	private native boolean getGamepad_D_DOWN();
	private native boolean getGamepad_D_LEFT();
	private native boolean getGamepad_D_RIGHT();
	private native boolean getGamepad_START();
	private native boolean getGamepad_BACK();
	private native boolean getGamepad_TH_RIGHT();
	private native boolean getGamepad_TH_LEFT();
	private native boolean getGamepad_SH_RIGHT();
	private native boolean getGamepad_SH_LEFT();
	
	private native int getTrigger_LEFT();
	private native int getTrigger_RIGHT();
	private native int getThumb_LX();
	private native int getThumb_LY();
	private native int getThumb_RX();
	private native int getThumb_RY();
	
	private native void Vibrate(float left, float right);
	
	public ControllerInfo() {
		System.out.println("Try to Initialize");
		int res = initController();
		System.out.println("Controller Init: "+res);
	}
	
	public void Update() {
		//B: Not Aus
		//A: Throttle = 0
		//Linkes Pad Y + -> Throttle Up
		//Linkes Pad Y - -> Throttle Down
		
		if (isConnected()) {
			boolean forceStop = Data.getForceStop();
			boolean forceDown = Data.getForceDown();
			
			
			//Controls
			if (getGamepad_B()) {
				forceStop = !forceStop;
				Data.setForceStop(forceStop);
			}
			if (getGamepad_A()) {
				forceDown = !forceDown;
				Data.setForceDown(forceDown);
			}
			//Rotationen
			int yaw = (mapValueAxesYaw(getThumb_LX()));
			int pitch= (3000-mapValueAxes(getThumb_RY()));
			int roll = (mapValueAxes(getThumb_RX()));
			
			//Throttle
			int old = Data.getCont_throttle().getWert();
			int throttle = (getThrottle(old,getThumb_LY()));
			
			//Set Exordinary Status
			//TODO: Nur einmal Wert setzen!
			if (forceStop) {
				throttle = (1000);
				pitch = (1000);
			}if (forceDown) {
				throttle = (1000);
			}
			
			
			//Setze neue Werte
			Data.setCont_throttle(throttle);
			Data.setCont_roll(roll);
			Data.setCont_pitch(pitch);
			Data.setCont_yaw(yaw);
			
		}else {
			System.out.println("Controller Fehler! Nicht Verbunden!");
		}
		
	}
	
	private int mapValueAxes(int value) {
		//Wert von -32768 - 32767
		value += 32796;
		float scale = (value/65536f);
		int result = (int)(scale * 500);
		//Wert von 1000 - 2000
		return clamp(result + 1250);
	}
	
	private int mapValueAxesYaw(int value) {
		//Wert von -32768 - 32767
		value += 32796;
		float scale = (value/65536f);
		int result = (int)(scale * 1000);
		//Wert von 1000 - 2000
		return clamp(result + 1000);
	}
	
	private int clamp(int value) {
		if (value < 1000)
			return 1000;
		if (value > 2000)
			return 2000;
		else
			return value;
	}
	
	private int getThrottle(int oldThrottle, int value) {
		float scale = (value+32796)/65536f;
		if (scale < 0.52f && scale > 0.48f)
			scale = 0;
		int result = (int)(scale * 100);
		result -=60;
		return clamp(oldThrottle+result);
	}
}
