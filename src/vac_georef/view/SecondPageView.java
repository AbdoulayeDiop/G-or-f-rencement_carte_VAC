package vac_georef.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import vacModele.FichierPDF;
import vac_georef.controller.CentralViewController;
import vac_georef.controller.SearchBarController;
import vac_georef.model.Model;

public class SecondPageView extends HBox {

	public SecondPageView(Model model, Stage stage) {
		super();
		// TODO Auto-generated constructor stub
		FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/resources/fxml/SearchBar.fxml"));
		FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/resources/fxml/CentralView.fxml"));
		Parent searchBar;
		Parent centralView;
		try {
			searchBar = loader1.load();
			centralView = loader2.load();
			searchBar.setStyle("-fx-border-color: gray");
			this.getChildren().addAll(searchBar, centralView);
			setAlignment(Pos.CENTER);
			stage.setMinHeight(500);
			stage.setMinWidth(820);
			stage.setMaxHeight(500);
			stage.setMaxWidth(820);
			
			SearchBarController controller1 = loader1.getController();
			controller1.setModel(model);//donner le model
			controller1.setMain(stage);
			
			CentralViewController controller2 = loader2.getController();
			controller2.setModel(model);//donner le model
			controller2.setMain(stage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SecondPageView(double spacing, Node... children) {
		super(spacing, children);
		// TODO Auto-generated constructor stub
	}

	public SecondPageView(double spacing) {
		super(spacing);
		// TODO Auto-generated constructor stub
	}

	public SecondPageView(Node... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}
	

}