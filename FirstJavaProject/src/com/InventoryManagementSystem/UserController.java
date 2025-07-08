package com.InventoryManagementSystem;
import java.util.*;

public class UserController {
    List<User> listUser;

    public void addUser(User user){
        listUser.add(user);
    }

    public void removeUser(User user){
        listUser.remove(user);
    }

}
