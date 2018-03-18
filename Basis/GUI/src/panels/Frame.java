package panels;
import java.awt.*;
import javax.swing.*;
public class Frame {
	static JDialog mf = new JDialog();
	static JInternalFrame inFrame1 = new JInternalFrame("Dokument 1", true, true, true, true);
	static JInternalFrame inFrame2 = new JInternalFrame("Dokument 2",true,true,true,true);
	static JInternalFrame inFrame3 = new JInternalFrame("Dokument 3",true,true,true,true);
	static JInternalFrame inFrame4 = new JInternalFrame("Dokument 4",true,true,true,true);
	static JInternalFrame inFrame5 = new JInternalFrame("Dokument 5",true,true,true,true);
	public Frame() {
		mf.setSize(1600, 1500);
		JDesktopPane p = new JDesktopPane();		
		p.add(inFrame1);
		p.add(inFrame2);
		p.add(inFrame3);
		p.add(inFrame4);
		p.add(inFrame5);
		inFrame1.setSize(250,500);
        inFrame2.setSize(250,500);
		inFrame3.setSize(250,500);
        inFrame4.setSize(250,500);
		inFrame5.setSize(250,500);
        inFrame1.setLocation(0,0);
        inFrame2.setLocation (250,0);
        inFrame3.setLocation (500,0);
        inFrame4.setLocation (750,0);
        inFrame5.setLocation (1000,0);
        inFrame1.show();
        inFrame2.show();
        inFrame3.show();
        inFrame4.show();
        inFrame5.show();
		mf.add(p);
		mf.setVisible(true);
	}
	public static void addtoFrame1(Component l) {
		inFrame1.add(l);
	}
	public static void addtoFrame2(Component l) {
		inFrame2.add(l);
	}
	public static void addtoFrame3(Component l) {
		inFrame3.add(l);
	}
	public static void addtoFrame4(Component l) {
		inFrame4.add(l);
	}
	public static void addtoFrame5(Component l) {
		inFrame5.add(l);
	}
}
