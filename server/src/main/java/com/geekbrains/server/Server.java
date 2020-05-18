package com.geekbrains.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    private Vector<Client> clients;
    private AuthService authService;

    public AuthService getAuthService() {
        return authService;
    }

    public Server() {
        clients = new Vector<>();
        authService = new SimpleAuthService();
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            System.out.println("Сервер запущен на порту 8189");
            while (true) {
                Socket socket = serverSocket.accept();
                new Client(this, socket);
                System.out.println("Подключился новый клиент");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Сервер завершил свою работу");
    }

    public void broadcastMsg(String msg) {
        for (Client client : clients) {
            client.sendMsg(msg);
        }
    }

    public void privateMsg(Client sender, String receiverNick, String msg) {
        if (sender.getNickname().equals(receiverNick)) {
            sender.sendMsg("заметка для себя: " + msg);
            return;
        }
        for (Client client : clients) {
            if (client.getNickname().equals(receiverNick)) {
                client.sendMsg("от " + sender.getNickname() + ": " + msg);
                sender.sendMsg("для " + receiverNick + ": " + msg);
                return;
            }
        }
        sender.sendMsg("Клиент " + receiverNick + " не найден");
    }

    public void subscribe(Client clientHandler) {
        clients.add(clientHandler);
        broadcastClientsList();
    }

    public void unsubscribe(Client clientHandler) {
        clients.remove(clientHandler);
        broadcastClientsList();
    }

    public boolean isNickBusy(String nickname) {
        for (Client client : clients) {
            if (client.getNickname().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    public void broadcastClientsList() {
        StringBuilder stringBuilder = new StringBuilder(15 * clients.size());
        stringBuilder.append("/clients ");
        for (Client client : clients) {
            stringBuilder.append(client.getNickname()).append(" ");
        }

        stringBuilder.setLength(stringBuilder.length() - 1);

        String out = stringBuilder.toString();
        for (Client client : clients) {
            client.sendMsg(out);
        }
    }
}
