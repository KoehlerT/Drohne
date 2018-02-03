package kamera;

import com.hopding.jrpicam.*;
import com.hopding.jrpicam.exceptions.FailedToRunRaspistillException;

class KameraThread extends Thread{
	
	byte[][] bild;
	private RPiCamera piCamera;
	
	KameraThread() {
		
	}
	
	@Override
	public void run() {
		if (!kameraAngeschlossen())
			return;
	}
	
	Boolean kameraAngeschlossen() {
		
		try {
			piCamera = new RPiCamera();
			return true;
		}catch(FailedToRunRaspistillException e) {
			System.out.println("Keine Kamera erkannt. Kamerathread wird beendet");
			return false;
		}
	}

}
