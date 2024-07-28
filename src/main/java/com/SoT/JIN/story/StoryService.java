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
    public List<Story> getAllStories() {
        return storyRepository.findAll();
    }

    public Map<String, List<Story>> getGroupedStories() {
        List<Story> stories = getAllStories();
        Pattern specificPattern = Pattern.compile(".*(시|도) .*(구|시)");

        // 위치 정보를 표준화
        List<Story> standardizedStories = stories.stream()
                .map(story -> {
                    story.setLocation(mapToProperLocation(story.getLocation()));
                    return story;
                })
                .collect(Collectors.toList());

        // 특정 형식의 위치 정보를 그룹화
        Map<String, List<Story>> groupedStories = standardizedStories.stream()
                .filter(story -> specificPattern.matcher(story.getLocation()).matches())
                .collect(Collectors.groupingBy(Story::getLocation))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() >= 3)
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().subList(0, Math.min(3, entry.getValue().size()))));

        // 특정 형식이 아닌 경우를 처리하는 로직 추가
        Map<String, List<Story>> genericGroup = standardizedStories.stream()
                .filter(story -> isValidGenericLocation(story.getLocation()))
                .collect(Collectors.groupingBy(story -> getGenericGroupName(story.getLocation())))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() >= 3)
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().subList(0, Math.min(3, entry.getValue().size()))));

        groupedStories.putAll(genericGroup);

        return groupedStories;
    }

    private String mapToProperLocation(String location) {
        // 표준화된 지역 이름 목록
        Map<String, String> locationMap = Map.of(
                "서울", "서울특별시",
                "경기", "경기도",
                "강원", "강원도",
                "부산", "부산광역시",
                "인천", "인천광역시",
                "대구", "대구광역시",
                "대전", "대전광역시",
                "울산", "울산광역시",
                "광주", "광주광역시",
                "세종", "세종특별자치시"
        );

        for (Map.Entry<String, String> entry : locationMap.entrySet()) {
            if (location.startsWith(entry.getKey()) && !location.startsWith(entry.getValue())) {
                return location.replaceFirst(entry.getKey(), entry.getValue());
            }
        }
        return location;
    }

    private boolean isValidGenericLocation(String location) {
        Pattern validPattern = Pattern.compile(".*(서울|경기|강원|부산|인천|대구|대전|울산|광주|세종).*");
        return validPattern.matcher(location).matches() && !location.matches(".*(시|도) .*(구|시)");
    }

    private String getGenericGroupName(String location) {
        Map<String, String> locationMap = Map.of(
                "서울특별시", "서울특별시 어딘가",
                "경기도", "경기도 어딘가",
                "강원도", "강원도 어딘가",
                "부산광역시", "부산광역시 어딘가",
                "인천광역시", "인천광역시 어딘가",
                "대구광역시", "대구광역시 어딘가",
                "대전광역시", "대전광역시 어딘가",
                "울산광역시", "울산광역시 어딘가",
                "광주광역시", "광주광역시 어딘가",
                "세종특별자치시", "세종특별자치시 어딘가"
        );

        for (Map.Entry<String, String> entry : locationMap.entrySet()) {
            if (location.startsWith(entry.getKey()) && !location.matches(entry.getKey() + " .*" + (entry.getKey().equals("경기도") ? "시" : "구"))) {
                return entry.getValue();
            }
        }
        return "기타";
    }
}
