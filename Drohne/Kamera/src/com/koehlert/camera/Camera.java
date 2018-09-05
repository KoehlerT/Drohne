package com.koehlert.camera;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class Camera {
	
	static{
		//System.out.println(System.getProperty("sun.arch.data.model"));
		//System.load("/home/pi/Documents/WCapture/picamdemo/libpiCamera.so");
		System.load("/lib/drohne/libpiCamera.so");
	}
	
	//Downsampling: how many detail levels (1 = just the capture res, >1 goes down by halves, 4 max)
	private native void init(int width, int height, int framerate);
	private native void stop();
	
	private native void getRGBAData(ByteBuffer buf);
	
	private int width;
	private int height;
	private int framerate;
	private float getTime;
	
	private ByteBuffer dataBuffer;
	
	private boolean doConversion;
	
	public Camera(int width, int height, int framerate, boolean doConversion){
		this.width = width;
		this.height = height;
		this.framerate = framerate;
		this.doConversion = doConversion;
		
		dataBuffer = ByteBuffer.allocateDirect(width * height * 4);
	}
	
	public void init(){
		init(width, height, framerate);
	}
	
	public void close(){
		stop();
	}
	
	private void getData(){
		long start = System.nanoTime();
		getRGBAData(dataBuffer);
		long diff = System.nanoTime() - start;
		getTime = (float)((float)diff/1000000.0f);
		
	}
	
	
	private int getColorAt(int index){
		int red = dataBuffer.get(index) & 0x000000FF;
		int green = dataBuffer.get(index+1) & 0x000000FF;
		int blue = dataBuffer.get(index+2) & 0x000000FF;
		int alpha = dataBuffer.get(index+3) & 0x000000FF;
		
		//normal
		if (!doConversion)
			return blue | green << 8 | red << 16 | alpha << 24;
		
		//yuv angenommen
		int _red = (int)(1.164*(double)(red-16)+1.596*(double)(blue-128));
		int _green = (int)(1.164*(double)(red-16)-0.813*(double)(blue-128)-0.391*(double)(green-128));
		int _blue = (int)(1.164*(double)(red-16)+2.018*(double)(green-128));
		
		_red = clamp(_red);
		_green = clamp(_green);
		_blue = clamp(_blue);
		
		return _blue | _green << 8 | _red << 16 | alpha << 24;
	}
	
	private int clamp(int value){
		if (value >=255)
			return 255;
		if (value <= 0)
			return 0;
		else return value;
	}
	
	public BufferedImage getBufferedImage(){
		getData();
		BufferedImage ret = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
	
		for (int y = 0; y < height; y++){
			for (int x=0; x<width; x++){
				int index = (x + (y * width))*4;
				int col = getColorAt(index);
				ret.setRGB(x, y, col);
			}
		}
		//printData();
		return ret;
	}
	
	public byte[][] getGrayArray(){
		getData();
		
		byte[][] ret = new byte[width][];
		for (int i = 0; i < ret.length; i++){
			ret[i] = new byte[height];
		}
		
		for (int y = 0; y < height; y++){
			for (int x = 0; x < width; x++){
				int index = (x + (y * width))*4;
				
				int color = getColorAt(index);
				
				int blue = color&0x000000FF;
				int green = (color >> 8) & 0x000000FF;
				int red = (color>>16) & 0x000000FF;
				
				ret[x][y] = (byte)((int)((float)(red+green+blue)/3.0f));
			}
		}
		
		
		return ret;
	}
	
	public int[][] getRGBArray(){
		getData();
		
		int[][] ret = new int[width][];
		for (int i = 0; i < ret.length; i++){
			ret[i] = new int[height];
		}
		
		for (int y = 0; y < height; y++){
			for (int x = 0; x < width; x++){
				int index = (x + (y * width))*4;
				
				int color = getColorAt(index);
				
				ret[x][y] = color;
			}
		}
		
		
		return ret;
	}
	
	public float getTime(){
		return getTime;
	}
	
	public void printData(){
		for (int i = 0; i< 100; i++){
			System.out.print((int)(dataBuffer.get()&0x000000FF)+", ");
		}
	}

}
