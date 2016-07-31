//TODO: usefull class?
package Control;

import UI.MainFrame;
import java.io.IOException;  
import java.io.InputStream;  
import java.io.PrintStream;  
import org.apache.commons.net.telnet.TelnetClient;

public class Telnet  extends  Thread 
{  
    private TelnetClient telnet = null;   
    private InputStream in;  
    private PrintStream out;  
    private String host = "";
    private int port = 23;
    private String user, password;
    private long timeout;
    public Telnet(String host, int port, String user, String password, long timeout)  
    {  
    	this.host = host;
    	this.user = user;
    	this.password = password;
    	this.timeout = timeout;
    }  
    public void run(){
    	 try  
         {  
         	
         	 telnet = new TelnetClient();
             telnet.connect(host, port);  
             in = telnet.getInputStream();  
             out = new PrintStream(telnet.getOutputStream());  
             login(user, password, timeout);
             disconnect();
         }  
         catch (Exception e)  
         {  
         	System.err.println("telnet: " + host+" failed!");
         }  
    }
  
    /** 
     *  
     * 
     * @param user 
     * @param password 
     * @param timeout ��λ����
     */  
    public void login(String user, String password, long timeout)  
    {  
    	System.out.println("telnet : " + host);
        if(!readUntil("Username:", timeout)){
        	System.out.println("Can not read 'Username:' from remote host.\nYou may have passed "+ host + " firewall!\nOr timeout value maybe too small.\nIf bad network speed please enlarge timeout value.");
        	return;
        }
        
        write(user);  
        System.out.println(user);
        if(!readUntil("Password:", timeout)){
        	System.out.println("Can not read 'Password:' from remote host.\nYou may have passed "+ host + " firewall!\nOr timeout value maybe too small.\nIf bad network speed please enlarge timeout value.");
        	return;
        }    
        write(password); 
        MainFrame.out.println(password);
        System.out.println("******");
        if(!readRemain(timeout)){
        	System.out.println("Telnet Finished.\nDisplayed message may not be full,you can larger timeout value to see longer message.");
        } 
       
       
    }  
    /** 
     * 
     * 
     * @param pattern 
     * @return 
     */  
    public boolean readUntil(String pattern ,long timeout){   
    	
    	try{  
    		long startTime  = System.currentTimeMillis();
            char lastChar = pattern.charAt(pattern.length() - 1);  
            StringBuffer sb = new StringBuffer();  
            int rint = in.read();
            char ch = (char) rint;
            String s = "";
            while (true){  
            	long currentTime = System.currentTimeMillis();
            	//Thread.sleep(500);
            	 
            	sb.append(ch);  

            	if(s.toLowerCase().contains("success") || s.toLowerCase().contains("passed")||s.toLowerCase().contains("thank you") ){
            		System.out.print("Pass Firewall:" + host +  " Success");    
  	            	return true;
  	            }
  	            if(s.toLowerCase().contains("error") || s.toLowerCase().contains("fail")){
  	            	System.out.print("Fail to Pass Firewall:" + host);  
  	            	return false;
  	            }
  	           
                
                if (ch == lastChar){  
                    if (sb.toString().endsWith(pattern)){  
                    	System.out.print(s);
                        return true;  
                    }  
                }  
                if((currentTime - startTime) > timeout||rint == -1){
	            	System.out.println(s);  
	            	return false;
    	        }
                if(s.toLowerCase().contains("aix version")){
	            	return false;
                }
              //  MainFrame.out.println(((currentTime - startTime) > timeout)+":"+currentTime);
                rint = in.read();
                ch = (char)rint;  
                s += ch;
                if(ch == '\n'){              	
                 	System.out.print(s);  
                 	s = "";
                }
                
               
                
            }  
        }catch (Exception e){  
        	System.err.println("read until "+pattern+" failed");
        	disconnect();
        }  
    	return false;
    }  
    
    public boolean readRemain(long timeout){
    	
    	long startTime  = System.currentTimeMillis();
    	char ch;
    	int rint = 0;
		try {
			ch = (char)in.read();
			String s = "";
	        while (true)  
	        {  
	        	//Thread.sleep(500);
	        	long currentTime = System.currentTimeMillis();
	        	
	            if(s.toLowerCase().contains("success") || s.toLowerCase().contains("passed")||s.toLowerCase().contains("thank you") ){
	            	System.out.print("Pass Firewall:" + host +  " Success");  
	            	return true;
	            }
	            if(s.toLowerCase().contains("error") || s.toLowerCase().contains("fail")){
	            	System.out.print("Fail to Pass Firewall:" + host);   
	            	return false;
	            }	 
	            
                //58 = ':'
	            if((currentTime - startTime) > timeout||rint == -1){
	            	System.out.println(s);  
	            	return false;
	            }
                rint = in.read();
                ch = (char)rint;  
	            s += ch;
	            if(ch == '\n'){              	
	            	System.out.print(s);  
	            	s = "";
	            }
	            
	        }  
		} catch (IOException e) {
			System.err.println("read remain message failed");
			disconnect();
		} 
        return false;
    }
  
   
  
    /** 
     * 
     * 
     * @param value 
     */  
    public void write(String value){  
        try {  
            out.println(value);  
            out.flush();  
        }catch (Exception e){  
        	System.err.println("write string failed");
        	disconnect();
        }  
    }  
  
    /** 
     * 
     * 
     * @param command 
     * @return 
     */  
//    public String sendCommand(String command)  
//    {  
//        try  
//        {  
//            write(command);  
//            return readUntil(prompt + "");  
//        }  
//        catch (Exception e)  
//        {  
//            e.printStackTrace();  
//        }  
//        return null;  
//    }  
//  
    /** 
     * 
     */  
    public void disconnect()  
    {  
        try  {  
            telnet.disconnect();  
        }  
        catch (Exception e){  
        	System.err.println("telnet disconnect failed");
        }  
    }  

}  