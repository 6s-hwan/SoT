package com.SoT.JIN.story;

import com.SoT.JIN.like.LikeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Arrays;
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

    // 위치 정보를 파싱하여 도 또는 시와 구를 반환하는 메서드
    public String[] parseLocation() {
        if (this.location == null || this.location.isEmpty()) {
            return new String[]{"", ""};
        }
        String[] parts = this.location.split(" ");
        if (parts.length >= 2) {
            String mainLocation = parts[0];
            String subLocation = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
            return new String[]{mainLocation, subLocation};
        }
        return new String[]{this.location, ""};
    }
}
