package com.worktrack.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worktrack.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    List<Comment> findByTaskIdOrderByCreatedAtAsc(UUID taskId);

}
