package com.site.restauranttier.service;

import com.site.restauranttier.entity.Restaurant;
import com.site.restauranttier.entity.RestaurantComment;
import com.site.restauranttier.entity.User;
import com.site.restauranttier.etc.SortComment;
import com.site.restauranttier.repository.RestaurantCommentRepository;
import com.site.restauranttier.repository.RestaurantRepository;
import com.site.restauranttier.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RestaurantCommentService {

    private final RestaurantCommentRepository restaurantCommentRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public String addComment(Integer restaurantId, String userTokenId, String commentBody) {
        RestaurantComment restaurantComment = new RestaurantComment();

        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);

        Optional<User> userOptional = userRepository.findByUserTokenId(userTokenId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            restaurantComment.setUser(user);
            restaurantComment.setRestaurant(restaurant);
            restaurantComment.setCommentBody(commentBody);
            restaurantComment.setStatus("ACTIVE");
            restaurantComment.setCreatedAt(LocalDateTime.now());

            restaurantCommentRepository.save(restaurantComment);

            return "ok";
        } else {
            return "userTokenId";
        }
    }

    public List<Object[]> getCommentList(int restaurantId, SortComment sortComment) {
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId);
        if (sortComment == SortComment.POPULAR) {
            return restaurantCommentRepository.findOrderPopular(restaurant);
        } else if (sortComment == SortComment.LATEST) {
            return restaurantCommentRepository.findOrderLatest(restaurant);
        } else {
            return null;
        }
    }
}
