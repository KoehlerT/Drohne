package steuerung;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JLabel;

public class UntenAction extends AbstractAction{
	// siehe Klasse HintenAction
	JLabel ljl = new JLabel();
	public UntenAction(JLabel j) {
		ljl = j;
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		ljl.setForeground(Color.ORANGE);
		System.out.println("Die Drohne fliegt nach unten");
	}


}
