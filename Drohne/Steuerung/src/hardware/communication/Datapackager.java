package hardware.communication;

import main.Daten;
import main.ProgramState;
import utility.FlyingMode;

public class Datapackager {
	
	public Datapackager(){
		
	}
	
	//-------------------------------------------BASIS------------------------------------------------------
	
	public static byte[] packageTransmit() {
		//EINST byte[] toTransmit = new byte[46];
		
		return SendBase.packageTransmitBase();
	}
	
	public static void untangleReceived(byte[] received) {
		byte controlWord = received[1];
		
		//System.out.println("CW: "+controlWord);
		ProgramState.getInstance().addControlWord(controlWord);
		int throttle = received[2] & 0x000000FF;
		throttle |= ((received[6]&0b11000000)<<2);
		throttle+=1000;
		
		int roll = received[3] & 0x000000FF;
		roll |= ((received[6]&0b0011_0000)<<4);
		roll += 1000;
		
		int pitch = received[4] & 0x000000FF;
		pitch |= ((received[6]&0b0000_1100)<<6);
		pitch += 1000;
		
		int yaw = received[5] & 0x00000FF;
		yaw |= ((received[6]&0b0000_0011)<<8);
		yaw += 1000;
		
		//System.out.println("Thr: "+throttle+" RLL: "+roll+" PTH: "+pitch+" YAW: "+yaw);
		Daten.setCont_throttle(throttle);
		Daten.setCont_roll(roll);
		Daten.setCont_pitch(pitch);
		Daten.setCont_yaw(yaw);
	}
	
	
	
	
	
	
	 //----------------------------------------------------- ARDUINO ------------------------------------------------------------------
	public static void untangleArduinoReceived(byte[] buffer) {
		ArduinoData.getArduinoData(buffer);
	}
	
	public static byte[] getArduinoSend(byte[] toSend) {
		
		ArduinoSender.getTransmitPackage(toSend);
		return toSend;
	}
	
	
	
	public static void printBinaryArray(byte[] bin) {
		for (int i = 0; i < bin.length; i++) {
			byte content = bin[i];
			String str = String.format("%8s", Integer.toBinaryString(content & 0xFF)).replace(' ', '0');
			System.out.println("Array ["+i+"]: "+str);
		}
	}
	
}
