package com.SoT.JIN.user;

import com.SoT.JIN.story.Story;
import com.SoT.JIN.story.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Comparator;

@Controller
public class WriterController {

    private final UserRepository userRepository;
    private final StoryRepository storyRepository;

    @Autowired
    public WriterController(UserRepository userRepository, StoryRepository storyRepository) {
        this.userRepository = userRepository;
        this.storyRepository = storyRepository;
    }

    @GetMapping("/writer/{username}")
    public String writerProfile(@PathVariable("username") String username, Model model,
                                @RequestParam(name = "sort", required = false) String sortCriteria) {
        // 사용자 정보 조회
        User user = userRepository.findByUsername(username).orElse(null);

        if (user != null) {
            List<Story> userStories = storyRepository.findByUsername(user.getEmail());

            if ("likes".equals(sortCriteria)) {
                userStories.sort((s1, s2) -> {
                    int cmp = Integer.compare(s2.getLikesCount(), s1.getLikesCount());
                    if (cmp == 0) {
                        return Integer.compare(s2.getViewCount(), s1.getViewCount());
                    }
                    return cmp;
                });
            } else if ("views".equals(sortCriteria)) {
                userStories = storyRepository.findByUsernameOrderByViewCountDesc(user.getEmail());
            } else if ("recent".equals(sortCriteria)) {
                userStories = storyRepository.findByUsernameOrderByUploadTimeDesc(user.getEmail());
            } else {
                userStories = storyRepository.findByUsername(user.getEmail());
            }

            // 전체 스토리 수 계산
            int totalStories = userStories.size();

            // 전체 조회수와 좋아요 수 계산
            int totalViews = 0;
            int totalLikes = 0;
            for (Story story : userStories) {
                totalViews += story.getViewCount();
                totalLikes += story.getLikes().size(); // 좋아요 수는 List의 크기로 계산
            }

            // 최근 업로드 일수 계산
            long daysSinceLastUpload = calculateDaysSinceLastUpload(userStories);

            // 테마 카운트 맵 초기화
            Map<String, Integer> themeCountMap = new HashMap<>();

            // 모든 스토리의 테마 카운트 계산
            for (Story story : userStories) {
                String[] themes = story.getTags().split(",\\s*"); // 쉼표 뒤에 공백을 포함하여 분리
                for (String theme : themes) {
                    themeCountMap.put(theme, themeCountMap.getOrDefault(theme, 0) + 1);
                }
            }

            // 가장 많이 나온 테마와 두 번째로 많이 나온 테마 구하기
            String topTheme = "";
            String secondTheme = "";
            int maxCount = 0;
            int secondMaxCount = 0;

            for (Map.Entry<String, Integer> entry : themeCountMap.entrySet()) {
                int count = entry.getValue();
                if (count > maxCount) {
                    secondMaxCount = maxCount;
                    maxCount = count;
                    secondTheme = topTheme;
                    topTheme = entry.getKey();
                } else if (count > secondMaxCount) {
                    secondMaxCount = count;
                    secondTheme = entry.getKey();
                }
            }

            // 현재 로그인한 사용자가 이 작가를 팔로우하고 있는지 확인
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();
            User currentUser = userRepository.findByEmail(currentUserEmail).orElse(null);
            boolean isFollowing = currentUser != null && currentUser.getFollowing().contains(user);

            // 모델에 사용자 정보, 스토리 리스트, 전체 스토리 수, 조회수, 좋아요 수, 최근 업로드 일수, 가장 많은 테마 정보 추가
            model.addAttribute("user", user);
            model.addAttribute("stories", userStories);
            model.addAttribute("totalStories", totalStories);
            model.addAttribute("totalViews", totalViews);
            model.addAttribute("totalLikes", totalLikes);
            model.addAttribute("topTheme", topTheme);
            model.addAttribute("secondTheme", secondTheme);
            model.addAttribute("daysSinceLastUpload", daysSinceLastUpload);
            model.addAttribute("followCount", user.getFollowers().size());
            model.addAttribute("isFollowing", isFollowing);

            return "writerpage"; // writer-profile.html로 이동
        }

        return "redirect:/error"; // 사용자를 찾을 수 없으면 에러 페이지로 리다이렉트
    }

