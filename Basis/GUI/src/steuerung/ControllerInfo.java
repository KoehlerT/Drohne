package steuerung;

import main.ControlWordHandler;
import main.Data;
import utillity.FlyingMode;

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
			boolean änderung = false;
			
			boolean grob = false;
			grob = getGamepad_SH_LEFT();
			
			//Controls
			if (getGamepad_B()) { //Not Aus
				Data.setFlyingMode(FlyingMode.FORCESTOP);
				änderung = true;
			}
			if (getGamepad_A()) { //Landen
				Data.setFlyingMode(FlyingMode.FORCEDOWN);
				änderung = true;
			}
			
			if (getGamepad_Y()) {
				Data.setFlyingMode(FlyingMode.AUTOMATIC);
				änderung = true;
			}
			
			if (getGamepad_X()) { //Beep
				Data.setFlyingMode(FlyingMode.MANUAL);
				änderung = true;
			}
			
			
			
			
			//Rotationen
			int yaw = (mapValueAxesYaw(getThumb_LX(),grob));
			int pitch= (3000-mapValueAxes(getThumb_RY(),grob));
			int roll = (mapValueAxes(getThumb_RX(),grob));
			
			//Throttle
			int old = Data.getCont_throttle().getWert();
			int throttle = (getThrottle(old,getThumb_LY()));
			
			//Set Exordinary Status
			FlyingMode mode = Data.getFlyingMode();
			if (mode == FlyingMode.FORCESTOP) {
				throttle = (1000);
				yaw = (1000);
			}if (mode == FlyingMode.FORCEDOWN) {
				//throttle = (1000);
			}
			
			//D-Pad steuerung
			if (getGamepad_D_UP()) {
				pitch = 1400;
			}
			if (getGamepad_D_DOWN()) {
				pitch = 1600;
			}if (getGamepad_D_LEFT()) {
				roll = 1400;
			}if (getGamepad_D_RIGHT()) {
				roll = 1600;
			}
			
			if (getGamepad_START()) {
				throttle = 2000;
				yaw = 1000;
				pitch = 1000;
				roll = 1000;
			}
			
			if (getGamepad_SH_RIGHT()) {
				//ControlWordHandler.getInstance().addSendingWord((byte)0x2);
				//Reset all Inputs
				throttle = 1500;
				roll = 1500;
				pitch = 1500;
				yaw = 1500;
			}
			
			//Setze neue Werte
			Data.setCont_throttle(throttle);
			Data.setCont_roll(roll);
			Data.setCont_pitch(pitch);
			Data.setCont_yaw(yaw);
			
			//Control Word
			if (änderung) {
				byte cw = (byte)0x11;
				if (mode == FlyingMode.AUTOMATIC)
					cw = (byte) 0x17; //Update Takeoff Throttle 
				if (mode == FlyingMode.FORCEDOWN)
					cw = (byte) 0x11; //Manual (Disabled)
				if (mode == FlyingMode.FORCESTOP)
					cw = (byte) 0x10;
				
				ControlWordHandler.getInstance().addSendingWord(cw);
			}
			
			
		}else {
			System.out.println("Controller Fehler! Nicht Verbunden!");
		}
		
	}
	
	private int mapValueAxes(int value,boolean grob) {
		int scl = 500;
		if (grob)
			scl = 250;
		//Wert von -32768 - 32767
		//value += 32796;
		float scale = (value/32796f);
		//System.out.println(scale);
		if (scale < 0.1 && scale > -0.1) {
			scale = 0f;
		}
		int result = (int)(scale * scl);
		//Wert von 1000 - 2000
		return clamp(result + 1500);
	}
	

	private int mapValueAxesYaw(int value,boolean grob) {
		int scl = 500;
		if (grob)
			scl = 250;
		
		//Wert von -32768 - 32767
		//value += 32796;
		float scale = (value/32796f);
		if (scale < 0.3 && scale > -0.3) {
			scale = 0f;
		}
		int result = (int)(scale * scl);
		//Wert von 1000 - 2000
		return clamp(result + 1500);
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
		float scale = (value+32768)/65536f;
		if (scale < 0.55f && scale > 0.45f)
			scale = 0.6f;
		int result = (int)(scale * 150);
		result -=90;
		return clamp(oldThrottle+result);
	}
}
