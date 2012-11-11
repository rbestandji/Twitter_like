package supplies;

import core.DAOExceptionUser;
import core.Status;
import core.UserDAO;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.User;

@Path( "/registration")
public class UserRegistration {

  /*
   * Fonction permettant de créer un compte.
   */
  @POST
  @Produces( MediaType.APPLICATION_JSON)
  public Response registration(@CookieParam("authCookie") Cookie authenciateCookie,
          @FormParam("email") String email,
          @FormParam("password") String password,
          @FormParam("name") String name,
          @FormParam("firstname") String firstname) {
    if (authenciateCookie != null) {
      return Response.status(new Status(Status.USER_LOGGED)).build();
    }

    Status sta = null;
    User newUser = new User(name, firstname, email, password);
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
