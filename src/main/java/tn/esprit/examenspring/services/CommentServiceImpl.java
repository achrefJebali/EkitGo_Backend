package tn.esprit.examenspring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.entities.Comment;
import tn.esprit.examenspring.Repository.CommentRepository;

import java.util.List;

@Service
@Slf4j
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private CommentRepository commentRepository; // Correction du nom de la variable

    @Override
    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

    @Override
    public Comment modifyComment(Comment comment) {
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }
}
