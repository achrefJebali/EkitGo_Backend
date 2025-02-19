package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Comment;
import tn.esprit.examenspring.entities.Formation;

import java.util.List;

public interface ICommentService {
    Comment addComment(Comment comment);
    List<Comment> getComments();
    Comment modifyComment(Comment comment);
    void deleteComment(Integer id);
}
