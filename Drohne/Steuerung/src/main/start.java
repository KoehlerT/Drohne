package main;

import bilderkennung.BildStart;
import kamera.KameraStart;
import hardware.HwStart;
import utility.*;

public class start {
	
	private static Managable[] manag = new Managable[3];

	public static void main(String[] args) {
		// Start des Programms
		System.out.println("Drohnenprogramm gestartet");
		starteProgrammteile();
		
		
		while(running()); //Warten, bis alle Abschnitte beendet sind
		System.out.println("Drohnenprogrammm beendet");
	}
	
	public static void starteProgrammteile() {
		/*Hier werden alle Manager-Objekte der Programmabschnitte erstellt
		 * 
		 * */
		manag[0] = new BildStart();
		manag[1] = new HwStart();
		manag[2] = new KameraStart();
		
		for (Managable m : manag) {
			m.start();
		}
	}
	
	public static boolean running() {
		//Prüft jeden Programmteil, ob er noch läuft.
		boolean res = false;
		for (Managable m : manag) {
			res = res || m.running();
		}
		return res;
	}

}
