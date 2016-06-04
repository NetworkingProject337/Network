/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author Shivanagesh Chandra
 */
public class Router {

    private int port;
    private InetAddress IPaddress;

    private HashMap<String, InetAddress> addressMap;

    private int routerNumber;

    private DatagramSocket routerSocket;

    public Router(int port, InetAddress IPaddress, int routerNumber) throws SocketException, Exception {
        this.port = port;
        this.IPaddress = IPaddress;
        this.routerNumber = routerNumber;

        routerSocket = new DatagramSocket(port, IPaddress);
    }

    public Router(int port) throws SocketException, Exception {
        this.port = port;
        routerSocket = new DatagramSocket(port, IPaddress);
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

        SocketAddress address = recvPacket.getSocketAddress();
        
        InetAddress add2 = recvPacket.getAddress();
        
        InetAddress dest = InetAddress.getByName("172.21.70.52");
 
        System.out.println("network.Router.handlePacket() Socket address" + address);
        System.out.println("network.Router.handlePacket() address "+ add2 );
        char targetMachine = (char) (data[0] & 0xFF);
        System.out.println("this packet should be sent to machine number: " + targetMachine);
        String dataRec = new String(data, "UTF-8").trim();
        System.out.println("network.Router.handlePacket() data "+ new String(data, "UTF-8").trim());
        
        String dataToSent = dataRec.substring(1);
        byte[] dataToSentBytes = dataToSent.getBytes();
        System.out.println("network.Router.handlePacket() data to be sent"+dataToSent);
        
        
        DatagramPacket sendPkt = new DatagramPacket(dataToSentBytes, dataToSentBytes.length, 
				dest, 4108);

        /* Forwards the packet */
        routerSocket.send(sendPkt);
        //System.out.println("Sent message to " + realDstIP.getHostAddress());
    }

    private DatagramPacket receivePacket() throws IOException {
        byte[] buffer = new byte[65535];

        DatagramPacket recvPacket = new DatagramPacket(buffer, buffer.length);
        routerSocket.receive(recvPacket);

        return recvPacket;
    }

    public static void main(String[] args) {

        System.out.print("Please input router number: ");

        int router_number = 0;

        Scanner scan = new Scanner(System.in);
        router_number = scan.nextInt();
        scan.close();

        System.out.println();

        Router router = null;

        try {
            router = new Router(4106);
        } catch (SocketException se) {
            String message = "Port in use";
            System.err.println(message);
        } catch (Exception se) {
            System.err.println(se.getMessage());

        }

        if (router != null) {
            router.begin();
        }

    }

}
