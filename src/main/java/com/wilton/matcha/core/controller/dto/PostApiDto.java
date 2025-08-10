package com.wilton.matcha.core.controller.dto;

public enum PostApiDto {
    ;

    public enum Request {
        ;

        public record Save(String firstName, String lastName, int age, double height, String description) {}
    }

    public enum Response {
        ;

        public record Full(String id, String firstName, String lastName, int age, double height, String description) {}
    }
}
