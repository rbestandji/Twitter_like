package Interface;

import Controller.SendNewMsg;
import Network.GetGeoIP;
import Network.GetPubIP;
import com.sun.jersey.api.client.ClientResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class INewTweet extends Parent {

  public Button send;
  TextField text = new TextField("Text");
  TextField lat = new TextField("Latitude");
  TextField lon = new TextField("Longitude");
  public static Double Lat = 0.;
  public static Double Long = 0.;
  private String ip;

  public INewTweet() {
    GridPane pane = new GridPane();

    send = new Button("Envoyer !");
    pane.add(text, 0, 0, 3, 1);
    pane.add(lat, 0, 1, 1, 1);
    pane.add(lon, 1, 1, 1, 1);
    pane.add(send, 2, 1, 1, 1);
    this.getChildren().add(pane);

    send.setOnAction(new SendNewMsg(this));
  }

  public String getText() {
    return text.getText();
  }

  public List<Double> getLatLong() throws MalformedURLException, IOException, URISyntaxException {
    List l = new ArrayList();
    Double x,y = null;
    try {
      x = Double.parseDouble(lat.getText());
      y = Double.parseDouble(lon.getText());
    } catch (Exception ex) {
      /* problème de barrier avec les threads
      // task 1
      Task<String> task = new Task<String>() {
        @Override
        protected String call() throws Exception {
          return GetPubIP.getPubIP().getIP();
        }
      };
      task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
        public void handle(WorkerStateEvent success) {
          ip = (String) success.getSource().getValue();
          System.out.println(ip);
        }
      });
      new Thread(task, "PublicIp Thread").start();
      // task 2
      Task<ClientResponse> task2 = new Task<ClientResponse>() {
        @Override
        protected ClientResponse call() throws Exception {
          return GetGeoIP.getGeoIP().getInfo(ip);
        }
      };
      task2.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
        public void handle(WorkerStateEvent success) {
          ClientResponse result = (ClientResponse) success.getSource().getValue();
          HashMap<String, ?> info = result.getEntity(HashMap.class);
          Lat = Double.parseDouble((String) info.get("latitude"));
          Long = Double.parseDouble((String) info.get("longitude"));
          System.out.println(Lat+" "+Long);
        }
      });
      new Thread(task2, "GeoIp Thread").start();
      */
      ip = GetPubIP.getPubIP().getIP();
      ClientResponse result = GetGeoIP.getGeoIP().getInfo(ip);
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
