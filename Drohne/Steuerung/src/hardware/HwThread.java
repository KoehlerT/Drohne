package hardware;


import main.Daten;
import main.Info;

class HwThread extends Thread{
	
	private Beeper beeper;
	//private Ultrasonic ultrasonic;
	//private GPS gps;
	
	//private Altitude alt;
	
	private Boolean running = true;
	
	
	public HwThread() {
		this.setName("Hardware");
			
		
		beeper = new Beeper();
		
	}
	
	@Override
	public void run() {
		
		System.out.println("SPI Run "+running);
		beeper.beep(50);
		while(Daten.running) {
			long startTime = System.nanoTime();
			//Nehme Variablen
			
			beeper.workBeeps();
			//Warte ein wenig
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
			
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
