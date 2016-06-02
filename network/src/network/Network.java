/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 *
 * @author neo
 */
public class Network {

    /**
     * These are source IP address and port in network
     */
    protected InetAddress realSourceIP;
    protected String realSourcePort;

    /**
     * These are real IP address of computer which is running router program
     */
    protected InetAddress routerIP; 
    protected String routerPort;

    public Network(InetAddress realSourceIP, String realSourcePort, InetAddress routerIP, String routerPort) {
        this.realSourceIP = realSourceIP;
        this.realSourcePort = realSourcePort;
        this.routerIP = routerIP;
        this.routerPort = routerPort;
    }
    
    

    /**
     * These are virtual IP address and port, these are used by only router to
     * divert the package, now where else these are used
     */
      protected String virtualIpAdress, virtualPort;

    public byte[] BuildPacket(String data, String relDestinationIp, String realDestinationPort) {

        int addresslength = relDestinationIp.length()+realDestinationPort.length()+ virtualIpAdress.length() + virtualPort.length();
        
        	byte[] bytes = new byte[addresslength + data.length()];
                
                
                
                String address =  relDestinationIp+realSourcePort+virtualIpAdress+virtualPort;
                
                byte[] addressInBytes = address.getBytes();
                byte[] messageInBytes = data.getBytes();
                
                /* Converts the bit strings into bytes and adds them
		 * to the byte array. */
		for (int i = 0; i < addresslength; i++) {
			
			bytes[i] = addressInBytes[i];
		}
		
		/* Adds the bytes of the message to the byte array. */
		for (int i = addresslength; i < bytes.length; i++) {
			bytes[i] = messageInBytes[i-addresslength];
		}
		
		return bytes;
    }
    
   public String getIp(byte[] data, int start){
       
     	String ip = "";
		
		for (int i = 0; i < 4; i++) {
			ip += "." + Integer.toString((int) (data[i + start] & 0xFF));
		}
		
        return ip.substring(1);
	
   }
   
   public String[] receiveMessage() {
	
       //To handle the recive of data
       return null;
   }
   
}
