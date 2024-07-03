package com.SoT.JIN.member;

import java.util.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {
    private final UserRepository userRepository;
    private final StoryRepository storyRepository;

    public MemberController(UserRepository userRepository, StoryRepository storyRepository) {
        this.userRepository = userRepository;
        this.storyRepository = storyRepository;
    }
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @PostMapping("/user")
    public String addUser(@RequestParam("email") String email,
                          @RequestParam("password") String password,
                          @RequestParam("username") String username,
                          @RequestParam("gender") String gender,
                          @RequestParam("birth_year") String birthYear,
                          @RequestParam("birth_month") String birthMonth,
                          @RequestParam("birth_day") String birthDay,
                          @RequestParam("phone1") String phone1,
                          @RequestParam("phone2") String phone2,
                          @RequestParam("phone3") String phone3) {

        String phone = phone1 + phone2 + phone3;
        String birth = birthYear + "-" + birthMonth + "-" + birthDay;

        User user = new User();
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setUsername(username);
        user.setGender(gender);
        user.setBirth(birth);
        user.setPhonenumber(phone);

        userRepository.save(user);
        return "redirect:/test";
    }

    @GetMapping("/my-page")
    public String myPage(Authentication authentication,
                         Model model,
                         @RequestParam(name = "sort", required = false) String sortCriteria) {

        String username = authentication.getName(); // 현재 인증된 사용자의 이메일을 가져옴

        // 사용자 정보 조회
        User user = userRepository.findByEmail(username).orElse(null);

        if (user != null) {
            List<Story> userStories;

            // 정렬 기준에 따라 스토리를 가져옴
            if (sortCriteria != null && sortCriteria.equals("likes")) {
                userStories = storyRepository.findByUsernameOrderByLikesDesc(username);
            } else {
                userStories = storyRepository.findByUsername(username);
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

            // 모델에 사용자 정보, 스토리 리스트, 전체 스토리 수, 조회수, 좋아요 수, 가장 많은 테마 정보 추가
            model.addAttribute("user", user);
            model.addAttribute("stories", userStories);
            model.addAttribute("totalStories", totalStories);
            model.addAttribute("totalViews", totalViews);
            model.addAttribute("totalLikes", totalLikes);
            model.addAttribute("topTheme", topTheme);
            model.addAttribute("secondTheme", secondTheme);

            return "mypage"; // mypage.html로 이동
        }

        return "redirect:/home"; // 사용자를 찾을 수 없으면 홈페이지로 리다이렉트
    }
}
