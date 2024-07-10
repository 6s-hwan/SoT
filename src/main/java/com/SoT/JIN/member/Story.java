package com.SoT.JIN.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storyId;
    private String username;
    private String image_url;
    private String title;
    private String date;
    private String location;
    private String tags;
    private String description;
    private int viewCount;

    // 업로드 시간을 저장할 필드
    private LocalDateTime uploadTime;

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL)
    private Set<LikeEntity> likes = new HashSet<>();

    public int getLikesCount() {
        return this.likes.size();
    }

    // 엔터티가 영속화되기 전에 호출되는 메서드
    @PrePersist
    protected void onCreate() {
        this.uploadTime = LocalDateTime.now();
    }
}
