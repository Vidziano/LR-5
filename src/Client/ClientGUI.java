package Client;

import interfaces.Result;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class ClientGUI extends JFrame {
    private JTextField ipAddressField, portField, numberField;
    private JTextArea resultArea;
    private JButton calculateButton, clearButton, exitButton;

    public ClientGUI() {
        setTitle("TCP Client");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        ipAddressField = new JTextField(10);
        portField = new JTextField(5);
        numberField = new JTextField(3);
        inputPanel.add(new JLabel("IP Address:"));
        inputPanel.add(ipAddressField);
        inputPanel.add(new JLabel("Port:"));
        inputPanel.add(portField);
        inputPanel.add(new JLabel("N:"));
        inputPanel.add(numberField);
        add(inputPanel, BorderLayout.NORTH);

        resultArea = new JTextArea(8, 40);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        calculateButton = new JButton("Calculate");
        clearButton = new JButton("Clear Result");
        exitButton = new JButton("Exit Program");
        buttonPanel.add(calculateButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);
        add(buttonPanel, BorderLayout.SOUTH);

        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateFactorial();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resultArea.setText("");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private void calculateFactorial() {
        try {
            int port = Integer.parseInt(portField.getText());
            String ipAddress = ipAddressField.getText();
            int number = Integer.parseInt(numberField.getText());
            Socket socket = new Socket(ipAddress, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(new JobOne(number));

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Result result = (Result) in.readObject();
            resultArea.setText("Connected to server\n" +
                    "Submitted a job for execution\n" +
                    "result = " + result.output() + ", time taken = " + result.scoreTime() + "ns");
            socket.close();
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ClientGUI();
    }
}
