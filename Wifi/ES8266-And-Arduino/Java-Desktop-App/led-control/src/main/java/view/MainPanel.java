package view;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import constants.Constants;
import controllers.LEDColorSelectionListener;
import net.miginfocom.swing.MigLayout;

public class MainPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JColorChooser colorChooser;
	
	public MainPanel() {
		super(new MigLayout(Constants.MIG_FILL));
		frame = new JFrame();
		colorChooser = new JColorChooser();
		colorChooser.getSelectionModel()
				.addChangeListener(new LEDColorSelectionListener(colorChooser.getSelectionModel(), this));
		add(colorChooser);
		frame.add(this);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	
}
