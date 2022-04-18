package com.company;

import java.io.Serializable;

public class Command implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private Object[] args;

    public String getName() {
        return name;
    }

    public Object[] getArgs() {
        return args;
    }

    private String login, password;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
