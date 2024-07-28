package com.SoT.JIN.user;

import com.SoT.JIN.story.Story;
import com.SoT.JIN.story.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class MemberController {

    private final UserRepository userRepository;
    private final StoryRepository storyRepository;

    @Autowired
    public MemberController(UserRepository userRepository, StoryRepository storyRepository) {
        this.userRepository = userRepository;
        this.storyRepository = storyRepository;
    }

    @GetMapping("/bookmark")
    public String bookmark() {
        return "BookmarkPage";
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
                          @RequestParam("phone3") String phone3,
                          @RequestParam("verificationCode") String verificationCode,
                          RedirectAttributes redirectAttributes) {

        // 전화번호 조합
        String phone = phone1 + phone2 + phone3;

// 국가 코드를 포함한 전화번호 문자열 생성
        String phoneNumber = "+82" + phone1.substring(1) + phone2 + phone3;
        // 생일 조합
        String birth = birthYear + "-" + birthMonth + "-" + birthDay;

        // 이메일, 비밀번호, 사용자명, 성별 등의 유효성 검사를 수행합니다.
        // 예를 들어, 이메일 형식 체크, 비밀번호 강도 확인 등이 필요합니다.

        // 사용자 조회
        Optional<User> userOptional = userRepository.findByPhoneNumberAndVerificationCode(phoneNumber, verificationCode);
        User user = userOptional.orElse(null);


        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "전화번호 또는 인증번호가 올바르지 않습니다.");
            return "redirect:/error";
        }

        try {
            // 사용자 정보 업데이트
            user.setEmail(email);
            user.setPassword(new BCryptPasswordEncoder().encode(password));
            user.setUsername(username);
            user.setGender(gender);
            user.setBirth(birth);

            // 사용자 정보 저장
            userRepository.save(user);
        } catch (Exception e) {
            // 비밀번호 인코딩 등에서 예외가 발생할 경우 처리합니다.
            redirectAttributes.addFlashAttribute("errorMessage", "회원 가입 중 오류가 발생했습니다.");
            return "redirect:/error";
        }

        // 회원 가입 성공 시, 테스트 페이지로 리다이렉트합니다.
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
            if ("likes".equals(sortCriteria)) {
                userStories = storyRepository.findByUsernameOrderByLikesDesc(username);
            } else if ("views".equals(sortCriteria)) {
                userStories = storyRepository.findByUsernameOrderByViewCountDesc(username);
            } else if ("recent".equals(sortCriteria)) {
                userStories = storyRepository.findByUsernameOrderByUploadTimeDesc(username);
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

            return "mypage"; // mypage.html로 이동
        }

        return "redirect:/test"; // 사용자를 찾을 수 없으면 홈페이지로 리다이렉트
    }

    // 최근 업로드 일수 계산 메서드
    private int calculateDaysSinceLastUpload(List<Story> userStories) {
        // 만약 userStories가 비어 있다면
        if (userStories.isEmpty()) {
            return 0; // 혹은 다른 의미있는 값을 리턴
        }

        // 최근 업로드 시간을 가져오기 위해 리스트에서 마지막 요소를 선택
        LocalDateTime mostRecentUploadTime = userStories.get(userStories.size() - 1).getUploadTime();

        // 만약 mostRecentUploadTime이 null이라면
        if (mostRecentUploadTime == null) {
            return 0; // 혹은 다른 의미있는 값을 리턴
        }

        // 현재 날짜
        LocalDate currentDate = LocalDate.now();

        // 최근 업로드 일자를 LocalDate로 변환
        LocalDate uploadDate = mostRecentUploadTime.toLocalDate();

        // 일자 차이 계산
        return (int) ChronoUnit.DAYS.between(uploadDate, currentDate);
    }
}