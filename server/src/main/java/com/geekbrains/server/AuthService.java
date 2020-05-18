package com.geekbrains.server;

//Создание интерфейса для аутентификации
public interface AuthService {

    String getNickname(String login, String password);
    // изменение ника
    boolean changeNickname(String currentNickname, String newNickname);
}
