package com.koehlert.camera;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Camera {
	
	static{
		System.out.println(System.getProperty("sun.arch.data.model"));
		System.load("/home/pi/Documents/WCapture/picamdemo/libpiCamera.so");
	}
	
	//Downsampling: how many detail levels (1 = just the capture res, >1 goes down by halves, 4 max)
	private native void init(int width, int height, int framerate);
	private native void stop();
	
	private native int[] getARGBArray(); //Returns argb values (byte 0,1 = alpga, 2,3 = red ...)
	private native byte[] getGrayArray();
	
	private int full_width;
	private int full_height;
	private int framerate;
	private int downsamplingLevels;
	
	public Camera(int width, int height, int framerate){
		full_width = width;
		full_height = height;
		this.framerate = framerate;
	}
	
	public void init(){
		init(full_width, full_height, framerate);
	}
	
	public void close(){
		stop();
	}
	
	public BufferedImage getBufferedImage(){
		int calc_width = full_width;
		int calc_height = full_height;
		
		BufferedImage res = new BufferedImage(calc_width, calc_height, BufferedImage.TYPE_INT_ARGB);
		
		int[] colors = getARGBArray();
		if (colors.length == 0)
			throw new RuntimeException("Null");
		
		for (int y = 0; y < calc_height; y++){
			for (int x = 0; x < calc_width; x++){
				int rgb = colors[y*calc_width+x] & 0x00111111;
				res.setRGB(x, y, rgb);
			}
			
		}
		
		return res;
	}
	
	public Color[][] getColorArray(){
		int calc_width = full_width;
		int calc_height = full_height;
		
		Color[][] res = new Color[calc_width][];
		for (int i = 0; i < res.length; i++){
			res[i] = new Color[calc_height];
		}
		
		int[] colors = getARGBArray();
		
		for (int y = 0; y < calc_height; y++){
			for (int x = 0; x < calc_width; x++){
				int rgb = colors[y*calc_width+x] & 0x00111111;
				res[x][y] = new Color(rgb);
			}
			
		}
		
		return res;
	}
	
	public byte[][] getGrayscaleArray(byte[][] buffer){
		int calc_width = full_width;
		int calc_height = full_height;
		
		byte[] grays = getGrayArray();
		
		for (int y = 0; y < calc_height; y++){
			for (int x = 0; x < calc_width; x++){
				byte color = grays[y*calc_width+x];
				buffer[x][y] = color;
			}
			
		}
		
		return buffer;
	}
	
	public byte[][] getGrayscaleArray(){
		int calc_width = full_width;
		int calc_height = full_height;
		
		byte[][] res = new byte[calc_width][];
		for (int i = 0; i < res.length; i++){
			res[i] = new byte[calc_height];
		}
		
		return getGrayscaleArray(res);
	}
	

}
