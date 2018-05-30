package main;
import utility.Vector3;

public class Daten {
	/*Klasse für alle Sensor und Flugdaten
	 * */
	private Daten() {}
	//Programmdaten
	private static byte controlWord;
	
	//Variablen: 
	//Fliegen, Fernbediengungersatz
	private static int cont_throttle; //Wert von 1000-2000 Geschwindigkeit der Motoren/Drohne
	private static int cont_roll; //Wert von 1000-2000 Links- Rechtsneigung der Drohne
	private static int cont_pitch; //Wert von 1000-2000 Vor- Zurückneigung der Drohne
	private static int cont_yaw; //Wert von 1000-2000 Links- Rechtsdrehung der Drohne
	
	//Sensorwerte Strom
	private static float voltageMain = 10; //Spannung der Hauptstromversorgung (11V)
	private static float voltage5v = 5; //Spannung der 5V schiene
	private static float voltage3v = 3.3f; //Spannung der 3,3V Schiene
	private static float amperage = 30; //Stromstärke bzw Stromverbrauch
	
	//Sensorwerte Position 
	private static Vector3 tilt; //Item1: roll Item2: pitch Item3: Yaw
	private static float latitude;
	private static float longitude;
	private static float gpsAltitude;
	private static float prsAltitude;
	
	//Sensorwerte Umgebung
	private static float distanceUltrasonic; //Distanz zum nächsten gegenstand
	private static float temperature;
	private static float pressure;
	
	//Statusinformationen
	private static int numGpsSatellites;
	private static boolean gpsAvailable;
	
	//Getter
	public static synchronized byte getContWord() {return controlWord;}
	
	public static synchronized int getCont_throttle() {return cont_throttle;}
	public static synchronized int getCont_roll() {return cont_roll;}
	public static synchronized int getCont_pitch() {return cont_pitch;}
	public static synchronized int getCont_yaw() {return cont_yaw;}
	
	public static synchronized float getVoltageMain() {return voltageMain;}
	public static synchronized float getVoltage5v() {return voltage5v;}
	public static synchronized float getVoltage3v() {return voltage3v;}
	public static synchronized float getAmperage() {return amperage;}
	
	public static synchronized Vector3 getTilt() {return tilt;}
	public static synchronized float getLatitude() {return latitude;}
	public static synchronized float getLongitude() {return longitude;}
	public static synchronized float getGpsAltitude() {return gpsAltitude;}
	public static synchronized float getPrsAltitude() {return prsAltitude;}
	
	public static synchronized float getDistanceUltrasonic() {return distanceUltrasonic;}
	public static synchronized float getTemperature() {return temperature;}
	public static synchronized float getPressure() {return pressure;}
	
	public static synchronized int getNumGpsSatellites() {return numGpsSatellites;}
	public static synchronized boolean getGpsAvailable() {return gpsAvailable;}
	
	//Setter
	public static synchronized void setControlWord(byte cw) {controlWord = cw;}
	
	public static synchronized void setCont_throttle(int th) {cont_throttle = th;}
	public static synchronized void setCont_roll(int rl) {cont_roll = rl;}
	public static synchronized void setCont_pitch(int pt) {cont_pitch = pt;}
	public static synchronized void setCont_yaw(int yw) {cont_yaw = yw;}
	
	public static synchronized void setVoltageMain(float vm) {voltageMain = vm;}
	public static synchronized void setVoltage5v(float v5) {voltage5v = v5;}
	public static synchronized void setVoltage3v(float v3) {voltage3v = v3;}
	public static synchronized void setAmperage(float ap) {amperage = ap;}
	
	public static synchronized void setTilt(Vector3 tlt) {tilt = tlt;}
	public static synchronized void setLatitude(float lt) {latitude = lt;}
	public static synchronized void setLongitude(float lt) {longitude = lt;}
	public static synchronized void setGpsAltitude(float alt) {gpsAltitude = alt;}
	public static synchronized void setPrsAltitude(float alt) {prsAltitude = alt;}
	
	public static synchronized void setDistanceUltrasonic(float newDistance) {distanceUltrasonic = newDistance;}
	public static synchronized void setTemperature(float newTemp) {temperature = newTemp;}
	public static synchronized void setPressure(float newPrs) {pressure = newPrs;}
	
	public static synchronized void setNumGpsSatellites(int sat) {numGpsSatellites = sat;}
	public static synchronized void setGpsAvailable(boolean available) {gpsAvailable = available;}
}







