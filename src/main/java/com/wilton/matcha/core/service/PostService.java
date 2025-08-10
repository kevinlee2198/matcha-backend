package com.wilton.matcha.core.service;

import com.wilton.matcha.common.domain.MatchaUser;
import com.wilton.matcha.core.domain.Post;
import com.wilton.matcha.core.service.dto.PostSaveRequest;
import java.util.Optional;

public interface PostService {
    /**
     *
     * @param id ID of the post
     * @param matchaUser the user making the request
     * @return the post if found
     */
    Optional<Post> getPost(String id, MatchaUser matchaUser);

    /**
     *
     * @param input post information to save
     * @param matchaUser the user making the request
     * @return the saved post
     */
    Post savePost(PostSaveRequest input, MatchaUser matchaUser);

    /**
     *
     * @param id ID of the post
     * @param matchaUser the user making the request
     */
    void deletePost(String id, MatchaUser matchaUser);
}
