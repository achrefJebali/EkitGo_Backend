package tn.esprit.examenspring.services;

import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.ChatroomRepository;
import tn.esprit.examenspring.Repository.MessageRepository;
import tn.esprit.examenspring.entities.Chatroom;
import tn.esprit.examenspring.entities.Message;
import tn.esprit.examenspring.exceptions.ResourceNotFoundException;

import java.util.List;

@Service
public class MessageServiceImpl implements IMessageService{
    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message findById(int id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id " + id));
    }
    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public void deleteById(int id) {
        messageRepository.deleteById(id);
    }

}
