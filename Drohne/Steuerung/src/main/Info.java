package main;

/**
 * Klasse f�r die Einstellungen und Variablen f�r das gesamte Programm
 * Alle Informationen sind public und read only (final)
 * F�r ver�ndernde Variablen wie Flugdaten ist die Daten Klasse verantwortlich
 * **/
public class Info {
	
	private Info() {}
	
	/**SPI Speed
	 * die Tacktfrequenz des SPI Busses: Normal 1MHz erfahrung: 200kHz
	 * **/
	public static final int SpiSpeed = 200000;
	/**SpiWait, die anzahl an ms, die nach jedem �bermittelten Byte per SPI pause gemacht werden muss
	 * **/
	public static final int SpiWait = 4;
	
	/**Geschwindigkeit des Tons in dm/ns (Dezimeter pro Nanosekunde)
	 * **/
	public static final float SoundSpeed = 0.000003f; //300m/s => 300*10^-8 dm/ns => 3*10^-6
}