package vac_georef.controller;

import javafx.fxml.FXML;

import java.awt.Button;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import vacModele.FichierPDF;
import vacModele.PagePDF;
import vac_georef.model.Model;

public class SearchBarController {
	private Model model;
	private Stage stage;

	@FXML
	private ListView<HBox> listView;
	@FXML
	private TextField textSearch;

	// Event Listener on TextField.onInputMethodTextChanged
	@FXML
	public void dynamicSearch(InputMethodEvent event) {
		String textDynamic = textSearch.getText();

	}

	// Event Listener on Button.onAction
	@FXML
	public void staticSearch(ActionEvent event) {
		String textStatic = textSearch.getText().toUpperCase();
		// listView.getItems().add(textStatic);
		ArrayList<FichierPDF> pdfArray = model.getPdfArray();
		for (int i = 0; i < pdfArray.size(); i++) {
			FichierPDF pdf = pdfArray.get(i);
			HBox hbox = new HBox();
			String carteNom = pdf.getName().split("/")[0].toUpperCase();// par exemple VAC/LFBO.pdf --> LFBO.pdf
			if (carteNom.equals(textStatic) && !listView.getItems().contains(carteNom)) {
				Label label = new Label(carteNom);
				hbox.getChildren().add(label);
				listView.getItems().add(hbox);
			}
		}
	}

	public void setModel(Model model) {
		this.model = model;

		List<File> fileList = model.getFileList();
		if (fileList.size() != 0) {
			listView.getItems().clear();
			for (File file : fileList) {
				HBox hbox = new HBox();
				Button button = new Button();
				Label label = new Label(file.getName());
				hbox.getChildren().add(label);
				listView.getItems().add(hbox);

			}

		}
	}

	public void setMain(Stage main) {
		stage = main;
	}
}
