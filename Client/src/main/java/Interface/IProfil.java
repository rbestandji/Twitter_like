package Interface;

import Controller.DisconnectUser;
import Controller.FollowStop;
import Controller.FollowUser;
import Map.WebMap;
import Network.GetUserTask;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import java.util.List;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import share.core.Status;
import share.model.User;
import share.model.UserAssignment;

public class IProfil extends Parent {

  private User user;

  public IProfil() {
  }

  public void setUser(User user) {
    this.user = user;
    for (UserAssignment u : user.getUsersFollowers()) {
      System.out.println("Profil : " + u.getId() + "  " + u.getFollower().getName() + "  " + u.getFollower().getId());
      System.out.println("Profil : " + u.getId() + "  " + u.getFollowing().getName() + "  " + u.getFollowing().getId());
    }

    this.getChildren().clear();
    init();
  }

  private void init() {
    VBox vbox = new VBox();
    vbox.setSpacing(4);
    vbox.getChildren().add(new Label(user.getName() + " " + user.getFirstname()));
    vbox.getChildren().add(new Label(user.getEmail()));

    final Button state = new Button();
    if (user.getId() == MainWindow.userConnected.getId()) {
      state.setText("Déconnexion");
      state.setOnAction(new DisconnectUser());
    } else {
      state.setText("... Chargement ...");

      Task<ClientResponse> task = new Task<ClientResponse>() {
        @Override
        protected ClientResponse call() throws Exception {
          return GetUserTask.getUserTask().getCall("follow/following/my");
        }
      };

      task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
        public void handle(WorkerStateEvent success) {
          ClientResponse result = (ClientResponse) success.getSource().getValue();
          if (result.getStatus() == Status.OK) {
            List<User> r = result.getEntity(new GenericType<List<User>>() {
            });
            boolean present = false;
            for (User u : r) {
              if (u.getId() == user.getId()) {
                present = true;
              }
            }
            if (present) {
              state.setText("Arrêter\n l'abonnement");
              state.setOnAction(new FollowStop(user.getId()));
            } else {
              state.setText("S'abonner");
              state.setOnAction(new FollowUser(user.getId()));
            }

          } else {
            System.out.println("Erreur follow : " + result.getStatus());
          }
        }
      });
      new Thread(task, "Chargement user Thread").start();


    }
    vbox.getChildren().add(state);
    final Button voirMap = new Button("Geolocalisation");
    voirMap.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent t) {
        WebMap m = new WebMap();
        m.mapUser(user);
      }
    });
    vbox.getChildren().add(voirMap);

    this.getChildren().add(vbox);
  }
}
