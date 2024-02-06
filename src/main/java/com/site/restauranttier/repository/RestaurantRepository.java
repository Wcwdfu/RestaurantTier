package com.site.restauranttier.repository;

import com.site.restauranttier.dto.RestaurantEverageScoreDTO;
import com.site.restauranttier.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant,Integer> {

    Restaurant findByRestaurantId(Integer id);
    Page<Restaurant> findByStatus(String status, Pageable pageable);


    Page<Restaurant> findByRestaurantCuisineAndStatus(String restaurantCuisine, String status, Pageable pageable);

    List<Restaurant> findByRestaurantCuisineAndStatus(String restaurantCuisine, String status);
    List<Restaurant> findByStatus(String status);

    // 페이징
    Page<Restaurant> findAll(Pageable pageable);
    // 검색결과 페이징
    Page<Restaurant> findAll(Specification<Restaurant> spec, Pageable pageable);

    @Query("SELECT 100.0 * COUNT(r) / (SELECT COUNT(e) FROM Restaurant e) " +
            "FROM Restaurant r " +
            "WHERE r.visitCount >= :#{#restaurant.visitCount}")
    Float getPercentOrderByVisitCount(Restaurant restaurant);

    @Query("SELECT new com.site.restauranttier.dto.RestaurantEverageScoreDTO(r, AVG(e.evaluationScore)) " +
            "FROM Restaurant r JOIN r.evaluationList e " +
            "GROUP BY r.restaurantId " +
            "HAVING COUNT(e) >= :dataNum " +
            "ORDER BY AVG(e.evaluationScore) DESC") // 내림차순 정렬
    List<RestaurantEverageScoreDTO> getAllRestaurantsOrderedByAvgScore(@Param("dataNum") Integer dataNum);

    @Query("SELECT new com.site.restauranttier.dto.RestaurantEverageScoreDTO(r, AVG(e.evaluationScore)) " +
            "FROM Restaurant r JOIN r.evaluationList e " +
            "WHERE r.restaurantCuisine = :cuisine  " +
            "GROUP BY r.restaurantId " +
            "HAVING COUNT(e) >= :dataNum " +
            "ORDER BY AVG(e.evaluationScore) DESC") // 내림차순 정렬
    List<RestaurantEverageScoreDTO> getCuisineRestaurantsOrderedByAvgScore(
            @Param("cuisine") String cuisine,
            @Param("dataNum") Integer dataNum
    );
}
