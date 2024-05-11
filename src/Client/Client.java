package Client;

import interfaces.Result;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter server IP address:");
        String ipAddress = scanner.nextLine();

        System.out.println("Enter server port:");
        int port = scanner.nextInt();

        System.out.println("Enter a number to calculate factorial:");
        int number = scanner.nextInt();

        try (Socket socket = new Socket(ipAddress, port)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            JobOne job = new JobOne(number);
            out.writeObject(job);

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Result result = (Result) in.readObject();
            System.out.printf("Result: %s, Time Taken: %.2f ns%n", result.output(), result.scoreTime());
        } finally {
            scanner.close();
        }
    }
}
