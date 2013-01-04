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

public class GetUsersIT extends TestCase {

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

  // Dans cette fonction nous allons récupérer des identifiants d'utilisateurs
  @Test
  public void testGetUsers() throws Exception {
    Form f = new Form();
    WebResource webResource;
    ClientResponse result;
    System.out.println("****************** Recupereration des utilisateurs ! ******************");      
    
    // tente de récupérer un utilisateur sans être connecté: échec attendu
    webResource = client.resource(new URL(this.baseUrl + "/users/getuserid/1").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.USER_OFFLINE, result.getStatus());
    System.out.println("Un utilisateur non connecte tente, sans succes, de recuperer les informations de l'utilisateur 1 par id");
    result.close();
    
    webResource = client.resource(new URL(this.baseUrl + "/users/getuseremail/le.julius@gmail.com").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.USER_OFFLINE, result.getStatus());
    System.out.println("Un utilisateur non connecte tente, sans succes, de recuperer les informations de l'utilisateur 1 par email");
    result.close();
    
    webResource = client.resource(new URL(this.baseUrl + "/users/search/jul").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.USER_OFFLINE, result.getStatus());
    System.out.println("Un utilisateur non connecte tente, sans succes, de rechercher des utilisateurs par nom");
    result.close();
    
    // Connexion de l'utilisateur 1: succès attendu 
    f.add("email", "le.jitou@gmail.com");
    f.add("password", "password");
    webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);    
    Assert.assertEquals(Status.OK, result.getStatus());
    System.out.println("L'utilisateur 1 se connecte");
    result.close();
    
    // tente de récupérer un utilisateur inexistant par id: échec attendu
    webResource = client.resource(new URL(this.baseUrl + "/users/getuserid/132324").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.USER_NO_ACCOUNT, result.getStatus());
    System.out.println("L'utilisateur 1 n'arrive pas a recuperer les informations d'un utilisateur inexistant");
    result.close();
    
    // récupère l'utilisateur ayant l'id 1 : succès attendu
    webResource = client.resource(new URL(this.baseUrl + "/users/getuserid/1").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.OK, result.getStatus());
    System.out.println("L'utilisateur 1 recupere les informations de l'utilisateur 1 par identifiant technique");
    result.close();

    // tente de récupérer un utilisateur inexistant par email: échec attendu
    webResource = client.resource(new URL(this.baseUrl + "/users/getuseremail/mauvaisemail@gmail.com").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.USER_NO_ACCOUNT, result.getStatus());
    System.out.println("L'utilisateur 1 tente de recuperer, sans succes, les informations d'un utilisateur inexistant par email");
    result.close();
    
    // récupère l'utilisateur 5 ayant l'email le.julius@gmail.com : succès attendu
    webResource = client.resource(new URL(this.baseUrl + "/users/getuseremail/le.julius@gmail.com").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.OK, result.getStatus());
    System.out.println("L'utilisateur 1 recupere les informations de l'utilisateur 5 par email");
    result.close();
    
    //récupère la liste des utilisateurs dont une partie du nom est 'n': succès attendu
    webResource = client.resource(new URL(this.baseUrl + "/users/search/n").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.OK, result.getStatus());
    List<LinkedHashMap<String, ?>> listUser = result.getEntity(List.class);
    String res = "L'utilisateur 1 recherche les utilisateurs possedants 'n' dans leur nom: ";
    for (LinkedHashMap<String, ?> u : listUser) {
      res+=u.get("email")+"  ";
    }
    System.out.println(res);
    result.close();    
  }
}
