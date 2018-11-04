package main;

/**
 * Klasse für die Einstellungen und Variablen für das gesamte Programm
 * Alle Informationen sind public und read only (final)
 * Für verändernde Variablen wie Flugdaten ist die Daten Klasse verantwortlich
 * **/
public class Info {
	
	private Info() {}
	
	/**Geschwindigkeit des Tons in dm/ns (Dezimeter pro Nanosekunde)
	 * **/
	public static final float SoundSpeed = 0.000003f; //300m/s => 300*10^-8 dm/ns => 3*10^-6
	
	/**Ist die untere Plattform angeschlossen
	 * **/
	public static final boolean sensorAttached = true;
	
	/**Zeit in Nanosekunden, nach welcher der Ultraschallsensor die Zeit überschreitet
	 * **/
	public static final long ultrasonicTimeout = 100000000l; //100 ms
	
	public static boolean CamAttached = true;
	
	public static final int pictureWidth = 640;
	public static final int pictureHeight = 480;
}
