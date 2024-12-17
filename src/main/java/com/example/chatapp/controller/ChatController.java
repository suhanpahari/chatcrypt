package com.example.chatapp.controller;

import com.example.chatapp.model.Chat;
import com.example.chatapp.model.User;
import com.example.chatapp.repository.ChatRepository;
import com.example.chatapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/chat")
@CrossOrigin(origins = "http://localhost:5500") // Adjust based on your frontend server
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> createNewChat(@RequestParam String mainUserEmail, @RequestParam String chattingWithEmail) {
        try {
            Chat mainUserChat = chatRepository.findByMainUser(mainUserEmail)
                    .stream()
                    .findFirst()
                    .orElseGet(() -> new Chat(mainUserEmail, new ArrayList<>()));

            if (!mainUserChat.getChattingWith().contains(chattingWithEmail)) {
                mainUserChat.getChattingWith().add(chattingWithEmail);
            }

            Chat chattingWithUserChat = chatRepository.findByMainUser(chattingWithEmail)
                    .stream()
                    .findFirst()
                    .orElseGet(() -> new Chat(chattingWithEmail, new ArrayList<>()));

            if (!chattingWithUserChat.getChattingWith().contains(mainUserEmail)) {
                chattingWithUserChat.getChattingWith().add(mainUserEmail);
            }

            chatRepository.save(mainUserChat);
            chatRepository.save(chattingWithUserChat);

            return ResponseEntity.status(HttpStatus.CREATED).body("Chat created successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating chat: " + e.getMessage());
        }
    }


    @GetMapping("/contacts")
    @ResponseBody
    public ResponseEntity<?> getChatContacts(@RequestParam String mainUserEmail) {
        try {
            List<Chat> chats = chatRepository.findByMainUser(mainUserEmail);
            if (chats.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No chats found for this user");
            }

            Chat chat = chats.get(0);
            List<String> emails = chat.getChattingWith();
            List<Map<String, Object>> users = new ArrayList<>();

            for (String email : emails) {
                User user = userRepository.findByEmail(email);
                if (user != null) {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("id", user.getId());
                    userMap.put("email", user.getEmail());
                    userMap.put("username", user.getUsername());
                    users.add(userMap);
                }
            }

            return ResponseEntity.ok(users);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching chat contacts: " + e.getMessage());
        }
    }
}
