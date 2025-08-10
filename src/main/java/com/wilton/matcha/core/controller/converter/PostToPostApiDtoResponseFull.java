package com.wilton.matcha.core.controller.converter;

import com.wilton.matcha.core.controller.dto.PostApiDto;
import com.wilton.matcha.core.domain.Post;
import org.springframework.core.convert.converter.Converter;

public class PostToPostApiDtoResponseFull implements Converter<Post, PostApiDto.Response.Full> {
    @Override
    public PostApiDto.Response.Full convert(Post source) {
        return new PostApiDto.Response.Full(
                source.getId(),
                source.getFirstName(),
                source.getLastName(),
                source.getAge(),
                source.getHeight(),
                source.getDescription());
    }
}
