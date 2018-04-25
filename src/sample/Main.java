package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setResizable(false);
        primaryStage.setTitle("MyPlayer");



        Scene scene = new Scene(root);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        // For modena.css a slight shaded background was introduced for all controls (and also to panes if a control is loaded), So if we want to set background visible as false we need to add the stylesheet
        root.setStyle("-fx-background-color: transparent");


        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
