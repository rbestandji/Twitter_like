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

public class WebappIT extends TestCase { 

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

  // Cette fonction test qu'un utilisateur ne peut se connecter (qu') avec son compte.
  @Test
  public void testConnectionAccount() throws Exception {
    Form f = new Form();
    WebResource webResource;
    ClientResponse result;
    System.out.println("****************** Tests des connexions! ******************");


    // Connexion de l'utilisateur 1 
    f.add("email", "le.jitou@gmail.com");
    f.add("password", "password");
    webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    //System.out.println(result.getEntity(String.class));
    Assert.assertEquals(result.getStatus(), Status.OK);
    result.close();

    // Abonnements aux utilisateurs 2 et 3
    webResource = client.resource(new URL(this.baseUrl + "/follow/following/2").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    //System.out.println("OK /: " + result.getStatus());
    result.close();

    webResource = client.resource(new URL(this.baseUrl + "/follow/following/3").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    //System.out.println("OK /: " + result.getStatus());
    result.close();

    // Mes abonnements (utilisateur 1)
    webResource = client.resource(new URL(this.baseUrl + "/follow/following/my").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    List<LinkedHashMap<String, ?>> listUser = result.getEntity(List.class);
    String res = "Liste des abonnements de 'le.jitou@gmail.com': ";
    for (LinkedHashMap<String, ?> u : listUser) {
      res+=u.get("email")+"  ";
    }
    //System.out.println(res);
    result.close();

    // Abonnés de l'utilisateur 2
    webResource = client.resource(new URL(this.baseUrl + "/follow/follower/get/2").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    listUser = result.getEntity(List.class);
    res = "Liste des abonnés de 'lavalber02@gmail.com': ";
    for (LinkedHashMap<String, ?> u : listUser) {
      res+=u.get("email")+"  ";
    }
    //System.out.println(res);
    result.close();

    // Arrêt de l'abonnement à l'utilisateur 2 
    webResource = client.resource(new URL(this.baseUrl + "/follow/stop/2").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    //System.out.println("STOP /: " + result.getStatus());
    result.close();

    // Abonnements de l'utilisateur 1
    webResource = client.resource(new URL(this.baseUrl + "/follow/following/get/1").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    listUser = result.getEntity(List.class);
    res = "Liste des abonnements de 'le.jitou@gmail.com': ";
    for (LinkedHashMap<String, ?> u : listUser) {
      res+=u.get("email")+"  ";
    }
    //System.out.println(res);
    result.close();

    // Abonnés de l'utilisateur 2
    webResource = client.resource(new URL(this.baseUrl + "/follow/follower/get/2").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    listUser = result.getEntity(List.class);
    res = "Liste des abonnés de 'lavalber02@gmail.com': ";
    for (LinkedHashMap<String, ?> u : listUser) {
      res+=u.get("email")+"  ";
    }
    //System.out.println(res);
    result.close();

    // Suppression de mon compte (utilisateur 1)
    /*webResource = client.resource(new URL(this.baseUrl + "/delete/my").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    System.out.println("Suppression de mon compte /: " + result.getStatus());
    //Assert.assertEquals(result.getStatus(), Status.OK);
    result.close();*/

    // Abonnés de l'utilisateur 3
    webResource = client.resource(new URL(this.baseUrl + "/follow/follower/get/3").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    listUser = result.getEntity(List.class);
    res = "Liste des abonnés de 'lionel.muller.34gmail.com': ";
    for (LinkedHashMap<String, ?> u : listUser) {
      res+=u.get("email")+"  ";
    }
    //System.out.println(res);
    result.close();

    // L'utilisateur 1 est encore connecté, tentative de connexion de l'utilisateur 2 
    f.clear();
    f.add("email", "lavalber02@gmail.com");
    f.add("password", "motdepasse");
    webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    Assert.assertEquals(result.getStatus(), Status.USER_ONLINE);
    result.close();


    // L'utilisateur 1 se déconnecte 
    webResource = client.resource(new URL(this.baseUrl + "/bye").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(result.getStatus(), Status.OK);
    result.close();


    // L'utilisateur 2 tente de se reconnecter avec un mauvais password 
    f.clear();
    f.add("email", "lavalber02@gmail.com");
    f.add("password", "password");
    webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    Assert.assertEquals(result.getStatus(), Status.USER_WRONG_PASSWORD);
    result.close();

    // L'utilisateur 1 tente de se reconnecter avec un mauvais identifiant 
    f.clear();
    f.add("email", "le.jidtouu@gmail.com");
    f.add("password", "password");
    webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    Assert.assertEquals(result.getStatus(), Status.USER_NO_ACCOUNT);
    result.close();

  }
/*
  // Cette fonction va tester la lecture d'un profil 
  @Test
  public void testViewUser() throws Exception {
  Form f = new Form();
  WebResource webResource;
  ClientResponse result;
  System.out.println("****************** Tests lecture profil ! ******************");

  // Connexion de l'utilisateur 1
  f.add("email", "le.jitou@gmail.com");
  f.add("password", "password");
  webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
  result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
  Assert.assertEquals(result.getStatus(), Status.OK);
  result.close();

  //Lecture de l'utilisateur 1
  webResource = client.resource(new URL(this.baseUrl + "/users/get/1").toURI());
  result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
  Assert.assertEquals(result.getEntity(User.class).getEmail(), "le.jitou@gmail.com");
  result.close();
  // Lecture de l'utilisateur 3 à partir du mail
  webResource = client.resource(new URL(this.baseUrl + "/users/getuser/lionel.muller.34@gmail.com").toURI());
  result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
  Assert.assertEquals(result.getEntity(User.class).getEmail(), "lionel.muller.34@gmail.com");
  result.close();

  // Test la fonction de recherche de tous les utilisateurs avec un 'l'

  webResource = client.resource(new URL(this.baseUrl + "/users/search/l").toURI());
  result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
  Assert.assertEquals(result.getEntity(List.class).size(), 2);
  result.close();

  }

  // Cette fonction va tester l'écriture et la lecture de commentaires
  @Test
  public void testComment() throws Exception {
  Form f = new Form();
  WebResource webResource;
  ClientResponse result;
  System.out.println("****************** Tests des comments ! ******************");

  // Connexion de l'utilisateur 1
  f.add("email", "le.jitou@gmail.com");
  f.add("password", "password");
  webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
  result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
  Assert.assertEquals(result.getStatus(), Status.OK);
  result.close();


  // Lecture des messages à partir du mail.
  webResource = client.resource(new URL(this.baseUrl + "/messages/get/1").toURI());
  result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
  List<Map<String, ?>> r = result.getEntity(List.class);
  //Pas très profirst mais ça va être amélioré.
  Assert.assertEquals(((List<Map<String, ?>>) r.get(0).get("comments")).get(0).get("text"), "Comment");
  result.close();
  }

  // Cette fonction va tester la suppression de messages
  @Test
  public void testDeleteMessage() throws Exception {
  Form f = new Form();
  WebResource webResource;
  ClientResponse result;
  System.out.println("****************** Tests des suppressions ! ******************");

  // Connexion de l'utilisateur 1
  f.add("email", "le.jitou@gmail.com");
  f.add("password", "password");
  webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
  result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
  Assert.assertEquals(result.getStatus(), Status.OK);
  result.close();

  // Lecture de tous les messages de l'utilisateur 1.
  webResource = client.resource(new URL(this.baseUrl + "/messages/get/1").toURI());
  result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
  result.close();

  //Suppression du message ayant l'id 4 (rattaché à l'utilisateur 1)
  webResource = client.resource(new URL(this.baseUrl + "/messages/delete/4").toURI());
  result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
  Assert.assertEquals(result.getStatus(), Status.OK);
  result.close();

  // Lecture des tous les messages de l'utilisateur 1.
  webResource = client.resource(new URL(this.baseUrl + "/messages/get/1").toURI());
  result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
  result.close();
  }*/
}
