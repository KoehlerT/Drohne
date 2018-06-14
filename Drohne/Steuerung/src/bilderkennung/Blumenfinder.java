package bilderkennung;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import utility.Blume;

final class Blumenfinder {
	private Blumenfinder() {} //Privater konstruktor -> keine Instanzen
	
	static void processPicture(byte[][] img) {
		//generateDebugImage(img);
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
		/*Diese Methode legt ein Raster über das Bild um Stichprobenartig Pixel auf ihr Blumigkeit zu überprüfen
		 * */
		System.out.println("Raster breite: "+img.length+" höhe: "+img[0].length+" incr: "+Einstellungen.rasterGröße * img.length);
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
		
		System.out.println("Rasterergebnis: "+ergs.size()+ " maxK. "+maxKontr);
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
			if (wx < 0 || wx >= img.length || wy >= img[0].length || wy < 0)
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
		
		float blumigkeit = (float)erfolge/(float)Einstellungen.schablonenChecks;
		maxKontr = Math.max(maxKontr, blumigkeit);
		//Bei genug erfolgreichen Schablonenanwendungen true zurückgeben
		//System.out.println("K("+x+"|"+y+") Blumigkeit= "+blumigkeit+" für r= "+r);
		if (blumigkeit >= Einstellungen.erfolgreich) {
			return true;
		}		
		else
			return false;
		
	}
	private static byte colorAt(int x, int y, int c,float r, byte[][] img) {
		int px = (int)(Einstellungen.cos[c] * r + x);
		int py = (int)(Einstellungen.sin[c]*r+y);
		
		byte ret = img[px][py];
		
		return (ret != 0)? ret : 1;
	}
	
	static float maxKontr = 0;
	
	private static boolean kontrast(byte ps, byte p1, byte p2) {
		//System.out.println("Weiß außen: "+p1+" Weiß innen: "+p2+" schwarz: "+p2);
		float wa = (float)((int)p1&0x000000FF);
		float wi = (float)((int)p2&0x000000FF);
		float sw = (float)((int)ps&0x000000FF);
		if ((wa/sw >= Einstellungen.kontrast) && (wi/sw >= Einstellungen.kontrast))
        {
			return true;
            
        }
		
		return false;
	}
	
	static int pictureNum = 0;
	
	private static void generateDebugImage(byte[][] image) {
		BufferedImage img = new BufferedImage(image.length, image[0].length, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < image.length; x++) {
			for (int y = 0; y < image[0].length;y++) {
				int gray = image[x][y];
				int rgb = gray;
				rgb = (rgb<<8)+gray;
				rgb = (rgb<<8)+gray;
				img.setRGB(x, y, rgb);
			}
		}
		File output = new File("picture#"+pictureNum+".png");
		try {
			ImageIO.write(img, "png", output);
			System.out.println("Saved Image# "+pictureNum);
			pictureNum++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Bildfehler");
			e.printStackTrace();
		}
	}
}
