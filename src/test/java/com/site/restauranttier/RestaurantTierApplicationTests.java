package com.site.restauranttier;

import com.site.restauranttier.entity.Restaurant;
import com.site.restauranttier.entity.Situation;
import com.site.restauranttier.entity.User;
import com.site.restauranttier.repository.RestaurantRepository;
import com.site.restauranttier.repository.SituationCategoryRepository;
import com.site.restauranttier.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class RestaurantTierApplicationTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
	@Autowired
    private SituationCategoryRepository situationCategoryRepository;

    // 테스트 데이터 추가
    @Test
    void textJpa() {

		// 유저 테스트 데이터 추가
        for (int i = 0; i < 10; i++) {
            User newUser = new User("userId" + i, "self", "pass" + i, "user" + i + "@example.com", "UserNickName" + i, "online", LocalDateTime.now());
            userRepository.save(newUser);
        }

		Restaurant res1 = new Restaurant("숨맑은집 호계점","카페","https://map.naver.com/p/search/%EC%88%A8%EB%A7%91%EC%9D%80%EC%A7%91/place/1468337474?c=14.28,0,0,0,dh",0,"카페/디저트","normal",LocalDateTime.now());
        // 식당 테스트 데이터 추가
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(res1);
        restaurantRepository.saveAll(restaurants);

        
		// 상황 테스트 데이터 저장
        Situation alone = new Situation("혼밥");
        Situation threeFour = new Situation("3~4인");
        Situation five = new Situation("5인 이상");
        Situation group  = new Situation("단체 회식");
        Situation delivery = new Situation("배달");
        Situation nightSnack = new Situation("야식");
        Situation invite = new Situation("친구 초대");
        Situation date = new Situation("데이트");
        Situation blindDate = new Situation("소개팅");
        List situationList = new ArrayList(Arrays.asList(alone,threeFour,five,group,delivery,nightSnack,invite,date,blindDate));
        situationCategoryRepository.saveAll(situationList);
    }

}
