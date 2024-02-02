package com.site.restauranttier.service;

import com.site.restauranttier.entity.Post;
import com.site.restauranttier.entity.PostScrap;
import com.site.restauranttier.entity.User;
import com.site.restauranttier.repository.PostRepository;
import com.site.restauranttier.repository.PostScrapRepository;
import com.site.restauranttier.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostScrapService {
    private final PostScrapRepository postScrapRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    public void scrapCreateOfDelete(Post post, User user){
        List<PostScrap> postScrapList =post.getPostScrapList();
        List<PostScrap> userScrapList =user.getScrapList();
        Optional<PostScrap> scrapOptional = postScrapRepository.findByUserAndPost(user,post);
        if(scrapOptional.isPresent()){
            PostScrap scrap = scrapOptional.get();
            postScrapRepository.delete(scrap);
            postScrapList.remove(scrap);
            userScrapList.remove(scrap);
        }
        else{
            PostScrap scrap = new PostScrap(user,post, LocalDateTime.now());
            PostScrap savedScrap= postScrapRepository.save(scrap);
            userScrapList.add(savedScrap);
            postScrapList.add(savedScrap);
        }
        postRepository.save(post);
        userRepository.save(user);
    }

}
