package supplies;

import core.CommunicationDAO;
import core.UserDAO;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import share.core.DAOExceptionUser;
import share.core.Status;

@Path( "/communications")
public class CommunicationsResource {
  /*
   * Retourne la liste des messages et commentaires de l'utilisateur connecté.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path( "/my")
  public Response getMyCommunications(@CookieParam("authCookie") Cookie authenciateCookie) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_OFFLINE)).build();
    }
    try {
      return Response.ok(CommunicationDAO.getCommunications(Long.parseLong(authenciateCookie.getValue())),
                         MediaType.APPLICATION_JSON)
                     .status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
  }

  /*
   * Retourne la liste des messages et commentaires de l'utilisateur avec l'identifiant en PathParam.
   */
  @Path("/get/{idUser}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCommunicationsWithID(@CookieParam("authCookie") Cookie authenciateCookie,
                                    @PathParam("idUser") String idUser) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_OFFLINE)).build();
    }
    try {
      return Response.ok(CommunicationDAO.getCommunications(Long.parseLong(idUser)), MediaType.APPLICATION_JSON)
                     .status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
  }

  /*
   * Retourne la liste des messages et commentaires de l'utilisateur avec le mail en PathParam.
   */
  @Path("/getuser/{email}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCommunicationsWithEmail(@CookieParam("authCookie") Cookie authenciateCookie, 
                                             @PathParam("email") String email) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_OFFLINE)).build();
    }
    Long id;
    try {
      id = UserDAO.getId(email);
      return Response.ok(CommunicationDAO.getCommunications(id), MediaType.APPLICATION_JSON)
                     .status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
  }

}
