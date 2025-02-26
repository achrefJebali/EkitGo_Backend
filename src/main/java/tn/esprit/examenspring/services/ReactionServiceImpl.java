package tn.esprit.examenspring.services;

import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.ChatroomRepository;
import tn.esprit.examenspring.Repository.ReactionRepository;
import tn.esprit.examenspring.entities.Chatroom;
import tn.esprit.examenspring.entities.Reaction;
import tn.esprit.examenspring.exceptions.ResourceNotFoundException;

import java.util.List;

@Service
public class ReactionServiceImpl implements IReactionService{
    private final ReactionRepository reactionRepository;

    public ReactionServiceImpl(ReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    @Override
    public List<Reaction> findAll() {
        return reactionRepository.findAll();
    }

    @Override
    public Reaction findById(int id) {
        return reactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reaction not found with id " + id));
    }
    @Override
    public Reaction save(Reaction reaction) {
        return reactionRepository.save(reaction);
    }

    @Override
    public void deleteById(int id) {
        reactionRepository.deleteById(id);
    }

}
