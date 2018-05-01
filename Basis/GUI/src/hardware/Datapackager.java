package hardware;

import main.Data;
import main.ProgramState;

public class Datapackager {
	
	public static void untangleReceivedPackage(byte[] received) {
		byte receivedControl = received[1];
		ProgramState.getInstance().addReceivedControlWord(receivedControl);
		//printBinaryArray(received);
		getGPSData(received);
		getPowerData(received);
		//Altitude
		int altitude = received[17]&0x000000FF;
		altitude = (altitude << 8) | (received[16]&0x000000FF);
		Data.setAltitude((float)altitude/100f);
		
	}
	
	private static void getGPSData(byte[] buffer) {
		int lon = (buffer[4]&0x000000FF);
		lon = (lon << 8) | (buffer[3]&0x000000FF);
		lon = (lon << 8) | (buffer[2]&0x000000FF);
		int lat = (buffer[7]&0x000000FF);
		lat = (lat << 8) | (buffer[6]&0x000000FF);
		lat = (lat << 8) | (buffer[5]&0x000000FF);
		Data.setLatitude((float)lat/1000f);
		Data.setLongitude((float)lon/1000f);
	}
	
	private static void getPowerData(byte[] buffer) {
		int[] powers = new int[4];
		for (int i = 0; i < powers.length; i++) {
			powers[i] = (buffer[8+(i*2)+1]& 0x000000FF);
			powers[i] = ((powers[i] << 8)&0x0000FF00) | (buffer[8+(i*2)]& 0x000000FF);
		}
		Data.setVoltageMain((float)powers[0]/1000f);
		Data.setVoltage5V((float)powers[1]/1000f);
		Data.setVoltage3v((float)powers[2]/1000f);
		Data.setAmperage((float)powers[3]/1000f);
	}
	
	public static byte[] getTransmitPackage() {
		byte[] toSend = new byte[8];
		
		toSend[0] = Data.getControlWord(); //1. Kontrol-Word
		//2. Kontroller Inputs
		setContollerInputs(toSend);
		
		
		//Stopbytes
		toSend[toSend.length-2] = (byte)0b01001010;
		toSend[toSend.length-1] = (byte)0b10010110;
		
		return toSend;
	}
	
	private static void setContollerInputs(byte[] buffer) {
		int throttle = Data.getCont_throttle().getWert();
		int roll = Data.getCont_roll().getWert();
		int pitch = Data.getCont_pitch().getWert();
		int yaw = Data.getCont_yaw().getWert();
		//10bits Pro Control
		buffer[1] = (byte) (throttle-1000);
		buffer[2] = (byte) (roll-1000);
		buffer[3] = (byte) (pitch-1000);
		buffer[4] = (byte) (yaw-1000);
		//Von jedem Control noch die übrigen 2 MSBs
		byte MSBs = (byte)((throttle-1000) >> 8);
		MSBs = (byte)((MSBs << 2) | ((roll-1000) >> 8));
		MSBs = (byte)((MSBs << 2) | ((pitch-1000) >> 8)); 
		MSBs = (byte)((MSBs << 2) | ((yaw-1000) >> 8)); 
		buffer[5] = MSBs;
	}
	
	private static void printBinaryArray(byte[] bin) {
		for (int i = 0; i < bin.length; i++) {
			byte content = bin[i];
			String str = String.format("%8s", Integer.toBinaryString(content & 0xFF)).replace(' ', '0');
			System.out.println("Array ["+i+"]: "+str);
		}
	}
	
	
}
