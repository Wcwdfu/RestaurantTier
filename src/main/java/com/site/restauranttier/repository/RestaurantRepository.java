package com.site.restauranttier.repository;

import com.site.restauranttier.dataBundle.RestaurantAverageScoreBundle;
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
    @Query("SELECT r " +
            "FROM Restaurant r " +
            "JOIN r.situationList s " +
            "WHERE r.status = :status AND s.situationName = :situation")
    Page<Restaurant> findActiveRestaurantsBySituation(String situation, String status, Pageable pageable);
    @Query("SELECT r " +
            "FROM Restaurant r " +
            "JOIN r.situationList s " +
            "WHERE r.status = :status AND s.situationName = :situation AND r.restaurantCuisine = :cuisine")
    Page<Restaurant> findActiveRestaurantsByCuisineAndSituation(String cuisine, String situation, String status, Pageable pageable);

    List<Restaurant> findByRestaurantCuisineAndStatus(String restaurantCuisine, String status);
    List<Restaurant> findByStatus(String status);

    @Query("SELECT r " +
            "FROM Restaurant r " +
            "JOIN r.situationList s " +
            "WHERE r.status = :status AND s.situationName = :situation AND r.restaurantCuisine = :cuisine")
    List<Restaurant> findActiveRestaurantsByCuisineAndSituation(String cuisine, String situation, String status);

    // 페이징
    Page<Restaurant> findAll(Pageable pageable);
    // 검색결과 페이징
    Page<Restaurant> findAll(Specification<Restaurant> spec, Pageable pageable);

    @Query("SELECT 100.0 * COUNT(r) / (SELECT COUNT(e) FROM Restaurant e) " +
            "FROM Restaurant r " +
            "WHERE r.visitCount >= :#{#restaurant.visitCount}")
    Float getPercentOrderByVisitCount(Restaurant restaurant);

    @Query("SELECT new com.site.restauranttier.dataBundle.RestaurantAverageScoreBundle(r, " +
            "CASE WHEN COUNT(e) >= :dataNum THEN AVG(e.evaluationScore) ELSE 0.0 END) " +
            "FROM Restaurant r LEFT JOIN r.evaluationList e " +
            "WHERE r.status = 'ACTIVE' " +
            "GROUP BY r.restaurantId " +
            "ORDER BY AVG(e.evaluationScore) DESC") // 내림차순 정렬
    List<RestaurantAverageScoreBundle> getAllRestaurantsOrderedByAvgScore(@Param("dataNum") Integer dataNum);
    @Query("SELECT new com.site.restauranttier.dataBundle.RestaurantAverageScoreBundle(r, " +
            "CASE WHEN COUNT(e) >= :dataNum THEN AVG(e.evaluationScore) ELSE 0 END) " +
            "FROM Restaurant r LEFT JOIN r.evaluationList e " +
            "WHERE r.restaurantCuisine = :cuisine AND r.status = 'ACTIVE' " +
            "GROUP BY r.restaurantId " +
            "ORDER BY AVG(e.evaluationScore) DESC") // 내림차순 정렬
    List<RestaurantAverageScoreBundle> getRestaurantsByCuisineOrderedByAvgScore(
            @Param("cuisine") String cuisine,
            @Param("dataNum") Integer dataNum
    );
    /*@Query("SELECT new com.site.restauranttier.dataBundle.RestaurantAverageScoreBundle(r, " +
            "CASE WHEN COUNT(e) >= :dataNum THEN AVG(e.evaluationScore) ELSE 0 END) " +
            "FROM Restaurant r LEFT JOIN r.evaluationList e " +
            "WHERE r.situationList = :situation AND r.status = 'ACTIVE' " +
            "GROUP BY r.restaurantId " +
            "ORDER BY AVG(e.evaluationScore) DESC") // 내림차순 정렬
    List<RestaurantAverageScoreBundle> getRestaurantsBySituationOrderedByAvgScore(
            @Param("situation") String situation,
            @Param("dataNum") Integer dataNum
    );*/
}
