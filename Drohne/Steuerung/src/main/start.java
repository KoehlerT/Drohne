package main;

import bilderkennung.BildStart;
import kamera.KameraStart;
import hardware.HwStart;
import utility.*;
import wlanClient.WlanManager;

public class start {
	
	private static Managable[] manag = new Managable[4];
	private static String host = "192.168.178.50";

	public static void main(String[] args) {
		// Start des Programms
		System.out.println("Drohnenprogramm gestartet");
		
		if (args.length == 0) {
			host = "192.168.178.50";
		}else{
			host = args[0];
		}
		System.out.println("Host: "+host);
		starteProgrammteile();
		
		
		
		//Beendigungsbedingung. Derzeit, jeder manager returnt running -> false. 
		//Sobald es "echte" Kommunikation gibt, kann diese Methode wahrscheinlich alleine beenden
		//Bis zu diesem Zeitpunkt programm einfach zwangsbeenden (Taskmanager/ Stopp-Symbol)
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
		manag[3] = new WlanManager(host);
		
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
