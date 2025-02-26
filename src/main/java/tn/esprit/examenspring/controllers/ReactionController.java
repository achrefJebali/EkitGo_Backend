package tn.esprit.examenspring.controllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.Chatroom;
import tn.esprit.examenspring.entities.Reaction;
import tn.esprit.examenspring.services.IChatroomService;
import tn.esprit.examenspring.services.IReactionService;

import java.util.List;

@RestController
@RequestMapping("/Reaction")
public class ReactionController {
    public IReactionService reactionService;

    public ReactionController(IReactionService reactionService) {
        this.reactionService = reactionService;
    }

    @GetMapping("/retrieve-all-Reactions")
    public List<Reaction> getAllReactions() {
        return reactionService.findAll();
    }

    @GetMapping("/{id}")
    public Reaction getReactionById(@PathVariable int id) {
        return reactionService.findById(id);

    }
    @PostMapping("/add-reaction")
    public Reaction createReaction(@RequestBody Reaction reaction) {
        return reactionService.save(reaction);
    }

    @PutMapping("/modify-reaction")
    public Reaction updateReaction(@PathVariable int id, @RequestBody Reaction updatedReaction) {
        Reaction existingReaction = reactionService.findById(id);

        existingReaction.setEmoji(updatedReaction.getEmoji());
        return reactionService.save(existingReaction);
    }
    @DeleteMapping("/{id}")
    public void deleteReaction(@PathVariable int id) {
        reactionService.deleteById(id);
    }
}
