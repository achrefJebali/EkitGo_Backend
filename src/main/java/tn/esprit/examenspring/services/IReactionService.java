package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Chatroom;
import tn.esprit.examenspring.entities.Reaction;

import java.util.List;

public interface IReactionService {
    List<Reaction> findAll();
    Reaction findById(int id);
    Reaction save(Reaction reaction);
    void deleteById(int id);}
