package com.site.restauranttier.repository;

import com.site.restauranttier.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment,Integer> {
}
