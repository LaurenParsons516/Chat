package com.laurenparsons516.chat.client;

import jdk.swing.interop.SwingInterOpUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLOutput;
import java.util.Scanner;

public class ChatClient {

    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket clientSocket = new Socket("localhost", 2222);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        Scanner reader = new Scanner(System.in);
        new Thread(new ReceiveMessage(clientSocket)).start();
        while (!clientSocket.isClosed()) {
            out.println(reader.nextLine());
        }
    }
}
