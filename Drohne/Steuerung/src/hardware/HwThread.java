package hardware;

import java.io.IOException;

import hardware.communication.Antenne;
import hardware.communication.Arduino;
import hardware.communication.WlanServer;
import main.Daten;
import main.Info;

class HwThread extends Thread{
	
	private Beeper beeper;
	private Ultrasonic ultrasonic;
	private GPS gps;
	
	private Altitude alt;
	
	private Boolean running = true;
	
	
	public HwThread() {
		this.setName("Hardware");
		
		if (Info.sensorAttached)
			ultrasonic = new Ultrasonic();
		
		beeper = new Beeper();
		alt = new Altitude(Altitude.MODE_STANDARD);
		
		gps = new GPS();
	}
	
	@Override
	public void run() {
		
		System.out.println("SPI Run "+running);
		beeper.beep(50);
		while(running) {
			long startTime = System.nanoTime();
			//Nehme Variablen
			if (Info.sensorAttached) {
				//SPI
				
				//Ultraschall
				try {
					 ultrasonic.measureDistanceDm();
				} catch (InterruptedException e) {
					System.out.println("Fehler beim Messen der Entfernung");
					e.printStackTrace();
				}
			}
			//GPS
			//Automatisch / Event driven
			//GPS.printGps();
			
			//Beeper
			beeper.workBeeps();
			
			//Altitude
			alt.readAllSensorData();
			
			
			Daten.setSensorRefresh((int)(1f/((float)(System.nanoTime()-startTime)/1000_000_000f)));
			//Warte ein wenig
			//try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
			
		}
	}
	
	
	/*private void printRecv(byte[] recv) {
		if (recv == null) {
			System.out.println("Arr: null");
			return;
		}
		System.out.print("Empfangen: ");
		for (byte b : recv) {
			System.out.print(b+", ");
		}
		System.out.println();
	}*/
}
