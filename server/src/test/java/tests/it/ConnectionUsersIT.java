package tests.it;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import java.net.URL;
import javax.ws.rs.core.MediaType;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import share.core.Status;

public class ConnectionUsersIT extends TestCase {

  private String baseUrl;
  private DefaultApacheHttpClientConfig config;
  private ApacheHttpClient client;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    String port = System.getProperty("servlet.port");
    this.baseUrl = "http://localhost:" + port + "/cargo-webapp";

    config = new DefaultApacheHttpClientConfig();

    //Autorisation des cookies
    config.getProperties().put(ApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, true);

    //Jersey configuration
    config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

    //Client apache 
    client = ApacheHttpClient.create(config);
    client.setFollowRedirects(Boolean.TRUE);// Plus utilisé 

  }

  // Dans cette fonction nous allons connecter des utilisateurs 
  @Test
  public void testConnectUsers() throws Exception {
    Form f = new Form();
    WebResource webResource;
    ClientResponse result;
    System.out.println("****************** Connexion des comptes ! ******************");

    // L'utilisateur 2 tente de se connecter avec un mauvais email sans succès
    f.add("email", "mauvais_lavalber02@gmail.com");
    f.add("password", "motdepasse");
    webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    Assert.assertEquals(result.getStatus(), Status.USER_NO_ACCOUNT);
    result.close();
    
    // L'utilisateur 3 tente de se connecter avec un mauvais password sans succès
    f.clear();
    f.add("email", "lionel.muller.34@gmail.com");
    f.add("password", "mauvaismotdepasse");
    webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    Assert.assertEquals(result.getStatus(), Status.USER_WRONG_PASSWORD);
    result.close();
    
    // Connexion de l'utilisateur 1 avec les bons identifiants succès attendu
    f.clear();
    f.add("email", "le.jitou@gmail.com");
    f.add("password", "password");
    webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    System.out.println(result.getEntity(String.class));
    Assert.assertEquals(result.getStatus(), Status.OK);
    result.close();
    
    // L'utilisateur 1 est encore connecté, tentative de connexion de l'utilisateur 2 échec attendu
    f.clear();
    f.add("email", "lavalber02@gmail.com");
    f.add("password", "motdepasse");
    webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    Assert.assertEquals(result.getStatus(), Status.USER_ONLINE);
    result.close();
  }
}
