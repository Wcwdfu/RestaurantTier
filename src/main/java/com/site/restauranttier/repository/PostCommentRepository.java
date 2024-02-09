package com.site.restauranttier.repository;

import com.site.restauranttier.entity.PostComment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment,Integer> {
    List<PostComment> findAll(Specification<PostComment> spec);
}
