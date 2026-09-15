package com.urferu.tvmiddleware.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.urferu.tvmiddleware.model.Comment;

public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findByShowId(Long showId);
}
