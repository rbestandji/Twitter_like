package Controller;

import Interface.Main;
import Interface.MainWindow;
import Network.GetUserTask;
import com.sun.jersey.api.client.ClientResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import share.core.Status;

public class DisconnectUser implements EventHandler<ActionEvent> {

  public DisconnectUser() {
  }

  public void handle(ActionEvent t) {
    MainWindow.getMainWindow().getProgress().setProgress(-1.);
    Task<ClientResponse> task = new Task<ClientResponse>() {
      @Override
      protected ClientResponse call() throws Exception {
        return GetUserTask.getUserTask().getCall("bye");
      }
    };

    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      public void handle(WorkerStateEvent success) {
        ClientResponse result = (ClientResponse) success.getSource().getValue();
        MainWindow.getMainWindow().getProgress().setProgress(0.);
        
        if (result.getStatus() == Status.OK) {
          result.close();
          try {
            //Main.getMainWindow().close();
            new Main().start(Main.getMainWindow());
          } catch (Exception ex) {
          System.out.println("Deconnexion probleme : "+ex);
          }
        } else {
          System.out.println("Deconnexion probleme : "+result.getStatus());
        }
      }
    });
    new Thread(task, "Chargement user Thread").start();
  }
}