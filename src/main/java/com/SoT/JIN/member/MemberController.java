package com.SoT.JIN.member;

import java.util.List; // 리스트 사용을 위해 임포트
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // 모델 사용을 위해 임포트
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {
    private final UserRepository userRepository;
    private final StoryRepository storyRepository; // StoryRepository 추가

    @GetMapping({"/home"})
    String list() {
        return "home";
    }

    @PostMapping({"/user"})
    String addUser(@RequestParam("email") String email, @RequestParam("password") String password,
                   @RequestParam("username") String username, @RequestParam("gender") String gender,
                   @RequestParam("birth_year") String birthYear, @RequestParam("birth_month") String birthMonth,
                   @RequestParam("birth_day") String birthDay, @RequestParam("phone1") String phone1,
                   @RequestParam("phone2") String phone2, @RequestParam("phone3") String phone3) {
        String phone = phone1 + phone2 + phone3;
        String birth = birthYear + "-" + birthMonth + "-" + birthDay;
        User user = new User();
        user.setEmail(email);
        user.setPassword((new BCryptPasswordEncoder()).encode(password));
        user.setUsername(username);
        user.setGender(gender);
        user.setBirth(birth);
        user.setPhonenumber(phone);
        this.userRepository.save(user);
        return "redirect:/test";
    }

    public MemberController(final UserRepository userRepository, final StoryRepository storyRepository) {
        this.userRepository = userRepository;
        this.storyRepository = storyRepository; // StoryRepository 주입
    }

    @GetMapping("/my-page")
    public String mypage(Authentication authentication, Model model, @RequestParam(name = "sort", required = false) String sortCriteria) {
        String username = authentication.getName(); // 현재 인증된 사용자의 이름 (이메일)

        // 사용자 이메일로 사용자 정보 찾기
        User user = userRepository.findByEmail(username).orElse(null);

        if (user != null) {
            // 사용자 이메일로 해당 사용자가 작성한 모든 스토리 찾기
            List<Story> userStories;

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
                totalLikes += story.getLikesCount();
            }

            // 모델에 사용자 정보, 스토리 리스트, 전체 스토리 수, 조회수, 좋아요 수 추가
            model.addAttribute("user", user);
            model.addAttribute("stories", userStories);
            model.addAttribute("totalStories", totalStories);
            model.addAttribute("totalViews", totalViews);
            model.addAttribute("totalLikes", totalLikes);

            return "mypage"; // mypage.html로 이동
        }

        return "redirect:/home"; // 사용자를 찾을 수 없으면 홈페이지로 리다이렉트
    }
}
