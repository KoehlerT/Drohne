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
	private Altitude alt;
	
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
		alt = new Altitude(Altitude.MODE_STANDARD);
		
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
			long startTime = System.nanoTime();
			//Nehme Variablen
			if (Info.sensorAttached) {
				//SPI
				System.out.println("Arduino?");
				arduinoMng.sendControllerInputs();
				
				//Ultraschall
				try {
					System.out.println("Ultraschall?");
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
			System.out.println("GPS?");
			GPS.printGps();
			
			//Beeper
			System.out.println("Beeper?");
			beeper.workBeeps();
			
			//Altitude
			alt.readAllSensorData();
			
			//Antenne
			//ant.receive();
			//byte[] toSend = Datapackager.packageTransmit();
			//ant.setTransmitBuffer(toSend);
			//ant.send();
			
			System.out.println("Wlan?");
			wlanServer.receive();
			wlanServer.sendPackage();
			
			System.out.println("Looptime: "+((System.nanoTime()-startTime)/1000)+"us");
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
