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
import java.util.LinkedHashMap;
import java.util.List;
import javax.ws.rs.core.MediaType;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class ViewUsersIT extends TestCase {

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
  
  // Cette fonction va tester la lecture des messages d'un utilisateur et de ceux suivis
  @Test
    public void testViewMsg() throws Exception {
    Form f = new Form();
    WebResource webResource;
    ClientResponse result;
    System.out.println("****************** visibilite des messages ! ******************");

    // Un utilisateur non identifié affiche son mur: échec attendu
    webResource = client.resource(new URL(this.baseUrl + "/users/getmywall").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.USER_OFFLINE, result.getStatus());
    result.close();
    
    // Connexion de l'utilisateur 3
    f.clear();
    f.add("email", "lionel.muller.34@gmail.com");
    f.add("password", "motdepasse");
    System.out.println("connection de user 3");
    webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    result.close();

    // envoie d'un message sur Twitter-like
    f.clear();
    f.add("msg", "msg de user 3");
    webResource = client.resource(new URL(this.baseUrl + "/messages/send").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    result.close();

    // envoie d'un deuxième message sur Twitter-like
    f.clear();
    f.add("msg", "Je suis un autre msg de user 3");
    webResource = client.resource(new URL(this.baseUrl + "/messages/send").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    result.close();

    // Commentaire du message précédent
    f.clear();
    f.add("comment", "Je suis un commentaire du msg 35");
    webResource = client.resource(new URL(this.baseUrl + "/comments/send/35").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    result.close();

    // Autre commentaire du même message précédent: succès attendu
    f.clear();
    f.add("comment", "Je suis un autre commentaire du msg 35");
    webResource = client.resource(new URL(this.baseUrl + "/comments/send/35").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    Assert.assertEquals(Status.OK, result.getStatus());
    result.close();

    // L'utilisateur 3 affiche ses messages: succès attendu
    webResource = client.resource(new URL(this.baseUrl + "/users/getmywall").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.OK, result.getStatus());
    System.out.println(result.getEntity(String.class));
    result.close();

    // s'abonner aux utilisateurs 1 et 2
    webResource = client.resource(new URL(this.baseUrl + "/follow/following/1").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.OK, result.getStatus());    
    result.close();

    webResource = client.resource(new URL(this.baseUrl + "/follow/following/2").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.OK, result.getStatus());
    result.close();

    // L'utilisateur 3 affiche ses messages ainsi que ceux des users suivis: succès attendu
    webResource = client.resource(new URL(this.baseUrl + "/users/getmywall").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.OK, result.getStatus());
    System.out.println("mur de user 3:");
    System.out.println(result.getEntity(String.class));
    result.close();

    // L'utilisateur 1 affiche ses messages ainsi que ceux des users suivis: succès attendu
    webResource = client.resource(new URL(this.baseUrl + "/users/getuserwall/1").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.OK, result.getStatus());
    System.out.println("mur de user 1:");
    System.out.println(result.getEntity(String.class));
    result.close();

    // un utilisateur inexistant tente d'afficher ses messages : échec attendu
    webResource = client.resource(new URL(this.baseUrl + "/users/getuserwall/6721").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.USER_NO_ACCOUNT, result.getStatus());  
    result.close();
  }
}
