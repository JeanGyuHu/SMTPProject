import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.net.ssl.*;

public class SocketNetwork {
	
    private BufferedReader in;
	private PrintWriter out;
	private SSLSocket socket;
	
	private String host;
	private String subject;
	private String data;
	private String from;
	private String to;
	private String id;
	private String password;
    private String[] temp;
    
    private String response;
    
	public SocketNetwork(String data, String from, String to,String password,String subject) throws Exception {
	    
	    temp = from.split("@");
	    if(temp.length==2)
	    	host = temp[1];
	    
	    this.data = data;
	    this.from = from;
	    this.to = to;
	    this.password = password;
	    this.subject = subject;
	}
	
    protected boolean sendMessage(String command, String responseCode) throws IOException {
        String response = "", code;
       
        out.println(command);
        response = in.readLine();
        
        System.out.println(response);
        code = response.substring(0,3);

        if(!code.equals(responseCode))
            return false;

        return true;
    }
    
	public boolean send() {
	      try {
			socket =(SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault())
	                .createSocket(InetAddress.getByName("smtp." + host), Constants.SMTP_CONNECT);
			if (socket == null) {
				return false;
		      }

		      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		      out = new PrintWriter(socket.getOutputStream(), true);
		      
		      if(sendMessage("EHLO smtp." + host, Constants.SMTP_EHLO)==false)
		    	  return false;
		      
		      id = Base64.getEncoder().encodeToString(temp[0].getBytes(StandardCharsets.UTF_8));
		      password = Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
		      
		      out.println("AUTH LOGIN");
		      
		      while(!((response=in.readLine().substring(0, 3)).equals(Constants.SMTP_LOGIN))) {
		      }
		      System.out.println(response);
		      
		      if(!response.equals(Constants.SMTP_LOGIN))
		    	  return false;
		      
		      out.println(id);
		      response= in.readLine();
		      
		      System.out.println(response);
		      if(!response.substring(0, 3).equals(Constants.SMTP_LOGIN))
		    	  return false;
		      
		      out.println(password);
		      response= in.readLine();
		      System.out.println(response);
		      
		      if(!response.substring(0, 3).equals(Constants.SMTP_LOGINSUCCESS))
		    	  return false;
		      
		      if(sendMessage("MAIL FROM: <" + from + ">", Constants.SMTP_FROM)==false)
		    	  return false;
		      
		      if(sendMessage("RCPT TO: <" + to + ">" , Constants.SMTP_TO)==false)
		    	  return false;
		      
		      if(sendMessage("DATA", Constants.SMTP_DATA)==false)
		    	  return false;

		      out.println("From: "+from+" <" + from + ">");
		      out.println("To: " + to + " <" + to + ">");
		      out.println("Subject: "+subject+"\r\n");
		      out.println(data);
		      
		      if(sendMessage(".", Constants.SMTP_QUIT)==false)
		    	  return false;
		      
		      out.println("QUIT");
		      in.close();
		      out.close();
		      socket.close();
		      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      return true;
	}
}