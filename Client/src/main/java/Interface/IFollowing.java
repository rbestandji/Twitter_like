package Interface;

import Controller.ConnectionOtherUser;
import Network.GetUserTask;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
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

public class IFollowing extends Parent {

  public IFollowing() {
  }

  public void setUser(final User user) {
    this.getChildren().clear();
    final VBox box = new VBox();
    ScrollPane s1 = new ScrollPane();
    s1.setFitToWidth(true);
    box.setAlignment(Pos.CENTER_LEFT);
    box.setSpacing(5);
    box.setTranslateX(5);

    box.getChildren().add(new Label("Abonnements"));
    ((Label) box.getChildren().get(0)).setTextFill(Color.BLUEVIOLET);

    Task<ClientResponse> task = new Task<ClientResponse>() {
      @Override
      protected ClientResponse call() throws Exception {
        return GetUserTask.getUserTask().getCall("follow/following/get/" + user.getId());
      }
    };
    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      public void handle(WorkerStateEvent success) {
        ClientResponse result = (ClientResponse) success.getSource().getValue();
        if (result.getStatus() == Status.OK) {
          List<User> listF = result.getEntity(new GenericType<List<User>>() {});

          for (int j = 0; j < listF.size(); j += 1) {
            Button b = new Button(listF.get(j).getName() + " "
                    + listF.get(j).getFirstname());
            box.getChildren().add(b);
            b.setOnAction(new ConnectionOtherUser(listF.get(j).getId(), null));
          }

        } else {
          System.out.println("Erreur chargement followings : " + result.getStatus());
        }
      }
    });
    new Thread(task, "following connection").start();

    s1.setContent(box);

    this.getChildren().add(s1);
  }
}
