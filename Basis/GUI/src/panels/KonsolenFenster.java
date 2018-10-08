package panels;
import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.*;

import steuerung.CommandProcesser;
public class KonsolenFenster {
	static Color c1 = Color.BLACK;
	static Color c2 = Color.BLACK;
	static Color c3 = Color.BLACK;
	static JLabel text1 = new JLabel("Hier könnte ihre Werbung stehen");
	static JLabel text2 = new JLabel("Hier könnte ihre Werbung stehen");
	static JLabel text3 = new JLabel("Hier könnte ihre Werbung stehen");
	@SuppressWarnings("serial")
	public KonsolenFenster(JDesktopPane p) {
		JInternalFrame j = new JInternalFrame("Konsole",true,true,true,true);
		p.add(j);
		j.setSize(500,210);
		j.setLocation(0, 500);
		j.setLayout(null);
		JTextField consol = new JTextField("Eingabe");
		consol.setBounds(0,120,490,30);
		JButton buttonOK = new JButton("OK");
		buttonOK.setBounds(220, 150, 60, 30);
		text1.setBounds(0,80,490,40);
		text2.setBounds(0,40,490,40);
		text3.setBounds(0,0,490,40);
		j.add(text1);
		j.add(text2);
		j.add(text3);
        j.add(buttonOK);
		j.add(consol);
		j.show();
		buttonOK.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//System.out.println(consol.getText());
				CommandProcesser.processCommand(consol.getText());
				consol.setText("");
			}
		});
		buttonOK.setText("OK");
	}
	public static void addText(String s, Color c) {
		c3 = c2;
		c2 = c1;
		c1 = c;
		text3.setText(text2.getText());
		text2.setText(text1.getText());
		text1.setText(s);
		text1.setForeground(c1);
		text2.setForeground(c2);
		text3.setForeground(c3);
	}
}
