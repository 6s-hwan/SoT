package com.SoT.JIN.story;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {

    List<Story> findByLocation(String location);

    // 사용자별 스토리 조회 (정렬 없음)
    List<Story> findByUsername(String username);

    List<Story> findByUsernameOrderByLikesDesc(String username);

    List<Story> findByUsernameOrderByViewCountDesc(String username);

    List<Story> findByUsernameOrderByUploadTimeDesc(String username);

    // 페이징 및 정렬을 위한 메서드
    Page<Story> findAll(Pageable pageable);

    // 사용자별 게시글 수를 세는 메서드
    long countByUsername(String username);

    List<Story> findByTitleContainingIgnoreCaseOrLocationContainingIgnoreCaseOrderByStoryIdDesc(String title, String location);


    // 특정 키워드가 포함된 스토리 ID 중 가장 큰 ID 조회
    @Query("SELECT s.storyId FROM Story s WHERE s.tags LIKE %:keyword% OR s.description LIKE %:keyword% OR s.location LIKE %:keyword% OR s.title LIKE %:keyword% ORDER BY s.storyId DESC")
    List<Long> findIdsByKeyword(String keyword, Pageable pageable);

    List<Story> findByThemeContaining(String selectedTheme);

    // 제목, 위치, 설명, 태그에서 검색어가 포함된 스토리 수를 계산
    @Query("SELECT COUNT(s) FROM Story s WHERE " +
            "LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.location) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.tags) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    int countByKeywordAcrossFields(@Param("keyword") String keyword);
}