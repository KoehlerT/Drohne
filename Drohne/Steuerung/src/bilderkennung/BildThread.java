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
			synchronized(this) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			processNextPicture();
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
	

	@Override
	public void neuesBild() {
		//Wird aufgerufen, wenn ein neues Bild angekommen ist
		//Wird von anderem Thread aufgerufen
		System.out.println("neuesBild()");
		synchronized (this) {
			System.out.println("Notifying");
			this.notify(); 
		}
		
	}
}
