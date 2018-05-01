package hardware;

import main.Daten;
import main.ProgramState;

public class Datapackager {
	
	public Datapackager(){
		
	}
	
	public static byte[] packageTransmit() {
		byte[] toTransmit = new byte[15];
		toTransmit[0] = Daten.getContWord();
		//GPS
		writeGPS(toTransmit);
		//Powers
		writePower(toTransmit);
		return toTransmit;
	}
	
	private static void writeGPS(byte[] buffer) {
		float lat = Daten.getLatitude();
		int latInt = (int)(lat * 1000); //7 Stellen
		buffer[1] = (byte)latInt;
		buffer[2] = (byte)(latInt >> 8);
		buffer[3] = (byte)(latInt >> 16);
		float lon = Daten.getLongitude();
		int lonInt = (int)(lon*1000);
		buffer[4] = (byte)lonInt;
		buffer[5] = (byte)(lonInt >> 8);
		buffer[6] = (byte)(lonInt >> 16);
	}
	
	private static void writePower(byte[] buffer) {
		//2 Bytes max: 65,536 V oder A
		int[] toWrite = new int[4];
		toWrite[0] = (int)(Daten.getVoltageMain()*1000); //Voltage Main
		toWrite[1] = (int)(Daten.getVoltage5v()*1000); //Voltage 5V
		toWrite[2] = (int)(Daten.getVoltage3v()*1000); //Voltage 3V
		toWrite[3] = (int)(Daten.getAmperage()*1000); //Ampere
		for (int i = 0; i < toWrite.length; i++) {
			buffer[7+(i*2)] = (byte)(toWrite[i]);
			buffer[7+(i*2)+1] = (byte)(toWrite[i]>>8);
		}
		/*for (int i = 0; i < toWrite.length; i++) {
			System.out.println("toWrite "+i+": "+toWrite[i]);
		}*/
		
		//Bis index 14
	}
	
	public static void untangleReceived(byte[] received) {
		byte controlWord = received[1];
		
		System.out.println("CW: "+controlWord);
		ProgramState.getInstance().addControlWord(controlWord);
		int throttle = received[2] & 0x00000FF;
		throttle |= ((received[6]&0b11000000)<<2);
		throttle+=1000;
		
		int roll = received[3] & 0x00000FF;
		roll |= ((received[6]&0b0011_0000)<<4);
		roll += 1000;
		
		int pitch = received[4] & 0x00000FF;
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
	
	
	public static void printBinaryArray(byte[] bin) {
		for (int i = 0; i < bin.length; i++) {
			byte content = bin[i];
			String str = String.format("%8s", Integer.toBinaryString(content & 0xFF)).replace(' ', '0');
			System.out.println("Array ["+i+"]: "+str);
		}
	}
	
}
