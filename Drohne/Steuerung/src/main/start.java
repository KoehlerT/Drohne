package main;

import java.util.Scanner;

import bilderkennung.BildStart;
import flightmodes.ControllingStart;
import kamera.KameraStart;
import hardware.HwStart;
import utility.*;

public class start {
	
	private static Managable[] manag = new Managable[4];
	
	private static Scanner inputsc = new Scanner(System.in);

	public static void main(String[] args) {
		// Start des Programms
		System.out.println("Drohnenprogramm gestartet");
		
		starteProgrammteile();
		
		System.out.println("Abschnitte Gestartet");
		
		//Beendigungsbedingung. Derzeit, jeder manager returnt running -> false. 
		//Sobald es "echte" Kommunikation gibt, kann diese Methode wahrscheinlich alleine beenden
		//Bis zu diesem Zeitpunkt programm einfach zwangsbeenden (Taskmanager/ Stopp-Symbol)
		while(running()) {//Warten, bis alle Abschnitte beendet sind
			
			
			//Detect Close
			if (inputsc.hasNext())
				Daten.running = false;
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		
		
		//Kamera Stoppen!!!
		System.out.println("Drohnenprogrammm beendet");
	}
	
	public static void starteProgrammteile() {
		/*Hier werden alle Manager-Objekte der Programmabschnitte erstellt
		 * 
		 * */
		manag[0] = new BildStart();
		manag[1] = new HwStart();
		manag[2] = new KameraStart();
		manag[3] = new ControllingStart();
		
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
