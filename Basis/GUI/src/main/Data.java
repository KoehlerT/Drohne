package main;

import wert.*;

public class Data {
	
	private Data() {}
	
	//ProgrammKonstanten
	public static final int numCrit = 6; //Kritisch: 6
	public static final int numUnCrit = 7; //Unkritisch: 7
	public static final String port = "COM6";
	
	//Programmdaten
	private static boolean manuMode = false;
	private static byte controlWord;
	
	//Flugdaten
	private static intWert cont_throttle= new intWert("Throttle",false,1000,2000,1000);
	private static intWert cont_pitch = new intWert("Pitch",false,1000,2000,1000);
	private static intWert cont_roll = new intWert("Roll",false,1000,2000,1000);
	private static intWert cont_yaw = new intWert("Yaw",false,1000,2000,1000);
	
	private static doubleWert voltageMain = new doubleWert("Hauptspannung",true,10.0,14.0,11.0);
	private static doubleWert voltage5v = new doubleWert("5V",true,2.5,7.5,5);
	private static doubleWert voltage3v = new doubleWert("3V",true,1.2,4.0,3.3);
	private static doubleWert amperage = new doubleWert("Strom",true,0,30,1);
	
	private static vektorenWert tilt = new vektorenWert("Rotation",false,-5,-5,-5,5,5,5,0,0,0);
	private static doubleWert latitude = new doubleWert("Latutide",true,0,0,0);
	private static doubleWert longitude = new doubleWert("Longitude",true,0,0,0);
	
	private static doubleWert distUltrasonic = new doubleWert("Frei",false,3.0,50.0,10.0);
	
	private static intWert numGpsSatellites = new intWert("GPS Satelliten",false,3,15,3);
	
	//Getter
	
	public static synchronized boolean getManuMode() {return manuMode;}
	public static synchronized byte getControlWord() {return controlWord;}
	
	//-Flugdaten
	public static synchronized intWert getCont_throttle() {return cont_throttle;}
	public static synchronized intWert getCont_pitch() {return cont_pitch;}
	public static synchronized intWert getCont_roll() {return cont_roll;}
	public static synchronized intWert getCont_yaw() {return cont_yaw;}
	
	public static synchronized doubleWert getVoltageMain() {return voltageMain;}
	public static synchronized doubleWert getVoltage5v() {return voltage5v;}
	public static synchronized doubleWert getVoltage3v() {return voltage3v;}
	public static synchronized doubleWert getAmperage() {return amperage;}
	
	public static synchronized vektorenWert getTilt() {return tilt;}
	public static synchronized doubleWert getLatitude() {return latitude;}
	public static synchronized doubleWert getLongitude() {return longitude;}
	
	public static doubleWert getDistUltrasonic() {return distUltrasonic;}
	
	public static intWert getNumGpsSatellites() {return numGpsSatellites;}
	
	
	//Setter
	public static synchronized void setManuMode(boolean mm) {manuMode = mm;}
	public static synchronized void setContrWord(byte cw) {controlWord = cw;}
	
	public static synchronized void setCont_throttle(int thr) {cont_throttle.setWert(thr);}
	public static synchronized void setCont_roll(int roll) {cont_roll.setWert(roll);}
	public static synchronized void setCont_pitch(int pitch) {cont_pitch.setWert(pitch);}
	public static synchronized void setCont_yaw(int yaw) {cont_yaw.setWert(yaw);}
}
