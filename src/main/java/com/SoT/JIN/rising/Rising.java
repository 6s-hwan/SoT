package com.SoT.JIN.rising;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "rising")
public class Rising {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;  // 급상승 검색어
    private int rankOrder;  // 급상승 순위
    private String location;  // 위치 정보
    private String imageUrl;  // 이미지 URL

    public Rising() {}

    public Rising(String keyword, int rankOrder, String location, String imageUrl) {
        this.keyword = keyword;
        this.rankOrder = rankOrder;
        this.location = location;
        this.imageUrl = imageUrl;
    }
}