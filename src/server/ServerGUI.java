package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import interfaces.Executable;
import interfaces.Result;

public class ServerGUI extends JFrame {
    private JTextArea logArea;
    private JButton startServerButton, stopServerButton, exitButton;
    private JTextField portField;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private boolean running = false;

    public ServerGUI() {
        setTitle("TCP Server");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel portLabel = new JLabel("Working Port:");
        portField = new JTextField(10);
        topPanel.add(portLabel);
        topPanel.add(portField);
        add(topPanel, BorderLayout.NORTH);

        logArea = new JTextArea(15, 30);
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        startServerButton = new JButton("Start Server");
        stopServerButton = new JButton("Stop Server");
        exitButton = new JButton("Exit Server");
        buttonPanel.add(startServerButton);
        buttonPanel.add(stopServerButton);
        buttonPanel.add(exitButton);
        add(buttonPanel, BorderLayout.SOUTH);

        startServerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });

        stopServerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopServer();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private void startServer() {
        int port = Integer.parseInt(portField.getText());
        logArea.append("The server is waiting for connections on port " + port + "...\n");
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                running = true;
                while (running) {
                    clientSocket = serverSocket.accept();
                    logArea.append("Connection started...\n");

                    new Thread(new ClientHandler(clientSocket)).start();
                }
            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    stopServer();
                }
            }
        }).start();
    }


    private void stopServer() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
                logArea.append("Server stopped.\n");
            }
        } catch (IOException e) {
            logArea.append("Error stopping server: " + e.getMessage() + "\n");
        }
    }



    class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                 ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

                Executable task = (Executable) in.readObject();
                double startTime = System.nanoTime();
                Object result = task.execute();
                double endTime = System.nanoTime();

                Result res = new ResultImpl(result, endTime - startTime);
                out.writeObject(res);

                logArea.append("Connection 1 [WORK DONE]\n");
                logArea.append("Connection 1 result sent. Finish connection.\n");
            } catch (IOException | ClassNotFoundException e) {
                logArea.append("Error: " + e.getMessage() + "\n");
            }
        }
    }

    public static void main(String[] args) {
        new ServerGUI();
    }
}