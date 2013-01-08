package Network;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

public class GetPubIP {
  private static GetPubIP singleTask = new GetPubIP();

  private GetPubIP() {}

  static public GetPubIP getPubIP() {
    return singleTask;
  }

  public String getIP() throws MalformedURLException, IOException { 
    String result;
    HttpClient client = new HttpClient();
    String Url = "http://checkip.dyndns.org/";
    URL url = new URL(Url);
    HttpMethod method = new GetMethod(Url);
    URLConnection conn = (HttpURLConnection) url.openConnection();
    client.executeMethod(method);
    result = method.getResponseBodyAsString();
    method.releaseConnection();
    // remove html
    result = result.substring(76, result.length()-16);
    return result;
  }
}
