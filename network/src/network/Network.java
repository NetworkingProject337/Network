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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Shivanagesh Chandra
 */
public class Network {

    /**
     * These are real IP address of computer which is running router program
     */
    protected static String srcId;
    protected static String srcPort;

    protected String routerIp;
    protected String routerPort;

    protected DatagramSocket socket;

    public Network(String routerIp, String routerPort, String srcPort) {

        this.routerIp = routerIp;
        this.routerPort = routerPort;
        this.srcPort = srcPort;
        try {
            socket = new DatagramSocket(Integer.parseInt(srcPort));
        } catch (Exception ex) {
            System.out.println("network.Network.<init>() :  Socket is alredy in use");
        }

        getIdFromRouter();

    }

    public void getIdFromRouter() {

        try {
            String reg = "RegClientToRouter#$";
            byte[] data = reg.getBytes();
            InetAddress router = InetAddress.getByName(routerIp);
            DatagramPacket theOutput
                    = new DatagramPacket(data, data.length, router, Integer.parseInt(routerPort));
            socket.send(theOutput);

        } catch (Exception ex) {
            System.err.println("Error in while registring with router");
        }

    }

    public void regRespnseFromRouter(DatagramPacket recvPacket) {

        byte[] data = recvPacket.getData();

        try {
            String reponseString = new String(data, "UTF-8").trim();
            List<String> reponse = new ArrayList<String>(Arrays.asList(reponseString.split("#$")));
            srcId = reponse.get(1);
            
        } catch (IOException ex) {
            System.err.println(ex);
        }

    }

  

    public byte[] BuildPacket(String data, String destinationId, String destinationPort) {

        int addresslength = destinationId.length() + destinationPort.length() + srcId.length() + srcPort.length();

        byte[] bytes = new byte[addresslength + data.length()];

        String address = relDestinationIp + realSourcePort + virtualIpAdress + virtualPort;

        byte[] addressInBytes = address.getBytes();
        byte[] messageInBytes = data.getBytes();

        /* Converts the bit strings into bytes and adds them
		 * to the byte array. */
        for (int i = 0; i < addresslength; i++) {

            bytes[i] = addressInBytes[i];
        }

        /* Adds the bytes of the message to the byte array. */
        for (int i = addresslength; i < bytes.length; i++) {
            bytes[i] = messageInBytes[i - addresslength];
        }

        return bytes;
    }

    public String getIp(byte[] data, int start) {

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
