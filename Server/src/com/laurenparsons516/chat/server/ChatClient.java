package com.laurenparsons516.chat.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;


public class ChatClient implements Runnable {

    private Socket clientSocket;
    private PrintStream clientOutput;
    private Scanner clientInput;
    private ChatChannel currentChannel;
    private String clientName;

    public ChatClient(Socket clientSocket, ChatChannel currentChannel)
    {
        try
        {
            this.clientSocket = clientSocket;
            this.currentChannel = currentChannel;
            this.clientOutput = new PrintStream(this.clientSocket.getOutputStream());
            this.clientInput = new Scanner(this.clientSocket.getInputStream());
        }
        catch (Exception e)
        {
            System.err.println("Bad things happened in thread!!!!!");
            e.printStackTrace();
        }

    }

    public String getClientName() {
        return clientName;
    }

    public void sendMessage(String message) {
        this.clientOutput.println(message);
    }

    public void disconnectClient() throws IOException {
        currentChannel.sendMessage(this, "disconnected");
        currentChannel.removeClient(this);
        clientSocket.close();
    }


    @Override
    public void run() {
        this.clientOutput.println("What is your name?");
        clientName = clientInput.nextLine();
        System.out.println("read: " + clientName);
        while (!clientSocket.isClosed()) {
            String message = clientInput.nextLine();
            if (message.equalsIgnoreCase("/leave")) {
                try {
                    disconnectClient();
                } catch (IOException e) {
                    sendMessage("An error occurred when trying to leave");
                }
                continue;
            }
            currentChannel.sendMessage(this, message);
        }
    }
}
