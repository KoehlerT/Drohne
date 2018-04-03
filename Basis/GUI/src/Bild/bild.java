package Bild;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class bild {
	public bild() {
		
		
	}
	public BufferedImage readPic() {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("D:\\Julia\\Pictures\\Earthporn\\Beispiel.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return img;
	}
}
