package Controller;

import Interface.MainWindow;
import Network.GetUserTask;
import com.sun.jersey.api.client.ClientResponse;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import share.core.Status;

public class DeleteMessage implements EventHandler<ActionEvent> {

  private Long id;

  public DeleteMessage(Long id) {
    this.id = id;
  }

  public void handle(ActionEvent t) {
    MainWindow.getMainWindow().getProgress().setProgress(-1.);
    Task<ClientResponse> task = new Task<ClientResponse>() {
      @Override
      protected ClientResponse call() throws Exception {
        return GetUserTask.getUserTask().getCall("messages/delete/" + id.toString());
      }
    };

    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      public void handle(WorkerStateEvent success) {
        ClientResponse result = (ClientResponse) success.getSource().getValue();
        MainWindow.getMainWindow().getProgress().setProgress(0.);
        result.close();

        if (result.getStatus() == Status.OK) {
          System.out.println("Message supprimé!");
          MainWindow.getMainWindow().setUser(MainWindow.userConnected);
        } else {
          System.out.println("delete probleme : " + result.getStatus());
        }
      }
    });
    new Thread(task, "Delete message").start();
  }
}