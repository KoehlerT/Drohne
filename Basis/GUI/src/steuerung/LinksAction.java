package steuerung;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.*;
// siehe Klasse HintenAction
@SuppressWarnings("serial")
public class LinksAction extends AbstractAction {
	JLabel ljl = new JLabel();
	public LinksAction(JLabel j) {
		ljl = j;
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		ljl.setForeground(Color.ORANGE);
		System.out.println("Die Drohne fliegt nach links");
	}

}
