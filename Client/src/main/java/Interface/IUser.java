package Interface;

import Network.GetUserTask;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.representation.Form;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import share.core.Status;
import share.model.User;

public class IUser extends Parent {

  private User user;
  private Label gridCaption;
  private GridPane grid;
  private Button validate;
  private Button returnB;
  private TextField email = new TextField();
  private TextField name = new TextField();
  private TextField firstname = new TextField();
  private TextField password = new PasswordField();
  private Text err = new Text();

  public IUser() throws URISyntaxException, MalformedURLException {
    grid = new GridPane();
    grid.setHgap(20);
    grid.setVgap(10);
    grid.setPadding(new Insets(10));

    gridCaption = new Label("Inscription");
    gridCaption.setWrapText(true);
    gridCaption.setFont(Font.font(null, FontWeight.BOLD, 12));
    GridPane.setConstraints(gridCaption, 1, 0);
    GridPane.setHalignment(gridCaption, HPos.CENTER);

    Label lemail = new Label("Email:");
    GridPane.setConstraints(lemail, 0, 1);
    GridPane.setHalignment(lemail, HPos.RIGHT);
    GridPane.setConstraints(email, 1, 1);
    GridPane.setHalignment(email, HPos.LEFT);

    Label lpassword = new Label("Mot de passe:");
    GridPane.setConstraints(lpassword, 0, 2);
    GridPane.setHalignment(lpassword, HPos.RIGHT);
    GridPane.setConstraints(password, 1, 2);
    GridPane.setHalignment(password, HPos.LEFT);

    Label lname = new Label("Nom:");
    GridPane.setConstraints(lname, 0, 3);
    GridPane.setHalignment(lname, HPos.RIGHT);
    GridPane.setConstraints(name, 1, 3);
    GridPane.setHalignment(name, HPos.LEFT);

    Label lfirstname = new Label("Prénom:");
    GridPane.setConstraints(lfirstname, 0, 4);
    GridPane.setHalignment(lfirstname, HPos.RIGHT);
    GridPane.setConstraints(firstname, 1, 4);
    GridPane.setHalignment(firstname, HPos.LEFT);

    // bouton valider
    validate = new Button("Valider");
    GridPane.setConstraints(validate, 1, 5);
    GridPane.setHalignment(validate, HPos.CENTER);

    // champ d'erreur
    GridPane.setConstraints(err, 0, 6, 3, 1);
    GridPane.setHalignment(err, HPos.CENTER);
    err.setFill(Color.ORANGE);
    
    // bouton retour
    returnB = new Button("Retour");
    GridPane.setConstraints(returnB, 2, 7);
    GridPane.setHalignment(returnB, HPos.CENTER);

    grid.getChildren().addAll(gridCaption, lemail, lpassword, lname, lfirstname, email,
                              password, name, firstname, err, validate, returnB);
    this.getChildren().add(grid);

    // click bouton retour
    returnB.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent t) {
        try {
          grid.getChildren().clear();
          getChildren().clear();
          CUser cuser = new CUser();
          Main.vbox.getChildren().add(cuser);
        } catch (URISyntaxException ex) {
          Logger.getLogger(IUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
          Logger.getLogger(IUser.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });

    // click bouton valider
    validate.setOnAction(new ServerRegistration());
  }

  private class ServerRegistration implements EventHandler<ActionEvent> {

    public void handle(ActionEvent t) {
      grid.setDisable(true);
      Main.progress.setProgress(-1);
      Task<ClientResponse> task = new Task<ClientResponse>() {
        @Override
        protected ClientResponse call() throws Exception {
          Form f = new Form();
          f.add("email", email.getText());
          f.add("password", password.getText());
          f.add("name", name.getText());
          f.add("firstname", firstname.getText());
          Thread.currentThread().sleep(1000);
          return GetUserTask.getUserTask().postCall("registration", f);
        }
      };
      task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
        public void handle(WorkerStateEvent success) {
          try {
            ClientResponse result = (ClientResponse) success.getSource().getValue();
            grid.setDisable(false);
            Main.progress.setProgress(0);
            if (result.getStatus() == Status.OK) {
              grid.getChildren().clear();
              getChildren().clear();
              CUser cuser = new CUser();
              Main.vbox.getChildren().add(cuser);
            } else {
              err.setText(new Status(result.getStatus()).getReasonPhrase());
            }
          } catch (URISyntaxException ex) {
            Logger.getLogger(CUser.class.getName()).log(Level.SEVERE, null, ex);
          } catch (MalformedURLException ex) {
            Logger.getLogger(CUser.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      });
      new Thread(task, "Fetch Registration Thread").start();
    }
  }
}
