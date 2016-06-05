/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketAddress;

/**
 *
 * @author Shivanagesh Chandra
 */
public class Client extends Network {

     public Client(String routerIp,String routerPort, String port){
         super(routerIp,routerPort,port);
     }
     
     public void sendMessage(){
         String str ="Hey";
         
         byte []data = BuildPacket(str, "4", "45455");
         try{
        InetAddress router = InetAddress.getByName(routerIp);
            DatagramPacket theOutput
                    = new DatagramPacket(data, data.length, router, Integer.parseInt(routerPort));
            socket.send(theOutput);
         }catch(Exception ex){
             System.out.println("network.Client.sendMessage()"+ex.getMessage());
         }
     }
     
     public void begin() {
        while (true) {
            try {
                handlePacket();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                continue;
            }
        }

    }

    private void handlePacket() throws IOException {
        DatagramPacket recvPacket = receivePacket();
        byte[] data = recvPacket.getData();
        
        //Checking router packet
        if((char) (data[0] & 0xFF) == 'R' && (char) (data[0] & 0xFF) == 'e' && (char) (data[0] & 0xFF) == 'g'){
            regRespnseFromRouter(recvPacket);
            return;
        }
        
        String  dataInString = receiveMessage(recvPacket);

    }

    private DatagramPacket receivePacket() throws IOException {
        byte[] buffer = new byte[65535];

        DatagramPacket recvPacket = new DatagramPacket(buffer, buffer.length);
        socket.receive(recvPacket);

        return recvPacket;
    }
    
}
