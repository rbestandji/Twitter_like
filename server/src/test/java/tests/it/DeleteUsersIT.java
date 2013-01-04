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

public class DeleteUsersIT extends TestCase {

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

  // Dans cette fonction nous allons supprimer des comptes d'utilisateur
  @Test
  public void testDeleteUsers() throws Exception {
    Form f = new Form();
    WebResource webResource;
    ClientResponse result;
    System.out.println("****************** Suppression des comptes ! ******************");      
    
    // tentative de suppression de mon compte (non identifié): échec attendu
    webResource = client.resource(new URL(this.baseUrl + "/delete/my").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);    
    Assert.assertEquals(Status.USER_OFFLINE, result.getStatus());
    System.out.println("Un utilisateur non connecte tente, sans succes, de supprimer son compte");
    result.close();
    
    // Connexion de l'utilisateur 8: succès attendu 
    f.add("email", "harry@poudlard.com");
    f.add("password", "motdepasse");
    webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);    
    Assert.assertEquals(Status.OK, result.getStatus());
    System.out.println("connexion de l'utilisateur 8");
    result.close();
    
    // tentative de suppression de mon compte : succès attendu
    webResource = client.resource(new URL(this.baseUrl + "/delete/my").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);    
    Assert.assertEquals(Status.OK, result.getStatus());
    System.out.println("l'utilisateur 8 reussi a supprimer son compte");
    result.close();
    
    // L'utilisateur se déconnecte
    webResource = client.resource(new URL(this.baseUrl + "/bye").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    System.out.println("L'utilisateur 8 se deconnecte");
    result.close();
    
    // Vérification que l'utilisateur 8 a bien été supprimé: succès attendu 
    f.add("email", "harry@poudlard.com");
    f.add("password", "motdepasse");
    webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);    
    Assert.assertEquals(Status.USER_NO_ACCOUNT, result.getStatus());
    System.out.println("Tentative rate de connexion de l'utilisateur 8 (suite a la suppression de son compte)");
    result.close();
  }
}
