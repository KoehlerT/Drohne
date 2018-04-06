package panels;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

import steuerung.*;
@SuppressWarnings ("serial")
public class ManuSteu  {
	JLabel[] a = new JLabel[6];
	int c = 0;
    public ManuSteu(String s, JDesktopPane p) {
    	//Anzeige der Steuermöglichkeiten
    	a[0] = new JLabel("Oben");
    	a[1] = new JLabel("Vorne");
    	a[2] = new JLabel("Unten");
    	a[3] = new JLabel("Links");
    	a[4] = new JLabel("Hinten");
    	a[5] = new JLabel("Rechts");
    	JInternalFrame j = new JInternalFrame("Key",true,true,true,true);
    	//Actions initalisieren um sie weiter unten verwenden zu können
    	Action linksAction = new LinksAction(a[3]);
    	Action rechtsAction = new RechtsAction(a[5]);
    	Action hintenAction = new HintenAction(a[4]);
    	Action untenAction = new UntenAction(a[2]);
    	Action obenAction = new ObenAction(a[0]);
    	Action vorneAction = new VorneAction(a[1]);
        p.add(j);
        //Bild festlegen
        j.setSize(250, 250);
        j.setLocation(0, 250);
        j.setTitle(s);
        j.setLayout(null);
        for (int i = 0; i < 3;i++) {
        	a[i].setBounds(i*50+20, 50, 35, 30);
        	a[i].setFont(new Font("Comic Sans MS",Font.BOLD,12));
        	j.add(a[i]);
        }
        for (int y = 3; y < 6;y++) {
        	a[y].setBounds(c*50+20, 100, 50, 30);
        	a[y].setFont(new Font("Comic Sans MS",Font.BOLD,12));
        	c++;
        	j.add(a[y]);
        }
        j.show();
        //Listener für Keybindings generieren
        InputMap asd = j.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap ap = j.getActionMap();
        //Keybindings und Actions festlegen
        asd.put(KeyStroke.getKeyStroke(KeyEvent.VK_A,0,false), "linksfliegen");
        ap.put("linksfliegen", linksAction);
        asd.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "stop6");
        ap.put("stop6", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				a[3].setForeground(Color.black);
			}});
        
        asd.put(KeyStroke.getKeyStroke(KeyEvent.VK_W,0,false), "vornefliegen");
        ap.put("vornefliegen", vorneAction);
        asd.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "stop5");
        ap.put("stop5", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				a[1].setForeground(Color.black);
			}});
        
        asd.put(KeyStroke.getKeyStroke(KeyEvent.VK_S,0,false), "hintenfliegen");
        ap.put("hintenfliegen", hintenAction);
        asd.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "stop4");
        ap.put("stop4", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				a[4].setForeground(Color.black);
			}});
        
        asd.put(KeyStroke.getKeyStroke(KeyEvent.VK_D,0,false), "rechtsfliegen");
        ap.put("rechtsfliegen", rechtsAction);
        asd.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "stop3");
        ap.put("stop3", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				a[5].setForeground(Color.black);
			}});
        
        asd.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q,0,false), "obenfliegen");
        ap.put("obenfliegen", obenAction);
        asd.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, true), "stop2");
        ap.put("stop2", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				a[0].setForeground(Color.black);
			}});
        
        asd.put(KeyStroke.getKeyStroke(KeyEvent.VK_E,0,false), "untenfliegen");
        ap.put("untenfliegen", untenAction);
        asd.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, true), "stop1");
        ap.put("stop1", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				a[2].setForeground(Color.black);
			}});
    }

}
