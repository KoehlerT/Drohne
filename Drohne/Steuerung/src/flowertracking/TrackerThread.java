package flowertracking;

import com.koehlert.flowerflyer.camera.*;
import com.koehlert.flowerflyer.main.Location;
import com.koehlert.flowerflyer.main.Vector3;
import com.koehlert.flowerflyer.recognition.Recognizer;
import com.koehlert.flowerflyer.target.SpeedController;
import com.koehlert.flowerflyer.target.Targeter;

import main.Daten;

class TrackerThread extends Thread{
	
	private PictureGetter pg;
	private Recognizer rg;
	private PictureWriter pw;
	
	private byte[][] bild;
	
	public TrackerThread() {
		pg = new PictureGetter();
		rg = new Recognizer(3);
		pw = new PictureWriter();
		
		bild = new byte[640][480];
	}
	
	@Override
	public void run() {
		long start;
		while (Daten.running) {
			start = System.nanoTime();
			bild = pg.getArr(bild);
			Location[] locs = rg.getLocations(bild);
			Location target = Targeter.target(locs);
			
			// Target to Daten
			Daten.setTarget(target);
			
			// Geschwindigkeit zu Daten
			Vector3 vel = SpeedController.calculatePid(target);
			Daten.setTargetVel(vel);
			
			while(System.nanoTime() - start < 300000000);
		}
		
		pg.close();
		pw.Stop();
	}
}
