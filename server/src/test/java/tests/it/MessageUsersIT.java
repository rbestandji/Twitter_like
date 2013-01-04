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

public class MessageUsersIT extends TestCase {

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
  
  // Cette fonction va tester l'envoi de messages et de commentaires ainsi que leur suppression 
  @Test
   public void testMessages() throws Exception {
   Form f = new Form();
   WebResource webResource;
   ClientResponse result;
   System.out.println("****************** ecriture et suppression de messages ! ******************");
    
   // Tentative d'envoi d'un message sans connection: échec attendu
   f.add("msg", "Hello tout le monde");
   webResource = client.resource(new URL(this.baseUrl + "/messages/send").toURI());
   result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
   Assert.assertEquals(Status.USER_OFFLINE, result.getStatus());
   System.out.println("Un utilisateur non connecte tente, sans succes, d'envoyer un message");
   result.close();

   // Connexion de l'utilisateur 1 : succès attendu
   f.clear();
   f.add("email", "le.jitou@gmail.com");
   f.add("password", "password");
   webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
   result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
   Assert.assertEquals(Status.OK, result.getStatus());
   System.out.println("L'utilisateur 1 se connecte");
   result.close();

   // envoie d'un message sur Twitter-like: succès attendu
   f.clear();
   f.add("msg", "Hello World");
   webResource = client.resource(new URL(this.baseUrl + "/messages/send").toURI());
   result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
   Assert.assertEquals(Status.OK, result.getStatus());
   System.out.println("L'utilisateur 1 envoie un message");
   result.close();  
   
   // Commentaire d'un message non-existant: échec attendu
   f.clear();
   f.add("comment", "Je suis un commentaire du msg 56");
   webResource = client.resource(new URL(this.baseUrl + "/comments/send/56313").toURI());
   result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
   System.out.println(result.getEntity(String.class));
   Assert.assertEquals(Status.NO_MESSAGE_ID, result.getStatus());
   System.out.println("L'utilisateur 1 tente, sans succes, de commenter un message inexistant");
   result.close();
   
   // Commentaire du message précédent: succès attendu
   f.clear();
   f.add("comment", "Je suis un commentaire du msg 1");
   webResource = client.resource(new URL(this.baseUrl + "/comments/send/11").toURI());
   result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
   Assert.assertEquals(Status.OK, result.getStatus());
   System.out.println("L'utilisateur 1 commente l'un de ses precedents messages");
   result.close();
  
   // Autre commentaire du même message précédent: succès attendu
   f.clear();
   f.add("comment", "Je suis un autre commentaire du msg 1");
   webResource = client.resource(new URL(this.baseUrl + "/comments/send/11").toURI());
   result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
   Assert.assertEquals(Status.OK, result.getStatus());
   System.out.println("L'utilisateur 1 commente une deuxième fois l'un de ses precedents messages");
   result.close();
  
   // L'utilisateur 1 se déconnecte: succès attendu
   webResource = client.resource(new URL(this.baseUrl + "/bye").toURI());
   result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
   Assert.assertEquals(Status.OK, result.getStatus());
   System.out.println("L'utilisateur 1 se deconnecte");
   result.close();

   // Reconnexion de l'utilisateur 1 : succès attendu
   f.clear();
   f.add("email", "le.jitou@gmail.com");
   f.add("password", "password");
   webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
   result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
   Assert.assertEquals(Status.OK, result.getStatus());
   System.out.println("L'utilisateur 1 se connecte. Ses informations sont:");
   System.out.println(result.getEntity(String.class));
   result.close();
     
   //Suppression d'un message inexistant: échec attendu
   webResource = client.resource(new URL(this.baseUrl + "/messages/delete/30032").toURI());
   result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
   Assert.assertEquals(Status.ID_NOT_EXIST, result.getStatus());
   System.out.println("L'utilisateur 1 tente, sans succes, de supprimer un message inexistant");
   result.close();
   
   //Suppression du commentaire ayant l'id 11 et des commentaires associés par cascade: succès attendu
   webResource = client.resource(new URL(this.baseUrl + "/messages/delete/11").toURI());
   result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
   Assert.assertEquals(Status.OK, result.getStatus());
   System.out.println("L'utilisateur 1 supprime 1 message et tous les sous commentaires sont supprimés par effet cascade");
   result.close();
   webResource = client.resource(new URL(this.baseUrl + "/messages/delete/11").toURI());
   result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
   Assert.assertEquals(Status.ID_NOT_EXIST, result.getStatus());
   System.out.println("L'utilisateur 1 vérifie que les sous commentaires ont bien ete supprimes en tentant, sans succes, de supprimer un des messages");
   result.close();
    
   // L'utilisateur 1 se déconnecte : succès attendu
   webResource = client.resource(new URL(this.baseUrl + "/bye").toURI());
   result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
   Assert.assertEquals(Status.OK, result.getStatus());
   System.out.println("L'utilisateur 1 se deconnecte");
   result.close();

   // L'utilisateur 2 se connecte 
   f.clear();
   f.add("email", "lavalber02@gmail.com");
   f.add("password", "motdepasse");
   webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
   result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
   Assert.assertEquals(Status.OK, result.getStatus());
   System.out.println("L'utilisateur 2 se connecte");
   result.close();

   // l'utilisateur 2 écrit un commentaire à l'utilisateur 1
   f.clear();
   f.add("comment", "je suis un commentaire du msg 2 du user 3");
   webResource = client.resource(new URL(this.baseUrl + "/comments/send/12").toURI());
   result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
   Assert.assertEquals(Status.OK, result.getStatus());
   System.out.println("L'utilisateur 2 ecrit un commentaire a l'utilisateur 3");
   result.close();
   
   //L'utilisateur 2 tente de supprimer le message 29 de l'utilisateur 1: échec attendu
   webResource = client.resource(new URL(this.baseUrl + "/messages/delete/12").toURI());
   result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
   Assert.assertEquals(Status.WRONG_USER, result.getStatus());
   System.out.println("L'utilisateur 2 tente, sans succes, de supprimer un message ecrit par l'utilisateur 3");
   result.close();
  }
}
