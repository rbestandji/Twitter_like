package Network;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.ws.rs.core.MediaType;

public class GetGeoIP {
  private String baseUrl = "http://freegeoip.net/json/";
  private ApacheHttpClient client;
  private static GetGeoIP singleTask = new GetGeoIP();

  private GetGeoIP() {

    DefaultApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
    //Jersey configuration
    config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
    //Client apache 
    client = ApacheHttpClient.create(config);
    client.setFollowRedirects(Boolean.TRUE);// Plus utilisé 
  }

  static public GetGeoIP getGeoIP() {
    return singleTask;
  }

  public ClientResponse getInfo(String ip) throws MalformedURLException, URISyntaxException {
    URL url = new URL(this.baseUrl + ip);
    WebResource resource = this.client.resource(url.toURI());
    return resource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
  }
}
