package com.laurenparsons516.chat.server;

import java.util.*;

public class ChatChannel {

    private Map<String, String> sharedTorrents = new HashMap<>();
    private List<ChatClient> chatClients = new ArrayList<>();

    public void addClient(ChatClient client) {
        chatClients.add(client);
    }

    public void removeClient(ChatClient client) {
        chatClients.remove(client);
    }


    public void sendMessage(ChatClient messenger, String message) {
        if (message.startsWith("|torrent upload|")) {
            String[] splitMessage = message.split("\\|");
            String fileName = splitMessage[2];
            String fileContent = splitMessage[3];
            sharedTorrents.put(fileName, fileContent);
            sendMessage(messenger, "uploaded torrent " + fileName);
        } else if (message.startsWith("/download")) {
            String[] splitMessage = message.split("/download ");
            String fileName = splitMessage[1];
            String fileContent = sharedTorrents.get(fileName);
            messenger.sendMessage("Downloading " + fileName);
            messenger.sendMessage("|torrent download|" + fileName + "|" +  fileContent);
        } else {
            message = messenger.getClientName() + ": " + message;
            for (ChatClient client : chatClients) {
                if (client != messenger) {
                    client.sendMessage(message);
                }
            }
        }
    }
}
