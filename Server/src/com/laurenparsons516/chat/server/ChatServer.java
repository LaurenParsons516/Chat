package com.laurenparsons516.chat.server;

import java.io.PrintStream;
import java.net.*;
import java.util.Scanner;

public class ChatServer
{
    public static void main(String[] args) throws Exception
    {
        ServerSocket s = new ServerSocket(2222);
        System.out.println("Listenning for Connection...");
        Socket client = s.accept(); //blocks
        System.out.println("Connection Established...");
        PrintStream clientOutput = new PrintStream(client.getOutputStream());
        Scanner clientInput = new Scanner(client.getInputStream());
        clientOutput.println("What is your name?");
        String name = clientInput.nextLine();
        System.out.println("read: " + name);
    }
}