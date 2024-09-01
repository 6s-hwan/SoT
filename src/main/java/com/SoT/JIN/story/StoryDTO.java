package com.SoT.JIN.story;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoryDTO {
    private Long storyId;
    private String title;
    private String imageUrl;
    private String description;
    private String location;
    private String date;
    private String tags;
    private int likesCount;
    private int bookmarksCount;
    private String username;
    private String profileImageUrl;
    private int followersCount;
    private boolean isLikedByUser;
    private boolean isBookmarkedByUser;
    private boolean isAuthenticated; // 추가된 필드: 사용자 인증 상태

    // 생성자
    public StoryDTO(Long storyId, String title, String imageUrl, String description, String location, String date,
                    String tags, int likesCount, int bookmarksCount,
                    String username, String profileImageUrl, int followersCount,
                    boolean isLikedByUser, boolean isBookmarkedByUser) {
        this.storyId = storyId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
        this.location = location;
        this.date = date;
        this.tags = tags;
        this.likesCount = likesCount;
        this.bookmarksCount = bookmarksCount;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.followersCount = followersCount;
        this.isLikedByUser = isLikedByUser;
        this.isBookmarkedByUser = isBookmarkedByUser;
    }
}

