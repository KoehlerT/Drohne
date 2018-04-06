package Bild;

import java.awt.*;

import javax.swing.*;
@SuppressWarnings("serial")
public class Kreiszeichner extends Canvas {
	int x;
	int y;
	int radius;
	public Kreiszeichner(JLayeredPane j,int xC, int yC,int r) {
		j.add(this,0);
		x = xC;
		y = yC;
		radius = r;
		j.setOpaque(false);
		System.out.println(this.isOpaque());
		this.setBounds(x, y, radius, radius);
	}
    public void paint(Graphics g) {
    	g.setColor(Color.RED);
    	g.drawOval(0, 0, radius, radius);
    } 

}
