package updWork;

import java.io.*;
import java.net.*;

public class UPDServer {
    private DatagramSocket socket;

    public UPDServer(int serverPort) {
        try {
            socket = new DatagramSocket(serverPort);
            System.out.println("Server start...");
        } catch (SocketException e) {
            System.out.println("Error: " + e);
        }
    }

    public void work(int bufferSize) {
        try {
            byte[] buffer = new byte[bufferSize];
            DatagramPacket packet;

            while (true) {
                packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                InetAddress address = packet.getAddress();
                int port = packet.getPort();

                System.out.println("Request from: " + address.getHostAddress() + " port: " + port);

                User usr = new User(address, port);
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bout);
                out.writeObject(usr);
                buffer = bout.toByteArray();

                packet = new DatagramPacket(buffer, buffer.length, address, port);
                socket.send(packet);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
        } finally {
            System.out.println("Server end...");
            socket.close();
        }
    }

    public static void main(String[] args) {
        new UPDServer(1501).work(256);
    }
}
