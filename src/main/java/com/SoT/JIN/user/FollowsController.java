package com.SoT.JIN.user;

import com.SoT.JIN.story.Story;
import com.SoT.JIN.story.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class FollowsController {

    private final UserRepository userRepository;
    private final StoryRepository storyRepository;

    @Autowired
    public FollowsController(UserRepository userRepository, StoryRepository storyRepository) {
        this.userRepository = userRepository;
        this.storyRepository = storyRepository;
    }

    @GetMapping("/follows")
    public String getFollowingList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUserEmail).orElse(null);

        if (currentUser != null) {
            // 팔로우한 유저들의 리스트를 가져옴
            List<User> followingList = userRepository.findFollowingByUserId(currentUser.getUserId());

            // 각 유저의 정보를 처리
            List<FollowerInfo> followsInfos = followingList.stream()
                    .map(followingUser -> {
                        // 유저의 스토리 리스트 가져오기
                        List<Story> userStories = storyRepository.findByUsername(followingUser.getEmail());

                        // 스토리 수 계산
                        int totalStories = userStories.size();

                        // 좋아요 수 계산
                        int totalLikes = userStories.stream()
                                .mapToInt(story -> story.getLikes().size())
                                .sum();

                        // 마지막 업로드 날짜 계산
                        String lastUploadDate = calculateLastUploadDate(userStories);

                        // 주요 테마 계산 (2개로 제한)
                        Map<String, Integer> themeCountMap = new HashMap<>();
                        for (Story story : userStories) {
                            String[] themes = story.getTags().split(",\\s*");
                            for (String theme : themes) {
                                themeCountMap.put(theme, themeCountMap.getOrDefault(theme, 0) + 1);
                            }
                        }

                        List<String> topThemes = themeCountMap.entrySet().stream()
                                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                                .limit(2)
                                .map(Map.Entry::getKey)
                                .collect(Collectors.toList());

                        // 최근 스토리 8개 가져오기
                        List<Story> recentStories = userStories.stream()
                                .sorted((s1, s2) -> {
                                    LocalDateTime uploadTime1 = s1.getUploadTime();
                                    LocalDateTime uploadTime2 = s2.getUploadTime();
                                    if (uploadTime1 == null && uploadTime2 == null) return 0;
                                    if (uploadTime1 == null) return 1;
                                    if (uploadTime2 == null) return -1;
                                    return uploadTime2.compareTo(uploadTime1);
                                })
                                .limit(8)
                                .collect(Collectors.toList());

                        // 나의 팔로우 여부
                        boolean isFollowing = currentUser.getFollowing().contains(followingUser);

                        // FollowerInfo 객체 생성
                        return new FollowerInfo(followingUser.getUsername(), followingUser.getProfileImageUrl(),
                                followingUser.getFollowers().size(), totalStories, totalLikes, lastUploadDate,
                                topThemes, recentStories, isFollowing);
                    })
                    .collect(Collectors.toList());

            model.addAttribute("followsInfos", followsInfos);
            return "following"; // following.html 뷰로 이동
        }

        return "redirect:/error";
    }

    private String calculateLastUploadDate(List<Story> userStories) {
        if (userStories.isEmpty()) {
            return "No uploads"; // 업로드된 스토리가 없는 경우
        }

        return userStories.stream()
                .map(Story::getUploadTime)
                .filter(Objects::nonNull) // null을 필터링
                .max(LocalDateTime::compareTo)
                .map(uploadTime -> uploadTime.toLocalDate().toString()) // 년-월-일 형식으로 변환
                .orElse("No uploads"); // 업로드 시간이 모두 null인 경우
    }

    // FollowerInfo 클래스
    public class FollowerInfo {
        private String username;
        private String profileImageUrl;
        private int followerCount;
        private int totalStories;
        private int totalLikes;
        private String lastUploadDate;
        private List<String> topThemes;
        private List<Story> recentStories;
        private boolean isFollowing;

        public FollowerInfo(String username, String profileImageUrl, int followerCount, int totalStories, int totalLikes,
                            String lastUploadDate, List<String> topThemes, List<Story> recentStories, boolean isFollowing) {
            this.username = username;
            this.profileImageUrl = profileImageUrl;
            this.followerCount = followerCount;
            this.totalStories = totalStories;
            this.totalLikes = totalLikes;
            this.lastUploadDate = lastUploadDate;
            this.topThemes = topThemes;
            this.recentStories = recentStories;
            this.isFollowing = isFollowing;
        }

        // Getter 메서드들
        public String getUsername() {
            return username;
        }

        public String getProfileImageUrl() {
            return profileImageUrl;
        }

        public int getFollowerCount() {
            return followerCount;
        }

        public int getTotalStories() {
            return totalStories;
        }

        public int getTotalLikes() {
            return totalLikes;
        }

        public String getLastUploadDate() {
            return lastUploadDate;
        }

        public List<String> getTopThemes() {
            return topThemes;
        }

        public List<Story> getRecentStories() {
            return recentStories;
        }

        public boolean isFollowing() {
            return isFollowing;
        }
    }
}
