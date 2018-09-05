package com.koehlert.flowerflyer.target;

import com.koehlert.flowerflyer.main.Location;
import com.sun.xml.internal.fastinfoset.tools.PrintTable;

/**Soll die anzufliegende Blume speichern
 * Soll fälschlich erkannte Blumen/ nebenblumen ignorieren
 * Soll eine Richtung angeben, in der sich die anzufliegende Blume befindet
 * Soll angeben, wann sie das letzte mal gefunden wurde
 * 
 * */
public class Targeter {
	private Targeter(){}
	
	private static Location locDrohne = new Location(0,0,0);
	
	private static Location target = null;
	
	public static Location target(Location[] locations){
		if (locations.length == 0) 
		{
			//Keine Blume erkannt...
		}else{
			
			/*Wenn es mehr Blumen gibt 2 Möglichkeiten: 
			 * 	a) Es ist keine Blume getargeted: näheste Location = target 
			 * 	b) es ist eine Blume getargeted: nächste Blume am target ist neues target*/
			if (target == null)
				target = getNearestLoc(locDrohne, locations);
			else
				target = getNearestLoc(target, locations);
		}
		
		
		if (target == null){
			System.out.println("No Target");
		}else{
			System.out.println("Target: "+target.toString());
		}
		return target;
		
	}
	
	public static Location getNearestLoc(Location start, Location[] locs){
		
		Location newTarg = null;
		float smallestDist = Float.MAX_VALUE;
		for (int i = 0; i < locs. length; i++){
			Location loc = locs[i];
			float dx = loc.x - start.x;
			float dy = loc.y - start.y;
			float dd = loc.dist/60f - start.dist/60f; //Normalised distance
			
			float avrgDist = (Math.abs(dx)+Math.abs(dy)+Math.abs(dd));
			if (avrgDist < smallestDist){
				newTarg = loc;
				smallestDist = avrgDist;
			}
		}
		
		return newTarg;
	}
	
	
}
