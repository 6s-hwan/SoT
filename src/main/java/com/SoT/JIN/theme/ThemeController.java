package com.SoT.JIN.theme;

import com.SoT.JIN.story.Story;
import com.SoT.JIN.story.StoryRepository;
import com.SoT.JIN.user.WriterController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ThemeController {

    @Autowired
    private StoryRepository storyRepository;
    private final WriterController writerController;

    public ThemeController(WriterController writerController) {
        this.writerController = writerController;
    }

    @GetMapping("/theme")
    public String getAllStories(@RequestParam(name = "sort", required = false) String sortCriteria, Model model) {
        List<Story> stories;

        // 정렬 기준에 따라 스토리를 가져옴
        if ("likes".equals(sortCriteria)) {
            stories = storyRepository.findAllByOrderByLikesDesc();
        } else if ("views".equals(sortCriteria)) {
            stories = storyRepository.findAllByOrderByViewCountDesc();
        } else if ("recent".equals(sortCriteria)) {
            stories = storyRepository.findAllByOrderByUploadTimeDesc();
        } else {
            stories = storyRepository.findAll();
        }

        List<WriterController.WriterInfo> popularWriters = writerController.fetchPopularWriters(6);

        model.addAttribute("stories", stories);
        model.addAttribute("selectedTheme", "모든 테마");
        return "ThemeDetailPage";
    }

    @GetMapping("/theme/{themeId}")
    public String getStoriesByTheme(@PathVariable int themeId, @RequestParam(name = "sort", required = false) String sortCriteria, Model model) {
        String[] themes = {"자연 속 여행", "역사와 문화", "식도락 여행", "축제", "예술 및 체험", "산악 여행", "도심 속 여행", "바다와 해변", "테마파크"};
        String selectedTheme = themes[themeId - 1];

        List<Story> stories;

        // 정렬 기준에 따라 스토리를 가져옴
        if ("likes".equals(sortCriteria)) {
            stories = storyRepository.findByThemeOrderByLikesDesc(selectedTheme);
        } else if ("views".equals(sortCriteria)) {
            stories = storyRepository.findByThemeOrderByViewCountDesc(selectedTheme);
        } else if ("recent".equals(sortCriteria)) {
            stories = storyRepository.findByThemeOrderByUploadTimeDesc(selectedTheme);
        } else {
            stories = storyRepository.findByTheme(selectedTheme);
        }

        model.addAttribute("stories", stories);
        model.addAttribute("selectedTheme", selectedTheme);
        return "ThemeDetailPage";
    }
}
