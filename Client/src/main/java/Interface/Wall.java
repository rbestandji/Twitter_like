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
import javafx.scene.layout.VBox;
import share.core.Status;
import share.model.Message;
import share.model.User;

public class Wall extends Parent {

  private User user;
  private VBox box;
  private INewTweet newTweet;

  public Wall() {
    box = new VBox();
    newTweet = new INewTweet();
    box.getChildren().add(newTweet);
    this.getChildren().add(box);
  }

  void setUser(User idUser) {
    this.user = user;
    box.getChildren().clear();
    ProgressIndicator p = new ProgressIndicator();
    box.getChildren().add(p);


    Task<ClientResponse> task = new Task<ClientResponse>() {
      @Override
      protected ClientResponse call() throws Exception {
        return GetUserTask.getUserTask().getCall("users/getmywall");
      }
    };


    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      public void handle(WorkerStateEvent success) {
        ClientResponse result = (ClientResponse) success.getSource().getValue();
        if (result.getStatus() == Status.OK) {
          box.getChildren().clear();
          box.getChildren().add(newTweet);
          ArrayList<Message> listMsg = result.getEntity(ArrayList.class);
          for (Message m : listMsg) {
            box.getChildren().add(new IMessage(m));
          }
        } else {
          System.out.println("Erreur chargement wall : " + result.getStatus());
        }
      }
    });
    new Thread(task, "Wall connection").start();
  }
}
