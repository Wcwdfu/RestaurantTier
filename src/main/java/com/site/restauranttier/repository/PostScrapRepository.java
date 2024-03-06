package com.site.restauranttier.repository;

import com.site.restauranttier.entity.Post;
import com.site.restauranttier.entity.PostScrap;
import com.site.restauranttier.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostScrapRepository extends JpaRepository<PostScrap, Integer> {
    Optional<PostScrap> findByUserAndPost(User user, Post post);
}
