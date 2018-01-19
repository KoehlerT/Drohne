package bilderkennung;

final class Einstellungen {
	static final float rasterGröße = 0.02f; //Vielfaches der Breite, die als Rasterabstand benutzt wird
	static final int schablonenChecks = 16; //Anzahl der Schablonenprüfungen pro Pixel
	static final float kontrast = 3.0f; //Minimaler Quotient aus Weiß/Schwarz (Farben: 0-255)
	static final float erfolgreich = 0.7f; //% der Checks, die beim Schablonen-prüfen erfolgreich sein sollen
	
	
	//Sinus und Cosinus vorberechnen
	static float[] sin = new float[schablonenChecks];
	static float[] cos = new float[schablonenChecks];
	static {
		for (int i = 0; i < schablonenChecks; i++) {
			double a = (2*Math.PI*i)/schablonenChecks;
			sin[i] = (float)Math.sin(a);
			cos[i] = (float)Math.cos(a);
		}
	}
}
