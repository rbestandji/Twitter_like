/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

/**
 *
 * @author jitou
 */
public class Status implements StatusType {

  public final static int UTILISATEUR_PAS_CONNECTE = 0;
  public final static int UTILISATEUR_CONNECTE = 1;
  public final static int UTILISATEUR_PAS_DE_COMPTE = 2;
  public final static int UTILISATEUR_MAUVAIS_MOT_PASS = 3;
  public final static int ERREUR_BDD = 4;
  public final static int OK = 200;
  private int code;

  public Status(int code) {
    this.code = code;
  }

  public int getStatusCode() {
    return code;
  }

  public Family getFamily() {
    return Family.OTHER;
  }

  public String getReasonPhrase() {
    String str = "";
    switch (code) {

    }
    return str;
  }
}
