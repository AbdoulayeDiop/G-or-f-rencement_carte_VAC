package vac_georef.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import vac_georef.model.Model;
import vac_georef.view.SecondPageView;

public class PrimaryPageController {
	private Model model;
	private Stage stage;
	private List<File> fileList;
	private File selectedDirectory;

	@FXML
	private ListView<HBox> filesListView;

	@FXML
	private Button directoryButton;

	// Event Listener on Button.onMouseClicked
	@FXML
	public void handleAddFileChooserButtonAction(MouseEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select PDF Files");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PDF Files", "*.pdf"));

		fileList.addAll(fileChooser.showOpenMultipleDialog(stage));
		
		if (fileList.size() != 0) {

			System.out.println(fileList.size());
			filesListView.getItems().clear();
			for (File file : fileList) {
				HBox hbox = new HBox();
				Button button = new Button();
				Label label = new Label(file.getName());
				hbox.getChildren().addAll(button, label);
				filesListView.getItems().add(hbox);

			}

		}
	}

	@FXML
	public void handleRemoveFileChooserButtonAction(MouseEvent event) {

	}

	// Event Listener on Button.onMouseClicked
	@FXML
	public void handleDirectoryChooserButtonAction(MouseEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Select a Directory");
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		selectedDirectory = directoryChooser.showDialog(stage);
		if (selectedDirectory != null) {
			directoryButton.setText(selectedDirectory.getAbsolutePath());
		}
	}

	// Event Listener on Button.onMouseClicked
	@FXML
	public void handleStartButtonAction(MouseEvent event) {
		if (fileList != null && selectedDirectory != null) {
			System.out.println("ok");
			setFileListInModel();//charger les fichier dans le model
			SecondPageView root = new SecondPageView(model, stage);
			stage.setScene(new Scene(root));
		} else {
			System.out.println("ko");
		}
	}

	public void setMain(Stage main) {
		stage = main;
	}

	@FXML
	public void initialize() {
		fileList = new ArrayList<File>();
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void setFileListInModel() {
		model.setFileList(fileList);
	}


}
