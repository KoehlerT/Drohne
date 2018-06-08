package kamera;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.hopding.jrpicam.RPiCamera;
import com.hopding.jrpicam.enums.Encoding;
import com.hopding.jrpicam.enums.Exposure;
import com.hopding.jrpicam.exceptions.FailedToRunRaspistillException;

import main.Info;

public class Camera {
	
	private int aufnahmezeit;
	private RPiCamera piCamera;
	
	private boolean connected = Info.CamAttached;
	
	
	public Camera() {
		//Kamera wird konfiguruiert
		kameraKonfigurieren();
	}
	
	public BufferedImage takePicture() {
		
		return bildAufnehmen();
	}
	
	public int getTime() {
		return aufnahmezeit;
	}
	
	private Boolean kameraAngeschlossen() {
		//Testet, ob Kamera angeschlossen ist
		try {
			piCamera = new RPiCamera();
			System.out.println("Kamera erkannt");
			return true;
		}catch(FailedToRunRaspistillException e) {
			System.out.println("Keine Kamera erkannt. Kamerathread wird beendet");
			return false;
		}
	}
	
	private void kameraKonfigurieren() {
		//Einstellungen. Die Besten müssen noch gefunden werden
		piCamera.setToDefaults();
		piCamera.setWidth(500)
				.setHeight(370)
				.setExposure(Exposure.SPORTS)
				.setTimeout(0)
				.setContrast(100)
				.setSharpness(100)
				.setEncoding(Encoding.BMP);
		piCamera.setFullPreviewOff();
		piCamera.setPreviewOpacity(0);
	}
	
	private BufferedImage bildAufnehmen() {
		try {
			long start = System.nanoTime();
			BufferedImage buffImg = piCamera.takeBufferedStill();
			aufnahmezeit = (int)(System.nanoTime()-start);
			//System.out.println("Bild aufgenommen in "+aufnahmezeit/1000000+" ms");
			return buffImg;
		}catch (InterruptedException e) {
			System.out.println("Bildaufnahme Unterbrochen!");
		}catch (IOException e) {
			System.out.println("Bildaufnahme Unterbrochen!");
		}
		return null;
	}
	
}
