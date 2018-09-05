package com.koehlert.flowerflyer.camera;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PictureWriter extends Thread{
	
	int[][] image;
	String nextName;
	boolean pictureRequested = false;
	boolean processing = false;
	boolean running = true;
	
	public PictureWriter(){
		this.start();
	}
	
	public void requestPicture(int[][] img, String name){
		if (processing)
			return;
		processing = true;
		image = img.clone();
		nextName = name;
		pictureRequested = true;
	}
	
	public void Stop(){
		running = false;
	}
	
	@Override
	public void run(){
		while(running){
			if (processing && pictureRequested){
				long start = System.nanoTime();
				BufferedImage img = new BufferedImage(image.length,image[0].length,BufferedImage.TYPE_INT_ARGB);
				for (int y = 0; y < image[0].length; y++){
					for (int x=0; x<image.length; x++){
						img.setRGB(x, y, image[x][y]);
					}
				}
				try {
					ImageIO.write(img, "jpg", new File("/home/pi/Bilder/"+nextName+".jpg"));
					System.out.println("Save Time:"+((float)System.nanoTime() - start)/1000000f+"ms");
					processing = false;
					pictureRequested = false;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
