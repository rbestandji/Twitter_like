package Interface;

import Network.GetUserTask;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.representation.Form;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import share.core.Status;
import share.model.Message;
import share.model.User;

public class Wall extends Parent {
  /*
   * Probleme user reste non connecté => getUserTask change !!!!
   * A regler demain urgent !
   */

  private User user;

  public Wall() {
    this.getChildren().clear();
  }

  void setUser(User idUser) {
    this.user = user;
    ProgressIndicator p = new ProgressIndicator();
    this.getChildren().add(p);
    Task<ClientResponse> task = new Task<ClientResponse>() {
      @Override
      protected ClientResponse call() throws Exception {
        return new GetUserTask().getCall("users/getmywall");
      }
    };
    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      public void handle(WorkerStateEvent success) {
        ClientResponse result = (ClientResponse) success.getSource().getValue();
        if (result.getStatus() == Status.OK) {
          getChildren().clear();
          ArrayList<Message> listMsg = result.getEntity(ArrayList.class);
          for (Message m : listMsg) {
            getChildren().add(new Label((String) m.getText()));
          }
        } else {
          System.out.println(result.getStatus());
        }
      }
    });
    new Thread(task, "Wall connection").start();
  }
}
