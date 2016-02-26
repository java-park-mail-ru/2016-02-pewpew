package com.pewpew.pewpew.additional;

import com.pewpew.pewpew.model.User;
import org.jetbrains.annotations.NotNull;


public class Validate {

    @NotNull
    public static boolean userRegister(User user) {
        if (user.getEmail() == null) return false;
        if (user.getEmail().isEmpty()) return false;
        if (user.getLogin().isEmpty()) return false;
        if (user.getLogin() == null) return false;
        if (user.getPassword().isEmpty()) return false;
        return user.getPassword() != null;
    }

    public static boolean userAuth(User user) {
        if (user.getLogin().isEmpty()) return false;
        if (user.getLogin() == null) return false;
        if (user.getPassword().isEmpty()) return false;
        return user.getPassword() != null;
    }
}
