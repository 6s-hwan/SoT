package com.SoT.JIN.bookmark;

import com.SoT.JIN.story.Story;
import com.SoT.JIN.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
    Optional<BookmarkEntity> findByStoryAndUser(Story story, User user);

    // 북마크한 스토리 조회
    List<BookmarkEntity> findByUser(User user);

    // 이 메서드가 없을 경우 추가하세요.
    boolean existsByStoryAndUser(Story story, User user);
}
