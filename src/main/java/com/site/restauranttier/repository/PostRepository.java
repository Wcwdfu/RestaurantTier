package com.site.restauranttier.repository;

import com.site.restauranttier.entity.Evaluation;
import com.site.restauranttier.entity.Post;
import com.site.restauranttier.entity.Restaurant;
import com.site.restauranttier.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer>{
    Post findByPostTitle(String title);

}
