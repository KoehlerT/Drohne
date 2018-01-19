package bilderkennung;

import java.awt.Point;
import java.util.LinkedList;

final class Blumenfinder {
	private Blumenfinder() {} //Privater konstruktor -> keine Instanzen
	
	static void processPicture(byte[][] img) {
		Point[] ergs = raster(img);
		System.out.println("Ergebnisse: "+ergs.length);
	}
	
	private static Point[] raster(byte[][] img) {
		/*Diese Methode legt ein Raster über das Bild um Stichprobenartig Pixel auf ihr Blumigkeit zu überprüfen
		 * */
		LinkedList<Point> ergs = new LinkedList<Point>();
		int width = img.length;
		int height = img[0].length;
		int a = (int)(Einstellungen.rasterGröße * width);
		for (int y = 0; y < height; y+= a) {
			for (int x = 0; x < width; x+= a) {
				if (checkPixel(x,y,img)) {
					ergs.add(new Point(x,y));
				}
			}
		}
		Point[] arr = new Point[ergs.size()];
		return ergs.toArray(arr);
	}
	
	private static boolean checkPixel(int x, int y, byte[][] img) {
		//prüft das Pixel auf verscheidene Skalen
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
		//Definieren der Abstände
		float schwarz = 0.2f * r; // Abstand von der Mitte des Schwarzen geprüften Pixels
		float weiß = 0.9f * r; float weiß2 = 0.6f * r; //Abstände von der Mitte zu den weißen
		for (int i = 0; i < Einstellungen.schablonenChecks; i++) {
			//Definieren der x und y Koordinaten des äußersten pixels
			int wx = (int)(Einstellungen.cos[i]*weiß+x);
			int wy = (int)(Einstellungen.sin[i]*weiß+y);
			//Check ob Index out of range
			if (wx < 0 || wx >= img.length || wy < 0 || wy <= img[0].length)
				return false;
			//Definiere die Farben der Pixel
			byte p1 = img[wx][wy]; //Farbe Weißes Pixel
			byte p2 = colorAt(x,y,i,weiß2,img);//Farbe 2. Weißes Pixel
			byte ps = colorAt(x,y,i,schwarz,img); //Farbe schwarzes Pixel
			//Bestimme den Kontrast
			if (kontrast(ps,p1,p2)) {
				erfolge++;
			}
		}
		//Bei genug erfolgreichen Schablonenanwendungen true zurückgeben
		if (erfolge/Einstellungen.schablonenChecks >= Einstellungen.erfolgreich)
			return true;
		else
			return false;
	}
	private static byte colorAt(int x, int y, int c,float r, byte[][] img) {
		int px = (int)(Einstellungen.cos[c] * r + x);
		int py = (int)(Einstellungen.sin[c]*r+y);
		return img[px][py];
	}
	private static boolean kontrast(byte ps, byte p1, byte p2) {
		if (((float)p1 / (float)ps >= Einstellungen.kontrast) && ((float)p2 / (float)ps >= Einstellungen.kontrast))
        {
            return true;
        }
		return false;
	}
}
