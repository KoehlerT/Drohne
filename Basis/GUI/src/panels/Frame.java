package panels;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import javax.swing.*;

import Bild.Kreiszeichner;
import wert.*;
@SuppressWarnings("unused")
public class Frame {
	int[] al = new int[5];
	static JDialog mf = new JDialog();
	static LinkedList<Werteverwalter> listkomplett = new LinkedList<Werteverwalter>();
	public Frame() {
		mf.setSize(1400, 750);
		JDesktopPane p = new JDesktopPane();
		//initalisieren aller Fenster
		new Bildanzeige(p);
		new Vitalmonitore(p,listkomplett);
		new ManuSteu("Key", p);
		new Buttonmanager(p);
		new KonsolenFenster(p);
		mf.add(p);
		mf.setVisible(true);
	}

	

	public static void addtoll(Werteverwalter w) {
		listkomplett.add(w);
	}




	
}
