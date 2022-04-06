package com.laurenparsons516.chat.server;

import java.util.ArrayList;
import java.util.List;

public class ChatChannel {
    private List<ChatClient> chatClients = new ArrayList<>();

    public void addClient(ChatClient client) {
        chatClients.add(client);
    }

    public void removeClient(ChatClient client) {
        chatClients.remove(client);
    }

    public void sendMessage(ChatClient messenger, String message) {
        message = messenger.getClientName() + ": " + message;
        for (ChatClient client : chatClients) {
            if (client != messenger) {
                client.sendMessage(message);
            }
        }
    }
}
