package Interface;


import javafx.application.Application;

import javafx.stage.Stage;

public class Main  extends Application {

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        new LoginPage(new PageProfil()).start(primaryStage);
    }

}
