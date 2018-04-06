package panels;
import java.awt.Component;
import java.util.LinkedList;

import javax.swing.*;

import wert.Werteverwalter;
public class Vitalmonitore {
	static JInternalFrame critic = new JInternalFrame("kritisch", true, true, true, true);
	static JInternalFrame uncritic = new JInternalFrame("unwichtig", true, true, true, true);
	static LinkedList<Werteverwalter> kritisch = new LinkedList<Werteverwalter>();
	static LinkedList<Werteverwalter> unkritisch = new LinkedList<Werteverwalter>();
	public Vitalmonitore(JDesktopPane p,  LinkedList<Werteverwalter> listkomplett) {
		//Neune Fenster+ anzeigen
		p.add(critic);
		p.add(uncritic);
		critic.setSize(250, 250);
		uncritic.setSize(250, 250);
		critic.setLocation(0, 0);
		uncritic.setLocation(250, 0);
		critic.setLayout(null);
		uncritic.setLayout(null);
		critic.show();
		uncritic.show();
		// die Liste aller Werte zerteilen
		splitlist(listkomplett);;
		//Array aller kritischen werte zu anzeige umwandeln
		JLabel[] c = new JLabel[kritisch.size()];
		for (int i = 0; i < c.length ; i++) {
			c[i] = new JLabel(kritisch.get(i).getname() + ": " + kritisch.get(i).getString());
			c[i].setForeground(kritisch.get(i).getColor());
			Vitalmonitore.addtocritic(c[i],0,i*15);
		}
		//Array aller unkritischen werte zu anzeige umwandeln
		JLabel[] uc = new JLabel[unkritisch.size()];
		for (int i = 0; i < uc.length; i++) {
			uc[i] = new JLabel(unkritisch.get(i).getname() + ": " + unkritisch.get(i).getString());
			uc[i].setForeground(unkritisch.get(i).getColor());
			Vitalmonitore.addtouncritic(uc[i],0,i*15);
		}
	}
	public static void addtocritic(Component l,int x,int y) {
		l.setBounds(x, y, 250, 30);
		critic.add(l);
	}

	public static void addtouncritic(Component l,int x, int y) {
		l.setBounds(x, y, 250, 30);
		uncritic.add(l);
	}
	public static void splitlist(LinkedList<Werteverwalter> l) {
		for (int i = 0; i < l.size(); i++) {
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
