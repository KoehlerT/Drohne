package kamera;

import java.awt.image.BufferedImage;

import com.koehlert.camera.*;

import main.Info;




public class Cam {
	
	static {
		System.load("/lib/drohne/libpiCamera.so");
	}
	
	private float aufnahmezeit;
	private Camera piCamera;
	
	//private boolean connected = Info.CamAttached;
	
	
	public Cam() {
		piCamera = new Camera(Info.pictureWidth,Info.pictureHeight,30,false);
		
		piCamera.init();
	}
	
	public BufferedImage takePicture() {
		
		return bildAufnehmen();
	}
	
	public float getTime() {
		return aufnahmezeit;
	}
	
	
	public BufferedImage bildAufnehmen() {
		long start = System.nanoTime();
		BufferedImage buffImg = piCamera.getBufferedImage();
		aufnahmezeit = (float)(System.nanoTime()-start)/1_000_000;
		//System.out.println("Bild aufgenommen in "+aufnahmezeit/1000000+" ms");
		return buffImg;
	}
	
	public byte[][] getGrayscaleImage(){
		long start = System.nanoTime();
		byte[][] res = piCamera.getGrayArray();
		aufnahmezeit = (float)(System.nanoTime()-start)/1_000_000;
		
		return res;
	}
	
	public void Close() {
		piCamera.close();
	}
	
}
