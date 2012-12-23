package Interface;

import javafx.application.Application;

import javafx.stage.Stage;

public class Main extends Application {
  
  public static void main(String[] args) {

    Application.launch(Main.class, args);

  }
  
  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setMinHeight(300);
    primaryStage.setMinWidth(500);
  

    primaryStage.setScene(MainWindow.getMainWindow());
    primaryStage.show();
  }
}
