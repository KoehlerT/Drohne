package bilderkennung;


class BildThread extends Thread{
	int test = 0;
	
	public BildThread() {
		
	}
	
	@Override
	public void run() {
		/*Kleines Testprogramm.
		 * Zählt bis 4 (jede 1/2 sekunde)
		 * Wenn fertig, terminiert es den Thread
		 * */
		while (true) {
			test ++;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
			if (test >= 4) {
				return;
			}
		}
	}
	
	void processPicture(byte[][] img) {
		Blumenfinder.processPicture(img);
	}
}
