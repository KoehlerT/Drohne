package com.koehlert.camera;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Camera camera = new Camera(640,480,10);
		camera.init();
		System.out.println("Initialized Camera");
		
		for (int i = 0; i < 10; i++){
			
			BufferedImage img = camera.getBufferedImage();
			System.out.println("Got Image");
			try {
				File dest = new File("Image"+i+".jpg");
				ImageIO.write(img, "JPG", dest);
				System.out.println("Written to:"+dest.getAbsolutePath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		camera.close();
		
	}

}
