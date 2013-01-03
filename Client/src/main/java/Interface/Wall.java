package Interface;

import Network.GetUserTask;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.representation.Form;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
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

  void setUser(final User user) {
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
          List<HashMap<String, ?>> listMsg = result.getEntity(List.class);
          box.setSpacing(10.);
          VBox boxTmp = new VBox();
          //boxTmp.setPrefWrapLength(200); 

          //ScrollPane s1 = new ScrollPane();
          //s1.setContent(boxTmp);
          for (HashMap<String, ?> m : listMsg) {
            System.out.println(m);
            User uu = null;
            if (m.get("author") != null) {
              uu.setId(Long.parseLong(((HashMap<String, ?>) m.get("author")).get("id").toString()));
              uu.setFirstname(((HashMap<String, ?>) m.get("author")).get("firstname").toString());
              uu.setName(((HashMap<String, ?>) m.get("author")).get("name").toString());
            } else {
              uu = user;
            }

            boxTmp.getChildren().add(new IMessage(Long.parseLong(m.get("id").toString()),
                    uu,
                    m.get("text").toString(), new Date(Long.parseLong(m.get("msgDate").toString())).toString()));
          }
          box.getChildren().add(boxTmp);

        } else {
          System.out.println("Erreur chargement wall : " + result.getStatus());
        }
      }
    });
    new Thread(task, "Wall connection").start();
  }
}
