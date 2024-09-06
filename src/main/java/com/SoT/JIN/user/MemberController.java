package com.SoT.JIN.user;

import com.SoT.JIN.story.Story;
import com.SoT.JIN.story.StoryRepository;
import com.SoT.JIN.story.S3Service;
import com.SoT.JIN.verification.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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
    private final S3Service s3Service;
    private final VerificationRepository verificationRepository;

    private static final String DEFAULT_PROFILE_IMAGE_URL = "https://soteulji.s3.ap-northeast-2.amazonaws.com/test/mypageprofile.png";

    @Autowired
    public MemberController(UserRepository userRepository, StoryRepository storyRepository, S3Service s3Service, VerificationRepository verificationRepository) {
        this.userRepository = userRepository;
        this.storyRepository = storyRepository;
        this.s3Service = s3Service;
        this.verificationRepository = verificationRepository;
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

        String phone = phone1 + phone2 + phone3;
        String phoneNumber = "+82" + phone1.substring(1) + phone2 + phone3;
        String birth = birthYear + "-" + birthMonth + "-" + birthDay;

        Optional<User> userOptional = userRepository.findByPhoneNumberAndVerificationCode(phoneNumber, verificationCode);
        User user = userOptional.orElse(null);

        // 인증번호가 유효하지 않으면 에러 처리
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "전화번호 또는 인증번호가 올바르지 않습니다.");
            return "redirect:/error";
        }

        try {
            user.setEmail(email);
            user.setPassword(new BCryptPasswordEncoder().encode(password));
            user.setUsername(username);
            user.setGender(gender);
            user.setBirth(birth);

            // 기본 프로필 이미지 설정
            user.setProfileImageUrl(DEFAULT_PROFILE_IMAGE_URL);

            // 인증번호 삭제
            user.setVerificationCode(null);
            // 유저 정보 저장
            userRepository.save(user);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "회원 가입 중 오류가 발생했습니다.");
            return "redirect:/error";
        }

        return "redirect:/home?status=success"; // 성공 시
    }

    @PostMapping("/update-profile-image")
    public String updateProfileImage(@RequestParam("file") MultipartFile file, Authentication authentication, RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        User user = userRepository.findByEmail(username).orElse(null);

        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "사용자를 찾을 수 없습니다.");
            return "redirect:/error";
        }

        try {
            String imageUrl = s3Service.uploadFile(file);
            user.setProfileImageUrl(imageUrl);
            userRepository.save(user);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "이미지 업로드 중 오류가 발생했습니다.");
            return "redirect:/error";
        }

        return "redirect:/my-page";
    }

    @GetMapping("/my-page")
    public String myPage(Authentication authentication, Model model, @RequestParam(name = "sort", required = false) String sortCriteria) {
        String username = authentication.getName();
        User user = userRepository.findByEmail(username).orElse(null);

        if (user != null) {
            List<Story> userStories = storyRepository.findByUsername(username);

            // 정렬 기준에 따라 스토리를 정렬
            if ("likes".equals(sortCriteria)) {
                userStories.sort((s1, s2) -> {
                    int cmp = Integer.compare(s2.getLikes() != null ? s2.getLikes().size() : 0,
                            s1.getLikes() != null ? s1.getLikes().size() : 0);
                    if (cmp == 0) {
                        return Integer.compare(s2.getViewCount(), s1.getViewCount());
                    }
                    return cmp;
                });
            } else if ("views".equals(sortCriteria)) {
                userStories.sort((s1, s2) -> Integer.compare(s2.getViewCount(), s1.getViewCount()));
            } else if ("recent".equals(sortCriteria)) {
                userStories.sort((s1, s2) -> {
                    if (s2.getUploadTime() == null && s1.getUploadTime() == null) return 0;
                    if (s2.getUploadTime() == null) return -1;
                    if (s1.getUploadTime() == null) return 1;
                    return s2.getUploadTime().compareTo(s1.getUploadTime());
                });
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

        return "redirect:/home"; // 사용자를 찾을 수 없으면 홈페이지로 리다이렉트
    }

    // 최근 업로드 일수 계산 메서드
    private int calculateDaysSinceLastUpload(List<Story> userStories) {
        // 만약 userStories가 비어 있다면
        if (userStories.isEmpty()) {
            return 0;
        }

        // 최근 업로드 시간을 가져오기 위해 리스트에서 마지막 요소를 선택
        LocalDateTime mostRecentUploadTime = userStories.get(userStories.size() - 1).getUploadTime();

        // 만약 mostRecentUploadTime이 null이라면
        if (mostRecentUploadTime == null) {
            return 0;
        }

        // 현재 날짜
        LocalDate currentDate = LocalDate.now();

        // 최근 업로드 일자를 LocalDate로 변환
        LocalDate uploadDate = mostRecentUploadTime.toLocalDate();

        // 일자 차이 계산
        return (int) ChronoUnit.DAYS.between(uploadDate, currentDate);
    }
}