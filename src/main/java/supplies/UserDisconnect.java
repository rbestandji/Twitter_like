package supplies;

import core.Status;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

@Path("/bye")
public class UserDisconnect {

  /*
   * Fonction de deconnection => destruction de authCookie
   */
  @GET
  @Produces( MediaType.APPLICATION_JSON)
  public Response deconnection() {
    NewCookie cookie = new NewCookie("authCookie", "-1", "/", "localhost", "", 0, false);
    return Response.status(new Status(Status.OK)).cookie(cookie).build();
  }
}
