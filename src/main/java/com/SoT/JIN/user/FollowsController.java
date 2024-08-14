package com.SoT.JIN.user;

import com.SoT.JIN.story.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
            // 정렬된 팔로잉 리스트를 가져옴
            List<User> followingList = userRepository.findFollowingByUserId(currentUser.getUserId());
            Collections.reverse(followingList);

            List<WriterInfo> followsInfos = followingList.stream()
                    .map(user -> {
                        long storyCount = storyRepository.countByUsername(user.getEmail());
                        System.out.println("Username: " + user.getUsername() + ", Story Count: " + storyCount);
                        return new WriterInfo(user.getUsername(), user.getProfileImageUrl(), storyCount);
                    })
                    .collect(Collectors.toList());

            model.addAttribute("followsInfos", followsInfos);
            return "following"; // following.html로 이동
        }

        return "redirect:/error";
    }


    public class WriterInfo {
        private String username;
        private String profileImageUrl;
        private long totalStories;

        public WriterInfo(String username, String profileImageUrl, long totalStories) {
            this.username = username;
            this.profileImageUrl = profileImageUrl;
            this.totalStories = totalStories;
        }

        public String getUsername() {
            return username;
        }

        public String getProfileImageUrl() {
            return profileImageUrl;
        }

        public long getTotalStories() {
            return totalStories;
        }
    }
}
