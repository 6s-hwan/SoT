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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class StoryController {
    private static final Logger logger = LoggerFactory.getLogger(StoryController.class);
    private final StoryRepository storyRepository;
    private final S3Service s3Service;
    private final StoryService storyService;
    private final UserRepository userRepository;

    @Autowired
    public StoryController(StoryRepository storyRepository, S3Service s3Service, StoryService StoryService, UserRepository userRepository) {
        this.storyRepository = storyRepository;
        this.s3Service = s3Service;
        this.storyService = StoryService;
        this.userRepository = userRepository;
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

    @GetMapping("/rise")
    public String rise() {
        return "RiseDetailPage";
    }

    @GetMapping("/season")
    public String season(@RequestParam(value = "season", required = false) String season,
                         @RequestParam(value = "sort", required = false) String sortCriteria,
                         Model model) {
        List<Story> stories = storyRepository.findAll();
        List<Story> seasonStories;

        if (season != null) {
            seasonStories = stories.stream()
                    .filter(story -> {
                        String dateStr = story.getDate();
                        if (dateStr == null || dateStr.isEmpty()) {
                            return false;
                        }

                        int month;
                        try {
                            month = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-M-d")).getMonthValue();
                        } catch (Exception e) {
                            return false;
                        }

                        switch (season) {
                            case "spring":
                                return month >= 3 && month <= 5;
                            case "summer":
                                return month >= 6 && month <= 8;
                            case "fall":
                                return month >= 9 && month <= 11;
                            case "winter":
                                return month == 12 || month <= 2;
                            default:
                                return false;
                        }
                    })
                    .collect(Collectors.toList());
        } else {
            seasonStories = stories;
        }

        // 정렬 기준에 따른 정렬
        if (sortCriteria != null) {
            switch (sortCriteria) {
                case "likes":
                    seasonStories.sort((s1, s2) -> Integer.compare(s2.getLikesCount(), s1.getLikesCount()));
                    break;
                case "views":
                    seasonStories.sort((s1, s2) -> Integer.compare(s2.getViewCount(), s1.getViewCount()));
                    break;
                case "recent":
                    seasonStories = seasonStories.stream()
                            .filter(story -> story.getUploadTime() != null) // UploadTime이 있는 스토리만 필터링
                            .sorted((s1, s2) -> s2.getUploadTime().compareTo(s1.getUploadTime()))
                            .collect(Collectors.toList());
                    break;
            }
        }

        model.addAttribute("seasonStories", seasonStories);
        model.addAttribute("selectedSeason", season);
        model.addAttribute("sortCriteria", sortCriteria);
        return "SeasonDetailPage";
    }

    @GetMapping("/best")
    public String best(Model model, @RequestParam(defaultValue = "24") int limit) {
        List<Story> topStories = storyService.getTopStories(limit);
        model.addAttribute("stories", topStories);
        model.addAttribute("limit", limit);
        return "BestStoryDetailPage";
    }
    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
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
