package server;

import interfaces.Executable;
import interfaces.Result;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Server {
    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is waiting for connections...");
            Socket clientSocket = serverSocket.accept();
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            Executable task = (Executable) in.readObject();

            long startTime = System.nanoTime();
            Object result = task.execute();
            long endTime = System.nanoTime();

            Result res = new ResultImpl(result, endTime - startTime);
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(res);

            System.out.println("Task executed and result sent.");
        }
    }
}
