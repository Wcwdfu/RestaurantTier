package com.site.restauranttier.repository;

import com.site.restauranttier.entity.HomeModal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HomeModalRepository extends JpaRepository<HomeModal,Integer> {
    HomeModal getHomeModalByModalId(Integer Id);
}
