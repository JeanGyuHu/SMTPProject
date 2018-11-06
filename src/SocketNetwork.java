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
	                .createSocket(InetAddress.getByName("smtp." + host), Constants.SMTP_CONNECT);
			if (smtpPipe == null) {
				return false;
		      }

		      in = new BufferedReader(new InputStreamReader(smtpPipe.getInputStream()));
		      out = new PrintWriter(smtpPipe.getOutputStream(), true);
		      
		      System.out.println("1");
		      if(sendCommand("EHLO ", "smtp." + host, Constants.SMTP_EHLO)==false)
		    	  return false;
		      
		      id = Base64.getEncoder().encodeToString(temp[0].getBytes(StandardCharsets.UTF_8));
		      password = Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
		      
		      out.println("AUTH LOGIN");
		      while(!in.readLine().substring(0, 3).equals(Constants.SMTP_LOGIN)) {
		      }
		      System.out.println("2");
		      
		      out.println(id);
		      System.out.println(in.readLine());
		      out.println(password);
		      System.out.println(in.readLine());
		      
		      if(sendCommand("MAIL FROM: ", "<" + from + ">", Constants.SMTP_FROM)==false)
		    	  return false;
		      
		      System.out.println("2");
		      
		      if(sendCommand("RCPT TO: ", "<" + to + ">" , Constants.SMTP_TO)==false)
		    	  return false;
		      
		      System.out.println("3");
		      
		      if(sendCommand("DATA", "", Constants.SMTP_DATA)==false)
		    	  return false;

		      out.println("From: "+from+" <" + from + ">");
		      out.println("To: " + to + " <" + to + ">");
		      out.println("Subject: HI!\r\n");
		      out.println("Content: " + data);
	            
		      System.out.println("4");
		      
		      if(sendCommand(".", "", Constants.SMTP_QUIT)==false)
		    	  return false;
		      
		      System.out.println("5");
		      
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
