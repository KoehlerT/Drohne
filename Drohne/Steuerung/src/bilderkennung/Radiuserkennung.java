package bilderkennung;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import utility.Blume;

public class Radiuserkennung {
	private Radiuserkennung() {} //Keine Objekte dieser Klasse!!!!!!!
	
	private static int iorC = 0; //IndexOutOfRange Couter. Ermittelt, wie oft bei einer Blumenerkennung das Array ‹berschritten wurde -> Zur ermittlung der Blumenvalidit‰t
	
	
	public static Blume[] erkennen(byte[][] bild, Point[] points) {
		List<Blume> erg = new LinkedList<Blume>();
		
		for (Point p : points) {
			UPunkt[] umriss = Umriss(bild, p);
			if (umriss == null)
				continue;
			int rad = radius(umriss);
			if (rad == 0)
				continue;
			int entf = entfernung(rad);
			
			erg.add(new Blume(p.x,p.y,rad,entf));
		}
		Blume[] ret = new Blume[erg.size()];
		erg.toArray(ret);
		return ret;
	}
	
	static UPunkt[] Umriss(byte[][] bild, Point mp) {
		iorC = 0; //Wie oft ist IndexOutOfRange aufgetreten
		UPunkt[] erg = new UPunkt[Einstellungen.anzTest];
		for (int i = 0; i < Einstellungen.anzTest; i++) {
			erg[i] = ende(bild, mp, i);
			if (iorC > 3) {//Invalide Blume, zu viele Array¸berschreitungen
				System.out.println("Range Exc");
				return null;
			}
		}
		
		return erg;
	}
	
	static UPunkt ende(byte[][] bild, Point mp, int check) {
		/*Punkte Wandern vom vermuteten Blumenmittelpunkt von Innen nach Auﬂen
		 * wenn sie auf Weiﬂes stoﬂen, wird der Punkt am Umschlag als Punkt auzsgegeben
		 * Damit entsteht dann ein ungef‰hrer umriss der Blumenmitte
		 * */
		
		int x = mp.x;
		int y = mp.y;
		int dx = (int)(Einstellungen.incr * Einstellungen.cos[check]);
		int dy = (int)(Einstellungen.incr * Einstellungen.sin[check]);
		boolean schwarz = true;
		while (schwarz) {
			x += dx;
			y += dy;
			if (x >= bild.length || x < 0 || y >= bild[0].length || y < 0) {
				iorC ++;
				return new UPunkt(false);
			}
				
			if (bild[x][y] >= Einstellungen.weiﬂ)
				return new UPunkt(x,y);
		}
		return new UPunkt(false);
	}
	
	static int radius(UPunkt[] umr) {
		List<Integer> radien = new LinkedList<Integer>();
		int insg = 0;
		for (int i = 0; i<Einstellungen.anzTest/2; i++) {
			UPunkt p1 = umr[i];
			UPunkt p2 = umr[i + Einstellungen.anzTest/2];
			int r = p1.dist(p2)/2;
			radien.add(r);
			insg += r;
		}
		//Durchschnittliche Abweichung
		int dRad = insg/(Einstellungen.anzTest/2);
		//Maximale Abweichung bestimmen
		int maxAbw = 0;
		for (Integer r : radien) {
			int abw = Math.abs(r-dRad);
			maxAbw = Math.max(maxAbw, abw);
		}
		
		if (maxAbw >= Einstellungen.maxAbw) {
			System.out.println("Abweichung "+maxAbw);
			return 0;
		}
			
		
		return dRad;
		
	}
	
	static int entfernung(int rad) {
		// s = (d*f)/a
		//a = rad*fakt
		return (int)((Einstellungen.radSchw*Einstellungen.fokus)/(rad*Einstellungen.faktor));
	}
}
