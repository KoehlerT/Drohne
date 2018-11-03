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
	
	Vector3 nullVect = new Vector3(0,0,0);
	
	public TrackerThread() {
		
	}
	
	@Override
	public void run() {
		pg = new PictureGetter();
		rg = new Recognizer(3);
		pw = new PictureWriter();
		
		bild = new byte[640][480];
		
		
		long start;
		while (Daten.running) {
			start = System.nanoTime();
			bild = pg.getArr(bild);
			Location[] locs = rg.getLocations(bild);
			Location target = Targeter.target(locs);
			
			// Target to Daten
			Daten.setTarget(target);
			
			// Geschwindigkeit zu Daten
			if (target != null) {
				Vector3 vel = SpeedController.calculatePid(target);
				Daten.setTargetVel(vel);
				System.out.println("Target: "+target.toString());
			}else {
				System.out.println("No Target");
				Daten.setTargetVel(nullVect);
			}
				
			System.out.println("Calc Time: "+(float)(System.nanoTime() - start)/1000000f+"ms");
		}
		
		pg.close();
		pw.Stop();
	}
}
