package hardware.dataHandling;

import flightmodes.FlightModeManager;
import main.Daten;
import utility.ArduinoInstruction;

public class ArduinoSender {
	
	private ArduinoSender() {}
	
	public static void getTransmitPackage(byte[] buffer) {
		ArduinoInstruction inst = ArduinoInstruction.getInst();
		
		if (inst.enabled()) {
			//Has enabled instruction -> Send the instruction
			setInstruction(buffer, inst);
			FlightModeManager.getInstance().sentCallback();
		}else {
			//Send Controller Inputs as default. Shorter Latencys
			//Controller Inputs
			controllerInputs(buffer);
		}
		
	}
	
	private static void setInstruction(byte[] buffer, ArduinoInstruction inst) {
		buffer[0] = inst.getControl();
		for (int i = 1; i < 10; i++) {
			buffer[i] = inst.getData()[i-1];
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
