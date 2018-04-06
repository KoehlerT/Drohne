package steuerung;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class HintenAction extends AbstractAction {

	JLabel ljl = new JLabel();
	public HintenAction(JLabel j) {
		ljl = j;
		
	}
	@Override // Die Action die ausgeführt werden soll
	public void actionPerformed(ActionEvent e) {
		ljl.setForeground(Color.ORANGE);
		System.out.println("Die Drohne fliegt nach links");
	}

}
