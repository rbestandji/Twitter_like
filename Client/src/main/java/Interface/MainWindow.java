package Interface;

import Network.GetUserTask;
import com.sun.jersey.api.client.ClientResponse;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import share.model.User;

public class MainWindow extends Scene {

  private static MainWindow mainWindow = new MainWindow();
  private ProgressBar progress;
  private TextField fieldSearch;
  private Button startSearch;
  private User user = null;
  private Wall wall = null;
  private IProfil profil = null;

  //private TextArea text;
  private MainWindow() {
    super(new StackPane());
    wall = new Wall();
    profil= new IProfil();
    init();
  }

  public static MainWindow getMainWindow() {
    return mainWindow;
  }

  public void setUser(User user) {
    this.user = user;
    wall.setUser(user);
    profil.setUser(user);
  }

  private void init() {
    int W = 10;
    int H = 5;
    // Scene root
    StackPane root = (StackPane) this.getRoot();


    GridPane grid = new GridPane();

    grid.setHgap(10);
    grid.setVgap(10);

    // Scene
    progress = new ProgressBar();
    progress.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    fieldSearch = new TextField();
    startSearch = new Button("Chercher");


    // Scene tree
    grid.add(progress, 0, 0, W / 2, 1);
    grid.add(fieldSearch, W / 2, 0, W / 4, 1);
    grid.add(startSearch, W / 2 + W / 4, 0, W / 4, 1);
    grid.add(profil, 0, 1, W / 4, W / 2);
    grid.add(wall,W / 4, 1, 3*W / 4, W / 2);

    startSearch.setOnAction(new GoodFetchMenuActionHandler());
    root.getChildren().add(grid);
    this.setRoot(root);
  }

  public void updateUserProfil(ClientResponse sta) {
    System.out.println("L 1  : " + sta.toString());
  }

  private class GoodFetchMenuActionHandler implements EventHandler<ActionEvent> {

    public void handle(ActionEvent t) {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }
}
