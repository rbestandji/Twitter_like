package supplies;

import share.core.DAOExceptionUser;
import share.core.Status;
import core.UserDAO;
import java.util.List;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import share.model.User;

@Path( "/users")
public class UserView {

  /*
   * Retourne le mur de l'utilisateur cad : ses messages et ceux des personnes suivis.
   */
  @Path("/getmywall")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getMyWall(@CookieParam("authCookie") Cookie authenciateCookie) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_OFFLINE)).build();
    }

    try {
      return Response.ok(UserDAO.getWall(Long.parseLong(authenciateCookie.getValue())), MediaType.APPLICATION_JSON).status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
  }

    /*
   * Retourne le mur d'un utilisateur identifié par id cad : ses messages et ceux des personnes suivis.
   */
  @Path("/getuserwall/{id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUserWall(@CookieParam("authCookie") Cookie authenciateCookie, @PathParam("id") String id) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_OFFLINE)).build();
    }
    
    try {
      return Response.ok(UserDAO.getWall(Long.parseLong(id)), MediaType.APPLICATION_JSON).status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
  }
  
  /*
   * Retourne la description de l'utilisateur
   */
  @Path("/getuserid/{id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUserWithID(@CookieParam("authCookie") Cookie authenciateCookie, @PathParam("id") String id) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_OFFLINE)).build();
    }

    User user;
    try {
      user = UserDAO.getUser(Long.parseLong(id));
      return Response.ok(user, MediaType.APPLICATION_JSON).status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
  }

  @Path("/getuseremail/{email}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUserWithEmail(@CookieParam("authCookie") Cookie authenciateCookie,
          @PathParam("email") String email) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_OFFLINE)).build();
    }
    Long id;
    try {
      id = UserDAO.getId(email);
      User user = UserDAO.getUser(id);
      return Response.ok(user, MediaType.APPLICATION_JSON).status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }

  }

  @Path("/search/{name}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUsersByName(@CookieParam("authCookie") Cookie authenciateCookie, @PathParam("name") String name) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_OFFLINE)).build();
    }
    try {
      List<User> u = UserDAO.searchUser(name);
      return Response.ok(u, MediaType.APPLICATION_JSON).status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
  }
}
