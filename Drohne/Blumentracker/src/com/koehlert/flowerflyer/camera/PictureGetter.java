package com.koehlert.flowerflyer.camera;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.koehlert.camera.Camera;

public class PictureGetter {
	
	int[][] lastPic;
	Camera cam;
	
	public PictureGetter(){
		cam = new Camera(640, 480, 30, true);
		cam.init();
		
		saveTest();
	}
	
	public void close(){
		cam.close();
	}
	
	public int[][] getRGB(){
		return cam.getRGBArray();
	}
	
	public byte[][] getArr(byte[][] res){
		int[][] arr = cam.getRGBArray();
		lastPic = arr;
		for (int x = 0; x < arr.length; x++){
			for (int y = 0; y < arr[x].length; y++){
				int rgb = arr[x][y];
				int red = (rgb >> 16) & 0x000000FF;
				int green = (rgb >>8 ) & 0x000000FF;
				int blue = (rgb) & 0x000000FF;
				int gray = (red+green+blue)/3;
				res[x][y] = (gray <= 125)?0:(byte)255;
			}
		}
		
		
		return res;
	}
	
	public byte[][] getArr(int vers2){
		long startImg = System.nanoTime();
		BufferedImage img = cam.getBufferedImage();
		System.out.println("getBuffImg: "+((float)(System.nanoTime()-startImg)/1000000f)+"ms");
		
		byte[][] arr = new byte[img.getWidth()][];
		for (int i = 0; i < arr.length; i++)
			arr[i] = new byte[img.getHeight()];
		
		for (int x = 0; x < arr.length; x++){
			for (int y = 0; y < arr[x].length; y++){
				int rgb = img.getRGB(x, y);
				int red = (rgb >> 16) & 0x000000FF;
				int green = (rgb >>8 ) & 0x000000FF;
				int blue = (rgb) & 0x000000FF;
				
				int res = (int)((float)(red+green+blue)/3f);
				
				arr[x][y] = (res < 125)? 0: (byte)255;
			}
		}
		return arr;
		
	}
	
	public int[][] getLastPic(){
		return lastPic;
	}
	
	public void savePicture(int[][] ga,String name){
		long start = System.nanoTime();
		BufferedImage img = new BufferedImage(ga.length,ga[0].length,BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y < ga[0].length; y++){
			for (int x=0; x<ga.length; x++){
				int gray = ga[x][y];
				//System.out.print(gray);
				
				//int rgb = gray | gray << 8 | gray << 16 | (byte)255 << 24;
				
				img.setRGB(x, y, gray);
			}
		}
		try {
			ImageIO.write(img, "jpg", new File("/home/pi/Bilder/"+name+".jpg"));
			System.out.println("Save Time:"+((float)System.nanoTime() - start)/1000000f+"ms");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void saveTest(){
		BufferedImage img = cam.getBufferedImage();
		try {
			ImageIO.write(img, "jpg", new File("/home/pi/Desktop/test1.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
