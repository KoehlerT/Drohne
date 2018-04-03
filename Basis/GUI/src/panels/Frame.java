package panels;

import java.awt.*;
import java.util.LinkedList;
import javax.swing.*;
import wert.*;

public class Frame {
	int[] al = new int[5];
	static JDialog mf = new JDialog();
	static JInternalFrame critic = new JInternalFrame("kritisch", true, true, true, true);
	static JInternalFrame uncritic = new JInternalFrame("unwichtig", true, true, true, true);
	static JInternalFrame bild = new JInternalFrame("bild", true, true, true, true);
	static LinkedList<Werteverwalter> listkomplett = new LinkedList<Werteverwalter>();
	static LinkedList<Werteverwalter> kritisch = new LinkedList<Werteverwalter>();
	static LinkedList<Werteverwalter> unkritisch = new LinkedList<Werteverwalter>();

	public Frame() {
		mf.setSize(800, 500);
		JDesktopPane p = new JDesktopPane();
		p.add(critic);
		p.add(uncritic);
		p.add(bild);
		critic.setSize(250, 500);
		uncritic.setSize(250, 500);
		bild.setSize(250, 500);
		critic.setLocation(0, 0);
		uncritic.setLocation(250, 0);
		bild.setLocation(500, 0);
		critic.show();
		uncritic.show();
		bild.show();
		ImageIcon icon = new ImageIcon("D:\\Julia\\Pictures\\Earthporn\\Beispiel.jpg");
		bild.add(new JLabel(icon));
		bild.pack();
		bild.setVisible(true);
		mf.add(p);
		mf.setVisible(true);
		splitlist(listkomplett);
		JLabel[] c = new JLabel[kritisch.size()];
		System.out.println(kritisch.size());
		System.out.println(kritisch);
		System.out.println(unkritisch);
		for (int i = 0; i < c.length - 1; i++) {
			c[i] = new JLabel(kritisch.get(i).getname() + ": " + kritisch.get(i).getString());
			c[i].setForeground(kritisch.get(i).getColor());
			System.out.println(c[i].getText());
			addtocritic(c[i]);
		}
		JLabel[] uc = new JLabel[unkritisch.size()];
		for (int i = 0; i < uc.length - 1; i++) {
			uc[i] = new JLabel(unkritisch.get(i).getname() + ": " + unkritisch.get(i).getString());
			uc[i].setForeground(unkritisch.get(i).getColor());
			System.out.println(uc[i].getText());
			addtouncritic(c[i]);
		}
	}

	public static void addtocritic(Component l) {
		critic.add(l);
	}

	public static void addtouncritic(Component l) {
		uncritic.add(l);
	}

	public static void addtoll(Werteverwalter w) {
		listkomplett.add(w);
	}

	public static void splitlist(LinkedList<Werteverwalter> l) {
		for (int i = 0; i < l.size() - 1; i++) {
			if (l.get(i).getkritisch() && !(kritisch.contains(l.get(i)))) {
				kritisch.add(l.get(i));
			} else if (!(l.get(i).getkritisch()) && !(unkritisch.contains(l.get(i)))) {
				unkritisch.add(l.get(i));
			} else {
				System.out.println("großer Fehler bei: " + l.get(i).getname());
			}
		}
	}
}
