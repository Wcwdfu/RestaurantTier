package com.site.restauranttier.user;

import com.site.restauranttier.entity.User;
import com.site.restauranttier.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserSecuriyService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<User> siteUser = this.userRepository.findById(userId);
        if(siteUser.isEmpty()){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        User user= siteUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if("admin".equals(userId)){
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        }else{
            authorities.add(new SimpleGrantedAuthority((UserRole.USER.getValue())));
        }
        // User 엔티티 아님 , 시큐리티에서 제공하는 User 클래스
        return new org.springframework.security.core.userdetails.User(user.getUserId(),user.getUserPassword(),authorities);
    }
}
