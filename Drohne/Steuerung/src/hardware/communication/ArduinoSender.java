package hardware.communication;

import main.Daten;
import utility.FlyingMode;

public class ArduinoSender {
	
	private ArduinoSender() {}
	
	public static void getTransmitPackage(byte[] buffer) {
		FlyingMode mode = Daten.getFlyingMode();
		if (mode != FlyingMode.FORCESTOP) {
			//Controller Inputs
			controllerInputs(buffer);
		}
		if (mode == FlyingMode.FORCESTOP) {
			//Send Killing Signal
			buffer[0] = 0x40; //Code Stop Motors
		}
		
	}
	
	private static void controllerInputs(byte[] buffer) {
		buffer[0] = 0x01; //Code Controller INputs
		
		int throttle = Daten.getThrottle();
		int pitch = Daten.getPitch();
		int roll = Daten.getRoll();
		int yaw = Daten.getYaw();
		
		write2(buffer,throttle,1);
		write2(buffer,roll,3);
		write2(buffer,pitch,5);
		write2(buffer,yaw,7);
	}
	
	private static void write2(byte[] arr, int val, int ind) {
		byte lb = (byte)(val & 0xFF); //Lower Byte value
		byte hb = (byte)((val >> 8)& 0xFF); //Higher Byte value
		arr[ind] = lb;
		arr[ind+1] = hb;
	}
}
