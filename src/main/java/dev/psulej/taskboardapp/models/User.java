package dev.psulej.taskboardapp.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class User {
    @Id
    private Long id;

    @Field
    private String login;

    @Field
    private String password;

    public User(Long id, String name, String surname) {
        this.id = id;
        this.login = name;
        this.password = surname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + login + '\'' +
                ", surname='" + password + '\'' +
                '}';
    }
}
