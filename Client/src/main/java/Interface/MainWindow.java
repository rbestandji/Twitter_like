package Interface;

import Controller.ConnectionOtherUser;
import Network.GetUserTask;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.representation.Form;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import share.core.Status;
import share.model.Message;
import share.model.User;

public class MainWindow extends Scene {
  
  private static MainWindow mainWindow = new MainWindow();
  private ProgressBar progress;
  private TextField fieldSearch;
  private Button startSearch;
  private User user = null;
  private Wall wall = null;
  private IProfil profil = null;
  private IFollower follower = null;
  private IFollowing following = null;
  public static User userConnected = null;

  //private TextArea text;
  private MainWindow() {
    super(new StackPane(), 700, 750);
    wall = new Wall();
    profil = new IProfil();
    follower = new IFollower();
    following = new IFollowing();
    init();
  }
  
  public static MainWindow getMainWindow() {
    return mainWindow;
  }
  
  public void setUser(User user) {
    this.user = user;
    wall.setUser(user);
    profil.setUser(user);
    follower.setUser(user);
    following.setUser(user);
  }
  
  public ProgressBar getProgress() {
    return progress;
  }
  
  private void init() {
    // Scene root
    StackPane root = (StackPane) this.getRoot();
    GridPane grid = new GridPane();
    grid.setHgap(7);
    grid.setVgap(5);
    
    int n = 11;
    for (int i = 0; i < n; i++) {
      ColumnConstraints c = new ColumnConstraints();
      c.setPrefWidth(20.);
      grid.getColumnConstraints().add(c);
    }
    n = 15;
    for (int i = 0; i < n; i++) {
      RowConstraints c = new RowConstraints();
      if (i == 0) {
        c.setPrefHeight(15.);
      } else {
        c.setPrefHeight(35.);
      }
      grid.getRowConstraints().add(c);
    }

    // Scene
    progress = new ProgressBar();
    progress.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    fieldSearch = new TextField();
    fieldSearch.setPrefWidth(100);
    startSearch = new Button("Chercher");

    // Scene tree
    grid.add(progress, 0, 0, 4, 1);
    grid.add(fieldSearch, 4, 0, 4, 1);
    grid.add(startSearch, 8, 0, 3, 1);
    progress.setProgress(0.);
    
    grid.add(profil, 0, 1, 4, 5);
    grid.add(follower, 0, 6, 4, 4);
    grid.add(following, 0, 10, 4, 4);
    grid.add(wall, 4, 1, 7, 14);
    
    
    startSearch.setOnAction(new SearchAction());
    root.getChildren().add(grid);
    this.setRoot(root);
  }
  
  private class SearchAction implements EventHandler<ActionEvent> {
    
    public void handle(ActionEvent t) {
      progress.setProgress(-1.);
      Task<ClientResponse> task = new Task<ClientResponse>() {
        @Override
        protected ClientResponse call() throws Exception {
          return GetUserTask.getUserTask().getCall("users/search/" + fieldSearch.getText());
        }
      };
      
      
      task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
        public void handle(WorkerStateEvent success) {
          ClientResponse result = (ClientResponse) success.getSource().getValue();
          progress.setProgress(0.);
          if (result.getStatus() == Status.OK) {
            
            Stage stage = new Stage();
            Group inside = new Group();
            stage.setScene(new Scene(inside, 200, 200));
            GridPane pane = new GridPane();
            pane.setHgap(15);
            pane.setVgap(10);
            List<HashMap<String, ?>> listUser = result.getEntity(List.class);
            int height = 0;
            for (HashMap<String, ?> u : listUser) {
              pane.add(new Label(u.get("firstname") + "  " + u.get("name")), 0, height, 1, 1);
              Button go = new Button("Voir");
              pane.add(go, 1, height, 1, 1);
              go.setOnAction(new ConnectionOtherUser(Long.parseLong(u.get("id").toString()), stage));
              height++;
            }
            if (listUser.isEmpty()) {
              pane.add(new Label("Aucun resultat :("), 0, height, 1, 1);
            }
            
            inside.getChildren().add(pane);
            stage.show();
          } else {
            System.out.println("Erreur chargement wall : " + result.getStatus());
          }
        }
      });
      new Thread(task, "Wall connection").start();
    }
  }
}
