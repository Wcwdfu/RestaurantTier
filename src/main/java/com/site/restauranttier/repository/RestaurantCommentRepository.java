package com.site.restauranttier.repository;

import com.site.restauranttier.entity.Restaurant;
import com.site.restauranttier.entity.RestaurantComment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class RestaurantCommentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(RestaurantComment restaurantComment) {
        entityManager.persist(restaurantComment);
    }

    public List<Object[]> findOrderPopular(Restaurant restaurant) {
        return entityManager.createQuery(
                        "SELECT e, COUNT(a) " +
                                "FROM RestaurantComment e " +
                                "LEFT JOIN e.restaurantCommentlikeList a " +
                                "WHERE e.restaurant = :value1 " +
                                "AND e.status = :value2 " +
                                "GROUP BY e " +
                                "ORDER BY COUNT(a) DESC", Object[].class)
                .setParameter("value1", restaurant)
                .setParameter("value2", "ACTIVE")
                .getResultList();
    }

    public List<Object[]> findOrderLatest(Restaurant restaurant) {
        return entityManager.createQuery(
                        "SELECT e, COUNT(a) " +
                                "FROM RestaurantComment e " +
                                "LEFT JOIN e.restaurantCommentlikeList a " +
                                "WHERE e.restaurant = :value1 " +
                                "AND e.status = :value2 " +
                                "GROUP BY e " +
                                "ORDER BY e.createdAt DESC", Object[].class)
                .setParameter("value1", restaurant)
                .setParameter("value2", "ACTIVE")
                .getResultList();
    }
}
