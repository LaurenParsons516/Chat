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
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        System.out.println(in.readLine());
        Scanner reader = new Scanner(System.in);
        out.println(reader.nextLine());
    }
}
