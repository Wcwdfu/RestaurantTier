package com.site.restauranttier.repository;

import com.site.restauranttier.entity.Evaluation;
import com.site.restauranttier.entity.Post;
import com.site.restauranttier.entity.Restaurant;
import com.site.restauranttier.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer>{
    Post findByPostTitle(String title);

    Page<Post> findByPostCategory( String postCategory,Pageable pageable);

    Page<Post> findAll(Specification<Post> spec, Pageable pageable);
    Page<Post> findAll(Pageable pageable);
    Page<Post> findByStatus(String status, Pageable pageable);

}
