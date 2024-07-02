package com.SoT.JIN.member;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

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
    private String date;
    private String location;
    private String tags;
    private String description;
    private int viewCount;
    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL)
    private Set<LikeEntity> likes = new HashSet<>();

}