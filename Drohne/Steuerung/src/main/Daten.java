package main;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import com.koehlert.flowerflyer.main.Location;
import com.koehlert.flowerflyer.main.Vector3;

import utility.Blume;

public class Daten {
	/*Klasse für alle Sensor und Flugdaten
	 * */
	private Daten() {}
	//Programmdaten
	private static byte controlWord;
	private static int refreshArduino;
	private static int refreshCommunicator;
	private static int refreshSensorread;
	private static long lastComm;
	
	public static boolean running = true;;
	
	//Variablen: 
	//Fliegen, Fernbediengungersatz
	private static int cont_throttle; //Wert von 1000-2000 Geschwindigkeit der Motoren/Drohne
	private static int cont_roll; //Wert von 1000-2000 Links- Rechtsneigung der Drohne
	private static int cont_pitch; //Wert von 1000-2000 Vor- Zurückneigung der Drohne
	private static int cont_yaw; //Wert von 1000-2000 Links- Rechtsdrehung der Drohne
	
	private static int throttle;
	private static int roll;
	private static int pitch;
	private static int yaw;
	
	private static Vector3 targetVel;
	
	//Sensorwerte Strom
	private static int voltageMain = 10; //Spannung der Hauptstromversorgung (11V)
	
	//Sensorwerte Telemetry 
	private static int latitude;
	private static int longitude;
	private static int altitude;
	private static int error;
	private static int start;
	private static int takeoff_throttle;
	private static int flightMode;
	private static int angle_roll;
	private static int angle_pitch;
	private static int angle_yaw;
	private static int heading_lock;
	private static int fix_type;
	private static int set1,set2,set3;
	
	//Sensorwerte Umgebung
	private static int temperature;
	private static Location target;
	
	
	//Statusinformationen
	private static int numGpsSatellites;
	private static boolean gpsAvailable;
	private static Queue<Character> console = new ArrayBlockingQueue<Character>(200);
	
	//Getter
	public static synchronized byte getContWord() {return controlWord;}
	public static synchronized int getArduinoRefresh() {return refreshArduino;}
	public static synchronized int getCommunicatorRefresh() {return refreshCommunicator;}
	public static synchronized int getSensorRefresh() {return refreshSensorread;}
	public static synchronized long getLastComm() {return lastComm;}
	
	public static synchronized int getCont_throttle() {return cont_throttle;}
	public static synchronized int getCont_roll() {return cont_roll;}
	public static synchronized int getCont_pitch() {return cont_pitch;}
	public static synchronized int getCont_yaw() {return cont_yaw;}
	
	public static synchronized Vector3 getTargetVel() {return targetVel;}
	
	public static synchronized int getThrottle() {return throttle;}
	public static synchronized int getRoll() {return roll;}
	public static synchronized int getPitch() {return pitch;}
	public static synchronized int getYaw() {return yaw;}
	
	public static synchronized int getVoltageMain() {return voltageMain;}

	
	public static synchronized int getLatitude() {return latitude;}
	public static synchronized int getLongitude() {return longitude;}
	public static synchronized int getAltitude() {return altitude;}
	public static synchronized int getError() {return error;}
	public static synchronized int getStart() {return start;}
	public static synchronized int getTakeoffThrottle() {return takeoff_throttle;}
	public static synchronized int getFlightInt() {return flightMode;}
	public static synchronized int getAngleRoll() {return angle_roll;}
	public static synchronized int getAnglePitch() {return angle_pitch;}
	public static synchronized int getAngleYaw() {return angle_yaw;};
	public static synchronized int getHeadingLock() {return heading_lock;}
	public static synchronized int getFixType() {return fix_type;}
	public static synchronized int getSet1() {return set1;}
	public static synchronized int getSet2() {return set2;}
	public static synchronized int getSet3() {return set3;}
	
	public static synchronized Location getTarget() {return target;}
	
	public static synchronized int getTemperature() {return temperature;}
	
	public static synchronized int getNumGpsSatellites() {return numGpsSatellites;}
	public static synchronized boolean getGpsAvailable() {return gpsAvailable;}
	public static synchronized char getNextConsole() {return (console.peek()==null)?0:console.poll();}
	
	//Setter
	public static synchronized void setControlWord(byte cw) {controlWord = cw;}
	public static synchronized void setArduinoRefresh(int lt) {refreshArduino = lt;}
	public static synchronized void setCommunicatorRefresh(int rf) {refreshCommunicator = rf;}
	public static synchronized void setSensorRefresh(int rf) { refreshSensorread = rf;}
	public static synchronized void setLastComm(long time) {lastComm = time;}
	
	public static synchronized void setCont_throttle(int th) {cont_throttle = th;}
	public static synchronized void setCont_roll(int rl) {cont_roll = rl;}
	public static synchronized void setCont_pitch(int pt) {cont_pitch = pt;}
	public static synchronized void setCont_yaw(int yw) {cont_yaw = yw;}
	
	public static synchronized void setTargetVel(Vector3 v) {targetVel = v;}
	
	public static synchronized void setThrottle(int thr) {throttle = thr;}
	public static synchronized void setRoll(int rll) {roll = rll;}
	public static synchronized void setPitch(int pth) {pitch = pth;}
	public static synchronized void setYaw(int yw) {yaw = yw;}
	
	public static synchronized void setVoltageMain(int vm) {voltageMain = vm;}
	
	public static synchronized void setLatitude (int var) {latitude = var;}
	public static synchronized void setLongitude (int var) {longitude = var;}
	public static synchronized void setAltitude (int var) {altitude = var;}
	public static synchronized void setError (int var) {error = var;}
	public static synchronized void setStart (int var) {start = var;}
	public static synchronized void setTakeoffThrottle(int var) {takeoff_throttle = var;}
	public static synchronized void setFlightInt (int var) {flightMode = var;}
	public static synchronized void setAngleRoll (int var) {angle_roll = var;}
	public static synchronized void setAnglePitch (int var) {angle_pitch = var;}
	public static synchronized void setAngleYaw (int var) {angle_yaw = var;};
	public static synchronized void setHeadingLock (int var) {heading_lock = var;}
	public static synchronized void setFixType (int var) {fix_type = var;}
	public static synchronized void setSet1 (int var) {set1 = var;}
	public static synchronized void setSet2 (int var) {set2 = var;}
	public static synchronized void setSet3 (int var) {set3 = var;}
	
	public static synchronized void setTarget(Location loc) {target = loc;}
	
	public static synchronized void setTemperature(int newTemp) {temperature = newTemp;}
	
	public static synchronized void setNumGpsSatellites(int sat) {numGpsSatellites = sat;}
	public static synchronized void setGpsAvailable(boolean available) {gpsAvailable = available;}
	public static synchronized void addConsole(String str) {
		if (console.size() + str.length() > 199)
			return;
		for (char c : str.toCharArray()) {console.add(c);}
	}
}







