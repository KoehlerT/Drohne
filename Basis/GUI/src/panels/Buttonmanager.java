package panels;

import java.awt.event.ActionEvent;

import javax.swing.*;

public class Buttonmanager {
	@SuppressWarnings("serial")
	public Buttonmanager(JDesktopPane p) {
		JButton[] b = new JButton[4];
		// neues Fenster machen
		JInternalFrame j = new JInternalFrame("Buttomanager",true);
		p.add(j);
		j.setSize(250, 250);
		j.setLocation(250,250);
		j.setLayout(null);
		// Knöpfe initalisieren und platzieren
		b[0]= new JButton("Start");
		b[1]= new JButton("Recall");
		b[2]= new JButton("Not aus");
		b[3]= new JButton("Landung");
		b[0].setBounds(0,0,115,110);
		b[1].setBounds(115,0,115,110);
		b[2].setBounds(0,110,115,110);
		b[3].setBounds(115,110,115,110);
		j.add(b[0]);
		j.add(b[1]);
		j.add(b[2]);
		j.add(b[3]);
		//Hier werden die Actions der Buttons festgelegt; Kann verändert werden und wenn nötig in eigene Klassen geschoben werden
		//siehe Steuerung
		b[0].setAction(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Wir starten");
				
			}});
		b[0].setText("START");
		
		b[1].setAction(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Zurück zur Basis");
				
			}});
		b[1].setText("Rückruf");
		
		b[2].setAction(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Alle Motoren sofort STOPP");
				
			}});
		b[2].setText("Not aus");
		
		b[3].setAction(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("NOTLANDEN");
				
			}});
		b[3].setText("Notlandung");
		j.show();
	}
}
