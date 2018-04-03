package panels;

import javax.swing.JDesktopPane;
import javax.swing.JDialog;

public class nerdGUI {
	private JDialog mf = new JDialog();
	public nerdGUI(){
		mf.setSize(1600, 1500);
		JDesktopPane p = new JDesktopPane();
		mf.add(p);
		mf.setVisible(true);
		
	}
}
