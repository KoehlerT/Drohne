package hardware;

class HwThread extends Thread{
	
	SPIManager arduinoMng;
	
	public HwThread() {
		arduinoMng = new SPIManager();
	}
	
	@Override
	public void run() {
		
	}
}
