package com.example.chatapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "chats")
public class Chat {

    @Id
    private String id;
    private String mainUser;
    private List<String> chattingWith;

    // Constructors, Getters, and Setters
    public Chat() {}

    public Chat(String mainUser, List<String> chattingWith) {
        this.mainUser = mainUser;
        this.chattingWith = chattingWith;
    }

    public String getId() { return id; }
    public String getMainUser() { return mainUser; }
    public List<String> getChattingWith() { return chattingWith; }
}
