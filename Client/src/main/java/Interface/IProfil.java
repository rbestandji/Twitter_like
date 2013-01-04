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
    GridPane pane = new GridPane();
    pane.add(new Label(user.getName()), 0, 0, 1, 1);
    pane.add(new Label(user.getFirstname()), 1, 0, 1, 1);
    pane.add(new Label(user.getEmail()), 0, 1, 2, 1);

    final Button state = new Button();
    if (user.getId() == MainWindow.userConnected.getId()) {
      state.setText("Deconnexion");
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
              state.setText("Arreter de suivre");
              state.setOnAction(new FollowStop(user.getId()));
            } else {
              state.setText("Suivre");
              state.setOnAction(new FollowUser(user.getId()));
            }

          } else {
            System.out.println("Erreur follow : " + result.getStatus());
          }
        }
      });
      new Thread(task, "Chargement user Thread").start();


    }
    pane.add(state, 0, 2, 2, 1);

    pane.setHgap(10.);
    this.getChildren().add(pane);
  }
}
