package supplies;

import core.CommentDAO;
import core.DAOExceptionUser;
import core.GroupDAO;
import core.MessageDAO;
import core.Status;
import core.UserDAO;
import java.io.IOException;
import java.util.Date;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.Group;
import model.Message;

@Path( "/group")
public class GroupsResource {

  /*
   * Retourne la liste des messages de l'utilisateur avec l'identifiant en PathParam.
   */
  @Path("/create/{name}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getMessageWithID(@CookieParam("authCookie") Cookie authenciateCookie,
          @PathParam("name") String name) throws IOException {

    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_OFFLINE)).build();
    }
    try {
      GroupDAO.createGroup(Long.parseLong(authenciateCookie.getValue()), new Group(name));
      return Response.status(Status.OK).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
  }
}
