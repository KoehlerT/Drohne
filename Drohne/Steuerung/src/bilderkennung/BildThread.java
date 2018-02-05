package bilderkennung;

import main.BildPool;
import utility.Bild;
import utility.BildNotify;

class BildThread extends Thread implements BildNotify{
	int test = 0;
	
	BildThread() {
		this.setName("Bilderkennung");
		BildPool.Abo(this);
	}
	
	@Override
	public void run() {
		/*Wartet, bis der thread notifyed wird.
		 * Dann wird ein neues Bild processed
		 * */
		while (true) { //Warte auf Bild
			anstoﬂen();
		}
	}
	
	void processNextPicture() {
		System.out.println("Bilderkennung gestartet");
		Bild img = BildPool.getBild();
		img.incref();
		long start = System.nanoTime();
		Blumenfinder.processPicture(img.getImageData());
		System.out.println("Blumen erkannt in: "+((System.nanoTime()-start)/1000000)+ " ms");
		img.decref();
	}
	
	public synchronized void anstoﬂen() {
		try {
			this.wait(); //Wartet auf notify/ neues Bild
		} catch (InterruptedException e) {
			
		}
		processNextPicture();
	}

	@Override
	public void neuesBild() {
		this.notify(); //Wird aufgerufen, wenn ein neues Bild angekommen ist
	}
}
