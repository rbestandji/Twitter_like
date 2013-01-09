package Controller;

import Interface.INewComment;
import Interface.MainWindow;
import Network.GetUserTask;
import com.sun.jersey.api.client.ClientResponse;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import share.core.Status;
import com.sun.jersey.api.representation.Form;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendNewComment implements EventHandler<ActionEvent> {

  private INewComment newTweet;

  public SendNewComment(INewComment newTweet) {
    this.newTweet = newTweet;
  }

  public void handle(ActionEvent t) {
    try {
      newTweet.getStage().close();
      
      final Form f = new Form();
      f.add("comment", newTweet.getText());
      List l = newTweet.getLatLong();
      f.add("latitude", l.get(0));
      f.add("longitude", l.get(1));


      MainWindow.getMainWindow().getProgress().setProgress(-1.);
      Task<ClientResponse> task = new Task<ClientResponse>() {
        @Override
        protected ClientResponse call() throws Exception {
          return GetUserTask.getUserTask().postCall("comments/send/"+newTweet.getIdMsg(), f);
        }
      };

      task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
        public void handle(WorkerStateEvent success) {
          ClientResponse result = (ClientResponse) success.getSource().getValue();
          MainWindow.getMainWindow().getProgress().setProgress(0.);
          if (result.getStatus() == Status.OK) {
            System.out.println("Comment envoyé !");
            MainWindow.getMainWindow().setUser(MainWindow.userConnected);
          } else {
            System.out.println("Comment pas envoyé : fail : " + result.getStatus());
          }
          result.close();
        }
      });
      new Thread(task, "Chargement user Thread").start();
    } catch (MalformedURLException ex) {
      Logger.getLogger(SendNewComment.class.getName()).log(Level.SEVERE, null, ex);
    } catch (URISyntaxException ex) {
      Logger.getLogger(SendNewComment.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}