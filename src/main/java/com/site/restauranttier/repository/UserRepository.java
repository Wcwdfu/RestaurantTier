package com.site.restauranttier.repository;

import com.site.restauranttier.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
}
