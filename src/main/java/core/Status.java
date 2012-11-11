package core;

import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

public class Status implements StatusType {

  public final static int USER_NO_LOGGED = 0;
  public final static int USER_LOGGED = 1;
  public final static int USER_NO_ACCOUNT = 2;
  public final static int USER_BAD_PASSWORD = 3;
  public final static int ERROR_DB = 4;
  public final static int OK = 200;
  public final static int EMAIL_TAKEN = 5;
  public final static int NOT_ID_MESSAGE = 6;
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
      /* Pff long à faire pour rien ....
       * Seriously !!!
       */
    }
    return str;
  }
}
