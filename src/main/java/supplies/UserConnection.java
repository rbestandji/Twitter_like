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
          @FormParam("password") String password) {


    if (authenciateCookie != null) {
      return Response.status(new Status(Status.USER_LOGGED_IN)).build();
    }

    try {
      User user = UserDAO.connection(email, password);
      NewCookie cookie = new NewCookie("authCookie", String.valueOf(user.getId()), "/", "localhost", "", 1000, false);
      return Response.ok(user, MediaType.APPLICATION_JSON).status(Status.OK).cookie(cookie).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }

  }

  
  /* POUR DES TESTS !!! */
  @PUT
  @Produces( MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public String test(String str) {
    return "fonction : " + str + "  --";
  }
}
