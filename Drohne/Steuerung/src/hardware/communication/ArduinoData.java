package hardware.communication;

import main.Daten;

public class ArduinoData {
	//Buffer vars
	static int last;
	
	static int temperature;
	static int altitude;
	static int takeoff_thr;
	static int ang_yaw;
	static int ang_pt;
	static int ang_rll;
	static int lat;
	static int lon;
	static int set1;
	static int set2;
	static int set3;
	
	private ArduinoData() {}
	
	public static char getArduinoData(byte[] rcv) {
		if (rcv[6] == (byte)0x76 && rcv[7] == (byte)0x9A) {
			int loop = getLoop(rcv);
			int send = getSent(rcv);
			
			if (loop == -1 || send == -1)
				return 2;
			
			int dur = (rcv[8] & 0x000000FF) | ((rcv[9]&0x000000FF)<<8);
			Daten.setArduinoRefresh(dur);
			
			getData(loop, send);
			
		}else {
			 return 1;
		}
		
		return 0;
	}
	
	private static int getLoop(byte[] rcv) {
		//2 gleiche Bytes reichen, um bearbeitet zu werden
		int res = -1;
		if (rcv[1] == rcv[2])
			res = (rcv[1]&0x000000FF);
		if (rcv[2] == rcv[0]) 
			res = rcv[2]&0x000000FF;
		if (rcv[0] == rcv[1])
			res = rcv[1]&0x000000FF;
		
		return res;
	}
	
	private static int getSent(byte[] rcv) {
		//2 gleiche Bytes reichen, um bearbeitet zu werden
		int res = -1;
		if (rcv[5] == rcv[4])
			res = (rcv[1]&0x000000FF);
		if (rcv[4] == rcv[3]) 
			res = rcv[2]&0x000000FF;
		if (rcv[3] == rcv[5])
			res = rcv[1]&0x000000FF;
		
		return res;
	}
	
	private static void getData(int loop, int send) {
		switch(loop) {
		case 3: Daten.setError(send); break;
		case 4: Daten.setFlightInt(send); break;
		case 5: Daten.setVoltageMain(send); break;
		case 6: temperature = send; break;
		case 7: temperature |= (send << 8); if (last==6) Daten.setTemperature(temperature); break;
		case 8: Daten.setAngleRoll(send); break;
		case 9: Daten.setAnglePitch(send); break;
		case 10: Daten.setStart(send);
		case 11: altitude = send;
		case 12: altitude |= (altitude << 8); if (last == 11) Daten.setAltitude(altitude); break;
		case 13: takeoff_thr = send;
		case 14: takeoff_thr |= (takeoff_thr<<8); if (last== 13) Daten.setTakeoffThrottle(takeoff_thr);break;
		case 15: ang_yaw = send;
		case 16: ang_yaw |= (send << 8); if (last == 15) Daten.setAngleYaw(ang_yaw); break;
		case 17: Daten.setHeadingLock(send);
		case 18: Daten.setNumGpsSatellites(send);
		case 19: Daten.setFixType(send);
		case 20: lat = send;
		case 21: if (last == 20) lat |= (send << 8); else lat = 0; break;
		case 22: if (last == 21 && lat != 0)lat |= (send << 16); else lat = 0; break;
		case 23: if (last == 22 && lat != 0)Daten.setLatitude(lat | (send << 24)); break;
		case 24: lon = send;
		case 25: if (last == 24) lon |= (send << 8); else lon = 0; break;
		case 26: if (last == 25 && lon != 0)lon |= (send << 16); else lon = 0; break;
		case 27: if (last == 26 && lon != 0)Daten.setLatitude(lon | (send << 24)); break;
		case 28: set1 = send;
		case 29: if (last == 28) set1 |= (send << 8); Daten.setSet1(set1);break;
		case 30: set2 = send;
		case 31: if (last == 30) set2 |= (send << 8); Daten.setSet1(set2);break;
		case 32: set3 = send;
		case 33: if (last == 32) set3 |= (send << 8); Daten.setSet1(set3);break;
		default: break;
		}
		last = loop;
	}
}
