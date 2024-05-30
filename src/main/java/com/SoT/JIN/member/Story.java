package com.SoT.JIN.member;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Story {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long storyId;
    private String username;
    private String image_url;
    private String title;
    private String location;
    private String tags;
    private String description;
}