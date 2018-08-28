package kamera;

import main.BildPool;
import main.Daten;
import main.Info;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


class KameraThread extends Thread{
	
	Cam c;
	
	KameraThread() {
		
		if (Info.CamAttached) {
			this.setName("Bildaufnahme");
			c = new Cam();
		}else {
			System.out.println("Camera disabled");
		}
	}
	
	int i = 0;
	
	@Override
	public void run() {
		if (!Info.CamAttached)
			return;
		try {
			ImageIO.write(c.bildAufnehmen(), "png", new File("/home/pi/Bilder/debug/pic#"+(i++)+".png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Jetzt werden Bilder aufgenommen
		while (Daten.running) {
			/*BufferedImage img = c.takePicture();
			
			if (img == null) {
				//System.out.println("Fehler Bild == null");
				continue;
			}*/
			
			//Neues Bildobjekt updaten
			byte[][] bild = c.getGrayscaleImage();
			BildPool.addBild(bild, c.getTime());
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		c.Close();
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
		
		int res = (int)((float)(red+green+blue)/3f);
		
		//return (byte)(res); //Linear
		return (byte)(255*(1/(1+Math.pow(Math.E,-0.025*(res-127)))));//Sigmoid
	}

}
