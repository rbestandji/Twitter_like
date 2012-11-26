/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package supplies;

import core.DAOExceptionUser;
import core.FollowDAO;
import core.Status;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jitou
 */
@Path("/follow/")
public class UserFollow {
  /*
   * Retourne juste un code d'erreur
   */

  @Path("following/{id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response createANewFollower(@CookieParam("authCookie") Cookie authenciateCookie, @PathParam("id") String id) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_OFFLINE)).build();
    }

    try {
      FollowDAO.followUser(Long.parseLong(authenciateCookie.getValue()), Long.parseLong(id));
      return Response.status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
  }
}
