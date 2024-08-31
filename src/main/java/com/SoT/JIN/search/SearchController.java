package com.SoT.JIN.search;

import com.SoT.JIN.rising.RisingService;
import com.SoT.JIN.story.Story;
import com.SoT.JIN.story.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SearchController {

    private final StoryRepository storyRepository;
    private final SearchService searchService;
    private final RisingService risingService; // RisingService 선언

    @Autowired
    public SearchController(StoryRepository storyRepository, SearchService searchService, RisingService risingService) {
        this.storyRepository = storyRepository;
        this.searchService = searchService;
        this.risingService = risingService; // RisingService 주입
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query,
                         @RequestParam(name = "limit", defaultValue = "24") int limit,
                         Model model) {
        // 검색어 저장
        searchService.saveSearchKeyword(query);

        // 검색어에 따른 스토리 검색
        List<Story> stories = storyRepository.findByTitleContainingIgnoreCase(query);
        List<Story> limitedStories = stories.stream().limit(limit).collect(Collectors.toList());

        // 모델에 검색 결과 추가
        model.addAttribute("stories", limitedStories);
        model.addAttribute("query", query);
        model.addAttribute("resultCount", stories.size());
        model.addAttribute("limit", limit);

        return "Searchresultpage";
    }
    @GetMapping("/rise/top-searches")
    public String showTopSearches(Model model) {
        List<Search> topSearches = searchService.getTopSearchKeywords(8);

        List<Story> topStories = topSearches.stream()
                .map(search -> searchService.getStoryWithSmallestId(search.getKeyword()))
                .collect(Collectors.toList());

        model.addAttribute("topSearches", topSearches);
        model.addAttribute("topStories", topStories);

        return "Home";
    }
    // 수동으로 Rising 업데이트하는 테스트 메서드
    @GetMapping("/update-rising-now")
    @ResponseBody
    public String updateRisingNow() {
        risingService.updateRisingFromSearch();
        return "Rising entity has been updated!";
    }

    @GetMapping("/reset-rising-now")
    @ResponseBody
    public String resetRisingKeywordsnow() {
        searchService.resetSearchCounts();
        return "Rising entity has been updated!";
    }
}