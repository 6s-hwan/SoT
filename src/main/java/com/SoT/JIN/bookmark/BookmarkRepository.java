package com.SoT.JIN.bookmark;

import com.SoT.JIN.story.Story;
import com.SoT.JIN.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
    Optional<BookmarkEntity> findByStoryAndUser(Story story, User user);
    boolean existsByStoryAndUser(Story story, User user);  // 사용자가 이 스토리를 북마크했는지 확인

}