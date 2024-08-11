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
public class BackStoryController {

    @Autowired
    private StoryRepository storyRepository;
    private final WriterController writerController;

    public BackStoryController(WriterController writerController) {
        this.writerController = writerController;
    }

    @GetMapping("/BackStory")
    public String getAllStories(@RequestParam(name = "sort", required = false) String sortCriteria, Model model) {
        // 모든 스토리를 데이터베이스에서 가져옴
        List<Story> stories = storyRepository.findAll();

        // 선택된 정렬 기준에 따라 스토리 리스트를 정렬
        sortStories(stories, sortCriteria);

//        인기 있는 작가 정보를 가져옴
//        List<WriterController.WriterInfo> popularWriters = writerController.fetchPopularWriters(6);

        // 모델에 속성을 추가하여 뷰에서 사용할 수 있도록 함
        model.addAttribute("stories", stories);
        model.addAttribute("selectedTheme", "모든 테마");
        model.addAttribute("sortCriteria", sortCriteria);

        // "AllStory"라는 이름의 뷰를 반환함
        return "AllStory";
    }

    private void sortStories(List<Story> stories, String sortCriteria) {
    }

}


