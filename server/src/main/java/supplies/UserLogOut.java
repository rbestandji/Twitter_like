package supplies;

import share.core.Status;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

@Path("/bye")
public class UserLogOut {

  /*
   * Fonction de déconnexion => destruction de authCookie
   */
  @GET
  @Produces( MediaType.APPLICATION_JSON)
  public Response logout() {
    NewCookie cookie = new NewCookie("authCookie", "-1", "/", "localhost", "", 0, false);
    return Response.status(new Status(Status.OK)).cookie(cookie).build();
  }
}
