package tn.esprit.examenspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.Comment;
import tn.esprit.examenspring.services.ICommentService;

import java.util.List;

@RestController
@RequestMapping("/comments")  // Nom de la ressource : "comments" (pluriel)
public class CommentController {

    @Autowired
    private ICommentService commentService;

    // Récupérer tous les commentaires
    @GetMapping("/retrieve-all-comments")
    public List<Comment> getComments() {
        return commentService.getComments();
    }

    // Ajouter un commentaire
    @PostMapping("/add-comment")
    public Comment addComment(@RequestBody Comment comment) {  // Utiliser "comment" comme nom de paramètre
        return commentService.addComment(comment);
    }

    // Supprimer un commentaire par ID
    @DeleteMapping("/remove-comment/{id}")  // Utiliser "id" comme nom de path variable
    public void removeComment(@PathVariable("id") Integer id) {  // Utiliser "id" comme nom de path variable
        commentService.deleteComment(id);
    }

    // Modifier un commentaire
    @PutMapping("/modify-comment")
    public Comment modifyComment(@RequestBody Comment comment) {  // Utiliser "comment" comme nom de paramètre
        return commentService.modifyComment(comment);
    }
}
