package com.SoT.JIN.search;

import com.SoT.JIN.story.Story;
import com.SoT.JIN.story.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SearchController {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public String search(@RequestParam("query") String query,
                         @RequestParam(name = "limit", defaultValue = "24") int limit,
                         Model model) {
        // 검색어 저장
        searchService.saveSearchKeyword(query);

        List<Story> stories = storyRepository.findByTitleContainingIgnoreCase(query);
        List<Story> limitedStories = stories.stream().limit(limit).collect(Collectors.toList());

        model.addAttribute("stories", limitedStories);
        model.addAttribute("query", query);
        model.addAttribute("resultCount", stories.size());
        model.addAttribute("limit", limit);

        return "Searchresultpage";
    }
}
