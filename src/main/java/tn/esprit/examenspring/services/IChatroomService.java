package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Chatroom;

import java.util.List;

public interface IChatroomService {
    List<Chatroom> findAll();
    Chatroom findById(int id);
    Chatroom save(Chatroom chatroom);
    void deleteById(int id);}
