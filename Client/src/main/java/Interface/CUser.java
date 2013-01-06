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

public class CUser extends Parent {

  private Label gridCaption;
  private GridPane grid;
  private Button validate;
  private Button registration;
  private TextField email = new TextField();
  private TextField password = new PasswordField();
  private Text err = new Text();

  public CUser() throws URISyntaxException, MalformedURLException {
    // grid
    grid = new GridPane();
    grid.setHgap(20);
    grid.setVgap(10);
    grid.setPadding(new Insets(10));

    // titre grid
    gridCaption = new Label("Connexion");
    gridCaption.setWrapText(true);
    gridCaption.setFont(Font.font(null, FontWeight.BOLD, 12));
    GridPane.setConstraints(gridCaption, 1, 0);
    GridPane.setHalignment(gridCaption, HPos.CENTER);

    Label lemail = new Label("Email:");
    GridPane.setConstraints(lemail, 0, 1);
    GridPane.setHalignment(lemail, HPos.RIGHT);
    GridPane.setConstraints(email, 1, 1);
    GridPane.setHalignment(email, HPos.LEFT);
    email.setText("le.jitou@gmail.com");
            
    Label lpassword = new Label("Mot de passe:");
    GridPane.setConstraints(lpassword, 0, 2);
    GridPane.setHalignment(lpassword, HPos.RIGHT);
    GridPane.setConstraints(password, 1, 2);
    GridPane.setHalignment(password, HPos.LEFT);
    password.setText("password");

    // bouton valider
    validate = new Button("Valider");
    GridPane.setConstraints(validate, 1, 3);
    GridPane.setHalignment(validate, HPos.CENTER);

    // champ d'erreur
    GridPane.setConstraints(err, 0, 4, 3, 1);
    GridPane.setHalignment(err, HPos.CENTER);
    err.setFill(Color.ORANGE);
    
    // bouton s'inscrire
    registration = new Button("S'inscrire");
    GridPane.setConstraints(registration, 2, 11);
    GridPane.setHalignment(registration, HPos.LEFT);

    grid.getChildren().addAll(gridCaption, lemail, lpassword, email, err, password, validate, registration);
    this.getChildren().add(grid);

    // click bouton s'inscrire
    registration.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent t) {
        try {
          grid.getChildren().clear();
          getChildren().clear();
          IUser iuser = new IUser();
          Main.vbox.getChildren().add(iuser);
        } catch (URISyntaxException ex) {
          Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
          Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });

    // click bouton valider
    validate.setOnAction(new ServerConnection());
  }

  private class ServerConnection implements EventHandler<ActionEvent> {

    public void handle(ActionEvent t) {
      grid.setDisable(true);
      Main.progress.setProgress(-1);
      Task<ClientResponse> task = new Task<ClientResponse>() {
        @Override
        protected ClientResponse call() throws Exception {
          Form f = new Form();
          f.add("email", email.getText());
          f.add("password", password.getText());
          return GetUserTask.getUserTask().postCall("connection", f);
        }
      };
      task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
        public void handle(WorkerStateEvent success) {
          ClientResponse result = (ClientResponse) success.getSource().getValue();
          grid.setDisable(false);
          Main.progress.setProgress(0);
          if (result.getStatus() == Status.OK) {
            grid.getChildren().clear();
            getChildren().clear();
            MainWindow mainwindow = MainWindow.getMainWindow();

            //Pas de suppression propre ?
            Main.getMainWindow().setScene(mainwindow);
            User u = (User) result.getEntity(User.class);
            MainWindow.userConnected = u;
            mainwindow.setUser(u);
          } else {
            err.setText(new Status(result.getStatus()).getReasonPhrase());
          }

        }
      });
      new Thread(task, "Fetch Connection Thread").start();
    }
  }
}
