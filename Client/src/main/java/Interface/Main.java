package Interface;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
  public Group root;  

  public static void main(String[] args) {
    Application.launch(Main.class, args);
  }
  
  @Override
  public void start(Stage primaryStage) throws Exception {
    init( primaryStage );
    primaryStage.show();
  }

  private void init(Stage primaryStage) throws URISyntaxException, MalformedURLException {
    primaryStage.setTitle("Client Twitter-like");
    root = new Group();
    Scene scene = new Scene(root, 500, 500, Color.WHITE);
    primaryStage.setScene(scene);

    // connexion
    final CUser cuser = new CUser();
    root.getChildren().add(cuser);

  }

}
