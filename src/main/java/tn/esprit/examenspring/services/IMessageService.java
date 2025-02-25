package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Chatroom;
import tn.esprit.examenspring.entities.Message;

import java.util.List;

public interface IMessageService {
    List<Message> findAll();
    Message findById(int id);
    Message save(Message message);
    void deleteById(int id);}
