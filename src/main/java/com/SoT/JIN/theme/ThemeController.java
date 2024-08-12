package com.SoT.JIN.theme;

import com.SoT.JIN.story.Story;
import com.SoT.JIN.story.StoryRepository;
import com.SoT.JIN.user.WriterController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class ThemeController {

    @Autowired
    private StoryRepository storyRepository;
    private final WriterController writerController;

    public ThemeController(WriterController writerController) {
        this.writerController = writerController;
    }

    @GetMapping("/theme")
    public String getStoriesByThemes(
            @RequestParam(name = "themes", required = false) List<Integer> themeIds,
            @RequestParam(name = "sort", required = false) String sortCriteria,
            Model model) {

        String[] themes = {"자연 속 여행", "역사와 문화", "식도락 여행", "축제", "예술 및 체험", "산악 여행", "도심 속 여행", "바다와 해변", "테마파크"};

        // 중복된 스토리를 제거하기 위해 Set 사용
        Set<Story> storySet = new HashSet<>();

        if (themeIds != null && !themeIds.isEmpty()) {
            for (int themeId : themeIds) {
                if (themeId >= 1 && themeId <= themes.length) {
                    String selectedTheme = themes[themeId - 1];
                    List<Story> stories = storyRepository.findByThemeContaining(selectedTheme);
                    storySet.addAll(stories); // 중복 제거
                }
            }
        } else {
            // 테마가 선택되지 않은 경우 모든 스토리를 가져옴
            storySet.addAll(storyRepository.findAll());
        }

        // Set을 List로 변환
        List<Story> storyList = new ArrayList<>(storySet);

        // 정렬 기준에 따라 스토리를 정렬
        sortStories(storyList, sortCriteria);

        List<WriterController.WriterInfo> popularWriters = writerController.fetchPopularWriters(6);

        model.addAttribute("stories", storyList);
        model.addAttribute("selectedTheme", themeIds != null ? "선택된 테마" : "모든 테마");
        model.addAttribute("sortCriteria", sortCriteria);
        return "ThemeDetailPage";
    }

    private void sortStories(List<Story> stories, String sortCriteria) {
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
    }
}