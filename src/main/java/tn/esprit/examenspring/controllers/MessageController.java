package tn.esprit.examenspring.controllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.Chatroom;
import tn.esprit.examenspring.entities.Message;
import tn.esprit.examenspring.services.IChatroomService;
import tn.esprit.examenspring.services.IMessageService;

import java.util.List;

@RestController
@RequestMapping("/Message")
public class MessageController {
    public IMessageService messageService;

    public MessageController(IMessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/retrieve-all-messages")
    public List<Message> getAllMessages() {
        return messageService.findAll();
    }

    @GetMapping("/{id}")
    public Message getMessageById(@PathVariable int id) {
        return messageService.findById(id);

    }
    @PostMapping("/add-message")
    public Message createMessage(@RequestBody Message message) {
        return messageService.save(message);
    }

    @PutMapping("/modify-message")
    public Message updateMessage(@PathVariable int id, @RequestBody Message updatedMessage) {
        Message existingMessage = messageService.findById(id);

        existingMessage.setMessageStatus(updatedMessage.getMessageStatus());
        return messageService.save(existingMessage);
    }
    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable int id) {
        messageService.deleteById(id);
    }
}
