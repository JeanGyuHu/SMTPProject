import java.io.*;
import java.net.*;

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
    private String[] temp;
    
	public SocketNetwork(String data, String from, String to) throws Exception {
	    localhost = InetAddress.getLocalHost();
	    
	    temp = from.split("@");
	    if(temp.length==2)
	    	host = temp[1];
	    
	    this.data = data;
	    this.from = from;
	    this.to = to;
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
		      
		      String initialID = in.readLine();
		      System.out.println(initialID);
		      
		      out.println("HELO " + localhost.getHostName());
		      String welcome = in.readLine();
		      System.out.println(welcome);
		      
		      out.println("MAIL From:<" + from + ">");
		      String senderOK = in.readLine();
		      System.out.println(senderOK);
		      
		      out.println("RCPT TO:<" + to + ">");
		      String recipientOK = in.readLine();
		      System.out.println(recipientOK);
		      
		      out.println("DATA");
		      out.println(data);

		      out.println(".");
		      String acceptedOK = in.readLine();
		      System.out.println(acceptedOK);
		      
		      out.println("QUIT");
		      
		      return true;
		      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      return true;
	      
	}
}
