package Controller;

import Interface.Main;
import Interface.MainWindow;
import Network.GetUserTask;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.representation.Form;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import share.core.Status;
import share.model.User;

public class ConnectionOtherUser implements EventHandler<ActionEvent> {

  private Long id;
  private Stage stage;

  public ConnectionOtherUser(Long id, Stage stage) {
    this.id = id;
    this.stage = stage;
  }

  public void handle(ActionEvent t) {
    stage.close();
    
    MainWindow.getMainWindow().getProgress().setProgress(-1.);
    Task<ClientResponse> task = new Task<ClientResponse>() {
      @Override
      protected ClientResponse call() throws Exception {
        return GetUserTask.getUserTask().getCall("users/getuserid/" + id.toString());
      }
    };


    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      public void handle(WorkerStateEvent success) {
        ClientResponse result = (ClientResponse) success.getSource().getValue();
        MainWindow.getMainWindow().getProgress().setProgress(0.);
        if (result.getStatus() == Status.OK) {
          MainWindow.getMainWindow().setUser((User) result.getEntity(User.class));
        } else {
          System.out.println(result.getStatus());
        }

      }
    });
    new Thread(task, "Chargement user Thread").start();
  }
}