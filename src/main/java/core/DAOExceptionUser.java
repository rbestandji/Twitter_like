
package core;


public class DAOExceptionUser extends Exception {
  private Status status;
  private String msg;
  
  public DAOExceptionUser(Status status){
    this(status, "");
  }
   public DAOExceptionUser(Status status, String msg){
    this.msg = msg;
    this.status = status;
  }

  public Status getStatus() {
    return status;
  }

  public String getMsg() {
    return msg;
  }
   
}
