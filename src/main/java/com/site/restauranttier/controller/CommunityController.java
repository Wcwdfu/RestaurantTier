package com.site.restauranttier.controller;

import com.site.restauranttier.entity.Post;
import com.site.restauranttier.entity.PostPhoto;
import com.site.restauranttier.etc.PostDTO;
import com.site.restauranttier.etc.PostMapper;
import com.site.restauranttier.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommunityController {
    private final PostRepository postRepository;
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/community")
    public String community(Model model) {
        List<Post> postList = postRepository.findAll();

        List<PostDTO> dtoList = new ArrayList();
        for(Post post : postList){
            logger.info(post.getPostPhotoList().get(0).getPhotoImgUrl());
            PostDTO postDTO = PostMapper.INSTANCE.postToPostDTO(post);

            if(!post.getPostPhotoList().isEmpty()){
                postDTO.setPhotoImgUrl(post.getPostPhotoList().get(0).getPhotoImgUrl());
            }
            else{
                postDTO.setPhotoImgUrl(null);
            }
            dtoList.add(postDTO);
        }
        for(PostDTO dto : dtoList){
            logger.info(dto.getPhotoImgUrl());
        }
        model.addAttribute("DTOList",dtoList);
        return "community";
    }



}
