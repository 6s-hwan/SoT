package com.SoT.JIN.member;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {

    // 사용자별 스토리 조회 (정렬 없음)
    List<Story> findByUsername(String username);

    // 사용자별 스토리 조회 (좋아요 수 기준 내림차순 정렬)
    List<Story> findByUsernameOrderByLikesDesc(String username);

    // 사용자별 스토리 조회 (조회수 기준 내림차순 정렬)
    List<Story> findByUsernameOrderByViewCountDesc(String username);
}