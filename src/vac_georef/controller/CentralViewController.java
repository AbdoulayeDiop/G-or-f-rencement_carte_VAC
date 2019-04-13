package vac_georef.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import vac_georef.model.Model;

public class CentralViewController {
	private Model model;
	private Stage stage;
	
	@FXML
	private Button leftButton;
	
	@FXML
	private Button rightButton;
	
	@FXML
	private StackPane stackPane;
	
	@FXML
	public void handleLeftButtonOnPressed(MouseEvent e) {
		
	}
	
	@FXML
	public void handleRightButtonOnPressed(MouseEvent e) {
		
	}
	
	public void setModel(Model model) {
		this.model = model;
	}

	public void setMain(Stage main) {
		stage = main;
	}

}
