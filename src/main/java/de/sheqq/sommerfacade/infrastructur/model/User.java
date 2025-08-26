package de.sheqq.sommerfacade.infrastructur.model;

import java.time.LocalDateTime;

public class User {
    private Integer id;
    private String email;
    private String username;
    private LocalDateTime[] member;
    private Integer level;
    private String color;

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime[] getMember() {
        return member;
    }

    public void setMember(LocalDateTime[] member) {
        this.member = member;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}