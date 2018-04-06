package steuerung;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
//siehe Klasse HintenAction
public class ObenAction extends AbstractAction{

	JLabel ljl = new JLabel();
	public ObenAction(JLabel j) {
		ljl = j;
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		ljl.setForeground(Color.ORANGE);
		System.out.println("Die Drohne fliegt nach oben");
	}

}
