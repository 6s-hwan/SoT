package com.SoT.JIN.story;

import com.SoT.JIN.search.Search;
import com.SoT.JIN.search.SearchService;
import com.SoT.JIN.user.User;
import com.SoT.JIN.user.UserRepository;
import com.SoT.JIN.user.WriterController;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class StoryController {
    private static final Logger logger = LoggerFactory.getLogger(StoryController.class);
    private final StoryRepository storyRepository;
    private final S3Service s3Service;
    private final StoryService storyService;
    private final UserRepository userRepository;
    private final SearchService searchService;
    private final WriterController writerController;
    private final OpenAIService openAIService;

    @Autowired
    public StoryController(StoryRepository storyRepository, S3Service s3Service, StoryService storyService, UserRepository userRepository, SearchService searchService, WriterController writerController, OpenAIService openAIService) {
        this.storyRepository = storyRepository;
        this.s3Service = s3Service;
        this.storyService = storyService;
        this.userRepository = userRepository;
        this.searchService = searchService;
        this.writerController = writerController;
        this.openAIService = openAIService;
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
                            .filter(story -> story.getUploadTime() != null)
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

    @GetMapping("/bookmark")
    public String bookmark() {
        return "BookmarkPage";
    }

    @GetMapping("/home")
    public String home(Model model, @RequestParam(defaultValue = "12") int limit) {
        List<Story> topStories = storyService.getTopStories(limit);
        model.addAttribute("stories", topStories);
        model.addAttribute("limit", limit);

        List<WriterController.WriterInfo> popularWriters = writerController.fetchPopularWriters(6);

        model.addAttribute("popularWriters", popularWriters);

        return "Home";
    }

    @GetMapping("/test")
    public String test(Model model) {
        List<Search> topSearches = searchService.getTopSearchKeywords(8);

        List<Story> topStories = topSearches.stream()
                .map(search -> searchService.getStoryWithSmallestId(search.getKeyword()))
                .collect(Collectors.toList());

        List<WriterController.WriterInfo> popularWriters = writerController.fetchPopularWriters(6);

        model.addAttribute("topSearches", topSearches);
        model.addAttribute("topStories", topStories);
        model.addAttribute("popularWriters", popularWriters);

        return "test";
    }

    @PostMapping("/upload")
    public String addStory(@RequestParam("image_url") String imageUrl,
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

        // OpenAI를 통해 테마 예측
        String theme = openAIService.predictTheme(title, location, description, tags);

        // 유효한 테마인지 확인
        String[] validThemes = {"자연 속 여행", "역사와 문화", "식도락 여행", "축제", "예술 및 체험", "산악 여행", "도심 속 여행", "바다와 해변", "테마파크"};
        if (!isValidTheme(theme, validThemes)) {
            return "redirect:/upload?error=Invalid theme returned: " + theme;
        }

        story.setTheme(theme);

        storyRepository.save(story);
        Long storyId = story.getStoryId();
        return "redirect:/story/" + storyId;
    }

    @PostMapping("/batch/updateThemes")
    @ResponseBody
    public String updateThemesForExistingStories() {
        List<Story> stories = storyRepository.findAll();
        String[] validThemes = {"자연 속 여행", "역사와 문화", "식도락 여행", "축제", "예술 및 체험", "산악 여행", "도심 속 여행", "바다와 해변", "테마파크"};

        for (Story story : stories) {
            if (story.getTheme() == null || story.getTheme().isEmpty()) {
                String theme = openAIService.predictTheme(story.getTitle(), story.getLocation(), story.getDescription(), story.getTags());
                if (isValidTheme(theme, validThemes)) {
                    story.setTheme(theme);
                    storyRepository.save(story);
                } else {
                    logger.warn("Invalid theme returned for story ID {}: {}", story.getStoryId(), theme);
                }
            }
        }

        return "Themes updated for existing stories.";
    }

    private boolean isValidTheme(String theme, String[] validThemes) {
        for (String validTheme : validThemes) {
            if (validTheme.equals(theme)) {
                return true;
            }
        }
        return false;
    }

    @GetMapping("/presigned-url")
    @ResponseBody
    public String getURL(@RequestParam String filename) {
        String result = s3Service.createPresignedUrl("test/" + filename);
        logger.info("Presigned URL generated: {}", result);
        return result;
    }

    @GetMapping("/rise/{keyword}")
    public String getStoriesByKeyword(@PathVariable String keyword, Model model) {
        List<Story> stories = storyService.findStoriesByKeyword(keyword);
        model.addAttribute("stories", stories);
        return "RiseDetailPage";
    }

    @GetMapping("/local")
    public String getStories(Model model) {
        Map<String, List<Story>> groupedStories = storyService.getGroupedStories();
        List<WriterController.WriterInfo> popularWriters = writerController.fetchPopularWriters(6);
        model.addAttribute("groupedStories", groupedStories);
        return "localList";
    }
}
