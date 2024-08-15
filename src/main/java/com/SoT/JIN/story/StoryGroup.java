package com.SoT.JIN.story;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class StoryGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupName;      // 그룹 이름 (한글)
    private String groupNameEn;    // 그룹 이름 (영문)
    private String groupImage;     // 그룹 이미지 URL

    // 기본 생성자
    public StoryGroup() {}

    // 필드를 초기화하는 생성자
    public StoryGroup(String groupName, String groupNameEn, String groupImage) {
        this.groupName = groupName;
        this.groupNameEn = groupNameEn;
        this.groupImage = groupImage;
    }

}