    @GetMapping("/popular")
    public String getPopularWriters(Model model) {
        List<WriterInfo> writerInfos = userRepository.findAll().stream()
                .map(user -> {
                    List<Story> userStories = storyRepository.findByUsername(user.getEmail());
                    int totalLikes = userStories.stream().mapToInt(Story::getLikesCount).sum();
                    int totalViews = userStories.stream().mapToInt(Story::getViewCount).sum();
                    int totalStories = userStories.size();

                    // 테마 카운트 계산
                    Map<String, Integer> themeCountMap = new HashMap<>();
                    for (Story story : userStories) {
                        String[] themes = story.getTags().split(",\\s*");
                        for (String theme : themes) {
                            if (!theme.trim().isEmpty()) {
                                themeCountMap.put(theme, themeCountMap.getOrDefault(theme, 0) + 1);
                            }
                        }
                    }

                    // 가장 많이 나온 테마와 두 번째로 많이 나온 테마 구하기
                    String topTheme = "";
                    String secondTheme = "";
                    int maxCount = 0;
                    int secondMaxCount = 0;
                    for (Map.Entry<String, Integer> entry : themeCountMap.entrySet()) {
                        int count = entry.getValue();
                        if (count > maxCount) {
                            secondMaxCount = maxCount;
                            maxCount = count;
                            secondTheme = topTheme;
                            topTheme = entry.getKey();
                        } else if (count > secondMaxCount) {
                            secondMaxCount = count;
                            secondTheme = entry.getKey();
                        }
                    }

                    return new WriterInfo(user.getUsername(), user.getProfileImageUrl(), totalStories, totalLikes, totalViews, topTheme, secondTheme, 0);
                })
                .sorted(Comparator.comparingInt(WriterInfo::getTotalLikes)
                        .thenComparingInt(WriterInfo::getTotalViews).reversed()
                        .thenComparingInt(WriterInfo::getTotalStories))
                .limit(72)
                .collect(Collectors.toList());

        // 등수 설정
        for (int i = 0; i < writerInfos.size(); i++) {
            writerInfos.get(i).setRank(i + 1);
        }

        model.addAttribute("popularWriters", writerInfos);
        return "PopularWriter";
    }

    public class WriterInfo {
        private String username;
        private String profileImageUrl;
        private int totalStories;
        private int totalLikes;
        private int totalViews; // 조회수 필드 추가
        private String topTheme;
        private String secondTheme;
        private int rank; // 등수 필드 추가

        public WriterInfo(String username, String profileImageUrl, int totalStories, int totalLikes, int totalViews, String topTheme, String secondTheme, int rank) {
            this.username = username;
            this.profileImageUrl = profileImageUrl;
            this.totalStories = totalStories;
            this.totalLikes = totalLikes;
            this.totalViews = totalViews; // 생성자에 조회수 추가
            this.topTheme = topTheme;
            this.secondTheme = secondTheme;
            this.rank = rank; // 생성자에 등수 추가
        }

        public String getUsername() {
            return username;
        }

        public String getProfileImageUrl() {
            return profileImageUrl;
        }

        public int getTotalStories() {
            return totalStories;
        }

        public int getTotalLikes() {
            return totalLikes;
        }

        public int getTotalViews() {
            return totalViews; // 조회수 getter 추가
        }

        public String getTopTheme() {
            return topTheme;
        }

        public String getSecondTheme() {
            return secondTheme;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
    }

    private int calculateDaysSinceLastUpload(List<Story> userStories) {
        // 만약 userStories가 비어 있다면
        if (userStories.isEmpty()) {
            return 0; // 혹은 다른 의미있는 값을 리턴
        }

        // 최근 업로드 시간을 가져오기 위해 리스트에서 마지막 요소를 선택
        LocalDateTime mostRecentUploadTime = userStories.get(userStories.size() - 1).getUploadTime();

        // 만약 mostRecentUploadTime이 null이라면
        return mostRecentUploadTime == null ? 0 : (int) ChronoUnit.DAYS.between(mostRecentUploadTime.toLocalDate(), LocalDate.now());
    }
}
