package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utillity.Blume;
import utillity.FlyingMode;
import wert.*;

public class Data {
	
	private Data() {}
	
	//ProgrammKonstanten
	public static final int numCrit = 23; //Kritisch: 7
	public static final int numUnCrit = 9; //Unkritisch: 7
	public static final String port = "COM3";
	
	//Programmdaten
	private static FlyingMode mode = FlyingMode.MANUAL;
	private static byte controlWord;
	
	//Statusinformationen
	private static intWert arduinoRefresh = new intWert("Arduino Updates",true,0,4000,1000);
	private static intWert communicatorRefresh = new intWert("Communicator Updates (10Hz)",false,0,150,100);
	private static intWert hardwareRefresh = new intWert("Hardware Updates",false,0,50,20);
	
	
	//Flugdaten
	private static intWert cont_throttle= new intWert("Throttle",false,1000,2000,1000);
	private static intWert cont_pitch = new intWert("Pitch",false,1000,2000,1500);
	private static intWert cont_roll = new intWert("Roll",false,1000,2000,1500);
	private static intWert cont_yaw = new intWert("Yaw",false,1000,2000,1500);
	
	private static intWert drone_throttle= new intWert("Throttle",false,1000,2000,1000);
	private static intWert drone_pitch = new intWert("Pitch",false,1000,2000,1500);
	private static intWert drone_roll = new intWert("Roll",false,1000,2000,1500);
	private static intWert drone_yaw = new intWert("Yaw",false,1000,2000,1500);
	
	private static doubleWert voltageMain = new doubleWert("Hauptspannung",true,10.0,14.0,11.0);
	
	private static vektorenWert tilt = new vektorenWert("Rotation",false,-5,-5,-5,5,5,5,0,0,0);
	private static intWert latitude = new intWert("Latutide",true,0,0,0);
	private static intWert longitude = new intWert("Longitude",true,0,0,0);
	private static intWert altitude = new intWert("Altitude",true,-5,15,0);
	private static intWert error = new intWert("Error",true,0,10,-1);
	private static intWert start = new intWert("Start",true,0,5,-1);
	private static intWert takeoff_throttle = new intWert("Takeoff",true,0,2000,-1);
	private static intWert flightModeInt = new intWert("Flightmode",true,0,5,-1);
	private static intWert temperature = new intWert("temperature",true,20,30,0);
	private static intWert angleRoll = new intWert("angle Roll",true, -100,100,0);
	private static intWert anglePitch = new intWert("angle Pitch",true,-100,100,0);
	private static intWert angleYaw = new intWert("angle Yaw",true,-100,100,0);
	private static intWert headingLock = new intWert("heading Lock",true,0,0,0);
	private static intWert fixType = new intWert("Fix type",true,0,0,0);
	private static doubleWert set1 = new doubleWert("Set 1",true,0,0,0);
	private static doubleWert set2 = new doubleWert("Set 2",true,0,0,0);
	private static doubleWert set3 = new doubleWert("Set 3",true,0,0,0);
	
	private static Blume blume;
	
	private static doubleWert distUltrasonic = new doubleWert("Distanz",false,3.0,50.0,10.0);
	
	private static intWert numGpsSatellites = new intWert("GPS Satelliten",false,3,15,3);
	
	//Getter
	
	public static synchronized FlyingMode getFlyingMode() {return mode;}
	public static synchronized byte getControlWord() {return controlWord;}
	
	//Statusinformationen
	public static synchronized intWert getArduinoRefresh() {return arduinoRefresh;}
	public static synchronized intWert getCommunicatorRefresh() {return communicatorRefresh;}
	public static synchronized intWert getHardwareRefresh() {return hardwareRefresh;}
	
	//-Flugdaten
	public static synchronized intWert getCont_throttle() {return cont_throttle;}
	public static synchronized intWert getCont_pitch() {return cont_pitch;}
	public static synchronized intWert getCont_roll() {return cont_roll;}
	public static synchronized intWert getCont_yaw() {return cont_yaw;}
	
	public static synchronized intWert getDrone_throttle() {return drone_throttle;}
	public static synchronized intWert getDrone_pitch() {return drone_pitch;}
	public static synchronized intWert getDrone_roll() {return drone_roll;}
	public static synchronized intWert getDrone_yaw() {return drone_yaw;}
	
