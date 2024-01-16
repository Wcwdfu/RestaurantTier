//package com.site.restauranttier.Service;
//
//import com.site.restauranttier.entity.Restaurant;
//import com.site.restauranttier.repository.RestaurantRepository;
//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.Join;
//import jakarta.persistence.criteria.JoinType;
//import jakarta.persistence.criteria.Predicate;
//import jakarta.persistence.criteria.Root;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.stereotype.Service;
//@RequiredArgsConstructor
//@Service
//public class RestaurantService {
//
//    private final RestaurantRepository restaurantRepository;
//
//    private Specification<Restaurant> search(String kw) {
//        return new Specification<>() {
//            private static final long serialVersionUID = 1L;
//            @Override
//            public Predicate toPredicate(Root<Restaurant> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                query.distinct(true);  // 중복을 제거
//                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
//                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
//                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
//                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
//                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
//                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
//                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
//                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
//            }
//        };
//    }
//
//}
