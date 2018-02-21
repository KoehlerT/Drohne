package kamera;

import main.BildPool;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.hopding.jrpicam.*;
import com.hopding.jrpicam.enums.Encoding;
import com.hopding.jrpicam.enums.Exposure;
import com.hopding.jrpicam.exceptions.FailedToRunRaspistillException;

class KameraThread extends Thread{
	
	private int aufnahmezeit;
	private RPiCamera piCamera;
	
	KameraThread() {
		this.setName("Bildaufnahme");
	}
	
	@Override
	public void run() {
		if (!kameraAngeschlossen())
			return;
		kameraKonfigurieren();
		while (true) {
			BufferedImage img = bildAufnehmen();
			if (img == null) {
				System.out.println("Fehler Bild == null");
				continue;
			}	
			byte[][] bild = grayArray(img);
			BildPool.addBild(bild, aufnahmezeit);
		}
	}
	
	private Boolean kameraAngeschlossen() {
		
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
		piCamera.setToDefaults();
		piCamera.setWidth(500)
				.setHeight(370)
				.setExposure(Exposure.SPORTS)
				.setTimeout(0)
				.setContrast(100)
				.setSharpness(100)
				.setEncoding(Encoding.BMP);
	}
	
	private BufferedImage bildAufnehmen() {
		try {
			long start = System.nanoTime();
			BufferedImage buffImg = piCamera.takeBufferedStill();
			aufnahmezeit = (int)(System.nanoTime()-start);
			System.out.println("Bild aufgenommen in "+aufnahmezeit/1000000+" ms");
			return buffImg;
		}catch (InterruptedException e) {
			System.out.println("Bildaufnahme Unterbrochen!");
		}catch (IOException e) {
			System.out.println("Bildaufnahme Unterbrochen!");
		}
		return null;
	}
	
	private byte[][] grayArray(BufferedImage img){
		byte[][] res = new byte[img.getWidth()][];
		for (int x = 0; x < res.length; x++) {
			res[x] = new byte[img.getHeight()];
			for (int y = 0; y < res[x].length; y++) {
				int rgb = img.getRGB(x, y);
				res[x][y] = getGray(rgb);
			}
		}
		return res;
	}
	
	private byte getGray(int rgb) {
		//rgb: erste 4 byte: alpha, zweite 4 byte: rot ...
		//Danke StackOvcerFlow https://stackoverflow.com/questions/2615522/java-bufferedimage-getting-red-green-and-blue-individually
		int red = (rgb >> 16) & 0x000000FF;
		int green = (rgb >>8 ) & 0x000000FF;
		int blue = (rgb) & 0x000000FF;
		return (byte)((red+green+blue)/3);
	}

}
