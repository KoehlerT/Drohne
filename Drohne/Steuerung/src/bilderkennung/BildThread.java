package bilderkennung;

import main.BildPool;
import main.Daten;
import utility.Bild;
import utility.BildNotify;

class BildThread extends Thread implements BildNotify{
	int test = 0;
	
	BildThread() {
		this.setName("Bilderkennung");
		//Sagt BildPool bescheid, dass diese Klasse von einem neuen aufgenommenen Bild benachrichtigt werden möchte
		BildPool.Abo(this);
	}
	
	@Override
	public void run() {
		/*Thread wartet, bis ein notify eintrifft
		 * notify wird von Bildpool ausgeführt, wenn ein neues Bild vorhanden ist.
		 * Dann wird das neue Bild bearbeitet
		 * */
		while (Daten.running) { //Warte auf Bild
			synchronized(this) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			processNextPicture(); //Wird von Blumenthread ausgeführt
		}
	}
	
	void processNextPicture() {
		System.out.println("Bilderkennung gestartet");
		Bild img = BildPool.getBild();
		img.incref(); //Das Bild wird reserviert, sodass es nicht überschrieben werden kann
		long start = System.nanoTime();//Zeitmessung
		
		Blumenfinder.processPicture(img.getImageData());//Bildbearbeitung startet
		
		System.out.println("Blumen erkannt in: "+((System.nanoTime()-start)/1000000)+ " ms");
		img.decref(); //Bild wird freigegeben
	}
	

	@Override
	public void neuesBild() {
		//Wird aufgerufen, wenn ein neues Bild angekommen ist
		//Wird vom Bildpool / Kamerathread aufgerufen
		System.out.println("neuesBild()");
		synchronized (this) {
			System.out.println("Notifying");
			this.notify(); //Kommuniziert zum Bildthread, dass der Kuchen fertig ist
		}
		
	}
}
