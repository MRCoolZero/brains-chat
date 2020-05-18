package com.geekbrains.server;

//authentication
public class DataAuth implements AuthService {
    @Override
    public String getNickname(String login, String password) {
        return DataBase.getUserNickName(login, password);
    }

    @Override
    public boolean changeNickname(String currentNickname, String newNickname) {
        return DataBase.changeUserNickname(currentNickname, newNickname);
    }
}
