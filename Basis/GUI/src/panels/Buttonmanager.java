package panels;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.*;

import main.ProgramState;

public class Buttonmanager {
	
	private JButton startB; //Starte die Drohne
	private JButton recallB; //Rufe die Drohne zurück
	private JButton stopB; //Stoppe alle Motoren
	private JButton landB; //Lande SOFORT
	private JButton manuB; //Manuelle Steuerung an
	
	
	public Buttonmanager(JDesktopPane p) {
		// neues Fenster machen
		JInternalFrame internalFr = new JInternalFrame("Buttonmanager",true);
		p.add(internalFr);
		internalFr.setSize(250, 250);
		internalFr.setLocation(250,250);
		internalFr.setLayout(null);
		
		// Knöpfe initalisieren
		startB= new JButton("Start");
		recallB= new JButton("Recall");
		stopB= new JButton("Not aus");
		landB= new JButton("Landung");
		manuB = new JButton("Maunuelle Steuerung");
		
		//Positionen
		startB.setBounds(0,0,83,83);
		recallB.setBounds(83,0,83,83);
		stopB.setBounds(0,83,83,83);
		landB.setBounds(83,83,83,83);
		manuB.setBounds(83+83, 0, 83, 83);;
		
		//Add buttons Internal Frame
		internalFr.add(startB);
		internalFr.add(recallB);
		internalFr.add(stopB);
		internalFr.add(landB);
		internalFr.add(manuB);
		
		addActions();
		
		internalFr.show();
	}
	
	private void addActions() {
		//Hier werden die Actions der Buttons festgelegt; Kann verändert werden und wenn nötig in eigene Klassen geschoben werden
				//siehe Steuerung
				startB.setAction(new AbstractAction() {

					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Wir starten");
						ProgramState.getInstance().addSendingWord((byte)0x2);
					}});
				startB.setText("START");
				startB.setBackground(Color.GREEN);
				
				recallB.setAction(new AbstractAction() {

					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Zurück zur Basis");
						
					}});
				recallB.setText("Rückruf");
				recallB.setBackground(Color.ORANGE);
				
				stopB.setAction(new AbstractAction() {

					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Alle Motoren sofort STOPP");
						
					}});
				stopB.setText("Not aus");
				stopB.setBackground(Color.RED);
				
				landB.setAction(new AbstractAction() {

					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("NOTLANDEN");
						
					}});
				landB.setText("<html>Not-<br>landung</html>");
				landB.setBackground(Color.ORANGE);
				
				manuB.setAction(new AbstractAction() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Manuell");
						
					}
				});
				manuB.setText("<html>Manuelle<br>Steuerung</html>");
				
				manuB.setAction(new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ManuSteu.getInstance().toggleManu();
					}
				});
				manuB.setBackground(Color.BLUE);
				manuB.setForeground(Color.WHITE);
	}
}
