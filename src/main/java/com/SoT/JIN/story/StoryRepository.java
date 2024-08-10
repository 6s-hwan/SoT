package com.SoT.JIN.story;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
    // 사용자별 스토리 조회 (정렬 없음)
    List<Story> findByUsername(String username);
    List<Story> findByUsernameOrderByLikesDesc(String username);
    List<Story> findByUsernameOrderByViewCountDesc(String username);
    List<Story> findByUsernameOrderByUploadTimeDesc(String username);
    // 페이징 및 정렬을 위한 메서드
    Page<Story> findAll(Pageable pageable);
    List<Story> findByTitleContainingIgnoreCase(String query);
    // 특정 키워드가 포함된 스토리 ID 조회
    @Query("SELECT s.storyId FROM Story s WHERE s.tags LIKE %:keyword% OR s.description LIKE %:keyword% OR s.location LIKE %:keyword% OR s.title LIKE %:keyword%")
    List<Long> findIdsByKeyword(String keyword);
    List<Story> findByUsernameOrderByLikesDescViewCountDesc(String username); // 새로운 메서드 추가
    List<Story> findByTheme(String theme);
}