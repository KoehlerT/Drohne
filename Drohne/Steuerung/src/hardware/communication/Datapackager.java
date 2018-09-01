package hardware.communication;

import java.util.List;

import main.Daten;
import main.ProgramState;
import utility.Blume;

public class Datapackager {
	
	public Datapackager(){
		
	}
	
	//-------------------------------------------BASIS------------------------------------------------------
	
	public static byte[] packageTransmit() {
		byte[] toTransmit = new byte[46];
		toTransmit[0] = ProgramState.getInstance().getNextSeningWord();
		//GPS
		writeGPS(toTransmit);
		//Powers
		writePower(toTransmit);
		//Altitude
		int alt = (int)(Daten.getPrsAltitude() * 100);
		toTransmit[15] = (byte)alt;
		toTransmit[16] = (byte)(alt >> 8);
		
		writeStatusInfo(toTransmit);
		writeControls(toTransmit);
		writeConsoleChars(toTransmit);
		writeBlumen(toTransmit);
		
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
		//buffer[14];
		/*for (int i = 0; i < toWrite.length; i++) {
			System.out.println("toWrite "+i+": "+toWrite[i]);
		}*/
		
		//Bis index 14
	}
	
	private static void writeStatusInfo(byte[] buffer) {
		//2 bytes max: 65536
		
		//Ultraschall (3 Stellen vor Komma!) bis 655,36 dm
		float dist = Daten.getDistanceUltrasonic();
		int distTrans = (int)(dist * 100);
		if (distTrans > 65536)
			distTrans = 65536;
		buffer[17] = (byte)distTrans;
		buffer[18] = (byte)(distTrans >> 8);
		
		//Arduino UPS (4 Stellen vor Komma)
		int ard = Daten.getArduinoRefresh();
		buffer[19] = (byte)ard;
		buffer[20] = (byte)(ard >> 8);
		//Hardwarethread UPS (2 Stellen vor Komma)
		buffer[21] = (byte)Daten.getSensorRefresh();
		//Communication UPS (2 Stellen vor Komma)
		buffer[22] = (byte)Daten.getCommunicatorRefresh();
	}
	
	private static void writeControls(byte[] buffer) {
		int thr = Daten.getThrottle() -1000;
		int rll = Daten.getRoll()-1000;
		int pth = Daten.getPitch()-1000;
		int yaw = Daten.getYaw()-1000;
		
		buffer[23] = (byte)thr;
		buffer[24] = (byte)(thr >> 8);
		buffer[25] = (byte)rll;
		buffer[26] = (byte)(rll >> 8);
		buffer[27] = (byte)pth;
		buffer[28] = (byte)(pth >> 8);
		buffer[29] = (byte)yaw;
		buffer[30] = (byte)(yaw >> 8);
		
	}
	
	private static void writeConsoleChars(byte[] buffer) {
		for (int i = 0; i < 3; i++) {
			char next = Daten.getNextConsole();
			buffer[31 + (i*2)] = (byte)next;
			buffer[31 + (i*2) +1] = (byte)(next >> 8);
		}
		//bis buffer[36]
	}
	
	private static void writeBlumen(byte[] buffer) {
		List<Blume> bl = Daten.getBlumen();
		//übertrage 3 Blumen
		//1. Byte relx 2.byte rel y 3.byte dist (cm)
		for (int i = 0; i < 3; i++) {
			byte x = 0;
			byte y = 0;
			byte dist = 0;
			
			if (bl.size() > i) {
				Blume akt = bl.get(i);
				x = (byte)(akt.relX() * 100.0f);
				y = (byte)(akt.relY() * 100.0f);
				dist = (byte)akt.getDist();
			}
			
			buffer[37+(i*3)+0] = x;
			buffer[37+(i*3)+1] = y;
			buffer[37+(i*3)+2] = dist;
			
		}
		//bis buffer[45]
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
		int[] powers = new int[4];
		for (int i = 0; i < 4; i++) {
			int p = buffer[i*2+1] & 0x000000FF;
			p = (p<<8)|(buffer[i*2]&0x000000FF);
			powers[i] = p;
		}
		
		int looptime = buffer[9] & 0x000000FF;
		looptime = (looptime << 8) | (buffer[8] & 0x000000FF);
		
		System.out.println("ARD: "+looptime);
		
		Daten.setVoltage5v((float)powers[3]/1000f);
		Daten.setVoltage3v((float)powers[2]/1000f);
		Daten.setVoltageMain(((float)powers[0] / 1000f)); 
		Daten.setAmperage(((float)powers[1]/1000f));
		Daten.setArduinoRefresh((int)(1f/((float)looptime/1000_000f)));
	}
	public static void printBinaryArray(byte[] bin) {
		for (int i = 0; i < bin.length; i++) {
			byte content = bin[i];
			String str = String.format("%8s", Integer.toBinaryString(content & 0xFF)).replace(' ', '0');
			System.out.println("Array ["+i+"]: "+str);
		}
	}
	
}
