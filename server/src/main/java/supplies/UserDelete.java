package supplies;

import core.UserDAO;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import share.core.DAOExceptionUser;
import share.core.Status;

@Path("/delete/my")
public class UserDelete {
   /*
   * Permet à l'utilisateur connecté de supprimer son compte
   */
  @GET
  @Produces( MediaType.APPLICATION_JSON)
  public Response deleteMyAccount(@CookieParam("authCookie") Cookie authenciateCookie) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_OFFLINE)).build();
    }
    Status sta; 
    try {
      UserDAO.deleteUser(Long.parseLong(authenciateCookie.getValue()));
      //NewCookie cookie = new NewCookie("authCookie", "-1", "/", "localhost", "", 0, false);
      sta = new Status(Status.OK);
      return Response.status(sta).build();
    } catch (DAOExceptionUser ex) {
      sta = ex.getStatus();
      return Response.ok(ex.getMsg()).status(sta).build();
    }
  }
}
