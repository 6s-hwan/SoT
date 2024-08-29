package com.SoT.JIN.rising;

import com.SoT.JIN.story.Story;
import com.SoT.JIN.story.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RisingController {

   @Autowired
    private final RisingService risingService;
    private final StoryService storyService;

    @Autowired
    public RisingController(RisingService risingService, StoryService storyService) {
        this.risingService = risingService;
        this.storyService = storyService;
    }

    @GetMapping("/rise/{keyword}")
    public String getStoriesByKeyword(@PathVariable String keyword, @RequestParam(value = "sort", required = false) String sortCriteria,
                                      @RequestParam(name = "limit", defaultValue = "24") int limit, Model model) {
        Rising rising = risingService.findByKeyword(keyword)
                .orElseThrow(() -> new IllegalArgumentException("급상승 키워드를 찾을 수 없습니다: " + keyword));

        List<Rising> otherRisings = risingService.getOtherTopRisings(keyword, 6);

        List<Story> stories = storyService.findStoriesByKeyword(keyword);

        if ("likes".equals(sortCriteria)) {
            stories.sort((s1, s2) -> Integer.compare(
                    (s2.getLikes() != null ? s2.getLikes().size() : 0),
                    (s1.getLikes() != null ? s1.getLikes().size() : 0)
            ));
        } else if ("views".equals(sortCriteria)) {
            stories.sort((s1, s2) -> Integer.compare(s2.getViewCount(), s1.getViewCount()));
        } else if ("recent".equals(sortCriteria)) {
            stories.sort((s1, s2) -> {
                if (s2.getUploadTime() == null && s1.getUploadTime() == null) return 0;
                if (s2.getUploadTime() == null) return -1;
                if (s1.getUploadTime() == null) return 1;
                return s2.getUploadTime().compareTo(s1.getUploadTime());
            });
        }

        List<Story> limitedStories = stories.stream().limit(limit).collect(Collectors.toList());

        model.addAttribute("resultCount", stories.size());
        model.addAttribute("rising", rising);
        model.addAttribute("stories", limitedStories);
        model.addAttribute("limit", limit);
        model.addAttribute("sortCriteria", sortCriteria);
        model.addAttribute("otherRisings", otherRisings);

        return "RiseDetailPage";
    }
}