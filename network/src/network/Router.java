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
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Shivanagesh Chandra
 */
public class Router {

    public static AtomicInteger nextId = new AtomicInteger();
    public static AtomicInteger routerNextId = new AtomicInteger();

    private int port;
    private InetAddress IPaddress;

    private HashMap<Integer, InetAddress> addressMap;

    private int routerNumber;

    private DatagramSocket routerSocket;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public HashMap<Integer, InetAddress> getAddressMap() {
        return addressMap;
    }

    public void setAddressMap(HashMap<Integer, InetAddress> addressMap) {
        this.addressMap = addressMap;
    }

    public int getRouterNumber() {
        return routerNumber;
    }

    public void setRouterNumber(int routerNumber) {
        this.routerNumber = routerNumber;
    }

    public DatagramSocket getRouterSocket() {
        return routerSocket;
    }

    public void setRouterSocket(DatagramSocket routerSocket) {
        this.routerSocket = routerSocket;
    }

    public Router(int port, String IPaddress) throws SocketException, Exception {
        this.port = port;
        try {
            this.IPaddress = InetAddress.getByName(IPaddress);
        } catch (UnknownHostException ex) {
            System.err.println("Host not found " + ex.getMessage());
        }
        this.routerNumber = routerNextId.incrementAndGet();
        
        addressMap = new HashMap<Integer, InetAddress>();

        routerSocket = new DatagramSocket(port, this.IPaddress);
    }

    public Router(int port) throws SocketException, Exception {
        this.port = port;
         addressMap = new HashMap<Integer, InetAddress>();
        routerSocket = new DatagramSocket(port);
    }

    public void begin() {
        while (true) {
            try {
                for (Integer name : addressMap.keySet()) {
                    String key = name.toString();
                    String value = addressMap.get(name).toString();
                    System.out.println(key + " " + value);

                }
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
        //Checking whether is registration packet
        String str = new String(data, "UTF-8").trim();
        if (str.equals("RegClientToRouter#$")) {
            registration(recvPacket);
        }else{
            //Get destination address 
             String detstinationId = ""+(char) (data[0] & 0xFF)+(char) (data[1] & 0xFF);
             String detstinationPort = ""+(char) (data[2] & 0xFF)+(char) (data[3] & 0xFF)+(char) (data[4] & 0xFF)+(char) (data[5] & 0xFF)+(char) (data[6] & 0xFF);
             int destId = Integer.parseInt(detstinationId);
             int destPort = Integer.parseInt(detstinationPort);
              System.out.println("network.Router.handlePacket()"+destId);
              System.out.println("network.Router.handlePacket()"+addressMap.get(destId));
              System.out.println("network.Router.handlePacket()"+addressMap.get(destId));
             
              DatagramPacket theOutput
                = new DatagramPacket(data, data.length, addressMap.get(destId),destPort);
            routerSocket.send(theOutput);
        }

    }

    public void registration(DatagramPacket regPacket) throws IOException {

        InetAddress cliecntAdd = regPacket.getAddress();
        int clientId = nextId.incrementAndGet();
        addressMap.put(clientId, cliecntAdd);
        String responseString ;
        if(clientId < 10){
             responseString = "RegRouterToClient#$0" + clientId;
        }else{
             responseString = "RegRouterToClient#$" + clientId;
        }
        byte[] reponseBytes = responseString.getBytes();
        System.out.println("network.Router.registration() client address "+ cliecntAdd + " "+ regPacket.getPort());
        DatagramPacket theOutput
                = new DatagramPacket(reponseBytes, reponseBytes.length, cliecntAdd, regPacket.getPort());
        routerSocket.send(theOutput);

    }

    private DatagramPacket receivePacket() throws IOException {
        byte[] buffer = new byte[1024];

        DatagramPacket recvPacket = new DatagramPacket(buffer, buffer.length);
        routerSocket.receive(recvPacket);

        return recvPacket;
    }

    public static void main(String[] args) {

        System.out.print("Please router number port number: ");

        int port_number = 0;

        Scanner scan = new Scanner(System.in);
        port_number = scan.nextInt();
        scan.close();

        System.out.println();

        Router router = null;

        try {
            router = new Router(port_number);
            System.out.print("Router " + router.getRouterNumber() + " is up and running :D");
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
