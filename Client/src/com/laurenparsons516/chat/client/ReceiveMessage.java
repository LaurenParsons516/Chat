package com.laurenparsons516.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ReceiveMessage implements Runnable {
    private Socket clientSocket;

    public ReceiveMessage(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message = in.readLine();
            while (message != null) {
                if (message.startsWith("|file download|")) {
                    String[] splitMessage = message.split("\\|");
                    String fileName = splitMessage[2];
                    byte[] fileContent = Base64.getDecoder().decode(splitMessage[3]);
                    Path downloadsFolder = Paths.get("Downloads");
                    Files.createDirectories(downloadsFolder);
                    Files.write(downloadsFolder.resolve(fileName), fileContent);
                    System.out.println("Downloaded " + fileName);
                } else {
                    System.out.println(message);
                }
                message = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
