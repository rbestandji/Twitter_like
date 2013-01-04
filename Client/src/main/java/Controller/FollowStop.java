package Controller;

import Interface.MainWindow;
import Network.GetUserTask;
import com.sun.jersey.api.client.ClientResponse;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import share.core.Status;

public class FollowStop implements EventHandler<ActionEvent> {

  private Long id;

  public FollowStop(Long id) {
    this.id = id;
  }

  public void handle(ActionEvent t) {
    MainWindow.getMainWindow().getProgress().setProgress(-1.);
    Task<ClientResponse> task = new Task<ClientResponse>() {
      @Override
      protected ClientResponse call() throws Exception {
        return GetUserTask.getUserTask().getCall("follow/stop/" + id.toString());
      }
    };

    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      public void handle(WorkerStateEvent success) {
        ClientResponse result = (ClientResponse) success.getSource().getValue();
        MainWindow.getMainWindow().getProgress().setProgress(0.);
        if (result.getStatus() == Status.OK) {
          System.out.println("Arret de suivre : ok");
          MainWindow.getMainWindow().setUser(MainWindow.userConnected);
        } else {
          System.out.println("Arret de suivre fail : " + result.getStatus());
        }
        result.close();

      }
    });
    new Thread(task, "Chargement user Thread").start();
  }
}