package com.site.restauranttier.service;

import com.site.restauranttier.DataNotFoundException;
import com.site.restauranttier.entity.Post;
import com.site.restauranttier.entity.PostComment;
import com.site.restauranttier.entity.PostScrap;
import com.site.restauranttier.entity.User;
import com.site.restauranttier.repository.PostRepository;
import com.site.restauranttier.repository.PostScrapRepository;
import com.site.restauranttier.repository.UserRepository;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostScrapRepository postScrapRepository;


    // 검색
    public Page<Post> getList(int page, String sort, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page, 20, Sort.by(sorts));
        Specification<Post> spec = search(kw);
        return this.postRepository.findAll(spec, pageable);
    }

    // 메인 로딩
    public Page<Post> getList(int page, String sort) {
        List<Sort.Order> sorts = new ArrayList<>();
        if (sort.isEmpty() || sort.equals("recent")) {
            sorts.add(Sort.Order.desc("createdAt"));
        } else if (sort.equals("popular")) {
            sorts.add(Sort.Order.desc("likeCount"));
        }
        Pageable pageable = PageRequest.of(page, 20, Sort.by(sorts));


        return this.postRepository.findAll(pageable);
    }

    //    드롭다운에서 카테고리 설정
    public Page<Post> getListByPostCategory(String postCategory, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page, 20, Sort.by(sorts));
        return this.postRepository.findByPostCategory(postCategory, pageable);
    }

    public Post getPost(Integer id) {
        Optional<Post> post = this.postRepository.findById(id);
        if (post.isPresent()) {
            return post.get();
        } else {
            throw new DataNotFoundException("post not found");
        }
    }

    public void create(Post post, User user) {
        post.setUser(user);
        Post savedpost = postRepository.save(post);
        user.getPostList().add(savedpost);
        userRepository.save(user);
    }

    public List<String> getTimeAgoList(Page<Post> postList) {
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

    // 조회수 증가
    public void increaseVisitCount(Post post) {
        int visitCount = post.getPostVisitCount();
        post.setPostVisitCount(++visitCount);
        postRepository.save(post);
    }

    // 글 좋아요
    public void likeCreateOrDelete(Post post, User user) {
        List<User> likeUserList = post.getLikeUserList();
        List<User> dislikeUserList = post.getDislikeUserList();
        List<Post> likePostList = user.getLikePostList();
        List<Post> dislikePostList = user.getDislikePostList();
        //해당 post 를 이미 like 한 경우 - 제거
        if (likeUserList.contains(user)) {
            post.setLikeCount(post.getLikeCount() - 1);
            likePostList.remove(post);
            likeUserList.remove(user);
        }
        //해당 post를 이미 dislike 한 경우 - 제거하고 추가
        else if (dislikeUserList.contains(user)) {
            post.setLikeCount(post.getLikeCount() + 2);
            dislikeUserList.remove(user);
            dislikePostList.remove(post);
            likeUserList.add(user);
            likePostList.add(post);
        }
        // 처음 like 하는 경우-추가
        else {
            post.setLikeCount(post.getLikeCount() + 1);
            likeUserList.add(user);
            likePostList.add(post);
        }
        postRepository.save(post);
        userRepository.save(user);
    }

    // 글 싫어요
    public void dislikeCreateOrDelete(Post post, User user) {
        List<User> likeUserList = post.getLikeUserList();
        List<User> dislikeUserList = post.getDislikeUserList();
        List<Post> likePostList = user.getLikePostList();
        List<Post> dislikePostList = user.getDislikePostList();
        //해당 post를 이미 dislike 한 경우 - 제거
        if (dislikeUserList.contains(user)) {
            post.setLikeCount(post.getLikeCount() + 1);

            dislikePostList.remove(post);
            dislikeUserList.remove(user);
        }
        //해당 post를 이미 like 한 경우 - 제거하고 추가
        else if (likeUserList.contains(user)) {
            post.setLikeCount(post.getLikeCount() - 2);

            likeUserList.remove(user);
            likePostList.remove(post);
            dislikeUserList.add(user);
            dislikePostList.add(post);
        }
        // 처음 dislike 하는 경우-추가
        else {
            post.setLikeCount(post.getLikeCount() - 1);

            dislikeUserList.add(user);
            dislikePostList.add(post);
        }
        postRepository.save(post);
        userRepository.save(user);
    }

    private Specification<Post> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Post> p, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거

                //조인
                Join<Post, User> u1 = p.join("user", JoinType.LEFT);
                Join<Post, PostComment> c = p.join("postCommentList", JoinType.LEFT);
                Join<PostComment, User> u2 = c.join("user", JoinType.LEFT);
                // 액티브 조건 추가
                Predicate statusPredicate = cb.equal(p.get("status"), "ACTIVE");

                // 검색 조건 결합
                return cb.and(statusPredicate, cb.or(cb.like(p.get("postTitle"), "%" + kw + "%"), // 제목
                        cb.like(p.get("postBody"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("userNickname"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(c.get("commentBody"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("userNickname"), "%" + kw + "%")));   // 답변 작성자
            }
        };
    }
}
