package com.company.Objects;

import java.util.Objects;

public class User {
    private String login, password;
    private static Integer ids = 0;
    private Integer id;

    public String getLogin() {
        return login;
    }

    public Integer getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public User(String login, String password) {
        id = ids;
        ids++;
        this.login = login;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(login, user.login) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
