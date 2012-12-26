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

public class MessageIT extends TestCase {

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

  // Cette fonction va tester l'envoi de Tweet ainsi que leur lecture 
  @Test
  public void testSendMsg() throws Exception {
    Form f = new Form();
    WebResource webResource;
    ClientResponse result;
    System.out.println("****************** Tests des messages ! ******************");

    // Tentative d'envoi d'un message sans connection
    f.add("msg", "Hello tout le monde");
    webResource = client.resource(new URL(this.baseUrl + "/communications/messages/send").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    Assert.assertEquals(result.getStatus(), Status.USER_OFFLINE);
    result.close();

    // Connexion de l'utilisateur 1 
    f.clear();
    f.add("email", "le.jitou@gmail.com");
    f.add("password", "password");
    webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    Assert.assertEquals(result.getStatus(), Status.OK);
    result.close();

    // envoie d'un message sur Twitter-like
    f.clear();
    f.add("msg", "Hello World");
    webResource = client.resource(new URL(this.baseUrl + "/communications/messages/send").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    Assert.assertEquals(result.getStatus(), Status.OK);
    result.close();
    
    // envoie d'un deuxième message sur Twitter-like
    f.clear();
    f.add("msg", "Je suis un Twitte de Twitter like !");
    webResource = client.resource(new URL(this.baseUrl + "/communications/messages/send").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    Assert.assertEquals(result.getStatus(), Status.OK);
    result.close();
    
    // Commentaire du message précédent
    f.clear();
    f.add("comment", "Je suis un commentaire");
    webResource = client.resource(new URL(this.baseUrl + "/communications/comments/send/28").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    result.close();

    // Autre commentaire du même message
    f.clear();
    f.add("comment", "Je suis un autre commentaire");
    webResource = client.resource(new URL(this.baseUrl + "/communications/comments/send/28").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    result.close();
    
    // L'utilisateur 1 se déconnecte 
    webResource = client.resource(new URL(this.baseUrl + "/bye").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(result.getStatus(), Status.OK);
    result.close();

    // Connexion de l'utilisateur 1 
    f.clear();
    f.add("email", "le.jitou@gmail.com");
    f.add("password", "password");
    webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    Assert.assertEquals(result.getStatus(), Status.OK);
    System.out.println(result.getEntity(String.class));
    result.close();

    // Lecture de mes messages et commentaires
    webResource = client.resource(new URL(this.baseUrl + "/communications/my").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    List<LinkedHashMap<String, ?>> listCom = result.getEntity(List.class);
    String res = "Liste des communications de 'le.jitou@gmail.com': ";
    for (LinkedHashMap<String, ?> c : listCom) {
      res+=c.get("text")+"; ";
    }
    System.out.println(res);
    result.close();
    
    //Suppression du commentaire ayant l'id 29 (rattaché à l'utilisateur 1 et au commentaire 28)
    webResource = client.resource(new URL(this.baseUrl + "/communications/delete/29").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    Assert.assertEquals(result.getStatus(), Status.OK);
    result.close();
    
    //Suppression du message ayant l'id 28 (rattaché à l'utilisateur 1)
    webResource = client.resource(new URL(this.baseUrl + "/communications/delete/28").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    Assert.assertEquals(result.getStatus(), Status.OK);
    result.close();
    
    // L'utilisateur 1 se déconnecte 
    webResource = client.resource(new URL(this.baseUrl + "/bye").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(result.getStatus(), Status.OK);
    result.close();

    // Connexion de l'utilisateur 1 
    f.clear();
    f.add("email", "le.jitou@gmail.com");
    f.add("password", "password");
    webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    System.out.println(result.getEntity(String.class));
    Assert.assertEquals(result.getStatus(), Status.OK);
    result.close();

    // Lecture de mes messages et commentaires
    webResource = client.resource(new URL(this.baseUrl + "/communications/my").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    listCom = result.getEntity(List.class);
    res = "Liste des communications de 'le.jitou@gmail.com': ";
    for (LinkedHashMap<String, ?> m : listCom) {
      res+=m.get("text")+"; ";
    }
    System.out.println(res);
    result.close();
    
/*
    // L'utilisateur 1 se déconnecte 
    webResource = client.resource(new URL(this.baseUrl + "/bye").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(result.getStatus(), Status.OK);
    result.close();

    // L'utilisateur 2 se connecte 
    f.clear();
    f.add("email", "lavalber02@gmail.com");
    f.add("password", "motdepasse");
    webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    Assert.assertEquals(result.getStatus(), Status.OK);
    result.close();

    // Envoie d'un troisième message sur Twitter-like avec le compte de Bernard
    f.clear();
    f.add("msg", "Moi je suis un message");
    webResource = client.resource(new URL(this.baseUrl + "/communications/messages/send").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
    Assert.assertEquals(result.getStatus(), Status.OK);
    result.close();

    // Bernard veut lire son message
    webResource = client.resource(new URL(this.baseUrl + "/communications/my").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    List<LinkedHashMap<String, ?>> listMsg = result.getEntity(List.class);
    boolean ff = false;
    for (LinkedHashMap<String, ?> m : listMsg) {
      if (((String) m.get("text")).compareTo("Moi je suis un message") == 0) {
        ff = true;
      }
    }
    Assert.assertEquals(ff, true);
    result.close();*/

    /*
      
    // Lecture des messages à partir du mail 
    webResource = client.resource(new URL(this.baseUrl + "/communications/getuser/lavalber02@gmail.com").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(((LinkedHashMap) (result.getEntity(List.class).get(0))).get("text"), "Moi je suis un message");
    //System.out.println(result.getEntity(List.class));
    result.close();

    // Lecture des messages à partir d'un mauvais mail 
    webResource = client.resource(new URL(this.baseUrl + "/communications/getuser/lavalber03@gmail.com").toURI());
    result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    Assert.assertEquals(Status.USER_NO_ACCOUNT, result.getStatus());
    result.close();
     */
  }
}
