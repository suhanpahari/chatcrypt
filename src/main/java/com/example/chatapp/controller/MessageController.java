package com.example.chatapp.controller;

import com.example.chatapp.model.Message;
import com.example.chatapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:5500")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    // Endpoint to send a message
    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        message.setTimestamp(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);
        return new ResponseEntity<>(savedMessage, HttpStatus.CREATED);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Message>> getMessages(@RequestParam String receiver, @RequestParam String sender) {
        List<Message> messagesFromSenderToReceiver = messageRepository.findBySenderAndReceiver(sender, receiver);
        List<Message> messagesFromReceiverToSender = messageRepository.findBySenderAndReceiver(receiver, sender);

        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(messagesFromSenderToReceiver);
        allMessages.addAll(messagesFromReceiverToSender);

        allMessages.sort(Comparator.comparing(Message::getTimestamp));

        return new ResponseEntity<>(allMessages, HttpStatus.OK);
    }
}
