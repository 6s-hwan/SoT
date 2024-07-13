package com.SoT.JIN.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
}