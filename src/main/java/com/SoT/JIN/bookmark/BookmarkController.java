package com.SoT.JIN.bookmark;

import com.SoT.JIN.story.Story;
import com.SoT.JIN.user.User;
import com.SoT.JIN.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/story/{storyId}/bookmark")
    public ResponseEntity<?> toggleBookmark(@PathVariable Long storyId, Principal principal) {
        // 인증 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        // 사용자 정보 가져오기
        Object principalObj = authentication.getPrincipal();
        String username;
        if (principalObj instanceof UserDetails) {
            username = ((UserDetails) principalObj).getUsername();
        } else {
            username = principalObj.toString();
        }

        Optional<User> optionalUser = userRepository.findByEmail(username);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User user = optionalUser.get();

        // 북마크 토글 처리
        try {
            bookmarkService.toggleBookmark(storyId, user);
            boolean isBookmarked = bookmarkService.isBookmarked(storyId, user); // 북마크 여부 확인

            Map<String, Object> response = new HashMap<>();
            response.put("bookmarked", isBookmarked); // 북마크 상태 반환
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing bookmark");
        }
    }


    @GetMapping("/bookmark")
    public String getBookmarkedStories(@RequestParam(name = "limit", defaultValue = "15") int limit, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        Object principalObj = authentication.getPrincipal();
        String username;
        if (principalObj instanceof UserDetails) {
            username = ((UserDetails) principalObj).getUsername();
        } else {
            username = principalObj.toString();
        }

        Optional<User> optionalUser = userRepository.findByEmail(username);
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 1. 북마크된 스토리들 가져오기
        List<Story> bookmarkedStories = bookmarkService.getBookmarkedStories(user);

        // 2. 스토리 순서를 거꾸로
        Collections.reverse(bookmarkedStories);

        // 3. 가져올 스토리 제한 (limit 만큼)
        List<Story> limitedBookmarkedStories = bookmarkedStories.stream().limit(limit).collect(Collectors.toList());

        // 4. 모델에 데이터 추가
        model.addAttribute("bookmarkedStories", limitedBookmarkedStories);
        model.addAttribute("totalStories", bookmarkedStories.size()); // 총 북마크된 스토리 수
        model.addAttribute("limit", limit);

        return "BookmarkPage"; // 북마크 페이지로 이동
    }

}
