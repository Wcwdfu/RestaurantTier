package com.site.restauranttier.service;

import com.site.restauranttier.DataNotFoundException;
import com.site.restauranttier.entity.Post;
import com.site.restauranttier.entity.User;
import com.site.restauranttier.repository.PostRepository;
import com.site.restauranttier.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    public List<Post> getList(){
        return this.postRepository.findAll();
    }

    public Post getPost(Integer id){
        Optional<Post> post = this.postRepository.findById(id);
        if(post.isPresent()){
            return post.get();
        }
        else{
            throw new DataNotFoundException("post not found");
        }
    }
    public void create(Post post, User user){
        post.setUser(user);
        Post savedpost = postRepository.save(post);
        user.getPostList().add(savedpost);
        userRepository.save(user);
    }
}
