package com.site.restauranttier.repository;

import com.site.restauranttier.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer>{
}
