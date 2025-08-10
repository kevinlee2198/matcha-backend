package com.wilton.matcha.core.controller.converter;

import com.wilton.matcha.core.controller.dto.PostApiDto;
import com.wilton.matcha.core.service.dto.PostSaveRequest;
import org.springframework.core.convert.converter.Converter;

public class PostApiDtoRequestSaveToPostSaveRequest implements Converter<PostApiDto.Request.Save, PostSaveRequest> {

    @Override
    public PostSaveRequest convert(PostApiDto.Request.Save source) {
        return new PostSaveRequest(
                null, source.firstName(), source.lastName(), source.age(), source.height(), source.description());
    }
}
