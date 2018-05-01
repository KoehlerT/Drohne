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
	private WlanServer wlanServer;
	
	private Boolean running = true;
	
	
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
		//ant = new Antenne();
		wlanServer = new WlanServer();
		
		
		wlanServer.acceptClients();
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
			
			//Beeper
			beeper.workBeeps();
			
			//Antenne
			//ant.receive();
			//byte[] toSend = Datapackager.packageTransmit();
			//ant.setTransmitBuffer(toSend);
			//ant.send();
			
			wlanServer.receive();
			wlanServer.sendPackage();
			
			//Warte ein wenig
			try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
			
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
