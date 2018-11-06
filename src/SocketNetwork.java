import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SocketNetwork {
	
	private InetAddress mailHost;
    private InetAddress localhost;
    private BufferedReader in;
	private PrintWriter out;
	private SSLSocket smtpPipe;
	
	private String host;
	private String data;
	private String from;
	private String to;
	private String id;
	private String password;
    private String[] temp;
    
	public SocketNetwork(String data, String from, String to,String password) throws Exception {
	    localhost = InetAddress.getLocalHost();
	    
	    temp = from.split("@");
	    if(temp.length==2)
	    	host = temp[1];
	    
	    this.data = data;
	    this.from = from;
	    this.to = to;
	    this.password = password;
	}
	
    protected boolean sendCommand(String command, String param, String responseCode) throws IOException {
        String response = "", code;
       
        out.println(command + param);
        response = in.readLine();
        
        System.out.println(response);
        code = response.substring(0,3);

        if(!code.equals(responseCode))
            return false;

        return true;
    }
    
	public boolean send() {
	      try {
			smtpPipe =(SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault())
	                .createSocket(InetAddress.getByName("smtp." + host), Constants.SMTP_PORT);
			if (smtpPipe == null) {
				return false;
		      }

		      in = new BufferedReader(new InputStreamReader(smtpPipe.getInputStream()));
		      out = new PrintWriter(smtpPipe.getOutputStream(), true);
		      
		      if(sendCommand("EHLO ", "smtp." + host, Constants.SMTP_EHLO)==false)
		    	  return false;
		      
		      id = Base64.getEncoder().encodeToString(temp[0].getBytes(StandardCharsets.UTF_8));
		      password = Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
		      
		      out.println("AUTH LOGIN");
		      
		      if(!in.readLine().substring(0, 3).equals(Constants.SMTP_LOGIN))
		    	  return false;
		      
		      out.println(id);
		      out.println(password);
		      
		      if(sendCommand("MAIL FROM: ", "<" + from + ">", Constants.SMTP_FROM)==false)
		    	  return false;
		      if(sendCommand("RCPT TO: ", "<" + to + ">" , Constants.SMTP_TO)==false)
		    	  return false;
		      if(sendCommand("DATA", "", Constants.SMTP_DATA)==false)
		    	  return false;

		      if(sendCommand(".", "", Constants.SMTP_QUIT)==false)
		    	  return false;
		      
		      out.println("QUIT");
		      in.close();
		      out.close();
		      smtpPipe.close();
		      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	      return true;
	      
	}
}
