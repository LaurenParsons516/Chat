package com.laurenparsons516.chat.client;

import com.turn.ttorrent.client.SimpleClient;
import com.turn.ttorrent.common.creation.MetadataBuilder;
import jdk.swing.interop.SwingInterOpUtils;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public static void main(String[] args) throws IOException {
        Socket clientSocket = new Socket("localhost", 2222);
        SimpleClient downloadClient = new SimpleClient();
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        Scanner reader = new Scanner(System.in);
        new Thread(new ReceiveMessage(clientSocket, downloadClient)).start();
        while (!clientSocket.isClosed()) {
            String message = reader.nextLine();
            if (message.startsWith("/upload")) {
                Path filePath = openFilePicker();
                if (filePath != null) {
                    Path torrentFile = Paths.get("torrents", filePath.getFileName() + ".torrent");
                    byte[] torrentFileBytes = new MetadataBuilder()
                            .setTracker("http://" + clientSocket.getInetAddress().getHostAddress() + ":6969")
                            .addFile(filePath.toFile())
                            .buildBinary();
                    Files.createDirectories(torrentFile.getParent());
                    Files.write(torrentFile, torrentFileBytes);
                    downloadClient.downloadTorrentAsync(torrentFile.toString(), filePath.getParent().toRealPath().toString(), InetAddress.getLocalHost());
                    String encodedTorrentFile = Base64.getEncoder().encodeToString(torrentFileBytes);
                    out.println("|torrent upload|" + filePath.getFileName() + "|" + encodedTorrentFile);
                }
            } else {
                out.println(message);
            }
        }
    }
}
