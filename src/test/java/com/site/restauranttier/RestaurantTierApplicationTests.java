package com.site.restauranttier;

import com.site.restauranttier.controller.MainController;
import com.site.restauranttier.entity.*;
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
    @Autowired
    private PostCommentRepository postCommentRepository;
    private static final Logger logger = LoggerFactory.getLogger(RestaurantTierApplicationTests.class);

    // 테스트 데이터 추가
    @Test
    @Transactional
    @Rollback(false)
    void textCommunity() {

    }

}
