package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Chatroom;

import java.util.List;

public interface IChatroomService {
    List<Chatroom> findAll();
    Chatroom findById(Long id);
    Chatroom save(Chatroom chatRoom);
    void deleteById(Long id);}
