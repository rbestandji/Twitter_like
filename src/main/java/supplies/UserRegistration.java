package supplies;

import core.DAOExceptionUser;
import core.Status;
import core.UserDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    User newUser = new User(nom, prenom, email, mdp);
    try {
      UserDAO.createUser(newUser);
      sta = new Status(Status.OK);
    } catch (DAOExceptionUser ex) {
      sta = ex.getStatus();
          return Response.ok(ex.getMsg()).status(sta).build();

    }
    return Response.status(sta).build();
  }
}
