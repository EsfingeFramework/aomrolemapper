package br.com.bernardorufino.esfinge.genexample.view;

import br.com.bernardorufino.esfinge.genexample.view.base.BaseController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Initializer extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main/main.fxml"));
        Parent root = (Parent) loader.load();
        BaseController controller = loader.getController();
        controller.setStage(primaryStage);
        primaryStage.setTitle("Esfinge Example");
        primaryStage.setScene(new Scene(root, 440, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}