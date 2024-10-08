package com.SoT.JIN.like;

import com.SoT.JIN.story.Story;
import com.SoT.JIN.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByStoryAndUser(Story story, User user);
    boolean existsByStoryAndUser(Story story, User user);  // 사용자가 이 스토리에 좋아요를 눌렀는지 확인

}
