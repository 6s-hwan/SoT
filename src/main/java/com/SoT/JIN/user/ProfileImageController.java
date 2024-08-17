package com.SoT.JIN.user;

import com.SoT.JIN.story.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/test/profile") // 기본 경로 설정
public class ProfileImageController {

    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Autowired
    public ProfileImageController(UserRepository userRepository, S3Service s3Service) {
        this.userRepository = userRepository;
        this.s3Service = s3Service;
    }

    @GetMapping
    public String showUploadForm() {
        return "uploadProfileImage"; // 프로필 이미지 업로드 폼 HTML 페이지
    }

    @PostMapping("/update-image")
    public String updateProfileImage(@RequestParam("file") MultipartFile file, Authentication authentication, RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        User user = userRepository.findByEmail(username).orElse(null);

        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "사용자를 찾을 수 없습니다.");
            return "redirect:/error";
        }

        try {
            // 파일 업로드 및 프로필 이미지 URL 업데이트
            String imageUrl = s3Service.uploadFile(file);
            user.setProfileImageUrl(imageUrl);
            userRepository.save(user);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "이미지 업로드 중 오류가 발생했습니다.");
            return "redirect:/error";
        }

        return "redirect:/my-page"; // 프로필 이미지 업데이트 후 마이 페이지로 리다이렉트
    }
}
