package com.site.restauranttier;

import com.site.restauranttier.controller.MainController;
import com.site.restauranttier.entity.Post;
import com.site.restauranttier.entity.PostPhoto;
import com.site.restauranttier.entity.Situation;
import com.site.restauranttier.entity.User;
import com.site.restauranttier.repository.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class RestaurantTierApplicationTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private SituationRepository situationRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostPhotoRepository postPhotoRepository;
    private static final Logger logger = LoggerFactory.getLogger(RestaurantTierApplicationTests.class);

    // 테스트 데이터 추가
    @Test
    @Transactional
    @Rollback(false)
    void textCommunity() {

        // 커뮤니티 테스트

        Post post = new Post(1,"부탄츄 후기", "부탄츄 건대점에 다녀왔습니다", "ACTIVE", LocalDateTime.now());
        // 이미지 설정과 양방향 매핑
        PostPhoto postPhoto = new PostPhoto("https://mblogthumb-phinf.pstatic.net/MjAyMDAxMTlfMTEg/MDAxNTc5NDMyNjY5OTg4.XkEOiHNJDaEgWAawh-IzZFPkovVqLXlQRcdDiWFOW5gg.h5WRn-eHCJ9FxK2ou6P892Zt0Xd1_MrBWOWc6t7VMJAg.JPEG.effy0424/1579432668513.jpg?type=w800", "ACTIVE");
        postPhoto.setPost(post);
        post.getPostPhotoList().add(postPhoto);
        // user 설정과 양방향 매핑
        Optional<User> userOptional = userRepository.findById(1);
        User user = userOptional.get();
        post.setUser(user);
        user.getPostList().add(post);
        postRepository.save(post);
        postPhotoRepository.save(postPhoto);


//        Post post2 = new Post(2,"긴자료코 후기", "긴자료코 건대점에 다녀왔습니다", "ACTIVE", LocalDateTime.now());
//        // 이미지 설정과 양방향 매핑
//        PostPhoto postPhoto2 = new PostPhoto("https://i0.wp.com/43.200.108.156/wp-content/uploads/ginjaRyoko_pork_cutlet.webp?fit=833%2C658&ssl=1", "ACTIVE");
//        postPhoto.setPost(post2);
//        post.getPostPhotoList().add(postPhoto2);
//        // user 설정과 양방향 매핑
//        Optional<User> userOptional2 = userRepository.findById(2);
//        User user2 = userOptional2.get();
//        post2.setUser(user2);
//        user2.getPostList().add(post2);
//        postRepository.save(post2);
//        postPhotoRepository.save(postPhoto2);

    }

}
