package com.wilton.matcha.common.http.impl;

import com.wilton.matcha.common.http.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class HttpServiceImpl implements HttpService {
    private final RestClient restClient;

    @Autowired
    public HttpServiceImpl(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    @Override
    public <T> T sendGet(String url, Map<String, String[]> headers, Class<T> clazz) {
        return sendRequest(HttpMethod.GET, url, headers, null, clazz);
    }

    @Override
    public <T> T sendPost(String url, Map<String, String[]> headers, MultiValueMap<String, ?> body, Class<T> clazz) {
        return sendRequest(HttpMethod.POST, url, headers, body, clazz);
    }

    @Override
    public <T> T sendRequest(HttpMethod httpMethod, String url, Map<String, String[]> headers, MultiValueMap<String, ?> body, Class<T> clazz) {
        RestClient.RequestBodySpec requestBodySpec = restClient.method(httpMethod)
                .uri(url);

        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach(requestBodySpec::header);
        }

        if (!CollectionUtils.isEmpty(body)) {
            requestBodySpec.body(body);
        }

        return requestBodySpec.retrieve().body(clazz);
    }
}