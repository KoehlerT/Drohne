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
	public static final int numCrit = 13; //Kritisch: 7
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
	private static doubleWert voltage5v = new doubleWert("5V",true,2.5,7.5,5);
	private static doubleWert voltage3v = new doubleWert("3V",true,1.2,4.0,3.3);
	private static doubleWert amperage = new doubleWert("Strom",true,0,30,1);
	
	private static vektorenWert tilt = new vektorenWert("Rotation",false,-5,-5,-5,5,5,5,0,0,0);
	private static doubleWert latitude = new doubleWert("Latutide",true,0,0,0);
	private static doubleWert longitude = new doubleWert("Longitude",true,0,0,0);
	private static doubleWert altitude = new doubleWert("Altitude",true,-5,15,0);
	private static List<Blume> blumen = Collections.synchronizedList(new ArrayList<Blume>(3));
	
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
	public static synchronized doubleWert getVoltage5v() {return voltage5v;}
	public static synchronized doubleWert getVoltage3v() {return voltage3v;}
	public static synchronized doubleWert getAmperage() {return amperage;}
	
	public static synchronized vektorenWert getTilt() {return tilt;}
	public static synchronized doubleWert getLatitude() {return latitude;}
	public static synchronized doubleWert getLongitude() {return longitude;}
	public static synchronized doubleWert getAltitude() {return altitude;}
	public static synchronized List<Blume> getBlumen(){return blumen;}
	
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
	public static synchronized void setVoltage5V(float v5) {voltage5v.setWert((double)v5);}
	public static synchronized void setVoltage3v(float v3) {voltage3v.setWert((double)v3);}
	public static synchronized void setAmperage(float amp) {amperage.setWert((double)amp);}
	public static synchronized void setBlumen(List<Blume> bl) {blumen.clear();blumen.addAll(bl);}
	
	public static synchronized void setLatitude(float lat) {latitude.setWert((double)lat);}
	public static synchronized void setLongitude(float lon) {longitude.setWert((double)lon);}
	public static synchronized void setAltitude(float alt) {altitude.setWert((double)alt);};
	
	public static synchronized void setDistUltrasonic(double dist) {distUltrasonic.setWert(dist);}
}
