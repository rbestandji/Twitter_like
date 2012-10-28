package tests.it;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import junit.framework.TestCase;
import java.net.URL;
import javax.ws.rs.core.MediaType;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import core.Status;
import java.util.LinkedHashMap;
import java.util.List;
import model.Message;
import model.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WebappIT extends TestCase {

    private String baseUrl;
    private DefaultApacheHttpClientConfig config;
    private ApacheHttpClient client;

    public void setUp() throws Exception {
        super.setUp();
        String port = System.getProperty("servlet.port");
        this.baseUrl = "http://localhost:" + port + "/cargo-webapp";

        config = new DefaultApacheHttpClientConfig();

        //Autorisation des cookies
        config.getProperties().put(ApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, true);

        //Jersey configuration
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        //Client apache :)
        client = ApacheHttpClient.create(config);
        client.setFollowRedirects(Boolean.TRUE);/* Plus utilisé */

    }

    /* Dans cette fonction nous allons creer des utilisateurs */
    @Before
    public void testCreationDesUsers() throws Exception {

        Form f = new Form();
        WebResource webResource = null;
        ClientResponse result = null;
        System.out.println("****************** Creation des comptes ! ******************");

        /* Utilisateur 1 */
        f.add("email", "le.jitou@gmail.com");
        f.add("mdp", "mdp");
        f.add("nom", "Pasquet");
        f.add("prenom", "Jerome");
        webResource = client.resource(new URL(this.baseUrl + "/inscription").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
        Assert.assertEquals(result.getStatus(), Status.OK);
        result.close();


        /* Utilisateur 2 */
        f.clear();
        f.add("email", "lavalber02@gmail.com");
        f.add("mdp", "monMDP");
        f.add("nom", "Laval");
        f.add("prenom", "Bernard");
        webResource = client.resource(new URL(this.baseUrl + "/inscription").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
        Assert.assertEquals(result.getStatus(), Status.OK);
        result.close();


        /* Utilisateur 3 */
        f.clear();
        f.add("email", "lionel.muller.34@gmail.com");
        f.add("mdp", "monMDP");
        f.add("nom", "Muller");
        f.add("prenom", "Lionel");
        webResource = client.resource(new URL(this.baseUrl + "/inscription").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
        Assert.assertEquals(result.getStatus(), Status.OK);
        result.close();

        /* Utilisateur 4 */
        f.clear();
        f.add("email", "lionel.muller.34@gmail.com");
        f.add("mdp", "dfdfs");
        f.add("nom", "castorPirate");
        f.add("prenom", "Waza");
        webResource = client.resource(new URL(this.baseUrl + "/inscription").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
        Assert.assertEquals(result.getStatus(), Status.EMAIL_PRISE);
        result.close();

    }

    /* Cette fonction test qu'un utilisateur peut se connecter avec (qu'avec) son compte.*/
    @Test
    public void testConnectionCompte() throws Exception {
        Form f = new Form();
        WebResource webResource;
        ClientResponse result;
        System.out.println("****************** Tests des connexions! ******************");


        /* Connexion de l'utilisateur 1 */
        f.add("email", "le.jitou@gmail.com");
        f.add("mdp", "mdp");
        webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
        Assert.assertEquals(result.getStatus(), Status.OK);
        result.close();


        /* L'utilisateur 1 est encore connecté tentative de connexion de l'utilisateur 2 */
        f.clear();
        f.add("email", "lavalber02@gmail.com");
        f.add("mdp", "monMDP");
        webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
        Assert.assertEquals(result.getStatus(), Status.UTILISATEUR_CONNECTE);
        result.close();


        /* L'utilisateur 1 se deconnecte */
        webResource = client.resource(new URL(this.baseUrl + "/bye").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        Assert.assertEquals(result.getStatus(), Status.OK);
        result.close();


        /* L'utilisateur 2  veut se reconnecter avec le mauvais mdp */
        f.clear();
        f.add("email", "lavalber02@gmail.com");
        f.add("mdp", "mdp");
        webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
        Assert.assertEquals(result.getStatus(), Status.UTILISATEUR_MAUVAIS_MOT_PASS);
        result.close();

        /* L'utilisateur 1  veut se reconnecter avec le mauvais identifiant */
        f.clear();
        f.add("email", "le.jidtouu@gmail.com");
        f.add("mdp", "mdp");
        webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
        Assert.assertEquals(result.getStatus(), Status.UTILISATEUR_PAS_DE_COMPTE);
        result.close();

    }

    /* Cette fonction va tester l'envoie de Twitte ainsi que leur lecture */
    @Test
    public void testEnvoieMsg() throws Exception {
        Form f = new Form();
        WebResource webResource;
        ClientResponse result;
        System.out.println("****************** Tests des messages ! ******************");

        /* Tentative d'envoie d'un message sans connection*/
        f.add("msg", "Hello tout le monde");
        webResource = client.resource(new URL(this.baseUrl + "/messages/envoie").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
        Assert.assertEquals(result.getStatus(), Status.UTILISATEUR_PAS_CONNECTE);
        result.close();

        /* Connexion de l'utilisateur 1 */
        f.clear();
        f.add("email", "le.jitou@gmail.com");
        f.add("mdp", "mdp");
        webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
        Assert.assertEquals(result.getStatus(), Status.OK);
        result.close();

        /* Envoie d'un message sur Twitter !!!*/
        f.clear();
        f.add("msg", "Hello World");
        webResource = client.resource(new URL(this.baseUrl + "/messages/envoie").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
        Assert.assertEquals(result.getStatus(), Status.OK);
        result.close();

        /* Envoie d'un deuxieme message sur Twitter !!!*/
        f.clear();
        f.add("msg", "Je sui un Twitte de Twitter like !");
        webResource = client.resource(new URL(this.baseUrl + "/messages/envoie").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
        Assert.assertEquals(result.getStatus(), Status.OK);
        result.close();

        /* L'utilisateur 1 se deconnecte */
        webResource = client.resource(new URL(this.baseUrl + "/bye").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        Assert.assertEquals(result.getStatus(), Status.OK);
        result.close();

        /* L'utilisateur 2  se connecter */
        f.clear();
        f.add("email", "lavalber02@gmail.com");
        f.add("mdp", "monMDP");
        webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
        Assert.assertEquals(result.getStatus(), Status.OK);
        result.close();

        /* Envoie d'un trooisieme message sur Twitter avec le compte de bernard !!!*/
        f.clear();
        f.add("msg", " Moi je suis un message");
        webResource = client.resource(new URL(this.baseUrl + "/messages/envoie").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
        Assert.assertEquals(result.getStatus(), Status.OK);
        result.close();

        /* Benard veut lire son message !*/
        webResource = client.resource(new URL(this.baseUrl + "/messages/my").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        Assert.assertEquals(result.getEntity(List.class).size(), 1);
        result.close();

        /* Benard veut lire les messages de 1 !*/
        webResource = client.resource(new URL(this.baseUrl + "/messages/get/1").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        Assert.assertEquals(result.getEntity(List.class).size(), 2); //Jitou à posté deux messages.
        result.close();

        /* Lecture des messages à partir du mail. */
        webResource = client.resource(new URL(this.baseUrl + "/messages/getuser/lavalber02@gmail.com").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        Assert.assertEquals(((LinkedHashMap) (result.getEntity(List.class).get(0))).get("text"), " Moi je suis un message");
        result.close();

        /* Lecture des messages à partir d'un mauvais mail. */
        webResource = client.resource(new URL(this.baseUrl + "/messages/getuser/lavalber03@gmail.com").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        Assert.assertEquals(Status.UTILISATEUR_PAS_DE_COMPTE, result.getStatus());
        result.close();

    }

    /* Cette fonction va tester la lecture d'un profile */
    @Test
    public void testViewUser() throws Exception {
        Form f = new Form();
        WebResource webResource;
        ClientResponse result;
        System.out.println("****************** Tests lecture profil ! ******************");

        /* Connexion de l'utilisateur 1 */
        f.add("email", "le.jitou@gmail.com");
        f.add("mdp", "mdp");
        webResource = client.resource(new URL(this.baseUrl + "/connection").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, f);
        Assert.assertEquals(result.getStatus(), Status.OK);
        result.close();

        /*Lecture des messages de l'utilisateur 1*/
        webResource = client.resource(new URL(this.baseUrl + "/users/get/1").toURI());
        result = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        Assert.assertEquals(result.getEntity(User.class).getEmail(), "le.jitou@gmail.com");
        result.close();
    }
}
