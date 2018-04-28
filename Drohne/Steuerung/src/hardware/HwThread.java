package hardware;

import java.io.IOException;

import main.Daten;
import main.Info;

class HwThread extends Thread{
	
	private Arduino arduinoMng;
	private Beeper beeper;
	private Ultrasonic ultrasonic;
	private GPS gps;
	private Antenne ant;
	
	
	private Boolean running = true;
	
	private byte[] toSend = new byte[32];
	
	public HwThread() {
		if (Info.sensorAttached) {
			ultrasonic = new Ultrasonic();
			try {
				arduinoMng = new Arduino();
				System.out.println("SPI gestartet");
			} catch (IOException e) {
				System.out.println("SPI Fehler");
				running = false;
			}
		}
		beeper = new Beeper();
		
		gps = new GPS();
		ant = new Antenne();
		
		//Fill ToSend
		for (int i = 0; i < toSend.length; i++) {
			toSend[i] = (byte)i;
		}
	}
	
	@Override
	public void run() {
		System.out.println("SPI Run "+running);
		beeper.beep(200);
		while(running) {
			//Nehme Variablen
			if (Info.sensorAttached) {
				//SPI
				arduinoMng.sendControllerInputs();
				
				//Ultraschall
				try {
					 ultrasonic.measureDistanceDm();
					System.out.println("nächster Gegenstand "+Daten.getDistanceUltrasonic()
						+" dm entfernt");
				} catch (InterruptedException e) {
					System.out.println("Fehler beim Messen der Entfernung");
					e.printStackTrace();
				}
			}
			//GPS
			//Automatisch / Event driven
			GPS.printGps();
			
			//Antenne
			//ant.setTransmitBuffer(toSend);
			//ant.send();
			ant.receive();
			
			//Warte ein wenig
			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
			
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
