package Network;

//import javax.ws.rs.core.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.concurrent.Task;

public class GetUserTask extends Task {

  private String nom;
  private String baseUrl;
  private WebResource rootResource;

  public GetUserTask(String nom) throws MalformedURLException {
    try {
      this.nom = nom;
      System.out.println("ICI");

      String port = "8080";
      baseUrl = "http://localhost:" + port + "/cargo-webapp";
      ClientConfig clientConfig = new DefaultClientConfig();
      clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
      Client client = Client.create(clientConfig);
      URL url = new URL(baseUrl);
      rootResource = client.resource(url.toURI());
      System.out.println("ICI");
    } catch (URISyntaxException ex) {
      System.out.println("ICI");
    }
  }

  @Override
  public ClientResponse call() {
    System.out.println("------------  : ");


    ClientResponse result = rootResource/*.accept(MediaType.APPLICATION_JSON)*/.get(ClientResponse.class);
    System.out.println("------------  : ");
    return result;
  }
}
