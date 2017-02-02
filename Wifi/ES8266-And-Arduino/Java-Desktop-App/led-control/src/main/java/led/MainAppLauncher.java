package led;

import javax.swing.SwingUtilities;

import view.MainPanel;

public class MainAppLauncher {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new MainPanel());
	}
}
