package main;

import java.util.Scanner;

import flightmodes.ControllingStart;
import flowertracking.TrackerStart;
import hardware.HwStart;
import utility.*;

public class start {
	
	private static Managable[] manag = new Managable[3];
	
	private static Scanner inputsc = new Scanner(System.in);

	public static void main(String[] args) {
		// Start des Programms
		System.out.println("Drohnenprogramm gestartet");
		
		if (args.length > 0) {
			System.out.println("Argument: "+args[0]);
			if (args[0].toLowerCase().contains("nocam")) {
				Info.CamAttached = false;
				System.out.println("Cam disabled");
			}else {
				Info.CamAttached = true;
			}
		}
			
		
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
				Thread.sleep(500);
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
		manag[0] = new TrackerStart();		//Kamera/ Flugsystem
		manag[1] = new HwStart();			//Kommunikation
		manag[2] = new ControllingStart();	//Flugprogramme
		
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
