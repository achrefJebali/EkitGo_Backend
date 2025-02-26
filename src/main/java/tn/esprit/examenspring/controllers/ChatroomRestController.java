package tn.esprit.examenspring.controllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.Chatroom;
import tn.esprit.examenspring.services.IChatroomService;

import java.util.List;

@RestController
@RequestMapping("/chatrooms")
@CrossOrigin(origins = "http://localhost:4200") // ✅ Enable CORS for Angular frontend
public class ChatroomRestController {

    private final IChatroomService chatroomService;

    // Constructor Injection (recommended)
    public ChatroomRestController(IChatroomService chatroomService) {
        this.chatroomService = chatroomService;
    }

    // Retrieve all chatrooms
    @GetMapping("/retrieve-all-chatrooms")
    public List<Chatroom> getAllChatrooms() {
        return chatroomService.findAll();
    }

    // Retrieve chatroom by ID
    @GetMapping("/{id}")
    public Chatroom getChatroomById(@PathVariable int id) {
        return chatroomService.findById(id);
    }

    // Add a new chatroom
    @PostMapping("/add-chatroom")
    public Chatroom createChatroom(@RequestBody Chatroom chatroom) {
        return chatroomService.save(chatroom);
    }

    // Update an existing chatroom
    @PutMapping("/modify-chatroom/{id}") // Corrected the URL path
    public Chatroom updateChatroom(@PathVariable int id, @RequestBody Chatroom updatedChatroom) {
        Chatroom existingChatroom = chatroomService.findById(id);

        existingChatroom.setName(updatedChatroom.getName());
        return chatroomService.save(existingChatroom);
    }

    // Delete a chatroom
    @DeleteMapping("/{id}")
    public void deleteChatroom(@PathVariable int id) {
        chatroomService.deleteById(id);
    }
}
