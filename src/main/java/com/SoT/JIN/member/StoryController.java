package com.SoT.JIN.member;

import com.sun.security.auth.UserPrincipal;
import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StoryController {
    private final StoryRepository storyRepository;
    private final S3Service s3Service;

    @GetMapping({"/write"})
    String write() {
        return "write";
    }

    @GetMapping({"/test"})
    String test() {
        return "test";
    }

    @PostMapping({"/upload"})
    String addUser(@RequestParam("image_url") String image_url, @RequestParam("title") String title, @RequestParam("location") String location, @RequestParam("tags") String tags, @RequestParam("description") String description, Principal principal) {
        // 현재 인증된 사용자의 정보를 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // authentication의 principal이 UserDetails 타입인지 확인
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            Story story = new Story();
            story.setImage_url(image_url);
            story.setTitle(title);
            story.setLocation(location);
            story.setTags(tags);
            story.setDescription(description);
            story.setUsername(userDetails.getUsername());

            this.storyRepository.save(story);
            Long storyId = story.getStoryId();
            return "redirect:/story/" + storyId;
        } else {
            // 적절한 예외 처리 또는 에러 페이지 리디렉션
            throw new RuntimeException("User details not found");
        }
    }

    @GetMapping({"/presigned-url"})
    @ResponseBody
    String getURL(@RequestParam String filename) {
        String result = this.s3Service.createPresignedUrl("test/" + filename);
        System.out.println(result);
        return result;
    }

    @GetMapping({"/story/{id}"})
    public String showStoryDetails(@PathVariable Long id, Model model) {
        Story story = (Story)this.storyRepository.findById(id).orElse((Story)null);
        model.addAttribute("story", story);
        return "StoryDetails";
    }

    public StoryController(final StoryRepository storyRepository, final S3Service s3Service) {
        this.storyRepository = storyRepository;
        this.s3Service = s3Service;
    }
}
