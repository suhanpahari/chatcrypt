package com.example.chatapp.repository;

import com.example.chatapp.model.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, Long> {
    List<Chat> findByMainUser(String mainUser);
}
