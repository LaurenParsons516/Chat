package com.laurenparsons516.chat.client;

import jdk.swing.interop.SwingInterOpUtils;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.Base64;
import java.util.Scanner;

public class ChatClient {

    public static Path openFilePicker() {
        JFileChooser chooser = new JFileChooser();
        int choice = chooser.showOpenDialog(null);

        if (choice != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        return chooser.getSelectedFile().toPath();
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket clientSocket = new Socket("localhost", 2222);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        Scanner reader = new Scanner(System.in);
        new Thread(new ReceiveMessage(clientSocket)).start();
        while (!clientSocket.isClosed()) {
            String message = reader.nextLine();
            if (message.startsWith("/upload")) {
                Path filePath = openFilePicker();
                if (filePath != null) {
                    byte[] fileContent = Files.readAllBytes(filePath);
                    String encodedPath = Base64.getEncoder().encodeToString(fileContent);
                    out.println("|file upload|" + filePath.getFileName() + "|" + encodedPath);
                }
            } else {
                out.println(message);
            }
        }
    }
}
