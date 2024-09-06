package com.SoT.JIN.bookmark;

import com.SoT.JIN.like.LikeEntity;
import com.SoT.JIN.story.Story;
import com.SoT.JIN.story.StoryRepository;
import com.SoT.JIN.user.UserRepository;
import com.SoT.JIN.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookmarkService {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    // 북마크 토글 로직
    public void toggleBookmark(Long storyId, User user) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new IllegalArgumentException("Story not found"));

        Optional<BookmarkEntity> existingBookmark = bookmarkRepository.findByStoryAndUser(story, user);
        if (existingBookmark.isPresent()) {
            story.getBookmarks().remove(existingBookmark.get());
            existingBookmark.get().setStory(null);
            existingBookmark.get().setUser(null);
            bookmarkRepository.delete(existingBookmark.get());
        } else {
            BookmarkEntity newBookmark = new BookmarkEntity();
            newBookmark.setStory(story);
            newBookmark.setUser(user);
            story.getBookmarks().add(newBookmark);
            bookmarkRepository.save(newBookmark);
        }
        storyRepository.save(story);
    }

    // 북마크된 스토리 조회
    public List<Story> getBookmarkedStories(User user) {
        return bookmarkRepository.findByUser(user).stream()
                .map(BookmarkEntity::getStory)
                .collect(Collectors.toList());
    }
}