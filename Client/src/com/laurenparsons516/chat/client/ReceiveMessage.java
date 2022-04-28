package com.laurenparsons516.chat.client;

import com.turn.ttorrent.client.SimpleClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ReceiveMessage implements Runnable {
    private Socket clientSocket;
    private SimpleClient downloadClient;

    public ReceiveMessage(Socket clientSocket, SimpleClient downloadClient) {
        this.clientSocket = clientSocket;
        this.downloadClient = downloadClient;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message = in.readLine();
            while (message != null) {
                if (message.startsWith("|torrent download|")) {
                    String[] splitMessage = message.split("\\|");
                    String fileName = splitMessage[2];
                    byte[] torrentFileBytes = Base64.getDecoder().decode(splitMessage[3]);
                    Path downloadsFolder = Paths.get("Downloads");
                    Files.createDirectories(downloadsFolder);
                    Path torrentFile = Paths.get("torrents", fileName + ".torrent");
                    Files.write(torrentFile, torrentFileBytes);
                    downloadClient.downloadTorrentAsync(torrentFile.toString(), downloadsFolder.toRealPath().toString(), InetAddress.getLocalHost());
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
