package com.SoT.JIN.story;

import com.SoT.JIN.bookmark.BookmarkEntity;
import com.SoT.JIN.bookmark.BookmarkRepository;
import com.SoT.JIN.like.LikeEntity;
import com.SoT.JIN.like.LikeRepository;
import com.SoT.JIN.user.User;
import com.SoT.JIN.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        // 필요한 만큼의 페이지 크기 설정 (예: 100개씩 가져오기)
        Pageable pageable = PageRequest.of(0, 100);  // 100개씩 가져옴
        List<Long> storyIds = storyRepository.findIdsByKeyword(keyword, pageable);

        if (storyIds.isEmpty()) {
            return Collections.emptyList();  // ID가 없으면 빈 리스트 반환
        }

        // ID 리스트를 Set으로 변환 후, 모든 스토리를 조회
        Set<Long> storyIdSet = storyIds.stream().collect(Collectors.toSet());
        return storyRepository.findAllById(storyIdSet);  // 해당 ID들로 스토리 조회
    }

    public Map<String, List<Story>> getGroupedStories() {
        List<Story> stories = getAllStories();

        // 스토리 필터링 및 그룹화
        Map<String, List<Story>> groupedStories = stories.stream()
                .filter(story -> !"test".equalsIgnoreCase(story.getLocation())) // "test" 위치 제외
                .collect(Collectors.groupingBy(Story::getLocation))             // 위치별 그룹화
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() >= 3)                  // 최소 3개 이상의 그룹만 유지
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().subList(0, Math.min(3, entry.getValue().size()))));

        // 그룹 생성 및 저장
        groupedStories.keySet().forEach(groupName -> {
            if (!storyGroupRepository.existsByGroupName(groupName)) {
                // groupImage URL을 그룹 이름에 기반하여 생성
                String groupImage = generateGroupImageURL(groupName);
                StoryGroup newGroup = new StoryGroup(groupName, null, groupImage); // 그룹명만 사용하고, 영문명과 이미지는 자동 설정
                storyGroupRepository.save(newGroup);
            }
        });

        return groupedStories;
    }

    // 그룹 이미지 URL 생성 메소드
    private String generateGroupImageURL(String groupName) {
        // 그룹 이름을 기반으로 URL 생성 (예: 그룹 이름에 따른 이미지 URL 생성)
        return "https://soteulji.s3.ap-northeast-2.amazonaws.com/test/regionmain.png";
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

            // 현재 사용자가 이 스토리의 작성자인지 여부를 확인
            boolean isOwnStory = user != null && user.getUsername().equals(story.getUsername());

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
                    isLiked, // 좋아요 상태
                    isBookmarked, // 북마크 상태
                    isOwnStory // 자신의 스토리 여부 추가
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving story details", e);
        }
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
