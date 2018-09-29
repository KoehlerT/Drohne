package panels;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import Bild.Kreiszeichner;
import main.Data;
import utillity.Blume;
@SuppressWarnings("unused")
public class Bildanzeige {
	static JInternalFrame bild = new JInternalFrame("bild", true, true, true, true);
	static JLayeredPane bil = bild.getLayeredPane();
	
	static final int width = 800;
	static final int height = 600;
	
	static BufferedImage bg;
	
	public Bildanzeige(JDesktopPane p) {
		p.add(bild);
		//Bildfenster festelegen
		bild.setSize(width, height);
		bild.setLocation(500, 0);
		bild.setLayout(null);
		//bild laden
		try {
			bg = ImageIO.read(new File("res/bg1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Tests zu beheben vom Bug der Kreise
		bil.setOpaque(false);
		bild.setOpaque(false);
		bild.show();
	}
	public static void bildAnzeigen(String filename) {
		//Bild als ImageIcon laden
		ImageIcon icon = new ImageIcon(filename);
		//Jlabel mit Icon zu bild hinzufügen
		JLabel a = new JLabel(icon);
		//Größe Festlegen
		a.setBounds(0, 0, width, height);
		bil.add(a,0);
		bil.setVisible(true);
	}
	public static void kreiszeichnen(JLayeredPane j,int x, int y,int r) {
		
		//Kreiszeichner krs = new Kreiszeichner(j,x,y,r);
		//krs.setBounds(x, y, 20, 20);
		
	}
	
	static List<Blume> lst = new ArrayList<Blume>(5);
	
	public static void drawBlumen() {
		Blume b = Data.getBlumen();
		if (b== null)
			return;
		
		Graphics g = bild.getGraphics();
		g.drawImage(bg, 0, 0, Color.WHITE, null);
		
			float rx = b.getX();
			float ry = b.getY();
			
			g.drawOval((int)(rx*width), (int)(ry*height), 50, 50);
		
	}
	
	public static void distAnzeige(JLayeredPane j,int x, int y,double wert) {
		JLabel an = new JLabel();
		an.setText(String.valueOf(wert));
		an.setBounds(x,y,20,10);
		bil.add(an, 0);
	}
	public static JLayeredPane getBil() {
		return bil;
	}
	
}
