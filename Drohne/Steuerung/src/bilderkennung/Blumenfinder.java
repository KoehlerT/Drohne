package bilderkennung;

import java.awt.Point;
import java.util.LinkedList;

import utility.Blume;

final class Blumenfinder {
	private Blumenfinder() {} //Privater konstruktor -> keine Instanzen
	
	static void processPicture(byte[][] img) {
		Point[] ergs = raster(img);
		printPoints(ergs);
		ergs = Cluster.cluster(ergs);
		printPoints(ergs);
		Blume[] blumen = Radiuserkennung.erkennen(img, ergs);
		printBlumen(blumen);
	}
	
	private static void printPoints(Point[] arr) {
		System.out.print(arr.length);
		System.out.print(" Ergebnisse: ");
		for (int i = 0; i < arr.length; i++) {
			System.out.print("("+arr[i].x+"|"+arr[i].y+"), ");
		}
		System.out.println("");
	}
	
	private static void printBlumen(Blume[] arr) {
		System.out.print(arr.length);
		System.out.print(" Blumen: ");
		for (int i = 0; i < arr.length; i++) {
			System.out.print("("+arr[i].getX()+"|"+arr[i].getY()+"|"+arr[i].getDist()+"), ");
		}
		System.out.println("");
	}
	
	private static Point[] raster(byte[][] img) {
		/*Diese Methode legt ein Raster �ber das Bild um Stichprobenartig Pixel auf ihr Blumigkeit zu �berpr�fen
		 * */
		System.out.println("Raster breite: "+img.length+" h�he: "+img[0].length+" incr: "+Einstellungen.rasterGr��e * img.length);
		LinkedList<Point> ergs = new LinkedList<Point>();
		int width = img.length;
		int height = img[0].length;
		int a = (int)(Einstellungen.rasterGr��e * width);
		for (int y = 0; y < height; y+= a) {
			for (int x = 0; x < width; x+= a) {
				if (checkPixel(x,y,img)) {
					ergs.add(new Point(x,y));
				}
			}
		}
		System.out.println("Rasterergebnis: "+ergs.size()+ " maxK. "+maxKontr);
		Point[] arr = new Point[ergs.size()];
		return ergs.toArray(arr);
	}
	
	private static boolean checkPixel(int x, int y, byte[][] img) {
		//pr�ft das Pixel auf verscheidene Skalen
		for (int g = 0; g < 10; g++) {
			int r = (int) (5* Math.pow(1.5, g));
			if (schablone(x,y,r,img)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean schablone(int x, int y, int r, byte[][] img) {
		int erfolge = 0;
		//Definieren der Abst�nde
		float schwarz = 0.2f * r; // Abstand von der Mitte des Schwarzen gepr�ften Pixels
		float wei� = 0.9f * r; float wei�2 = 0.6f * r; //Abst�nde von der Mitte zu den wei�en
		for (int i = 0; i < Einstellungen.schablonenChecks; i++) {
			//Definieren der x und y Koordinaten des �u�ersten pixels
			int wx = (int)(Einstellungen.cos[i]*wei�+x);
			int wy = (int)(Einstellungen.sin[i]*wei�+y);
			//Check ob Index out of range
			if (wx < 0 || wx >= img.length || wy < 0 || wy >= img[0].length)
				return false;
			//Definiere die Farben der Pixel
			byte p1 = img[wx][wy]; //Farbe Wei�es Pixel
			byte p2 = colorAt(x,y,i,wei�2,img);//Farbe 2. Wei�es Pixel
			byte ps = colorAt(x,y,i,schwarz,img); //Farbe schwarzes Pixel
			//Bestimme den Kontrast
			if (kontrast(ps,p1,p2)) {
				erfolge++;
			}
		}
		
		//maxKontr = Math.max(maxKontr, erfolge);
		maxKontr = Math.max(maxKontr, (float)erfolge/(float)Einstellungen.schablonenChecks);
		//Bei genug erfolgreichen Schablonenanwendungen true zur�ckgeben
		if ((float)erfolge/(float)Einstellungen.schablonenChecks >= Einstellungen.erfolgreich) {
			return true;
		}		
		else
			return false;
		
	}
	private static byte colorAt(int x, int y, int c,float r, byte[][] img) {
		int px = (int)(Einstellungen.cos[c] * r + x);
		int py = (int)(Einstellungen.sin[c]*r+y);
		
		byte ret = img[px][py];
		
		return (ret > 0)? ret : 1;
	}
	
	static float maxKontr = 0;
	
	private static boolean kontrast(byte ps, byte p1, byte p2) {
		
		if (((float)p1 / (float)ps >= Einstellungen.kontrast) && ((float)p2 / (float)ps >= Einstellungen.kontrast))
        {
			return true;
            
        }
		
		return false;
	}
}
