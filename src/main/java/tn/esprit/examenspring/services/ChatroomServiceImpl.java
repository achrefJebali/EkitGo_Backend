package tn.esprit.examenspring.services;

import org.springframework.stereotype.Service;
import tn.esprit.examenspring.entities.Chatroom;
import tn.esprit.examenspring.exceptions.ResourceNotFoundException;
import tn.esprit.examenspring.Repository.ChatroomRepository;

import java.util.List;

@Service
public class ChatroomServiceImpl implements IChatroomService{
    private final ChatroomRepository chatroomRepository;

    public ChatroomServiceImpl(ChatroomRepository chatroomRepository) {
        this.chatroomRepository = chatroomRepository;
    }

    @Override
    public List<Chatroom> findAll() {
        return chatroomRepository.findAll();
    }

    @Override
    public Chatroom findById(int id) {
        return chatroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChatRoom not found with id " + id));
    }
    @Override
    public Chatroom save(Chatroom chatroom) {
        return chatroomRepository.save(chatroom);
    }

    @Override
    public void deleteById(int id) {
        chatroomRepository.deleteById(id);
    }

}
