package tn.esprit.examenspring.controllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.Chatroom;
import tn.esprit.examenspring.exceptions.ResourceNotFoundException;
import tn.esprit.examenspring.services.IChatroomService;

import java.util.List;
@RestController
@RequestMapping("/Chatroom")
public class ChatroomRestController {

    private final IChatroomService chatroomService;

    public ChatroomRestController(IChatroomService chatroomService) {
        this.chatroomService = chatroomService;
    }

    @GetMapping("/retrieve-all-chatrooms")
    public List<Chatroom> getAllChatrooms() {
        return chatroomService.findAll();
    }

    @GetMapping("/{id}")
    public Chatroom getChatroomById(@PathVariable Long id) {
        return chatroomService.findById(id);

    }
    @PostMapping("/add-chatroom")
    public Chatroom createChatroom(@RequestBody Chatroom chatroom) {
        return chatroomService.save(chatroom);
    }

    @PutMapping("/modify-chatroom")
    public Chatroom updateChatroom(@PathVariable Long id, @RequestBody Chatroom updatedChatroom) {
        Chatroom existingChatroom = chatroomService.findById(id);

        existingChatroom.setName(updatedChatroom.getName());
        return chatroomService.save(existingChatroom);
    }
    @DeleteMapping("/{id}")
    public void deleteChatroom(@PathVariable Long id) {
        chatroomService.deleteById(id);
    }
}
