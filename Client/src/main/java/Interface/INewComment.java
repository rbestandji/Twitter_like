package Interface;

import Controller.SendNewComment;
import Network.GetGeoIP;
import com.sun.jersey.api.client.ClientResponse;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class INewComment extends Parent {

  public Button send;
  private TextField text = new TextField("Text");
  private TextField lat = new TextField("Latitude");
  private TextField lon = new TextField("Longitude");
  private Long id;
  private Stage stage;
  private Double Lat = 0.;
  private Double Long = 0.;
  
  public INewComment(Long id, Stage stage) {
    this.id = id;
    this.stage = stage;
    GridPane pane = new GridPane();

    send = new Button("Envoyer !");
    pane.add(text, 0, 0, 3, 1);
    pane.add(lat, 0, 1, 1, 1);
    pane.add(lon, 1, 1, 1, 1);
    pane.add(send, 2, 1, 1, 1);
    this.getChildren().add(pane);

    send.setOnAction(new SendNewComment(this));
  }
  
  public Stage getStage(){
    return stage;
  }
  
 public Long getIdMsg(){
    return id;
  }
 
  public String getText() {
    return text.getText();
  }

  public List<Double> getLatLong() throws MalformedURLException, URISyntaxException {
    List l = new ArrayList();
    Double x,y = null;
    try {
      x = Double.parseDouble(lat.getText());
      y = Double.parseDouble(lon.getText());
    } catch (Exception ex) {
      ClientResponse result = GetGeoIP.getGeoIP().getInfo("");
      HashMap<String, ?> info = result.getEntity(HashMap.class);
      Lat = Double.parseDouble((String) info.get("latitude"));
      Long = Double.parseDouble((String) info.get("longitude"));
      l.add(Lat);
      l.add(Long);
      return l;
    }
    l.add(x);
    l.add(y);
    return l;
  }

}
