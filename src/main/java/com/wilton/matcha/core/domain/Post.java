package com.wilton.matcha.core.domain;

import com.wilton.matcha.common.domain.AbstractAuditingEntity;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Someone who the users of the app are posting about
 */
@Document
public class Post extends AbstractAuditingEntity<String> {
    @Id
    private String id;

    private String firstName;

    private String lastName;

    private int age;

    private double height;

    private String description;

    private Set<String> pictureURIs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getPictureURIs() {
        return pictureURIs;
    }

    public void setPictureURIs(Set<String> pictureURIs) {
        this.pictureURIs = pictureURIs;
    }
}
