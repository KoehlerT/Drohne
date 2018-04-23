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
	JDialog mf = new JDialog();
	
	
	private Bildanzeige bildanzeige;
	private Vitalmonitore vitalmonitore;
	private ManuSteu manuelleSteuerung;
	private Buttonmanager buttonmanager;
	private KonsolenFenster konsolenFenster;
	
	public Frame() {
		mf.setSize(1400, 750);
		JDesktopPane p = new JDesktopPane();
		//initalisieren aller Fenster
		bildanzeige = new Bildanzeige(p);
		vitalmonitore = new Vitalmonitore(p);
		manuelleSteuerung = new ManuSteu("Key", p);
		buttonmanager = new Buttonmanager(p);
		konsolenFenster = new KonsolenFenster(p);
		mf.add(p);
		mf.setVisible(true);
	}
	
	public void update() {
		vitalmonitore.update();
	}
}
