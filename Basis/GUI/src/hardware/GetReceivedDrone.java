package hardware;

import java.awt.Color;

import main.Data;
import panels.KonsolenFenster;
import utillity.Blume;

public class GetReceivedDrone {
	private GetReceivedDrone() {}
	
	public static void untangleReceived(byte[] arr) {
		byte pack = arr[1];
		if (pack == 0)
			untangleA(arr);
		if (pack == 1)
			untangleB(arr);
	}
	private static void untangleA(byte[] arr) {
		Data.setError(read8(arr,2));
		Data.setFlightModeInt(read8(arr,3));
		Data.setVoltageMain((float)read8(arr,4)/10f);
		Data.setTemperature((float) ((float) ((float)read16(arr,5)/(float)340.00)+36.53));
		Data.setAngleRoll(read8(arr,7));
		Data.setAnglePitch(read8(arr,8));
		Data.setStart(read8(arr,9));
		Data.setAltitude(read16(arr,10)-1000);
		Data.setTakeoffThrottle(read16(arr,12));
		Data.setAngleYaw(read16(arr,14));
		Data.setHeadingLock(read8(arr,16));
		Data.setNumGpsSatellites(read8(arr,17));
		Data.setFixType(read8(arr,18));
		Data.setLatitude(read32(arr,19));
		readBlume(arr,23);
		readConsoleData(arr,26,1);
	}
	
	private static void untangleB(byte[] arr) {
		Data.setLongitude(read32(arr,2));
		Data.setSet1((float)read16(arr,6)/100f);
		Data.setSet2((float)read16(arr,8)/100f);
		Data.setSet3((float)read16(arr,10)/100f);
		Data.setArduinoRefresh(read16(arr,12));
		Data.setCommunicatorRefresh(read16(arr,14));
		Data.setDrone_throttle(read16(arr,16));
		Data.setDrone_roll(read16(arr,18));
		Data.setDrone_pitch(read16(arr,20));
		Data.setDrone_yaw(read16(arr,22));
	}
	
	
	
	private static int read8(byte[] arr, int ind){
		return (arr[ind]&0x000000FF);
	}
	private static int read16(byte[] arr, int ind) {
		return read8(arr,ind) | (read8(arr,ind+1)<<8);
	}
	private static int read32(byte[]arr, int ind) {
		return read16(arr,ind)| (read16(arr,ind+2)<<16);
	}
	
	private static void readBlume(byte[] arr, int ind) {
		int x = read8(arr, ind);
		int y = read8(arr,ind+1);
		int d = read8(arr, ind+2);
		
		Blume b = new Blume((float)(x-100)/100f, (float)(y-100)/100f, d);
		//System.out.println("Blume: "+b.toString());
		Data.setBlume(b);
	}
	private static String nextLine = "";
	public static void readConsoleData(byte[] buffer, int ind,int len) {
		char[] newChars = new char[len];
		
		for (int i = 0; i < len; i++) {
			newChars[i] = (char) ((buffer[ind + (i*2)]&0x000000FF) | ((buffer[ind +(i*2)+1]&0x000000FF) << 8));
		}
		
		for (int i = 0; i < len; i++) {
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
	}
}
