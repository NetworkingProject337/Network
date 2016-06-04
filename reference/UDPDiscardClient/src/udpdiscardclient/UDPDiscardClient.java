/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udpdiscardclient;

import java.net.*;
import java.io.*;

/**
 *
 * @author Navid
 */
public class UDPDiscardClient {

    public final static int PORT = 9;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String hostname = args.length > 0 ?  args[0] : "localhost";
    
        try (DatagramSocket theSocket = new DatagramSocket()) {
            InetAddress server = InetAddress.getByName(hostname);
            BufferedReader userInput 
                = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String theLine = userInput.readLine();
                if (theLine.equals(".")) break;
                byte[] data = theLine.getBytes();
                DatagramPacket theOutput 
                    = new DatagramPacket(data, data.length, server, PORT);
                theSocket.send(theOutput);
            } // end while
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
}
