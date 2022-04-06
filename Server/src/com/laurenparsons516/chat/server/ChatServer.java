package com.laurenparsons516.chat.server;

import java.io.PrintStream;
import java.net.*;
import java.util.Scanner;

public class ChatServer
{
    public static void main(String[] args) throws Exception
    {
        ServerSocket s = new ServerSocket(2222);
        ChatChannel channel = new ChatChannel();
        while (true) {
            System.out.println("Listening for Connection...");
            Socket clientSocket = s.accept(); //blocks
            System.out.println("Connection Established...");
            ChatClient client = new ChatClient(clientSocket, channel);
            channel.addClient(client);
            client.sendMessage("Welcome!");
            new Thread(client).start();
        }
    }
}