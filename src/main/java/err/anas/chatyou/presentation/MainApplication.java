package err.anas.chatyou.presentation;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        AppNavigator.setStage(primaryStage);
        AppNavigator.loadLoginScene();
    }
}