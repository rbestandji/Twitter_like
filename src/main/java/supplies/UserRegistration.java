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
import javax.ws.rs.core.Response;
import model.User;

@Path( "/inscription")
public class UserRegistration {

  /*
   * Fonction permettant de creer un compte.
   */
  @POST
  @Produces( MediaType.APPLICATION_JSON)
  public Response inscription(@CookieParam("authCookie") Cookie authenciateCookie,
          @FormParam("email") String email,
          @FormParam("mdp") String mdp,
          @FormParam("nom") String nom,
          @FormParam("prenom") String prenom) {
    if (authenciateCookie != null) {
      return Response.status(new Status(Status.UTILISATEUR_CONNECTE)).build();
    }

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
        User newUser = new User(nom, prenom, email, mdp);
        em.persist(newUser);
        sta = new Status(Status.OK);
      } else {
        sta = new Status(Status.EMAIL_PRISE);
      }
      utx.commit();
    } catch (Exception ex) {
      sta = new Status(Status.ERREUR_BDD);
      try {
        if (utx != null) {
          utx.setRollbackOnly();
        }
      } catch (Exception rollbackEx) {
        // Impossible d'annuler les changements, vous devriez logguer une erreur,
        // voir envoyer un email à l'exploitant de l'application.
      }
    }
    return Response.status(sta).build();
  }
}
