package com.example.project;

public class Report {
    public String login;
    public String comment;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Report{" +
                "login='" + login + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
