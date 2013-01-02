package Network;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.ws.rs.core.MediaType;

public class GetUserTask {

  private final static String port = "8080";
  private String baseUrl = "http://localhost:" + port + "/cargo-webapp/";
  private ApacheHttpClient client;
  private static GetUserTask singleTask = new GetUserTask();

  private GetUserTask() {

    DefaultApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
    //Autorisation des cookies
    config.getProperties().put(ApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, true);
    //Jersey configuration
    config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
    //Client apache 
    client = ApacheHttpClient.create(config);
    client.setFollowRedirects(Boolean.TRUE);// Plus utilisé 
  }

  static public GetUserTask getUserTask() {
    return singleTask;
  }

  public ClientResponse getCall(String endUrl) throws MalformedURLException, URISyntaxException {
    URL url = new URL(this.baseUrl + endUrl);
    WebResource resource = this.client.resource(url.toURI());
    return resource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
  }

  public ClientResponse postCall(String endUrl, Form f) throws MalformedURLException, URISyntaxException {
    URL url = new URL(this.baseUrl + endUrl);
    WebResource resource = this.client.resource(url.toURI());
    return resource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
  }
}
