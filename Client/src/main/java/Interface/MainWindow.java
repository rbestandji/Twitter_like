/*package Interface;

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
  private IUser iUser;
  
  
  //private TextArea text;
  private MainWindow() {
    super(new StackPane());
    iUser = new IUser();;
    init();
  }

  public static MainWindow getMainWindow() {
    return mainWindow;
  }

  public void setUser(User user) {
    iUser.setUser(user);
  }

  private void init() {
    int W = 10;
    int H = 5;
    // Scene root
    StackPane root = (StackPane) this.getRoot();


    GridPane grid = new GridPane();
    
    grid.setHgap(W);
    grid.setVgap(H);

    // Scene
    progress = new ProgressBar();
    progress.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    fieldSearch = new TextField();
    startSearch = new Button("Chercher");


    // Scene tree
    grid.add(progress, 0, 0, W / 2, 1);
    grid.add(fieldSearch, W / 2, 0, W / 4, 1);
    grid.add(startSearch, W / 2 + W / 4, 0, W / 4, 1);
    grid.add(iUser, 0, 1, W / 4, W / 2);

    startSearch.setOnAction(new GoodFetchMenuActionHandler());
    root.getChildren().add(grid);
    this.setRoot(root);
  }

  public void updateUserProfil(ClientResponse sta) {
    System.out.println("L 1  : " + sta.toString());
  }

  
  
  private class GoodFetchMenuActionHandler implements EventHandler<ActionEvent> {

    public void handle(ActionEvent t) {
      System.out.println("Debut !");
      GetUserTask  task = null;
      try {
        task = new GetUserTask( "1");
      } catch (MalformedURLException ex) {
        System.out.println("LAAAAA");
      }
      
      task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      public void handle(WorkerStateEvent success) {
          updateUserProfil((ClientResponse)success.getSource().getValue());
      }
    });
    new Thread(task, "Search").start();


    System.out.println("Fin !");
    }
  }
}
*/