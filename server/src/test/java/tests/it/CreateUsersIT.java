package tests.it;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import share.core.Status;
import java.net.URL;
import javax.ws.rs.core.MediaType;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class CreateUsersIT extends TestCase {

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

  // Dans cette fonction nous allons créer des utilisateurs 
  @Test
  public void testCreateUsers() throws Exception {
    Form f = new Form();
    WebResource webResource;
    ClientResponse result;
    System.out.println("****************** Creation des comptes ! ******************");

    // création d'un nouveau compte déjà existant: user 1: échec attendu
    f.clear();
    f.add("email", "le.jitou@gmail.com");
    f.add("password", "password");
    f.add("name", "Pasquet");
    f.add("firstname", "Jerome");
    webResource = client.resource(new URL(this.baseUrl + "/registration").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    Assert.assertEquals(result.getStatus(), Status.EMAIL_VALIDATED);
    result.close();

    // création d'un nouveau compte : succès attendu
    f.clear();
    f.add("email", "castorEnrage@gmail.com");
    f.add("password", "sdsdsdsdsdsdsd");
    f.add("name", "Laval");
    f.add("firstname", "Bernard");
    webResource = client.resource(new URL(this.baseUrl + "/registration").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    Assert.assertEquals(result.getStatus(), Status.OK);
    result.close();

    // Connexion de l'utilisateur 1 avec les bons identifiants: succès attendu
    f.clear();
    f.add("email", "le.jitou@gmail.com");
    f.add("password", "password");
    webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    System.out.println(result.getEntity(String.class));
    Assert.assertEquals(result.getStatus(), Status.OK);
    result.close();

    // tentative de création d'un nouveau compte avec user 1 déjà connecté: échec attendu
    f.clear();
    f.add("email", "boblemarsoin@gmail.com");
    f.add("password", "etc");
    f.add("name", "dilan");
    f.add("firstname", "bob");
    webResource = client.resource(new URL(this.baseUrl + "/registration").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    Assert.assertEquals(result.getStatus(), Status.USER_ONLINE);
    result.close();
  }
}
