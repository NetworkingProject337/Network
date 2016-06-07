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
public class Client2 extends Network {

    public Client2(String routerIp, String routerPort, String port) {
        super(routerIp, routerPort, port);
        (new Thread(new receivePacket())).start();
    }

    public void sendMessage() {
        String str = "Hey hello";

        byte[] data = BuildPacket(str, "01", "04108");
        try {
            InetAddress router = InetAddress.getByName(routerIp);
            System.out.println(new String(data, "UTF-8").trim());
            DatagramPacket theOutput
                    = new DatagramPacket(data, data.length, router, Integer.parseInt(routerPort));
            socket.send(theOutput);
            System.out.println("Sent Message");
        } catch (Exception ex) {
            System.out.println("network.Client.sendMessage()" + ex.getMessage());
        }
    }

    class receivePacket implements Runnable {

        public void run() {
            while (true) {
                try {
                    handlePacket();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    continue;
                }
            }
        }

    }

   
    private void handlePacket() throws IOException {
        DatagramPacket recvPacket = receivePacket();
        byte[] data = recvPacket.getData();
        //System.out.println("network.Client.handlePacket() got packet form Router");
        //String reponseString = new String(data, "UTF-8").trim();
        //System.out.println("network.Client.handlePacket()" + reponseString);

        //Checking router packet
        if((char) (data[0] & 0xFF) == 'R' && (char) (data[1] & 0xFF) == 'e' && (char) (data[2] & 0xFF) == 'g'){
            regRespnseFromRouter(recvPacket);
            return;
        }
        
        String  dataInString = receiveMessage(recvPacket);
        System.out.println("Message "+dataInString);

    }

    private DatagramPacket receivePacket() throws IOException {
        byte[] buffer = new byte[65535];

        DatagramPacket recvPacket = new DatagramPacket(buffer, buffer.length);
        socket.receive(recvPacket);

        return recvPacket;
    }

    public static void main(String[] args) {

        Client2 c1 = new Client2("127.0.0.1", "04106", "04109");
        Client2 c2 = new Client2("127.0.0.1", "04106", "04108");
        try{
        Thread.sleep(300);
        }catch(Exception ex){
            System.out.println("network.Client2.main()"+ex.getMessage());
        }
        c1.sendMessage();
    }

}
