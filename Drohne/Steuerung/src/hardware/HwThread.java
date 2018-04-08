package hardware;

import java.io.IOException;

import main.Daten;

class HwThread extends Thread{
	
	private Arduino arduinoMng;
	private Beeper beeper;
	private Ultrasonic ultrasonic;
	private GPS gps;
	private Antenne ant;
	
	private Boolean running = true;
	
	public HwThread() {
		beeper = new Beeper();
		ultrasonic = new Ultrasonic();
		gps = new GPS();
		ant = new Antenne();
		try {
			arduinoMng = new Arduino();
			System.out.println("SPI gestartet");
		} catch (IOException e) {
			System.out.println("SPI Fehler");
			running = false;
		}
	}
	
	@Override
	public void run() {
		System.out.println("SPI Run "+running);
		beeper.beep(200);
		while(running) {
			//Nehme Variablen
			//SPI
			arduinoMng.sendControllerInputs();
			
			//Ultraschall
			try {
				 ultrasonic.measureDistanceDm();
				System.out.println("nächster Gegenstand "+Daten.getDistanceUltrasonic()
					+" dm entfernt");
			} catch (InterruptedException e) {
				System.out.println("Fehler beim Messend er Entfernung");
				e.printStackTrace();
			}
			
			//GPS
			//Automatisch / Event driven
			GPS.printGps();
			
			//Antenne
			
			
			
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
