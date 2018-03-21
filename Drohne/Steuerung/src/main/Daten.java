package main;
import utility.Vector3;

public class Daten {
	/*Klasse für alle Sensor und Flugdaten
	 * */
	private Daten() {}
	//Fliegen, Fernbediengungersatz
	public static int cont_throttle; //Wert von 1000-2000 Geschwindigkeit der Motoren/Drohne
	public static int cont_roll; //Wert von 1000-2000 Links- Rechtsneigung der Drohne
	public static int cont_pitch; //Wert von 1000-2000 Vor- Zurückneigung der Drohne
	public static int cont_yaw; //Wert von 1000-2000 Links- Rechtsdrehung der Drohne
	
	//Sensorwerte Strom
	public static float voltageMain; //Spannung der Hauptstromversorgung (11V)
	public static float voltage5v; //Spannung der 5V schiene
	public static float voltage3v; //Spannung der 3,3V Schiene
	public static float amperage; //Stromstärke bzw Stromverbrauch
	
	//Sensorwerte Position 
	public static Vector3 tilt; //Item1: roll Item2: pitch Item3: Yaw
	
	
}
