package com.example.backend.controller;

import com.example.backend.entity.Comment;
import com.example.backend.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/all")
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }


    @PutMapping("/update/{id}")
    public Comment updateCommentStatus(@PathVariable Long id, @RequestParam String status) {
        // 使用 orElseThrow 来抛出一个自定义异常
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));  // 你可以根据需要替换 RuntimeException

        comment.setStatus(status);
        return commentRepository.save(comment);
    }

}
