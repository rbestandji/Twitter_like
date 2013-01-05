package Interface;

import Controller.DisconnectUser;
import Controller.FollowStop;
import Controller.FollowUser;
import Network.GetUserTask;
import com.sun.jersey.api.client.ClientResponse;
import java.util.HashMap;
import java.util.List;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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
    vbox.getChildren().add(new Label(user.getName()+" "+user.getFirstname()));
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
            List<HashMap<String, ?>> r = result.getEntity(List.class);
            boolean present = false;
            for (HashMap<String, ?> u : r) {
              if (Long.parseLong(u.get("id").toString()) == user.getId()) {
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

    this.getChildren().add(vbox);
  }
}
