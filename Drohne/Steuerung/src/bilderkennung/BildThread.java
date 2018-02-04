package bilderkennung;

import main.BildPool;
import utility.BildNotify;

class BildThread extends Thread implements BildNotify{
	int test = 0;
	
	BildThread() {
		
	}
	
	@Override
	public synchronized void run() {
		/*Wartet, bis der thread notifyed wird.
		 * Dann wird ein neues Bild processed
		 * */
		while (true) {
			try {
				this.wait(); //Wartet auf notify/ neues Bild
			} catch (InterruptedException e) {
				processPicture(BildPool.getBild().getImageData()); //Neues Bild verarbeiten
			}
		}
	}
	
	void processPicture(byte[][] img) {
		Blumenfinder.processPicture(img);
	}

	@Override
	public void neuesBild() {
		this.notify(); //Wird aufgerufen, wenn ein neues Bild angekommen ist
	}
}
