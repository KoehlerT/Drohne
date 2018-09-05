package com.koehlert.flowerflyer.recognition;

import com.koehlert.flowerflyer.main.Location;
import com.koehlert.kreiserkennung.Einstellungen;
import com.koehlert.kreiserkennung.Kreis;
import com.koehlert.kreiserkennung.Kreiserkenner;

public class Recognizer {
	
	Kreiserkenner ke;
	int rotations;
	
	public Recognizer(int RightRotations){
		Einstellungen.setDefaults();
		rotations = RightRotations;
	}
	
	public Location[] getLocations(byte[][] bild){
		Kreis[] kreise = Kreiserkenner.verarbeiteBild(bild,true);
		Kreiserkenner.printKreise(kreise);
		
		
		Location[] locs = new Location[kreise.length];
		float width = bild.length;
		float height = bild[0].length;
		
		for (int i = 0; i < locs.length; i++){
			float _x =  (float)kreise[i].getX()/width;
			float _y =  (float)kreise[i].getY()/height;
			locs[i] = new Location(_x,_y,kreise[i].getDistanz());
			rotateLocation(locs[i]);
			toStandardSystem(locs[i]);
			
			System.out.println(locs[i].toString());
		}
		return locs;
		
	}
	
	private void rotateLocation(Location loc){
		for (int r = 0; r < rotations; r++){
			float y = loc.y;
			loc.y = loc.x;
			loc.x = 1-y;
		}
	}
	
	private void toStandardSystem(Location loc){
		loc.y = (1-loc.y)*2-1;
		loc.x = (loc.x *2) -1;
	}
	
	
}
