package com.SoT.JIN.story;

import com.SoT.JIN.like.LikeEntity;
import com.SoT.JIN.like.LikeRepository;
import com.SoT.JIN.user.User;
import com.SoT.JIN.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
}
