package Map;

import com.sun.glass.ui.Screen;
import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import share.model.Message;
import share.model.User;

public class WebMap {

  public void mapUser(User user) {
    ArrayList<Double> lat = new ArrayList<Double>();
    ArrayList<Double> lon = new ArrayList<Double>();

    for (Message m : user.getMessages()) {
      boolean valide = true;
      if (m.getLatitude() != null) {
        if (Math.abs(m.getLatitude()) > 400) {
          valide = false;
        }
      }
      if (m.getLongitude() != null) {
        if (Math.abs(m.getLongitude()) > 400) {
          valide = false;
        }
      }
      if (valide) {
        lat.add(m.getLatitude());
        lon.add(m.getLongitude());
      }
    }

    String url = getUrl(lat, lon);
    Stage stage = new Stage();
    WebMap w = new WebMap();
    w.aff(stage, url);
  }

  private static String getUrl(ArrayList<Double> lat, ArrayList<Double> lon) {
    String urlString = new String();
    urlString += ("https://maps.google.com/maps?");
    if (lat.size() > 0) {
      urlString += ("&saddr=");
      urlString += lat.get(0) + "," + lon.get(0);
      urlString += "&daddr=";
      for (int i = 1; i < lat.size(); i++) {
        urlString += lat.get(i) + "," + lon.get(i);
        if (i + 1 < lat.size()) {
          urlString += "+to:";
        }
      }
    }
    urlString += "";
    return urlString;
  }

  private void aff(Stage stage, String url) {
    Group root = new Group();
    stage.setScene(new Scene(root, 1000, 800));
    WebView webView = new WebView();
    stage.setFullScreen(true);
    webView.setPrefSize(Screen.getMainScreen().getWidth() - 10, Screen.getMainScreen().getHeight() - 10);

    final WebEngine webEngine = webView.getEngine();
    webEngine.setJavaScriptEnabled(true);
    webEngine.load(url);

    root.getChildren().add(webView);
    stage.show();


  }
}
