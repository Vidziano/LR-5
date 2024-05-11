package updWork;

import java.io.*;
import java.net.*;

public class UDPClient {
    private InetAddress serverAddress;
    private int serverPort;

    public UDPClient(String address, int port) throws UnknownHostException {
        serverAddress = InetAddress.getByName(address);
        serverPort = port;
    }

    public void startClients(int numberOfClients) {
        for (int i = 0; i < numberOfClients; i++) {
            int clientPort = 57125 + i;
            try {
                DatagramSocket socket = new DatagramSocket(clientPort);
                socket.setSoTimeout(1000);
                communicateWithServer(socket, clientPort);
                socket.close();
            } catch (SocketException e) {
                System.out.println("Error with client on port " + clientPort + ": " + e);
            }
        }
    }

    private void communicateWithServer(DatagramSocket socket, int clientPort) {
        try {
            byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
            socket.send(packet);
            System.out.println("Client with port " + clientPort + " sending request");
            System.out.println("              |");
            System.out.println("              V");

            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            if (packet.getLength() == 0) return;

            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(packet.getData(), 0, packet.getLength()));
            User usr = (User) in.readObject();
            System.out.println("Registered User: Host name: " + usr.getAddress().getHostName() +
                    "      Host address: " + usr.getAddress().getHostAddress() + " Port: " + usr.getPort());
            System.out.println("------------------------------------------------------------------------------");
        } catch (SocketTimeoutException e) {
            System.out.println("Server is unreachable: " + e);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e);
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        new UDPClient("127.0.0.1", 1501).startClients(4);
    }
}
