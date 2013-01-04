package Interface;

import Controller.ConnectionOtherUser;
import Network.GetUserTask;
import com.sun.jersey.api.client.ClientResponse;
import java.util.HashMap;
import java.util.List;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import share.core.Status;
import share.model.User;

public class IFollower extends Parent {

  public IFollower() {
  }

  public void setUser(final User user) {
    this.getChildren().clear();
    final VBox box = new VBox();
    ScrollPane s1 = new ScrollPane();
    s1.setFitToWidth(true);
    box.setAlignment(Pos.CENTER_LEFT);
    box.setSpacing(9);
    box.setTranslateX(10);

    box.getChildren().add(new Label("Abonnées"));
    ((Label)box.getChildren().get(0)).setTextFill(Color.BLUEVIOLET);

    Task<ClientResponse> task = new Task<ClientResponse>() {
      @Override
      protected ClientResponse call() throws Exception {
        return GetUserTask.getUserTask().getCall("follow/follower/get/" + user.getId());
      }
    };
    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      public void handle(WorkerStateEvent success) {
        ClientResponse result = (ClientResponse) success.getSource().getValue();
        if (result.getStatus() == Status.OK) {
          List<HashMap<String, ?>> listF = result.getEntity(List.class);

          for (int j = 0; j < listF.size(); j += 1) {
            Button b = new Button(listF.get(j).get("name").toString() + "  "
                    + listF.get(j).get("firstname").toString());
            box.getChildren().add(b);
            b.setOnAction(new ConnectionOtherUser(Long.parseLong(listF.get(j).get("id").toString()), null));
          }

        } else {
          System.out.println("Erreur chargement followers : " + result.getStatus());
        }
      }
    });
    new Thread(task, "follower connection").start();

    s1.setContent(box);

    this.getChildren().add(s1);
  }
}
