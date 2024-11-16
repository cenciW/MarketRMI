package server.controllers;

import entitites.Product;
import entitites.User;
import server.handlers.FileHandler;
import server.interfaces.IUserController;
import server.utils.Cache;

import java.io.File;
import java.rmi.RemoteException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

public class UserController implements IUserController {

    FileHandler fileHandler;

    public UserController() {
        fileHandler = new FileHandler(new File("src/server/database/usersList.txt").getAbsolutePath());
    }


    @Override
    public boolean login(User user) {
        Optional<User> userOptional = fileHandler.searchUserOnFile(user);
        boolean ret = false;
        if (userOptional.isEmpty()) {
            System.out.println("Cliente: " + user.getUsername() + " tentativa de login não autorizada");
            ret = false;
        } else {
            ret = true;
            System.out.println("Cliente: " + user.getUsername() + " entrou no servidor.");
            Cache.usersList.add(new User(user.getUsername(), user.getPassword(), user.getClientInterface()));
        }

        return ret;
    }
}
