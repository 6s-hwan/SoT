package com.SoT.JIN.story;

import com.SoT.JIN.like.LikeEntity;
import com.SoT.JIN.like.LikeRepository;
import com.SoT.JIN.user.User;
import com.SoT.JIN.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class StoryService {
    private final StoryRepository storyRepository;
    private final LikeRepository likeRepository;
    private final SearchService searchService;
    @Autowired
    public StoryService(StoryRepository storyRepository, LikeRepository likeRepository, SearchService searchService) {
        this.storyRepository = storyRepository;
        this.likeRepository = likeRepository;
        this.searchService = searchService;
    }
    @Transactional
    public Story getStoryAndIncrementViewCount(Long id) {
        Story story = storyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
        story.setViewCount(story.getViewCount() + 1);
        return story;
        }

    public void toggleLike(Long storyId, User user) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new IllegalArgumentException("Story not found"));

        Optional<LikeEntity> existingLike = likeRepository.findByStoryAndUser(story, user);
        if (existingLike.isPresent()) {
            story.getLikes().remove(existingLike.get());
            existingLike.get().setStory(null);
            existingLike.get().setUser(null);
            likeRepository.delete(existingLike.get());
            story.setViewCount(story.getViewCount() - 1);
        } else {
            LikeEntity newLike = new LikeEntity();
            newLike.setStory(story);
            newLike.setUser(user);
            story.getLikes().add(newLike);
            likeRepository.save(newLike);
            story.setViewCount(story.getViewCount() - 1);
        }
        storyRepository.save(story);
    }

    public List<Story> getTopStories(int limit) {
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
        List<Story> stories = getAllStories(); // DB에서 모든 스토리 가져오기

        // Location 값이 "test"가 아닌 경우에만 그룹화
        Map<String, List<Story>> groupedStories = stories.stream()
                .filter(story -> !"test".equalsIgnoreCase(story.getLocation())) // "test" 값을 가진 스토리는 제외
                .collect(Collectors.groupingBy(Story::getLocation))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() >= 3) // 3개 이상의 스토리만 필터링
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().subList(0, Math.min(3, entry.getValue().size()))));

        return groupedStories;
    }

    // 모든 스토리를 가져오는 메서드 (구현 예시)
    private List<Story> getAllStories() {
        // DB에서 스토리 가져오는 로직이 들어갑니다.
        return storyRepository.findAll(); // 예시로 storyRepository를 사용
    }

}
