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
import share.core.Status;
import share.model.User;

public class FollowUser implements EventHandler<ActionEvent> {

  private Long id;

  public FollowUser(Long id) {
    this.id = id;
  }

  public void handle(ActionEvent t) {
    MainWindow.getMainWindow().getProgress().setProgress(-1.);
    Task<ClientResponse> task = new Task<ClientResponse>() {
      @Override
      protected ClientResponse call() throws Exception {
        return GetUserTask.getUserTask().getCall("follow/following/" + id.toString());
      }
    };

    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      public void handle(WorkerStateEvent success) {
        ClientResponse result = (ClientResponse) success.getSource().getValue();
        MainWindow.getMainWindow().getProgress().setProgress(0.);
        if (result.getStatus() == Status.OK) {
          System.out.println("ICIIII");
          result.close();
          MainWindow.getMainWindow().setUser(MainWindow.userConnected);
        } else {
          System.out.println("Erreur follow : "+result.getStatus());
        }
      }
    });
    new Thread(task, "Chargement user Thread").start();
  }
}