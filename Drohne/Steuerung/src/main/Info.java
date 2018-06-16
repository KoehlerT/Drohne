package main;

/**
 * Klasse für die Einstellungen und Variablen für das gesamte Programm
 * Alle Informationen sind public und read only (final)
 * Für verändernde Variablen wie Flugdaten ist die Daten Klasse verantwortlich
 * **/
public class Info {
	
	private Info() {}
	
	/**SPI Speed
	 * die Tacktfrequenz des SPI Busses: Normal 1MHz erfahrung: 200kHz
	 * **/
	public static final int SpiSpeed = 200000;
	/**SpiWait, die anzahl an ms, die nach jedem übermittelten Byte per SPI pause gemacht werden muss
	 * **/
	public static final int SpiWait = 0;
	
	/**Geschwindigkeit des Tons in dm/ns (Dezimeter pro Nanosekunde)
	 * **/
	public static final float SoundSpeed = 0.000003f; //300m/s => 300*10^-8 dm/ns => 3*10^-6
	
	/**Ist die untere Plattform angeschlossen
	 * **/
	public static final boolean sensorAttached = true;
	
	/**Zeit in Nanosekunden, nach welcher der Ultraschallsensor die Zeit überschreitet
	 * **/
	public static final long ultrasonicTimeout = 100000000l; //100 ms
	
	public static final boolean CamAttached = false;
}
