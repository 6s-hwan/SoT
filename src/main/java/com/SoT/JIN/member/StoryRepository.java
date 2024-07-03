package com.SoT.JIN.member;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {

    // 사용자별 스토리 조회 (정렬 없음)
    List<Story> findByUsername(String username);
    List<Story> findByUsernameOrderByLikesDesc(String username);
    List<Story> findByUsernameOrderByViewCountDesc(String username);
    List<Story> findByUsernameOrderByUploadTimeDesc(String username);
}