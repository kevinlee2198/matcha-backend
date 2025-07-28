package com.wilton.matcha.common.http;

import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public interface HttpService {
    /**
     * Send a GET http request
     *
     * @param url     url of the resource
     * @param headers headers to include
     * @param clazz   class to return the response as
     * @return the response of the GET request
     */
    <T> T sendGet(String url, Map<String, String[]> headers, Class<T> clazz);

    /**
     * Send a POST http request
     *
     * @param url     url of the resource
     * @param headers headers to include
     * @param body    body of the request
     * @param clazz   class to return the response as
     * @return the response of the POST request
     */
    <T> T sendPost(String url, Map<String, String[]> headers, MultiValueMap<String, ?> body, Class<T> clazz);

    /**
     * Send an http request
     *
     * @param httpMethod GET, POST, PUT, etc.
     * @param url        url of the resource
     * @param headers    headers to include
     * @param body       body of the request
     * @param clazz      class to return the response as
     * @return the response of the http request
     */
    <T> T sendRequest(HttpMethod httpMethod, String url, Map<String, String[]> headers, MultiValueMap<String, ?> body, Class<T> clazz);
}
