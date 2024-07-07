package com.SoT.JIN.member;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
public class StoryController {
    private static final Logger logger = LoggerFactory.getLogger(StoryController.class);
    private final StoryRepository storyRepository;
    private final S3Service s3Service;
    private final StoryService storyService;
    private final UserRepository userRepository; // UserRepository 추가

    @Autowired
    public StoryController(StoryRepository storyRepository, S3Service s3Service, StoryService storyService, UserRepository userRepository) {
        this.storyRepository = storyRepository;
        this.s3Service = s3Service;
        this.storyService = storyService;
        this.userRepository = userRepository; // UserRepository 주입
    }

    @PostMapping("/story/{storyId}/like")
    public String toggleLike(@PathVariable Long storyId, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByEmail(userDetails.getUsername());

        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        storyService.toggleLike(storyId, user);

        return "redirect:/story/" + storyId;
    }

    @GetMapping("/story/{id}")
    public String showStoryDetails(@PathVariable Long id, Model model) {
        Story story = storyService.getStoryAndIncrementViewCount(id);
        model.addAttribute("story", story);
        return "StoryDetails";
    }

    @GetMapping("/write")
    public String write() {
        return "write";
    }

    @GetMapping("/rise")
    public String rise() {
        return "RiseDetailPage";
    }

    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/best")
    public String best() {
        return "BestStoryDetailPage";
    }
    @GetMapping("/popular")
    public String popular() {
        return "PopularWriter";
    }

    @PostMapping("/upload")
    public String addUser(@RequestParam("image_url") String imageUrl,
                          @RequestParam("title") String title,
                          @RequestParam("date_year") String dateYear,
                          @RequestParam("date_month") String dateMonth,
                          @RequestParam("date_day") String dateDay,
                          @RequestParam("location") String location,
                          @RequestParam("tags") String tags,
                          @RequestParam("description") String description,
                          Principal principal) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Story story = new Story();
        story.setImage_url(imageUrl);
        story.setTitle(title);
        String date = dateYear + "-" + dateMonth + "-" + dateDay;
        story.setDate(date);
        story.setLocation(location);
        story.setTags(tags);
        story.setDescription(description);
        story.setUsername(userDetails.getUsername());

        storyRepository.save(story);
        Long storyId = story.getStoryId();
        return "redirect:/story/" + storyId;
    }

    @GetMapping("/presigned-url")
    @ResponseBody
    public String getURL(@RequestParam String filename) {
        String result = s3Service.createPresignedUrl("test/" + filename);
        logger.info("Presigned URL generated: {}", result);
        return result;
    }
}
