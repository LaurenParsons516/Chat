package com.laurenparsons516.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiveMessage implements Runnable {
    private Socket clientSocket;

    public ReceiveMessage(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String nextLine = in.readLine();
            while (nextLine != null) {
                System.out.println(nextLine);
                nextLine = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
