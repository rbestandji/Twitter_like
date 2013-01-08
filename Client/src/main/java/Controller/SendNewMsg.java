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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendNewMsg implements EventHandler<ActionEvent> {

  private INewTweet newTweet;

  public SendNewMsg(INewTweet newTweet) {
    this.newTweet = newTweet;
  }

  public void handle(ActionEvent t) {
    try {
      final Form f = new Form();
      f.add("msg", newTweet.getText());
      List l = newTweet.getLatLong();
      f.add("latitude", l.get(0));
      f.add("longitude", l.get(1));

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
    } catch (MalformedURLException ex) {
      Logger.getLogger(SendNewMsg.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(SendNewMsg.class.getName()).log(Level.SEVERE, null, ex);
    } catch (URISyntaxException ex) {
      Logger.getLogger(SendNewMsg.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}