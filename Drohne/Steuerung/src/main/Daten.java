package main;
import utility.Vector3;

public class Daten {
	/*Klasse f�r alle Sensor und Flugdaten
	 * */
	private Daten() {}
	//Variablen: 
	//Fliegen, Fernbediengungersatz
	private static int cont_throttle; //Wert von 1000-2000 Geschwindigkeit der Motoren/Drohne
	private static int cont_roll; //Wert von 1000-2000 Links- Rechtsneigung der Drohne
	private static int cont_pitch; //Wert von 1000-2000 Vor- Zur�ckneigung der Drohne
	private static int cont_yaw; //Wert von 1000-2000 Links- Rechtsdrehung der Drohne
	
	//Sensorwerte Strom
	private static float voltageMain; //Spannung der Hauptstromversorgung (11V)
	private static float voltage5v; //Spannung der 5V schiene
	private static float voltage3v; //Spannung der 3,3V Schiene
	private static float amperage; //Stromst�rke bzw Stromverbrauch
	
	//Sensorwerte Position 
	private static Vector3 tilt; //Item1: roll Item2: pitch Item3: Yaw
	
	//Getter
	public static synchronized int getCont_throttle() {return cont_throttle;}
	public static synchronized int getCont_roll() {return cont_roll;}
	public static synchronized int getCont_pitch() {return cont_pitch;}
	public static synchronized int getCont_yaw() {return cont_yaw;}
	
	public static synchronized float getVoltageMain() {return voltageMain;}
	public static synchronized float getVoltage5v() {return voltage5v;}
	public static synchronized float getvoltage3v() {return voltage3v;}
	public static synchronized float getAmperage() {return amperage;}
	
	public static synchronized Vector3 getTilt() {return tilt;}
	
	//Setter
	public static synchronized void setCont_throttle(int th) {cont_throttle = th;}
	public static synchronized void setCont_roll(int rl) {cont_roll = rl;}
	public static synchronized void setCont_pitch(int pt) {cont_pitch = pt;}
	public static synchronized void setCont_yaw(int yw) {cont_yaw = yw;}
	
	public static synchronized void setVoltageMain(float vm) {voltageMain = vm;}
	public static synchronized void setVoltage5v(float v5) {voltage5v = v5;}
	public static synchronized void setVoltage3v(float v3) {voltage3v = v3;}
	public static synchronized void setAmperage(float ap) {amperage = ap;}
	
	public static synchronized void setTilt(Vector3 tlt) {tilt = tlt;}
}






