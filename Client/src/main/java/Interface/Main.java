package Interface;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
  public static Group root;  
  public static ProgressBar progress;

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
    root.setTranslateX(10);
    root.setTranslateY(10);
    Scene scene = new Scene(root, 500, 500, Color.WHITE);
    primaryStage.setScene(scene);

    // Progress Bar
    progress = new ProgressBar();
    progress.setTranslateX(10);
    progress.setTranslateY(450);
    progress.setMaxSize( Double.MAX_VALUE, Double.MAX_VALUE );
    progress.setProgress( 1 );

    // connexion
    CUser cuser = new CUser();
    
    root.getChildren().addAll(cuser,progress);
  }

}
