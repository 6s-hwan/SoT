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

import java.util.Collections;
import java.util.Comparator;
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
        List<Story> stories = storyRepository.findAll();

        // 정렬 기준에 따라 스토리를 정렬
        if ("likes".equals(sortCriteria)) {
            stories.sort((s1, s2) -> {
                int cmp = Integer.compare(
                        s2.getLikes() != null ? s2.getLikes().size() : 0,
                        s1.getLikes() != null ? s1.getLikes().size() : 0
                );
                if (cmp == 0) {
                    return Integer.compare(s2.getViewCount(), s1.getViewCount());
                }
                return cmp;
            });
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

        List<WriterController.WriterInfo> popularWriters = writerController.fetchPopularWriters(6);

        model.addAttribute("stories", stories);
        model.addAttribute("selectedTheme", "모든 테마");
        model.addAttribute("sortCriteria", sortCriteria);
        return "ThemeDetailPage";
    }

    @GetMapping("/theme/{themeId}")
    public String getStoriesByTheme(@PathVariable int themeId, @RequestParam(name = "sort", required = false) String sortCriteria, Model model) {
        String[] themes = {"자연 속 여행", "역사와 문화", "식도락 여행", "축제", "예술 및 체험", "산악 여행", "도심 속 여행", "바다와 해변", "테마파크"};
        String selectedTheme = themes[themeId - 1];

        List<Story> stories = storyRepository.findByTheme(selectedTheme);

        // 정렬 기준에 따라 스토리를 정렬
        if ("likes".equals(sortCriteria)) {
            stories.sort((s1, s2) -> {
                int cmp = Integer.compare(
                        s2.getLikes() != null ? s2.getLikes().size() : 0,
                        s1.getLikes() != null ? s1.getLikes().size() : 0
                );
                if (cmp == 0) {
                    return Integer.compare(s2.getViewCount(), s1.getViewCount());
                }
                return cmp;
            });
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

        model.addAttribute("stories", stories);
        model.addAttribute("selectedTheme", selectedTheme);
        model.addAttribute("sortCriteria", sortCriteria);
        return "ThemeDetailPage";
    }
}