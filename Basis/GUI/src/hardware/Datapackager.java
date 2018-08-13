package hardware;

import main.Data;
import panels.KonsolenFenster;
import utillity.Blume;
import utillity.FlyingMode;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import main.ControlWordHandler;

public class Datapackager {
	
	private static String nextLine = "";
	
	public static void untangleReceivedPackage(byte[] received) {
		byte receivedControl = received[1];
		ControlWordHandler.getInstance().addReceivedControlWord(receivedControl);
		//printBinaryArray(received);
		getGPSData(received);
		getPowerData(received);
		//Altitude
		int altitude = received[17]&0x000000FF;
		altitude = (altitude << 8) | (received[16]&0x000000FF);
		Data.setAltitude((float)altitude/100f);
		getStatusInfo(received);
		getControllerData(received);
		getConsoleData(received);
		getBlumen(received);
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
	
	public static void getStatusInfo(byte[] buffer) {
		
		//Ultraschall  18:lsb 19:msb
		int ulr = ((buffer[19] & 0x000000FF)<<8)|buffer[18];
		Data.setDistUltrasonic((double)ulr/100.0);
		
		//Arduino: 20: lsb 21:msb
		int ard = ((buffer[21] & 0x000000FF)<<8)|buffer[20];
		Data.setArduinoRefresh(ard);
		//Sensor: 22
		Data.setHardwareRefresh(buffer[22]);
		//Comm: 23 (in 10Hz)
		Data.setCommunicatorRefresh(buffer[23]);
		
	}
	
	public static void getControllerData(byte[] buffer) {
		int thr = (buffer[24]&0x000000FF) | ((buffer[25]&0x000000FF) << 8);
		int rll = (buffer[26]&0x000000FF) | ((buffer[27]&0x000000FF) << 8);
		int pth = (buffer[28]&0x000000FF) | ((buffer[29]&0x000000FF) << 8);
		int yaw = (buffer[30]&0x000000FF) | ((buffer[31]&0x000000FF) << 8);
		
		Data.setDrone_throttle(thr+1000);
		Data.setDrone_roll(rll+1000);
		Data.setDrone_pitch(pth+1000);
		Data.setDrone_yaw(yaw+1000);
	}
	
	public static void getConsoleData(byte[] buffer) {
		char[] newChars = new char[3];
		
		for (int i = 0; i < 3; i++) {
			newChars[i] = (char) ((buffer[32 + (i*2)]&0x000000FF) | ((buffer[32 +(i*2)+1]&0x000000FF) << 8));
		}
		
		for (int i = 0; i < 3; i++) {
			char nextCh = newChars[i];
			if (nextCh == 0)
				continue;
			if (nextCh == '\n') {
				KonsolenFenster.addText(nextLine, Color.BLACK);
				nextLine = "";
				continue;
			}
			nextLine += nextCh;
		}
		//bis [37]
	}
	
	private static List<Blume> bl = new ArrayList<Blume>(3);
	
	public static void getBlumen(byte[] buffer) {
		bl.clear();
		for (int i = 0; i < 3; i++) {
			float x = (float)(buffer[38 + (i*3) + 0] / 100.0f);
			float y = (float)(buffer[38 + (i*3) + 1] / 100.0f);
			int dist = (int)buffer[38 + (i*3) + 2];
			
			if (x != 0 && y != 0 && dist != 0) {
				System.out.println("Blume "+x+", "+y+", "+dist);
				bl.add(new Blume(x,y,dist));
			}
		}
		Data.setBlumen(bl);
	}
	
	public static byte[] getTransmitPackage() {
		byte[] toSend = new byte[8];
		
		toSend[0] = ControlWordHandler.getInstance().getNextSendingWord(); //1. Kontrol-Word
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
		//Force Stop/Down
		FlyingMode mode = Data.getFlyingMode();
		if (mode == FlyingMode.FORCEDOWN || mode == FlyingMode.FORCESTOP)
			throttle = 1000;
		if (mode == FlyingMode.FORCESTOP)
			yaw = 1000;
		
		
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
