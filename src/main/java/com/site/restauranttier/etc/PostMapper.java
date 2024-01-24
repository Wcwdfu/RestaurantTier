package com.site.restauranttier.etc;

import com.site.restauranttier.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);
    // Post 객체를 PostDTO로 매핑하는 메서드
    @Mapping(source = "postId", target = "postId")
    @Mapping(source = "postTitle", target = "postTitle")
    @Mapping(source = "postBody", target = "postBody")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "user", target = "user")
    PostDTO postToPostDTO(Post post);
}
