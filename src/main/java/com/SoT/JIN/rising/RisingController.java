package com.SoT.JIN.rising;

import com.SoT.JIN.story.Story;
import com.SoT.JIN.story.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    public String getStoriesByKeyword(@PathVariable String keyword, Model model) {
        // 키워드에 해당하는 Rising 엔티티 가져오기
        Rising rising = risingService.findByKeyword(keyword)
                .orElseThrow(() -> new IllegalArgumentException("급상승 키워드를 찾을 수 없습니다: " + keyword));

        // 해당 키워드에 대한 스토리 리스트 가져오기
        List<Story> stories = storyService.findStoriesByKeyword(keyword);

        // 모델에 데이터 추가
        model.addAttribute("resultCount", stories.size());
        model.addAttribute("rising", rising); // 메인 Rising 데이터를 모델에 추가
        model.addAttribute("stories", stories); // 현재 키워드의 Story 리스트를 모델에 추가


        return "RiseDetailPage"; // HTML 페이지로 전달
    }
}
