package sample;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.PublicKey;
import java.util.Hashtable;

public class UDPModel implements Runnable {

    private DatagramSocket udpSocket = null;
    private UDPEvent event;

    private int port = 8989;

    public UDPModel(UDPEvent event){
        this.event = event;
    }

    @Override
    public void run() {
        try {
            if (udpSocket == null) {
                udpSocket = new DatagramSocket();
            }
            receiveDataViaUDP();
        } catch (SocketException e) {
            System.out.println("UDP:" + "Socket Error:" + e);
        }
    }

    public void receiveDataViaUDP() {
        try {
            try {
                DatagramSocket receiverSocket = new DatagramSocket(port);
                System.out.println("UDP is started listening!");
                while (true) {

                    byte[] buff = new byte[100];

                    DatagramPacket receivedPacket = new DatagramPacket(buff, buff.length);
                    receiverSocket.setBroadcast(true);
                    receiverSocket.receive(receivedPacket);

                    System.out.println("Received data from: " + receivedPacket.getAddress());

                    if(receivedPacket.getLength() > 0){
                        event.onMessageReceived(new String(receivedPacket.getData()));


                    }
                }
            } catch (Exception e){
                System.out.println("Exception: " + e);
            }

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }


    public void sendDataViaUDP(String ip, String message) {
        try {
                if (udpSocket == null) {
                    udpSocket = new DatagramSocket();
                }

                byte[] buffer = (message).getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ip), port);
                udpSocket.send(packet);
        } catch (Exception e) {
            System.out.println("EXCEPTION: Probably source/destination IP is not available. "+e.getLocalizedMessage());
        }
    }

    public void close() {
        if (udpSocket != null) {
            udpSocket.close();
        }
    }

    public interface UDPEvent{
        void onMessageReceived(String message) throws IOException, ClassNotFoundException;
    }
}
