package com.SoT.JIN.rising;

import com.SoT.JIN.story.Story;
import com.SoT.JIN.story.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class RisingController {

    private final RisingService risingService;
    private final StoryService storyService;

    @Autowired
    public RisingController(RisingService risingService, StoryService storyService) {
        this.risingService = risingService;
        this.storyService = storyService;
    }

    @GetMapping("/rise/{keyword}")
    public String getStoriesByKeyword(@PathVariable String keyword,
                                      @RequestParam(value = "sort", required = false) String sortCriteria,
                                      @RequestParam(name = "limit", defaultValue = "24") int limit,
                                      Model model) {
        // 1. 키워드에 해당하는 Rising 엔티티 조회
        Rising rising = risingService.findByKeyword(keyword)
                .orElseThrow(() -> new IllegalArgumentException("급상승 키워드를 찾을 수 없습니다: " + keyword));

        // 2. 자신을 제외한 상위 6개의 다른 급상승 키워드에 해당하는 Story 가져오기
        Map<Rising, Story> otherTopStories = risingService.getTopStoriesExcludingKeyword(keyword, 6);

        // 3. 검색어에 해당하는 스토리 가져오기
        List<Story> stories = storyService.findStoriesByKeyword(keyword);

// 4. 정렬 기준에 따라 스토리 정렬
        if ("likes".equals(sortCriteria)) {
            stories.sort((s1, s2) -> {
                int likesComparison = Integer.compare(
                        (s2.getLikes() != null ? s2.getLikes().size() : 0),
                        (s1.getLikes() != null ? s1.getLikes().size() : 0)
                );

                // 좋아요 수가 같다면 조회수로 정렬
                if (likesComparison == 0) {
                    return Integer.compare(s2.getViewCount(), s1.getViewCount());
                }

                return likesComparison;
            });
        } else if ("views".equals(sortCriteria)) {
            stories.sort((s1, s2) -> {
                int viewsComparison = Integer.compare(s2.getViewCount(), s1.getViewCount());

                // 조회수가 같다면 좋아요 수로 정렬
                if (viewsComparison == 0) {
                    return Integer.compare(
                            (s2.getLikes() != null ? s2.getLikes().size() : 0),
                            (s1.getLikes() != null ? s1.getLikes().size() : 0)
                    );
                }

                return viewsComparison;
            });
        } else if ("recent".equals(sortCriteria)) {
            stories.sort((s1, s2) -> {
                if (s2.getUploadTime() == null && s1.getUploadTime() == null) return 0;
                if (s2.getUploadTime() == null) return -1;
                if (s1.getUploadTime() == null) return 1;
                return s2.getUploadTime().compareTo(s1.getUploadTime());
            });
        }

        // 5. 정렬된 스토리 중 limit 만큼 가져오기
        List<Story> limitedStories = stories.stream().limit(limit).collect(Collectors.toList());

        // 6. 모델에 데이터 추가
        model.addAttribute("resultCount", stories.size());
        model.addAttribute("rising", rising);
        model.addAttribute("stories", limitedStories);
        model.addAttribute("limit", limit);
        model.addAttribute("sortCriteria", sortCriteria);
        model.addAttribute("otherTopStories", otherTopStories); // 상위 6개의 급상승 키워드에 해당하는 스토리들

        return "RiseDetailPage";
    }
}