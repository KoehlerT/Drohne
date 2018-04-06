package panels;
import javax.swing.*;

import Bild.Kreiszeichner;
@SuppressWarnings("unused")
public class Bildanzeige {
	static JInternalFrame bild = new JInternalFrame("bild", true, true, true, true);
	static JLayeredPane bil = bild.getLayeredPane();
	public Bildanzeige(JDesktopPane p) {
		p.add(bild);
		//Bildfenster festelegen
		bild.setSize(800, 600);
		bild.setLocation(500, 0);
		bild.setLayout(null);
		//bild laden
		bildAnzeigen("D:\\Julia\\Pictures\\Earthporn\\Unbenannt-1.jpg");
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
		a.setBounds(0, 0, 800, 600);
		bil.add(a,0);
		bil.setVisible(true);
	}
	public static void kreiszeichnen(JLayeredPane j,int x, int y,int r) {
		
		Kreiszeichner krs = new Kreiszeichner(j,x,y,r);
		//krs.setBounds(x, y, 20, 20);
		
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