	public static synchronized doubleWert getVoltageMain() {return voltageMain;}
	
	public static synchronized vektorenWert getTilt() {return tilt;}
	public static synchronized intWert getLatitude() {return latitude;}
	public static synchronized intWert getLongitude() {return longitude;}
	public static synchronized intWert getAltitude() {return altitude;}
	public static synchronized intWert getError() {return error;}
	public static synchronized intWert getStart(){return start;}
	public static synchronized intWert getTakeoffThrottle() {return takeoff_throttle;}
	public static synchronized intWert getFlightModeInt() {return flightModeInt;}
	public static synchronized intWert getTemoerature() {return temperature;}
	public static synchronized intWert getAngleRoll() {return angleRoll;}
	public static synchronized intWert getAnglePitch() {return anglePitch;}
	public static synchronized intWert getAngleYaw() {return angleYaw;}
	public static synchronized intWert getHeadingLock() {return headingLock;}
	public static synchronized intWert getFixType() {return fixType;}
	public static synchronized doubleWert getSet1() {return set1;}
	public static synchronized doubleWert getSet3() {return set3;}
	public static synchronized doubleWert getSet2() {return set2;}
	
	
	public static synchronized Blume getBlumen(){return blume;}
	
	public static doubleWert getDistUltrasonic() {return distUltrasonic;}
	
	public static intWert getNumGpsSatellites() {return numGpsSatellites;}
	
	
	//Setter
	public static synchronized void setFlyingMode(FlyingMode newMode) {mode = newMode;}
	public static synchronized void setContrWord(byte cw) {controlWord = cw;}
	
	//Stausinformatioenen
	public static synchronized void setArduinoRefresh(int rf) {arduinoRefresh.setWert(rf);}
	public static synchronized void setHardwareRefresh(int rf) {hardwareRefresh.setWert(rf);}
	public static synchronized void setCommunicatorRefresh(int rf) {communicatorRefresh.setWert(rf);}
	
	public static synchronized void setCont_throttle(int thr) {cont_throttle.setWert(thr);}
	public static synchronized void setCont_roll(int roll) {cont_roll.setWert(roll);}
	public static synchronized void setCont_pitch(int pitch) {cont_pitch.setWert(pitch);}
	public static synchronized void setCont_yaw(int yaw) {cont_yaw.setWert(yaw);}
	
	public static synchronized void setDrone_throttle(int thr) {drone_throttle.setWert(thr);}
	public static synchronized void setDrone_roll(int roll) {drone_roll.setWert(roll);}
	public static synchronized void setDrone_pitch(int pitch) {drone_pitch.setWert(pitch);}
	public static synchronized void setDrone_yaw(int yaw) {drone_yaw.setWert(yaw);}
	
	public static synchronized void setVoltageMain(float vm) {voltageMain.setWert((float)vm);}
	public static synchronized void setBlume(Blume bl) {blume = bl;}
	
	public static synchronized void setLatitude(int lat) {latitude.setWert((int)lat);}
	public static synchronized void setLongitude(int lon) {longitude.setWert((int)lon);}
	public static synchronized void setAltitude(int alt) {altitude.setWert((int)alt);};
	public static synchronized void setError(int var) { error.setWert(var);}
	public static synchronized void setStart(int var){ start.setWert(var);}
	public static synchronized void setTakeoffThrottle(int var) { takeoff_throttle.setWert(var);}
	public static synchronized void setFlightModeInt(int var) { flightModeInt.setWert(var);}
	public static synchronized void setTemperature(int var) {temperature.setWert(var);}
	public static synchronized void setAngleRoll(int var) { angleRoll.setWert(var);}
	public static synchronized void setAnglePitch(int var) { anglePitch.setWert(var);}
	public static synchronized void setAngleYaw(int var) { angleYaw.setWert(var);}
	public static synchronized void setHeadingLock(int var) { headingLock.setWert(var);}
	public static synchronized void setFixType(int var) { fixType.setWert(var);}
	public static synchronized void setSet1(float var) { set1.setWert(var);}
	public static synchronized void setSet3(float var) { set3.setWert(var);}
	public static synchronized void setSet2(float var) { set2.setWert(var);}
	
	public static synchronized void setDistUltrasonic(double dist) {distUltrasonic.setWert(dist);}

	public static synchronized void setNumGpsSatellites(int num) {numGpsSatellites.setWert(num);}
}
