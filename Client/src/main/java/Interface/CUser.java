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
import javafx.stage.Stage;
import share.core.Status;
import share.model.User;

public class CUser extends Parent {

  private Label gridCaption;
  private GridPane grid;
  private Button validate;
  private Button registration;
  private GetUserTask post = new GetUserTask();
  private TextField email = new TextField();
  private TextField password = new PasswordField();

  public CUser() throws URISyntaxException, MalformedURLException {
    // titre grid
    gridCaption = new Label("Connexion");
    gridCaption.setWrapText(true);

    // grid
    grid = new GridPane();
    grid.setHgap(20);
    grid.setVgap(10);
    grid.setPadding(new Insets(18));

    Label lemail = new Label("Email:");
    GridPane.setConstraints(lemail, 0, 0);
    GridPane.setHalignment(lemail, HPos.RIGHT);

    GridPane.setConstraints(email, 1, 0);
    GridPane.setHalignment(email, HPos.LEFT);

    Label lpassword = new Label("Mot de passe:");
    GridPane.setConstraints(lpassword, 0, 1);
    GridPane.setHalignment(lpassword, HPos.RIGHT);

    GridPane.setConstraints(password, 1, 1);
    GridPane.setHalignment(password, HPos.LEFT);

    // bouton valider
    validate = new Button("Valider");
    validate.setTranslateX(120);
    validate.setTranslateY(80);

    // bouton s'inscrire
    registration = new Button("S'inscrire");
    registration.setTranslateX(400);
    registration.setTranslateY(450);

    grid.getChildren().addAll(lemail, lpassword, email, password);
    this.getChildren().addAll(gridCaption, grid, validate, registration);

    // click bouton s'inscrire
    registration.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent t) {
        try {
          grid.getChildren().clear();
          getChildren().clear();
          IUser iuser = new IUser();
          Main.root.getChildren().add(iuser);
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
      gridCaption.setDisable(true);
      grid.setDisable(true);
      validate.setDisable(true);
      Main.progress.setProgress(-1);
      Task<ClientResponse> task = new Task<ClientResponse>() {
        @Override
        protected ClientResponse call() throws Exception {
          Form f = new Form();
          f.add("email", email.getText());
          f.add("password", password.getText());
          Thread.currentThread().sleep(1000);
          return post.postCall("connection", f);
        }
      };
      task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
        public void handle(WorkerStateEvent success) {
          ClientResponse result = (ClientResponse) success.getSource().getValue();
          gridCaption.setDisable(false);
          grid.setDisable(false);
          validate.setDisable(false);
          Main.progress.setProgress(1);
          if (result.getStatus() == Status.OK) {
            grid.getChildren().clear();
            getChildren().clear();
            MainWindow wall = MainWindow.getMainWindow();

            //Pas de suppression propre ?
            Main.getMainWindow().setScene(wall);
            wall.setUser((User) result.getEntity(User.class));

          } else {
            System.out.println(result.getStatus());
          }

        }
      });
      new Thread(task, "Fetch Connection Thread").start();
    }
  }
}
