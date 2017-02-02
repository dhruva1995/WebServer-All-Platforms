package controllers;

import java.awt.Color;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import service.LEDService;
import view.MainPanel;

public class LEDColorSelectionListener implements ChangeListener {

	private MainPanel parentPanelForErr;
	
	private ColorSelectionModel model;
	public LEDColorSelectionListener(ColorSelectionModel model, MainPanel mainPanel){
		this.parentPanelForErr = mainPanel;
		this.model = model;
	}


	@Override
	public void stateChanged(ChangeEvent e) {
		Color selectedColor = this.model.getSelectedColor();
		System.out.println(selectedColor.toString());
		LEDService.makeAsynchronousHttpCall(selectedColor, this.parentPanelForErr);
	}


}
