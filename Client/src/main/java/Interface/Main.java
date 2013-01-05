package Interface;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

  private Group root;
  public static ProgressBar progress;
  public static VBox vbox;
  private static Stage primaryStage;
  
  
  public static void main(String[] args) {
    Application.launch(Main.class, args);
  }

  static public Stage getMainWindow() {
    return primaryStage;
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Main.primaryStage = primaryStage;
    init(primaryStage);
    primaryStage.show();
  }

  private void init(Stage primaryStage) throws URISyntaxException, MalformedURLException {
    primaryStage.setTitle("Client Twitter-like");
    root = new Group();
    root.setTranslateX(10);
    root.setTranslateY(10);
    Scene scene = new Scene(root, 390, 280, Color.WHITE);
    primaryStage.setScene(scene);

    // vbox
    vbox = new VBox();
    vbox.setAlignment(Pos.CENTER);

    // Progress Bar
    progress = new ProgressBar();
    progress.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    progress.setProgress(0);

    // connexion
    CUser cuser = new CUser();

    vbox.getChildren().addAll(progress, cuser);
    root.getChildren().add(vbox);
  }
}
