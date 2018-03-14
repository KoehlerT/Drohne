package hardware;

import java.io.IOException;

class HwThread extends Thread{
	
	SPIManager arduinoMng;
	
	public HwThread() {
		try {
			arduinoMng = new SPIManager();
		} catch (IOException e) {
			System.out.println("SPI Fehler");
		}
	}
	
	@Override
	public void run() {
		
	}
}
