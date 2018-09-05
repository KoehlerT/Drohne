package com.koehlert.flowerflyer.main;

import com.koehlert.flowerflyer.camera.PictureGetter;
import com.koehlert.flowerflyer.camera.PictureWriter;
import com.koehlert.flowerflyer.recognition.Recognizer;
import com.koehlert.flowerflyer.target.SpeedController;
import com.koehlert.flowerflyer.target.Targeter;

public class main {

	/**
	 * @param args
	 */
	static PictureGetter pg;
	static Recognizer rg;
	static PictureWriter pw;
	
	/*public static void main(String[] args) {
		rundown();
	}*/
	
	public static void rundown() {
		System.out.println("Hello World");
		pg = new PictureGetter();
		rg = new Recognizer(3);
		pw = new PictureWriter();
		
		byte[][] bild = new byte[640][];
		for (int i = 0; i < bild.length; i++)
			bild[i] = new byte[480];
		
		long start;
		
		for (int i = 0; i < 100; i++){
			start = System.nanoTime();
			bild = pg.getArr(bild);
			Location[] locs = rg.getLocations(bild);
			Location target = Targeter.target(locs);
			
			if (target != null){
				Vector3 vel = SpeedController.calculatePid(target);
				System.out.println("Geschwindigkeiten: Rechts: "+vel.x+" Oben "+vel.z+" Hinten "+vel.y);
			}
			
			if (i%10 == 0){
				//pw.requestPicture(pg.getLastPic(), "Pic"+i);
			}
			
			System.out.println("Calc Time: "+((float)System.nanoTime() - start)/1000000 + "ms");
			
			while(System.nanoTime() - start < 300000000);
		}
		pg.saveTest();
		
		pg.savePicture(pg.getRGB(),"Test");
		
		System.out.println("Finished Recognition");
		pg.close();
		pw.Stop();
	}

}
