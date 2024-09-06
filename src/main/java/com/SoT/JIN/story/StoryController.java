package com.SoT.JIN.story;

import com.SoT.JIN.rising.Rising;
import com.SoT.JIN.rising.RisingService;
import com.SoT.JIN.search.Search;
import com.SoT.JIN.search.SearchService;
import com.SoT.JIN.user.User;
import com.SoT.JIN.user.UserRepository;
import com.SoT.JIN.user.WriterController;
import com.SoT.JIN.story.StoryGroupRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import java.util.ArrayList;
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
    private final StoryGroupRepository storyGroupRepository;
    private final RisingService risingService; // RisingService 추가

    @Autowired
    public StoryController(StoryRepository storyRepository, S3Service s3Service, StoryService storyService,
                           UserRepository userRepository, SearchService searchService, WriterController writerController,
                           OpenAIService openAIService, StoryGroupRepository storyGroupRepository,
                           RisingService risingService) { // RisingService 생성자에 추가
        this.storyRepository = storyRepository;
        this.s3Service = s3Service;
        this.storyService = storyService;
        this.userRepository = userRepository;
        this.searchService = searchService;
        this.writerController = writerController;
        this.openAIService = openAIService;
        this.storyGroupRepository = storyGroupRepository;
        this.risingService = risingService; // RisingService 초기화
    }

    @PostMapping("/story/{storyId}/like")
    public ResponseEntity<Void> toggleLike(@PathVariable Long storyId, Principal principal) {
        User user = getUserFromPrincipal(principal);
        storyService.toggleLike(storyId, user);
        return ResponseEntity.ok().build();
    }

    private User getUserFromPrincipal(Principal principal) {
        String email = principal.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @GetMapping("/story/{id}")
    public String showStoryDetails(@PathVariable Long id, Model model) {
        Story story = storyService.getStoryAndIncrementViewCount(id);
        model.addAttribute("story", story);
        return "StoryDetailS";
    }

    @GetMapping("/season")
    public String season(@RequestParam(value = "season", required = false) String season,
                         @RequestParam(value = "sort", required = false) String sortCriteria,
                         @RequestParam(name = "limit", defaultValue = "24") int limit,
                         Model model) {
        List<Story> stories = storyRepository.findAll();
        List<Story> seasonStories;

        // 시즌별 필터링
        if (season != null && !season.isEmpty()) {  // 시즌 값이 null이거나 빈 문자열이 아닌 경우에만 필터링 적용
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
            // season 값이 없으면 전체 stories 반환
            seasonStories = stories;
        }

        // 정렬 기준 적용
        if (sortCriteria != null && !sortCriteria.isEmpty()) {  // sortCriteria가 null 또는 빈 문자열이 아닐 때만 적용
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

        // 페이징 처리: limit만큼 가져오기
        List<Story> limitedStories = seasonStories.stream().limit(limit).collect(Collectors.toList());

        model.addAttribute("seasonStories", limitedStories);
        model.addAttribute("selectedSeason", season);
        model.addAttribute("sortCriteria", sortCriteria);
        model.addAttribute("resultCount", seasonStories.size());
        model.addAttribute("limit", limit);

        // 총 좋아요와 조회수를 모델에 추가
        int totalLikes = seasonStories.stream().mapToInt(Story::getLikesCount).sum();
        int totalViews = seasonStories.stream().mapToInt(Story::getViewCount).sum();
        model.addAttribute("totalLikes", totalLikes);
        model.addAttribute("totalViews", totalViews);

        return "SeasonDetailPage";
    }
    private boolean isValidTheme(String theme, String[] validThemes) {
        for (String validTheme : validThemes) {
            if (validTheme.equals(theme)) {
                return true;
            }
        }
        return false;
    }
    @GetMapping("/best")
    public String best(Model model, @RequestParam(defaultValue = "24") int limit) {
        List<Story> bestStories = storyService.getBestStories(limit);
        model.addAttribute("stories", bestStories);
        model.addAttribute("limit", limit);
        return "BestStoryDetailPage";
    }
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/bookmark")
    public String bookmark() {
        return "BookmarkPage";
    }
    @GetMapping("/email")
    public String email() {
        return "fragments/emailcheck_popup";
    }
    @GetMapping("/email2")
    public String email2() {
        return "fragments/emailfind_popup";
    }@GetMapping("/join")
    public String join() {
        return "fragments/joinfinish_popup";
    }

    @GetMapping("/home")
    public String home(@RequestParam(value = "season", required = false) String season,
                       @RequestParam(defaultValue = "12") int limit,
                       Model model) {

        // Rising 엔티티에서 상위 8개의 검색어 가져오기
        List<Rising> topRisings = risingService.getTopRisings(8);
        logger.info("Rising 엔티티 개수: " + topRisings.size());

        // Rising 엔티티에서 가져온 검색어를 바탕으로 Story 가져오기
        List<Story> topStories = topRisings.stream()
                .map(rising -> searchService.getStoryWithSmallestId(rising.getKeyword()))
                .collect(Collectors.toList());

        // 베스트 스토리 가져오기
        List<Story> bestStories = storyService.getBestStories(limit);
        model.addAttribute("stories", bestStories);
        model.addAttribute("limit", limit);

        // 시즌별 스토리 필터링
        List<Story> seasonStories;
        if (season != null) {
            seasonStories = storyService.getSeasonStories(season, 6);
            model.addAttribute("selectedSeason", season);
        } else {
            // 시즌이 지정되지 않았을 경우 모든 스토리를 보여주기
            seasonStories = storyService.getBestStories(6); // 예시로 베스트 스토리 6개를 가져오도록 함
        }
        model.addAttribute("seasonStories", seasonStories);

        // 인기 작가 정보 가져오기
        List<WriterController.WriterInfo> popularWriters = writerController.fetchPopularWriters(6);

        // 모델에 데이터 추가
        model.addAttribute("topSearches", topRisings);
        model.addAttribute("topStories", topStories);
        model.addAttribute("popularWriters", popularWriters);

        return "Home";
    }

    @GetMapping("/test")
    public String test(Model model) {

        // Rising 엔티티에서 상위 8개의 검색어 가져오기
        List<Rising> topRisings = risingService.getTopRisings(8);
        logger.info("Rising 엔티티 개수: " + topRisings.size());

        // Rising 엔티티에서 가져온 검색어를 바탕으로 Story 가져오기
        List<Story> topStories = topRisings.stream()
                .map(rising -> searchService.getStoryWithSmallestId(rising.getKeyword()))
                .collect(Collectors.toList());

        // 인기 작가 가져오기
        List<WriterController.WriterInfo> popularWriters = writerController.fetchPopularWriters(6);

        // 모델에 데이터 추가
        model.addAttribute("topSearches", topRisings);
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

        // OpenAI를 통해 여러 테마 예측
        List<String> themes = new ArrayList<>(openAIService.predictThemes(title, location, description, tags));

        // 유효한 테마들만 필터링
        String[] validThemes = {"자연 속 여행", "역사와 문화", "식도락 여행", "축제", "예술 및 체험", "산악 여행", "도심 속 여행", "바다와 해변", "테마파크"};
        themes.removeIf(theme -> !isValidTheme(theme, validThemes));

        // 유효한 테마가 없으면 기본 테마 "기타"를 설정
        if (themes.isEmpty()) {
            themes.add("기타");
        }

        // 선택된 테마들을 콤마로 연결하여 저장
        story.setTheme(String.join(", ", themes));

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
            // 새로운 테마를 예측
            List<String> themes = openAIService.predictThemes(story.getTitle(), story.getLocation(), story.getDescription(), story.getTags());

            // themes 리스트가 불변일 수 있으므로, 가변 리스트로 변환
            themes = new ArrayList<>(themes);

            // 유효한 테마들만 필터링
            themes.removeIf(theme -> !isValidTheme(theme, validThemes));

            if (!themes.isEmpty()) {
                // 선택된 테마들을 콤마로 연결하여 저장
                story.setTheme(String.join(", ", themes));
                storyRepository.save(story);
            } else {
                logger.warn("Invalid themes returned for story ID {}: {}", story.getStoryId(), themes);
            }
        }

        return "Themes updated for existing stories.";
    }


    @GetMapping("/presigned-url")
    @ResponseBody
    public String getURL(@RequestParam String filename) {
        String result = s3Service.createPresignedUrl("test/" + filename);
        logger.info("Presigned URL generated: {}", result);
        return result;
    }


    @GetMapping("/local")
    public String getStories(Model model) {
        Map<String, List<Story>> groupedStories = storyService.getGroupedStories();
        model.addAttribute("groupedStories", groupedStories);
        return "localList";
    }

    @GetMapping("/localDetailPage")
    public String getLocalDetailPage(@RequestParam("location") String location,
                                     @RequestParam(value = "sort", required = false) String sortCriteria,
                                     @RequestParam(name = "limit", defaultValue = "24") int limit,
                                     Model model) {
        // 1. 해당 위치에 해당하는 StoryGroup 조회
        StoryGroup storyGroup = storyGroupRepository.findByGroupName(location)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        // 2. 해당 위치에 있는 스토리들 가져오기
        List<Story> stories = storyService.getStoriesByLocation(location);

        // 3. 정렬 기준에 따라 스토리 정렬
        if ("likes".equals(sortCriteria)) {
            stories.sort((s1, s2) -> {
                int likesComparison = Integer.compare(
                        (s2.getLikes() != null ? s2.getLikes().size() : 0),
                        (s1.getLikes() != null ? s1.getLikes().size() : 0)
                );

                // 좋아요 수가 같다면 조회수로 정렬
                if (likesComparison == 0) {
                    return Integer.compare(s2.getViewCount(), s1.getViewCount());
                }

                return likesComparison;
            });
        } else if ("views".equals(sortCriteria)) {
            stories.sort((s1, s2) -> {
                int viewsComparison = Integer.compare(s2.getViewCount(), s1.getViewCount());

                // 조회수가 같다면 좋아요 수로 정렬
                if (viewsComparison == 0) {
                    return Integer.compare(
                            (s2.getLikes() != null ? s2.getLikes().size() : 0),
                            (s1.getLikes() != null ? s1.getLikes().size() : 0)
                    );
                }

                return viewsComparison;
            });
        } else if ("recent".equals(sortCriteria)) {
            stories.sort((s1, s2) -> {
                if (s2.getUploadTime() == null && s1.getUploadTime() == null) return 0;
                if (s2.getUploadTime() == null) return -1;
                if (s1.getUploadTime() == null) return 1;
                return s2.getUploadTime().compareTo(s1.getUploadTime());
            });
        }

        // 4. 정렬된 스토리 중 limit 만큼 가져오기
        List<Story> limitedStories = stories.stream().limit(limit).collect(Collectors.toList());

        // 5. 모델에 데이터 추가
        model.addAttribute("location", location);
        model.addAttribute("stories", limitedStories);
        model.addAttribute("storyGroup", storyGroup);
        model.addAttribute("resultCount", stories.size()); // 총 스토리 수
        model.addAttribute("limit", limit);
        model.addAttribute("sortCriteria", sortCriteria);

        return "localDetailPage"; // 해당 템플릿 페이지로 이동
    }

}
