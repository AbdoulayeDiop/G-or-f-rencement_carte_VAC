package vac_georef.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import vac_georef.controller.PrimaryPageController;
import vac_georef.controller.SearchBarController;
import vac_georef.model.Model;
import vac_georef.view.PrimaryPageView;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Model model = new Model();
		
		FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/resources/fxml/PrimaryPage.fxml"));
		Parent root1 = loader1.load();
		PrimaryPageController controller1 = loader1.getController();
		controller1.setMain(primaryStage);
		controller1.setModel(model);//donner le model

		PrimaryPageView scene1 = new PrimaryPageView(root1, 400, 300);
		
		primaryStage.setTitle("Charger des cartes VAC");
		primaryStage.setScene(scene1);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
		//Application.launch(SecondPageView.class,(String)null);
	}
}