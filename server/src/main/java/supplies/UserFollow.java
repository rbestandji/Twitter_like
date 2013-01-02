/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package supplies;

import share.core.DAOExceptionUser;
import core.FollowDAO;
import share.core.Status;
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
   * suivre qq.Retourne juste un code d'erreur
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
      return Response.ok(ex.getMessage()).status(ex.getStatus()).build();
    }
  }

   /*
   * arrêter de suivre qq. Retourne juste un code d'erreur
   */
  @Path("stop/{id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response stopAFollowing(@CookieParam("authCookie") Cookie authenciateCookie, @PathParam("id") String id) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_OFFLINE)).build();
    }

    try {
      FollowDAO.stopFollowUser(Long.parseLong(authenciateCookie.getValue()), Long.parseLong(id));
      return Response.status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.ok(ex.getMsg()).status(ex.getStatus()).build();
    }
  }

  /*
   * Retourne la liste des abonnements de l'utilisateur connecté.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path( "following/my")
  public Response getMyFollowings(@CookieParam("authCookie") Cookie authenciateCookie) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_OFFLINE)).build();
    }
    try {
      return Response.ok(FollowDAO.getFollows(Long.parseLong(authenciateCookie.getValue()), "following"),
                         MediaType.APPLICATION_JSON)
                     .status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
  }

  /*
   * Retourne la liste des abonnés de l'utilisateur connecté.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path( "follower/my")
  public Response getMyFollowers(@CookieParam("authCookie") Cookie authenciateCookie) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_OFFLINE)).build();
    }
    try {
      return Response.ok(FollowDAO.getFollows(Long.parseLong(authenciateCookie.getValue()), "follower"),
                         MediaType.APPLICATION_JSON)
                     .status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
  }

   /*
   * Retourne la liste des abonnements d'un utilisateur
   */
  @Path("following/get/{id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getFollowingsWithID(@CookieParam("authCookie") Cookie authenciateCookie, @PathParam("id") String id) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_OFFLINE)).build();
    }
    try {
      return Response.ok(FollowDAO.getFollows(Long.parseLong(id), "following"), MediaType.APPLICATION_JSON)
                     .status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
  }

   /*
   * Retourne la liste des abonnés d'un utilisateur
   */
  @Path("follower/get/{id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getFollowersWithID(@CookieParam("authCookie") Cookie authenciateCookie, @PathParam("id") String id) {
    if (authenciateCookie == null) {
      return Response.status(new Status(Status.USER_OFFLINE)).build();
    }
    try {
      return Response.ok(FollowDAO.getFollows(Long.parseLong(id), "follower"), MediaType.APPLICATION_JSON)
                     .status(new Status(Status.OK)).build();
    } catch (DAOExceptionUser ex) {
      return Response.status(ex.getStatus()).build();
    }
  }

}
