package com.wilton.matcha.core.controller;

import com.wilton.matcha.common.domain.MatchaUser;
import com.wilton.matcha.common.exception.ResourceNotFoundException;
import com.wilton.matcha.core.controller.dto.PostApiDto;
import com.wilton.matcha.core.service.PostService;
import com.wilton.matcha.core.service.dto.PostSaveRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final ConversionService conversionService;

    @Autowired
    public PostController(PostService postService, ConversionService conversionService) {
        this.postService = postService;
        this.conversionService = conversionService;
    }

    @GetMapping("/{id}")
    public PostApiDto.Response.Full getPost(@PathVariable String id, @AuthenticationPrincipal MatchaUser user) {
        return postService
                .getPost(id, user)
                .map(post -> conversionService.convert(post, PostApiDto.Response.Full.class))
                .orElseThrow(() -> new ResourceNotFoundException("post", "id", id));
    }

    @PostMapping
    public PostApiDto.Response.Full createPost(
            @RequestBody PostApiDto.Request.Save input, @AuthenticationPrincipal MatchaUser user) {
        return conversionService.convert(
                postService.savePost(conversionService.convert(input, PostSaveRequest.class), user),
                PostApiDto.Response.Full.class);
    }

    @PutMapping("/{id}")
    public PostApiDto.Response.Full updatePost(
            @PathVariable String id,
            @RequestBody PostApiDto.Request.Save input,
            @AuthenticationPrincipal MatchaUser user) {
        PostSaveRequest saveRequest = conversionService.convert(input, PostSaveRequest.class);
        saveRequest.setId(id);
        return conversionService.convert(postService.savePost(saveRequest, user), PostApiDto.Response.Full.class);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable String id, @AuthenticationPrincipal MatchaUser user) {
        postService.deletePost(id, user);
    }
}
