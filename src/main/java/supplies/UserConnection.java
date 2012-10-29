package supplies;

import core.Status;
import java.util.List;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import model.User;

@Path( "/connection")
public class UserConnection {

  /*
   * Fonction de connection (=> creation du cookie : authCookie)
   */
  @POST
  @Produces( MediaType.APPLICATION_JSON)
  public Response connection(@CookieParam("authCookie") Cookie authenciateCookie,
          @FormParam("email") String email,
          @FormParam("mdp") String mdp) {


    if (authenciateCookie != null) {
      return Response.status(new Status(Status.UTILISATEUR_CONNECTE)).build();
    }
    NewCookie cookie = null;

    User utilisateur = null;
    Status sta = null;
    UserTransaction utx = null;
    try {
      InitialContext ic = new InitialContext();
      utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
      EntityManager em = (EntityManager) ic.lookup("java:comp/env/persistence/EntityManager");

      utx.begin();
      em.joinTransaction();
      List<User> lu = em.createQuery("SELECT x FROM User x WHERE x.email='" + email + "'").getResultList();
      if (lu.isEmpty()) {
        sta = new Status(Status.UTILISATEUR_PAS_DE_COMPTE);
      } else {
        if (((User) lu.get(0)).getMdp().equals(mdp)) {
          utilisateur = lu.get(0);
          cookie = new NewCookie("authCookie", String.valueOf(utilisateur.getId()), "/", "localhost", "", 1000, false);
          sta = new Status(Status.OK);
          return Response.ok("test", MediaType.APPLICATION_JSON).status(sta).cookie(cookie).build();
        } else {
          sta = new Status(Status.UTILISATEUR_MAUVAIS_MOT_PASS);
        }
      }
      utx.commit();
    } catch (Exception ex) {
      try {
        if (utx != null) {
          utx.setRollbackOnly();
        }
      } catch (Exception rollbackEx) {
        // Impossible d'annuler les changements, vous devriez logguer une erreur,
        // voir envoyer un email à l'exploitant de l'application.
      }
      sta = new Status(Status.ERREUR_BDD);
      return Response.ok("Erreur : " + ex.getLocalizedMessage() + "  " + ex.toString(), MediaType.APPLICATION_JSON).status(sta).build();
    }
    return Response.status(sta).build();
  }
}
