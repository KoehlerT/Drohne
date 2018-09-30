package hardware.communication;

import com.koehlert.flowerflyer.main.Location;

import main.Daten;
import utility.Blume;

public class SendBase {
	private SendBase() {}
	
	static int pack = 0; //0/1 je nachdem, was geschickt wird
	
	
	static byte[] packageTransmitBase() {
		byte[] toTransmit = new byte[28];
		
		if (pack == 0) {
			fillA(toTransmit);
			pack = 1;
			return toTransmit;
		}else /*(pack == 1)*/ {
			fillB(toTransmit);
			pack = 0;
			return toTransmit;
		}
		
	}
	
	private static void fillA(byte[] trans) {
		write8(trans, 0, 0); //pack 0 wird geschriebebn
		write8(trans,1,Daten.getError());
		write8(trans,2,Daten.getFlightInt());
		write8(trans,3,Daten.getVoltageMain());
		write16(trans,4,Daten.getTemperature());
		write8(trans,6,Daten.getAngleRoll());
		write8(trans,7,Daten.getAnglePitch());
		write8(trans,8,Daten.getStart());
		write16(trans,9,Daten.getAltitude());
		write16(trans,11,Daten.getTakeoffThrottle());
		write16(trans,13,Daten.getAngleYaw());
		write8(trans,15,Daten.getHeadingLock());
		write8(trans,16,Daten.getNumGpsSatellites());
		write8(trans,17,Daten.getFixType());
		write32(trans,18,Daten.getLatitude());
		writeFlower(trans,22);
		writeChars(trans,25,1);
	}
	
	private static void fillB(byte[] trans) {
		write8(trans,0,1);
		write32(trans,1,Daten.getLongitude());
		write16(trans,5,Daten.getSet1());
		write16(trans,7,Daten.getSet2());
		write16(trans,9,Daten.getSet3());
		write16(trans,11,Daten.getArduinoRefresh());
		write16(trans,13,Daten.getCommunicatorRefresh());
		write16(trans,15,Daten.getThrottle());
		write16(trans,17,Daten.getRoll());
		write16(trans,19,Daten.getPitch());
		write16(trans,21,Daten.getYaw());
		write16(trans,23,Daten.getRoll());
		
	}
	
	private static void write8(byte[] arr, int ind, int val) {
		arr[ind] = (byte)val;
	}
	private static void write16(byte[] arr, int ind, int val) {
		write8(arr, ind, val);
		write8(arr, ind+1,val>>8);
	}
	
	private static void write32(byte[] arr, int ind, int val) {
		write16(arr, ind, val);
		write16(arr, ind+2,val>>16);
	}
	private static void writeChars(byte[] arr, int ind, int numCh) {
		for (int i = 0; i < numCh; i++) {
			char next = Daten.getNextConsole();
			arr[ind + (i*2)] = (byte)next;
			arr[ind + (i*2) +1] = (byte)(next >> 8);
		}
	}
	private static void writeFlower(byte[] arr, int ind) {
		byte x = 0;
		byte y = 0;
		byte dist = 0;
		
		Location loc = Daten.getTarget();
		if (loc != null) {
			
			x = (byte)((loc.x*100) + 100);
			y = (byte)((loc.y*100)+100);
			dist = (byte)loc.dist;
		}
		
		arr[ind+0] = x;
		arr[ind+1] = y;
		arr[ind+2] = dist;
	}
}
