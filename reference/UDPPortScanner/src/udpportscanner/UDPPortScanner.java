/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udpportscanner;

import java.net.*;

/**
 *
 * @author Navid
 */
public class UDPPortScanner {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for (int port = 1024; port <= 65535; port++) {
            try {
                // the next line will fail and drop into the catch block if
                // there is already a server running on port i
                DatagramSocket server = new DatagramSocket(port);
                server.close();
            } catch (SocketException ex) {
                System.out.println("There is a server on port " + port + ".");
            } 
        }
    }
    
}
