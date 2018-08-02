package main;

import bilderkennung.BildStart;
import flightmodes.FlightModeManager;
import kamera.KameraStart;
import hardware.HwStart;
import utility.*;

public class start {
	
	private static Managable[] manag = new Managable[3];

	public static void main(String[] args) {
		// Start des Programms
		System.out.println("Drohnenprogramm gestartet");
		
		starteProgrammteile();
		
		
		
		//Beendigungsbedingung. Derzeit, jeder manager returnt running -> false. 
		//Sobald es "echte" Kommunikation gibt, kann diese Methode wahrscheinlich alleine beenden
		//Bis zu diesem Zeitpunkt programm einfach zwangsbeenden (Taskmanager/ Stopp-Symbol)
		long startTime = System.nanoTime();
		while(running()) {//Warten, bis alle Abschnitte beendet sind
			ProgramState.getInstance().evaluateControlWord();
			
			long diff = System.nanoTime() - startTime;
			FlightModeManager.getInstance().updateFlightmode((float)diff/1000000000.0f);
			startTime = System.nanoTime();
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
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
