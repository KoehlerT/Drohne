package Bild;

import java.awt.*;

import javax.swing.*;
@SuppressWarnings("serial")
public class Kreiszeichner extends Canvas {
	int x;
	int y;
	int radius;
	Color c;
	public Kreiszeichner(JLayeredPane j,int xC, int yC,int r) {
		j.add(this,0);
		x = xC;
		y = yC;
		radius = r;
		this.setBounds(x, y, radius, radius);
	}
    public void paint(Graphics g) {
    	g.drawOval(0, 0, radius, radius);
    	g.setColor(Color.GREEN);
    } 

}
