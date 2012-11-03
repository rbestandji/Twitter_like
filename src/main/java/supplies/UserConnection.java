package supplies;

import core.DAOExceptionUser;
import core.Status;
import core.UserDAO;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
   * Fonction de connexion (=> création du cookie : authCookie)
   */
  @POST
  @Produces( MediaType.APPLICATION_JSON)
  public Response connection(@CookieParam("authCookie") Cookie authenciateCookie,
          @FormParam("email") String email,
          @FormParam("mdp") String mdp) {


    if (authenciateCookie != null) {
      return Response.status(new Status(Status.UTILISATEUR_CONNECTE)).build();
    }

    try {
      User utilisateur = UserDAO.connection(email, mdp);
      NewCookie cookie = new NewCookie("authCookie", String.valueOf(utilisateur.getId()), "/", "localhost", "", 1000, false);
      return Response.ok(utilisateur, MediaType.APPLICATION_JSON).status(Status.OK).cookie(cookie).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }

  }

  @PUT
  @Produces( MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public String test(String str) {
    return "fonction : " + str + "  --";
  }
}
