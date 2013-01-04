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

public class FollowUsersIT extends TestCase {

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

  // Dans cette fonction nous allons suivre des utilisateurs
  @Test
  public void testFollowUsers() throws Exception {
    Form f = new Form();
    WebResource webResource;
    ClientResponse result;
    System.out.println("****************** Suivi des utilisateurs ! ******************");      
    
    // Connexion de l'utilisateur 1: succès attendu 
    f.add("email", "le.jitou@gmail.com");
    f.add("password", "password");
    webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);    
    Assert.assertEquals(Status.OK, result.getStatus());
    System.out.println("L'utilisateur 1 se connecte");
    result.close();

    // s'abonner à un utilisateur inexistant: échec attendu
    webResource = client.resource(new URL(this.baseUrl + "/follow/following/5656").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.ID_NOT_EXIST, result.getStatus());
    System.out.println("L'utilisateur 1 tente de suivre, sans succes, un utilisateur inexistant");
    result.close();
    
    // s'abonner aux utilisateurs 2 et 3: succès attendu
    webResource = client.resource(new URL(this.baseUrl + "/follow/following/2").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.OK, result.getStatus());
    System.out.println("L'utilisateur 1 suit maintenant l'utilsateur 2");
    result.close();

    webResource = client.resource(new URL(this.baseUrl + "/follow/following/3").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.OK, result.getStatus());
    System.out.println("L'utilisateur 1 suit maintenant l'utilisateur 3");
    result.close();
    
    // retourner les abonnés d'un utilisateur inexistant: échec attendu
    webResource = client.resource(new URL(this.baseUrl + "/follow/follower/get/5675").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.USER_NO_ACCOUNT, result.getStatus());
    System.out.println("La liste d'abonnes d'un utilisateur inexistant n'est pas trouve");
    result.close();
    
    // retourner les abonnements de l'utilisateur 1: succès attendu
    webResource = client.resource(new URL(this.baseUrl + "/follow/following/my").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.OK, result.getStatus());
    List<LinkedHashMap<String, ?>> listUser = result.getEntity(List.class);
    String res = "Liste des abonnements de l'utilisateur 1 : 'le.jitou@gmail.com': ";
    for (LinkedHashMap<String, ?> u : listUser) {
      res+=u.get("email")+"  ";
    }
    System.out.println(res);
    result.close();

    // retourner les abonnés de l'utilisateur 2: succès attendu
    webResource = client.resource(new URL(this.baseUrl + "/follow/follower/get/2").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.OK, result.getStatus());
    listUser = result.getEntity(List.class);
    res = "Liste des abonnes de l'utilisateur 2 :'lavalber02@gmail.com': ";
    for (LinkedHashMap<String, ?> u : listUser) {
      res+=u.get("email")+"  ";
    }
    System.out.println(res);
    result.close();

    // Arrêt de l'abonnement à un utilisateur inexistant : échec attendu
    webResource = client.resource(new URL(this.baseUrl + "/follow/stop/372832").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.ID_NOT_EXIST, result.getStatus());
    System.out.println("L'utilisateur 1 tente, sans succes, de ne plus suivre un utilisateur inexistant");
    result.close();
    
    // Arrêt de l'abonnement à un utilisateur non suivi : échec attendu
    webResource = client.resource(new URL(this.baseUrl + "/follow/stop/7").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.NOT_FOLLOWING, result.getStatus());
    System.out.println("L'utilisateur 1 tente, sans succes, de ne plus suivre un utilisateur non suivi");
    result.close();
    
    // Arrêt de l'abonnement à l'utilisateur 2 : succès attendu
    webResource = client.resource(new URL(this.baseUrl + "/follow/stop/2").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.OK, result.getStatus());
    System.out.println("L'utilisateur 1 arrete de suivre l'utilisateur 2");
    result.close();

    // regarder les abonnements de l'utilisateur 1
    webResource = client.resource(new URL(this.baseUrl + "/follow/following/get/1").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    listUser = result.getEntity(List.class);
    res = "Liste des abonnements de l'utilisateur 1 : 'le.jitou@gmail.com': ";
    for (LinkedHashMap<String, ?> u : listUser) {
      res+=u.get("email")+"  ";
    }
    System.out.println(res);
    result.close();

    // regarder les abonnés de l'utilisateur 2
    webResource = client.resource(new URL(this.baseUrl + "/follow/follower/get/2").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    listUser = result.getEntity(List.class);
    res = "Liste des abonnés de l'utilisateur 2: 'lavalber02@gmail.com': ";
    for (LinkedHashMap<String, ?> u : listUser) {
      res+=u.get("email")+"  ";
    }
    System.out.println(res);
    result.close();
    
    // L'utilisateur 1 se déconnecte
    webResource = client.resource(new URL(this.baseUrl + "/bye").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    System.out.println("L'utilisateur 1 se deconnecte");
    result.close();
    
    // Connexion de l'utilisateur 2
    f.add("email", "lavalber02@gmail.com");
    f.add("password", "motdepasse");
    webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);    
    System.out.println("L'utilisateur 2 se connecte");
    result.close();
    
    // s'abonner à l'utilisateurs 1: succès attendu
    webResource = client.resource(new URL(this.baseUrl + "/follow/following/1").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.OK, result.getStatus());  
    System.out.println("L'utilisateur 2 suit l'utilisateur 1");
    result.close();
    
    // retourner les abonnements de l'utilisateur 2: succès attendu
    webResource = client.resource(new URL(this.baseUrl + "/follow/following/my").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    listUser = result.getEntity(List.class);
    res = "Liste des abonnements de l'utilisateur 2 : 'lavalber02@gmail.com': ";
    for (LinkedHashMap<String, ?> u : listUser) {
      res+=u.get("email")+"  ";
    }
    System.out.println(res);
    result.close();
    
    // retourner la liste des abonnés à l'utilisateur 2: succès attendu
    webResource = client.resource(new URL(this.baseUrl + "/follow/follower/my").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.OK, result.getStatus());    
    listUser = result.getEntity(List.class);
    res = "Liste des abonnes a l'utilisateur 2 : 'lavalber02@gmail.com': ";
    for (LinkedHashMap<String, ?> u : listUser) {
      res+=u.get("email")+"  ";
    }
    System.out.println(res);
    result.close();
  }
}
