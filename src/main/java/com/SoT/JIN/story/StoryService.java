package com.SoT.JIN.story;

import com.SoT.JIN.bookmark.BookmarkEntity;
import com.SoT.JIN.bookmark.BookmarkRepository;
import com.SoT.JIN.like.LikeEntity;
import com.SoT.JIN.like.LikeRepository;
import com.SoT.JIN.user.User;
import com.SoT.JIN.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StoryService {

    private final StoryRepository storyRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final StoryGroupRepository storyGroupRepository;
    private final BookmarkRepository bookmarkRepository;

    @Autowired
    public StoryService(StoryRepository storyRepository, LikeRepository likeRepository,
                        UserRepository userRepository, StoryGroupRepository storyGroupRepository,
                        BookmarkRepository bookmarkRepository) {
        this.storyRepository = storyRepository;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.storyGroupRepository = storyGroupRepository;
        this.bookmarkRepository = bookmarkRepository;
    }

    @Transactional
    public Story getStoryAndIncrementViewCount(Long id) {
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        story.setViewCount(story.getViewCount() + 1);
        return story;
    }

    public List<Story> getBestStories(int limit) {
        return storyRepository.findAll().stream()
                .sorted(Comparator.comparingInt((Story s) -> s.getLikes().size())
                        .thenComparingInt(Story::getViewCount).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Story> findStoriesByKeyword(String keyword) {
        Set<Long> storyIds = storyRepository.findIdsByKeyword(keyword).stream().collect(Collectors.toSet());
        return storyRepository.findAllById(storyIds);
    }

    public Map<String, List<Story>> getGroupedStories() {
        List<Story> stories = getAllStories();

        Map<String, List<Story>> groupedStories = stories.stream()
                .filter(story -> !"test".equalsIgnoreCase(story.getLocation()))
                .collect(Collectors.groupingBy(Story::getLocation))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() >= 3)
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().subList(0, Math.min(3, entry.getValue().size()))));

        groupedStories.keySet().forEach(groupName -> {
            if (!storyGroupRepository.existsByGroupName(groupName)) {
                StoryGroup newGroup = new StoryGroup(groupName, null, null);
                storyGroupRepository.save(newGroup);
            }
        });
        return groupedStories;
    }

    private List<Story> getAllStories() {
        return storyRepository.findAll();
    }

    public List<Story> getStoriesByLocation(String location) {
        return storyRepository.findByLocation(location);
    }

    public List<Story> getStoriesByUser(User user) {
        return storyRepository.findByUsername(user.getUsername());
    }

    @Transactional
    public Story updateStory(Long storyId, String title, String description, String location) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new IllegalArgumentException("Story not found"));

        story.setTitle(title);
        story.setDescription(description);
        story.setLocation(location);
        story.setUploadTime(LocalDateTime.now());

        return storyRepository.save(story);
    }

    @Transactional(readOnly = true)
    public StoryDTO getStoryDetail(Long storyId, User user) {
        try {
            Story story = storyRepository.findById(storyId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid story ID: " + storyId));

            boolean isLiked = user != null && likeRepository.existsByStoryAndUser(story, user);
            boolean isBookmarked = user != null && bookmarkRepository.existsByStoryAndUser(story, user);

            User storyUser = userRepository.findByEmail(story.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + story.getUsername()));

            int bookmarksCount = story.getBookmarks().size();
            int likesCount = story.getLikesCount();
            int followersCount = storyUser.getFollowers().size();

            return new StoryDTO(
                    story.getStoryId(),
                    story.getTitle(),
                    story.getImage_url(),
                    story.getDescription(),
                    story.getLocation(),
                    story.getDate(),
                    story.getTags(),
                    likesCount,
                    bookmarksCount,
                    storyUser.getUsername(),
                    storyUser.getProfileImageUrl(),
                    followersCount,
                    isLiked, // 이 부분에서 올바르게 좋아요 상태 전달
                    isBookmarked // 이 부분에서 올바르게 북마크 상태 전달
            );
        } catch (Exception e) {  // 이 블록이 올바르게 try 블록과 연관되도록 수정
            e.printStackTrace();
            throw new RuntimeException("Error retrieving story details", e);
        }
    }

    public List<Story> searchStoriesByTitle(String title) {
        return storyRepository.findByTitleContainingIgnoreCase(title);
    }
    public List<Story> getSeasonStories(String season, int limit) {
        List<Story> stories = storyRepository.findAll();
        List<Story> seasonStories = stories.stream()
                .filter(story -> {
                    String dateStr = story.getDate();
                    if (dateStr == null || dateStr.isEmpty()) {
                        return false;
                    }

                    int month;
                    try {
                        month = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-M-d")).getMonthValue();
                    } catch (Exception e) {
                        return false;
                    }

                    switch (season) {
                        case "spring":
                            return month >= 3 && month <= 5;
                        case "summer":
                            return month >= 6 && month <= 8;
                        case "fall":
                            return month >= 9 && month <= 11;
                        case "winter":
                            return month == 12 || month <= 2;
                        default:
                            return false;
                    }
                })
                .limit(limit)
                .collect(Collectors.toList());

        return seasonStories;
    }
    public void deleteStoryById(Long storyId) {
        storyRepository.deleteById(storyId);
    }

    public boolean toggleLike(Long storyId, User user) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid story Id:" + storyId));

        Optional<LikeEntity> existingLike = likeRepository.findByStoryAndUser(story, user);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return false;
        } else {
            LikeEntity newLike = new LikeEntity();
            newLike.setStory(story);
            newLike.setUser(user);
            likeRepository.save(newLike);
            return true;
        }
    }

    public boolean toggleBookmark(Long storyId, User user) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid story Id:" + storyId));

        Optional<BookmarkEntity> existingBookmark = bookmarkRepository.findByStoryAndUser(story, user);
        if (existingBookmark.isPresent()) {
            bookmarkRepository.delete(existingBookmark.get());
            return false;
        } else {
            BookmarkEntity newBookmark = new BookmarkEntity();
            newBookmark.setStory(story);
            newBookmark.setUser(user);
            bookmarkRepository.save(newBookmark);
            return true;
        }
    }
}
