package com.site.restauranttier;

import com.site.restauranttier.restaurant.Restaurant;
import com.site.restauranttier.restaurant.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
class RestaurantTierApplicationTests {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RestaurantRepository restaurantRepository;

	@Test
	void textJpa() {
		User user= new User("1","1234","dlawogud517@naver.com","임재","online",LocalDateTime.now());
		Optional<Restaurant> d = this.restaurantRepository.findById(500);

			Restaurant f = d.get();
			assertEquals("인정국물떡볶이",f.getRestaurantName());

	}

}
