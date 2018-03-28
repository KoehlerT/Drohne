package hardware;

import java.io.IOException;

import main.Daten;

class HwThread extends Thread{
	
	SPIManager arduinoMng;
	private Boolean running = true;
	
	public HwThread() {
		try {
			arduinoMng = new SPIManager();
			System.out.println("SPI gestartet");
		} catch (IOException e) {
			System.out.println("SPI Fehler");
			running = false;
		}
	}
	
	@Override
	public void run() {
		byte[] ci = new byte[8]; //Controller input bytes
		System.out.println("SPI Run");
		while(running) {
			//Nehme Variablen
			int throttle = Daten.getCont_throttle();
			int pitch = Daten.getCont_pitch();
			int roll = Daten.getCont_roll();
			int yaw = Daten.getCont_yaw();
			
			writeToArray(ci,throttle,0);
			writeToArray(ci,pitch,2);
			writeToArray(ci,roll,4);
			writeToArray(ci,yaw,6);
			
			byte[] recv = arduinoMng.sendAndReceive(ci);
			printRecv(recv);
		}
	}
	private void writeToArray(byte[] arr, int val, int ind) {
		byte lb = (byte)(val & 0xFF); //Lower Byte value
		byte hb = (byte)((val >> 8)& 0xFF); //Higher Byte value
		arr[ind] = lb;
		arr[ind+1] = hb;
	}
	
	private void printRecv(byte[] recv) {
		System.out.print("Empfangen: ");
		for (byte b : recv) {
			System.out.print(b+", ");
		}
		System.out.println();
	}
}
