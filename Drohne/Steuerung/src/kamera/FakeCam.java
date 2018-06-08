package kamera;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FakeCam {
	
	String path = "/home/pi/Bilder/";
	File[] pictures;
	int index = 0;
	
	public FakeCam() {
		File dir = new File(path);
		pictures = dir.listFiles();
	}
	
	
	public BufferedImage takePicture() {
		index = (index++)%pictures.length;
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			return ImageIO.read(pictures[index]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public int getTime() {
		return 0;
	}
	
}
