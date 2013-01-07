package Interface;

import Network.GetUserTask;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.representation.Form;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import share.core.DAOExceptionUser;
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
    /* utilisiteur regarde son wall */
    /* Ses messages et celui des personnes suivis */
    if (user.getId() == MainWindow.userConnected.getId()) {
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

            ScrollPane s1 = new ScrollPane();
            s1.setFitToWidth(true);
            s1.setMinWidth(550);
            s1.setMinHeight(600);
            boxTmp.setSpacing(10);

            ArrayList<Message> liste = new ArrayList<Message>();
            for (int j = 0; j < listMsg.size(); j += 2) {
              HashMap<String, ?> m1 = listMsg.get(j);//identifiant
              HashMap<String, ?> m2 = listMsg.get(j + 1);//message

              User uu = null;
              if (m1 != null) {
                uu = new User();
                uu.setId(Long.parseLong(m1.get("id").toString()));
                try {
                  uu.setFirstname((String) m1.get("firstname"));
                  uu.setName((String) m1.get("name"));
                } catch (DAOExceptionUser ex) {
                  Logger.getLogger(Wall.class.getName()).log(Level.SEVERE, null, ex);
                }
              } else {
                uu = user;
              }

              ArrayList<HashMap<String, ?>> listeComment = (ArrayList<HashMap<String, ?>>) m2.get("comments");
              ArrayList<Message> listeCommentMessage = new ArrayList<Message>();
              try {
                listeCommentMessage = convertirEnMessage(listeComment);
              } catch (Exception ex) {
              }

              boxTmp.getChildren().add(new IMessage(Long.parseLong(m2.get("id").toString()), uu,
                      (String) m2.get("text"), new SimpleDateFormat("yyyy.MM.dd ' à ' HH:mm:ss").
                      format(new Date(Long.parseLong(m2.get("msgDate").toString()))).toString(),
                      listeCommentMessage));
            }

            s1.setContent(boxTmp);
            box.getChildren().add(s1);
          } else {
            System.out.println("Erreur chargement wall : " + result.getStatus());
          }
        }

        private ArrayList<Message> convertirEnMessage(ArrayList<HashMap<String, ?>> listeCommentMessage) throws Exception {
          ArrayList<Message> m = new ArrayList<Message>();
          for (HashMap<String, ?> u : listeCommentMessage) {
            Message a = new Message();
            a.setMsgDate(new Date(Long.parseLong(u.get("msgDate").toString())));
            a.setId(Long.parseLong(u.get("id").toString()));
            a.setText((String) u.get("text"));
            a.setAuthor(new User("         ", "      ", "ddddd@ddd.com", "fffffffff"));
            m.add(a);
            a.setComments(convertirEnMessage((ArrayList<HashMap<String, ?>>) u.get("comments")));
          }
          return m;
        }
      });
      new Thread(task, "Wall connection").start();

    } else {
      //Regarde que les messages de la personne.
      // UNIQUEMENT LES SIENS !
      box.getChildren().clear();
      box.getChildren().add(newTweet);
      box.setSpacing(10.);
      VBox boxTmp = new VBox();
      boxTmp.setSpacing(10);
      ScrollPane s1 = new ScrollPane();
      s1.setFitToWidth(true);
      s1.setMinWidth(550);
      s1.setMinHeight(600);
      for (Message m : user.getMessages()) {
        boxTmp.getChildren().add(new IMessage(m.getId(), user,
                m.getText(), new SimpleDateFormat("yyyy.MM.dd ' à ' HH:mm:ss").format(m.getMsgDate()).toString(),
                (ArrayList) m.getComments()));
      }

      s1.setContent(boxTmp);
      box.getChildren().add(s1);
    }
  }
}
