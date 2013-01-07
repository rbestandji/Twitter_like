package Controller;

import Interface.INewTweet;
import Interface.MainWindow;
import Network.GetUserTask;
import com.sun.jersey.api.client.ClientResponse;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import share.core.Status;
import com.sun.jersey.api.representation.Form;

public class SendNewMsg implements EventHandler<ActionEvent> {

  private INewTweet newTweet;

  public SendNewMsg(INewTweet newTweet) {
    this.newTweet = newTweet;
  }

  public void handle(ActionEvent t) {
    final Form f = new Form();
    f.add("msg", newTweet.getText());
    f.add("latitude", newTweet.getLatitude());
    f.add("longitude", newTweet.getLongitude());


    MainWindow.getMainWindow().getProgress().setProgress(-1.);
    Task<ClientResponse> task = new Task<ClientResponse>() {
      @Override
      protected ClientResponse call() throws Exception {
        return GetUserTask.getUserTask().postCall("messages/send/", f);
      }
    };

    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      public void handle(WorkerStateEvent success) {
        ClientResponse result = (ClientResponse) success.getSource().getValue();
        MainWindow.getMainWindow().getProgress().setProgress(0.);
        if (result.getStatus() == Status.OK) {
          System.out.println("Message envoyé !");
          MainWindow.getMainWindow().setUser(MainWindow.userConnected);
        } else {
          System.out.println("Message as envoyé : fail : " + result.getStatus());
        }
        result.close();

      }
    });
    new Thread(task, "Chargement user Thread").start();
  }
}