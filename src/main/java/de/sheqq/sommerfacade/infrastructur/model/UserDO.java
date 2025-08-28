package de.sheqq.sommerfacade.infrastructur.model;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;


public class UserDO {
    @NotBlank
    private String id;
    @NotBlank
    private String username;
    private LocalDateTime[] member;
    private Integer level;
    private String color;

    public UserDO() {}
    public UserDO(String id, String username) {
        this.id = id;
        this.username = username;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) { this.id = id; }


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