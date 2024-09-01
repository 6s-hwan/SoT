package com.SoT.JIN.story;

import com.SoT.JIN.user.User;
import com.SoT.JIN.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StoryDetailController {

    private final StoryService storyService;
    private final UserRepository userRepository;

    public StoryDetailController(StoryService storyService, UserRepository userRepository) {
        this.storyService = storyService;
        this.userRepository = userRepository;
    }

    @GetMapping("/story/detail/{storyId}")
    public StoryDTO getStoryDetail(@PathVariable Long storyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = null;
        boolean isAuthenticated = false;

        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication.getPrincipal() instanceof String && "anonymousUser".equals(authentication.getPrincipal()))) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                user = userRepository.findByEmail(userDetails.getUsername())
                        .orElse(null);
                isAuthenticated = true;
            }
        }

        // 조회수 증가 로직 호출
        storyService.getStoryAndIncrementViewCount(storyId);

        // 사용자 정보와 함께 스토리 상세 정보 가져오기
        StoryDTO storyDTO = storyService.getStoryDetail(storyId, user);
        storyDTO.setAuthenticated(isAuthenticated);  // 사용자 인증 상태 설정
        return storyDTO;
    }
}

