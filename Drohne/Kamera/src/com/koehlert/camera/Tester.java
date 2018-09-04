package com.koehlert.camera;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		Camera c = new Camera(640,480,30,false);
		
		c.init();
		System.out.println("Initialized Camera");
		
		BufferedImage[] arr = new BufferedImage[20];
		
		for (int i=0; i<arr.length; i++){
			arr[i] = c.getBufferedImage();
		}
		
		for (int i = 0; i < arr.length; i++){
			File f = new File("image"+i+".jpg");
			try {
				ImageIO.write(arr[i],"jpg",f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		BufferedImage nachz = c.getBufferedImage();
		try {
			ImageIO.write(nachz,"jpg",new File("nachz.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		c.close();
		
		
	}

}
