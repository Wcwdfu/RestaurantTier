package com.site.restauranttier.service;

import com.site.restauranttier.DataNotFoundException;
import com.site.restauranttier.entity.Post;
import com.site.restauranttier.entity.PostScrap;
import com.site.restauranttier.entity.User;
import com.site.restauranttier.repository.PostRepository;
import com.site.restauranttier.repository.PostScrapRepository;
import com.site.restauranttier.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostScrapRepository postScrapRepository;
    public List<Post> getList(){
        return this.postRepository.findAll();
    }
    public List<Post> getListByPostCategory(String postCategory){
        return this.postRepository.findByPostCategory(postCategory);
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
    public List<String> getTimeAgoList(List<Post> postList){
        LocalDateTime now = LocalDateTime.now();

        // postList의 createdAt 필드를 문자열 형식으로 만들어 timeAgoList에 할당
        List<String> timeAgoList = postList.stream()
                .map(post -> {
                    LocalDateTime createdAt = post.getCreatedAt();
                    // datetime 타입의 createdAt을 string 타입으로 변환해주는 함수
                    return timeAgo(now, createdAt);
                })
                .collect(Collectors.toList());
        return timeAgoList;

    }

    // 작성글이나 댓글이 만들어진지 얼마나 됐는지 계산하는 함수
    public String timeAgo(LocalDateTime now, LocalDateTime past) {
        long minutes = Duration.between(past, now).toMinutes();
        if (minutes < 60) {
            return minutes + "분 전";
        }
        long hours = minutes / 60;
        if (hours < 24) {
            return hours + "시간 전";
        }
        long days = hours / 24;
        return days + "일 전";
    }
    public void increaseVisitCount(Post post){
        int visitCount = post.getPostVisitCount();
        post.setPostVisitCount(++visitCount);
        postRepository.save(post);
    }
    public void likeCreateOrDelete(Post post, User user){
        List<User> likeUserList =post.getLikeUserList();
        List<User> dislikeUserList = post.getDislikeUserList();
        List<Post> likePostList =user.getLikePostList();
        List<Post> dislikePostList =user.getDislikePostList();
        //해당 post 를 이미 like 한 경우 - 제거
        if(likeUserList.contains(user)){
            likePostList.remove(post);
            likeUserList.remove(user);
        }
        //해당 post를 이미 dislike 한 경우 - 제거하고 추가
        else if(dislikeUserList.contains(user)){
            dislikeUserList.remove(user);
            dislikePostList.remove(post);
            likeUserList.add(user);
            likePostList.add(post);
        }
        // 처음 like 하는 경우-추가
        else{
            likeUserList.add(user);
            likePostList.add(post);
        }
        postRepository.save(post);
        userRepository.save(user);
    }
    public void dislikeCreateOrDelete(Post post, User user){
        List<User> likeUserList =post.getLikeUserList();
        List<User> dislikeUserList = post.getDislikeUserList();
        List<Post> likePostList =user.getLikePostList();
        List<Post> dislikePostList =user.getDislikePostList();
        //해당 post를 이미 dislike 한 경우 - 제거
        if(dislikeUserList.contains(user)){
            dislikePostList.remove(post);
            dislikeUserList.remove(user);
        }
        //해당 post를 이미 like 한 경우 - 제거하고 추가
        else if(likeUserList.contains(user)){
            likeUserList.remove(user);
            likePostList.remove(post);
            dislikeUserList.add(user);
            dislikePostList.add(post);
        }
        // 처음 dislike 하는 경우-추가
        else{
            dislikeUserList.add(user);
            dislikePostList.add(post);
        }
        postRepository.save(post);
        userRepository.save(user);
    }



}
