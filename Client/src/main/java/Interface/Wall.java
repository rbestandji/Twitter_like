package Interface;

import Network.GetUserTask;
import com.sun.jersey.api.client.ClientResponse;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class Wall extends Parent {
  private Button deconnection;
  private GetUserTask get = new GetUserTask();

  public Wall() throws URISyntaxException, MalformedURLException {
    // bouton deconnection
    deconnection = new Button("Deconnexion");
    deconnection.setTranslateX(400);
    deconnection.setTranslateY(450);

    this.getChildren().add(deconnection);
    
    // click bouton deconnection
    deconnection.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent t) {
        try {
          ClientResponse result = get.getCall("bye");
          result.close();
          getChildren().clear();
          CUser cuser = new CUser();
          getChildren().add(cuser);
        } catch (URISyntaxException ex) {
          Logger.getLogger(IUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
          Logger.getLogger(IUser.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });
  } 
}
